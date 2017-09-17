package mmletmesee.animeclient

import javafx.fxml
import mmletmesee.animeclient.default_._
import java.net.URL
import mmletmesee.resources.R

class AnimeFactory {
  import mmletmesee.utils.With._
  
  private val viewSourceUrl: URL = R("fxml/anime-view-source.fxml") ifNone {
    throw new Exception("Cannot load resource: /fxml/anime-view-source.fxml")
  }
  private val loader = new fxml.FXMLLoader
  
  val animeService = AnimeConfig.AnimeServiceClass.getConstructor().newInstance()
  val animeView = new DefaultAnimeView
  animeView.loadScene(loader, viewSourceUrl)
  val animeController = loader.getController[DefaultAnimeController]()
  animeController.init(animeService, animeView)
}