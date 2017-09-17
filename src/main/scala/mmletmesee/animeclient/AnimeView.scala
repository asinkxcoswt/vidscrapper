package mmletmesee.animeclient

import javafx.{scene => jfxs}
import javafx.scene.{image => jfximg}
import javafx.fxml
import java.net.URL
import javafx.scene.Scene

trait AnimeView {
  val placeHolderCover: jfximg.Image
  val videoErrorImage: jfximg.Image
  var scene: jfxs.Scene = _
  def apply(selector: String): jfxs.Node = scene.getRoot.lookup(selector)
  def loadScene(loader: fxml.FXMLLoader, viewSourceUrl: URL) {
    scene = new Scene(loader
        .load(viewSourceUrl
            .openStream()))
  }
}