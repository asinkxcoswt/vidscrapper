package mmletmesee.animeclient.serviceimpl

import mmletmesee.animeclient.AnimeService
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import scala.collection.immutable.{TreeMap => Map}
import java.net.{URL, URLDecoder}
import java.net.HttpURLConnection
import java.util.regex.{Pattern, Matcher}

trait Anime {
  val title: String
  val url: URL
  def allEpisodes: List[String]
  val coverUrl: URL
  def videoUrl(episode: String): URL
}

object DefaultGogoAnime extends Anime {
  def allEpisodes: List[String] = List()
  val coverUrl: URL = new URL("http://www.userlogos.org/files/logos/Pap/gogoanime.jpg")
  val title: String = ""
  val url: URL = throw new Exception("No url")
  def videoUrl(episode: String): URL = throw new Exception("No video")
}

class GogoAnime(val title: String, val url: URL) extends Anime {
  import scala.collection.JavaConversions._
  import GogoAnimeService._
  
  private val doc = Doc(url) ensuring(_ != null, "Cannot load document from: " + url)
  val episodesxsUrls: Map[String, String] = {
    val itemList = for (ele <- doc.select("div.postlist a")) yield (ele.text, ele.attr("href"))
    Map(itemList.toList:_*)
  }
  
  val coverUrl = doc.select("div.catdescription img") match {
    case e if(e.size > 0) => GogoAnimeUrl(e.get(0).attr("src"))
    case _ => throw new Exception("Cover not found for anime:" + title)
  }
  def allEpisodes = episodesxsUrls.keys.toList
  def videoUrl(episode: String): URL = GogoAnimeUrl {
    val playerUrl = Doc(GogoAnimeUrl(episodesxsUrls(episode))).select("div.postcontent iframe") match {
      case e if e.size > 0 => e.get(0).attr("src")
      case _ => throw new Exception("Video not found for anime: " + title + " episode: " + episode)
    }
    
    Doc(new URL(playerUrl)).select("script").last() match {
      case e if e != null => {
        val m = VideoUrlPattern.matcher(e.html)
        m.find() match {
          case true => URLDecoder.decode(m.group(1), "UTF-8")
          case _ => throw new Exception("Video not found for anime: " + title + " episode: " + episode)
        }
      }
      case _ => throw new Exception("Video not found for anime: " + title + " episode: " + episode)
    }
  }
}

object GogoAnimeService {
  val BaseURL = new URL("http://www.gogoanime.com")
  val TitleListURL = GogoAnimeUrl("/watch-anime-list")
  val VideoUrlPattern = Pattern.compile("_url *= *\"(.+)\"")
  def Doc(url: URL) = Jsoup.connect(url.toString()).userAgent("Mozilla").get()
  def GogoAnimeUrl(fullOrRelativePath: String) = new URL(BaseURL, fullOrRelativePath)
}

class GogoAnimeService extends AnimeService {
  import scala.collection.JavaConversions._
  import GogoAnimeService._
  
  private val titlesxurls: Map[String, String] = {
    val doc = Doc(TitleListURL)
    val itemList = for (ele <- doc.select("li.cat-item a")) yield (ele.text, ele.attr("href"))
    Map(itemList.toList:_*)
  }
  
  private var selectedAnime: Anime = _
  
  private def animeForTitle(title: String): Anime = {
    if (selectedAnime == null || selectedAnime.title != title) 
      selectedAnime = try new GogoAnime(title, GogoAnimeUrl(titlesxurls(title))) catch {
        case e: Exception => {
          e.printStackTrace()
          DefaultGogoAnime
        }
      }
    selectedAnime
  }
  
  override def allTitles: List[String] = {
    titlesxurls.keys.toList
  }
  
  override def allEpisodes(title: String): List[String] = {
    animeForTitle(title).allEpisodes
  }

  override def coverUrl(title: String): URL = {
    animeForTitle(title).coverUrl
  }

  override def videoUrl(title: String, episode: String): URL = {
    animeForTitle(title).videoUrl(episode)
  }
}