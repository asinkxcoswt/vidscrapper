package mmletmesee.animeclient.default_

import mmletmesee.animeclient._
import javafx.{scene => jfxs}
import javafx.scene.{image => jfximg}
import javafx.fxml
import mmletmesee.utils.MediaUtils
import mmletmesee.resources.R

class DefaultAnimeView extends AnimeView {
  override val placeHolderCover: jfximg.Image = MediaUtils.jfxImage(R("image/placeholder-cover.jpg"))
  override val videoErrorImage: jfximg.Image = MediaUtils.jfxImage(R("image/placeholder-cover.jpg"))
}