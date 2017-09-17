package mmletmesee.animeclient

import java.net.URL

trait AnimeService {
  def allTitles: List[String]
  def allEpisodes(title: String): List[String]
  def videoUrl(title: String, episode: String): URL
  def coverUrl(title: String): URL
}