package mmletmesee.animeclient.default_

import mmletmesee.animeclient._
import javafx.scene.{layout => jfxl}
import javafx.scene.{control => jfxc}
import javafx.scene.{media => jfxm}
import javafx.scene.{image => jfximg}
import javafx.{collections => jfxcol}
import javafx.fxml
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import java.net.URL
import java.io.File
import java.awt.image.BufferedImage
import mmletmesee.utils.MediaUtils
import javafx.fxml.FXML


class DefaultAnimeController extends AnimeController {
  import mmletmesee.utils.With._
  implicit def listToObservableList[T](l: List[T]) = jfxcol.FXCollections.observableArrayList[T](l:_*)
  
  private var titleList: jfxc.ListView[String] = _
  private var episodeList: jfxc.ListView[String] = _
  private var playerPane: jfxl.BorderPane = _
  private var playerView: jfxm.MediaView = _
  private var coverPane: jfximg.ImageView = _
  private var service: AnimeService = _
  private var view: AnimeView = _
  
  def init(serv: AnimeService, view: AnimeView) {
    this.service = serv
    this.view = view
    titleList = view("#titleList").asInstanceOf[jfxc.ListView[String]]
    episodeList = view("#episodeList").asInstanceOf[jfxc.ListView[String]]
    playerPane = view("#playerPane").asInstanceOf[jfxl.BorderPane]
    playerView = view("#playerView").asInstanceOf[jfxm.MediaView]
    coverPane = view("#coverPane").asInstanceOf[jfximg.ImageView]
    showAllTitles()
  }
  
  private def selectedTitle = titleList.getSelectionModel.getSelectedItem
  private def selectedEpisode = episodeList.getSelectionModel.getSelectedItem
  
  @fxml.FXML def onTitleSelected() {
    intercept()
    showAllEpisodes(selectedTitle)
    showCover(selectedTitle)
  }
  
  @fxml.FXML def onEpisodeSelected() {
    intercept()
    showPlayer(selectedTitle, selectedEpisode)
  }
  
  @fxml.FXML def onPlayButtonClick() {
    playVideo()
  }
  
  @fxml.FXML def onStopButtonClick() {
    pauseVideo()
  }
  
  private def showAllTitles()  {
    println("showAllTitles")
    titleList.setItems(service.allTitles)
    titleList.getSelectionModel.selectFirst()
  }

  private def showAllEpisodes(title: String) {
    println("showAllEpisodes: " + title)
    episodeList.setItems(service.allEpisodes(title))
  }
  
  private def showCover(title: String) {
    println("showCover: " + title + " => " + service.coverUrl(title))
    val img = try MediaUtils.jfxImage(service.coverUrl(title)) catch {
      case e: Exception => {
        e.printStackTrace()
        view.placeHolderCover
      }
    }
    setImage(img)
  }
  
  private def setImage(img: jfximg.Image) {
    coverPane.setImage(img)
    showCoverPane()
  }
  
  private def showPlayer(title: String, episode: String) {
    println("showPlayer: " + title + ", " + episode + " => " + service.videoUrl(title, episode))

    val video = try MediaUtils.jfxMedia(service.videoUrl(title, episode)) catch {
      case e: Exception => {
        e.printStackTrace()
      }
    } 
    video match {
      case v: jfxm.Media => setVideo(v)
      case _ => setImage(view.videoErrorImage)
    }
  }
  
  private def setVideo(video: jfxm.Media) {
    playerView.setMediaPlayer(new jfxm.MediaPlayer(video))
    showMediaPane()
  }
  
  private def showCoverPane() {
    playerPane.setVisible(false)
    coverPane.setVisible(true)
  }
  
  private def showMediaPane() {
    playerPane.setVisible(true)
    coverPane.setVisible(false)
  }
  
  private def playVideo() {
    println("playVideo")
    Option(playerView.getMediaPlayer) match {
      case Some(player) if playerPane.isVisible() => player.play()
      case _ => println("No player to play")
    }
  }
  
  private def pauseVideo() {
    println("pauseVideo")
    Option(playerView.getMediaPlayer) match {
      case Some(player) if playerPane.isVisible() => player.pause()
      case _ => println("No player to pause")
    }
  }
  
  private def intercept() {
    disposeOngoingPlayer()
  }
  
  private def disposeOngoingPlayer() {
    playerView.getMediaPlayer ifSome { _.dispose() }
  }
}