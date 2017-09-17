package mmletmesee.utils

import javafx.scene.{image => jfximg}
import javafx.scene.{media => jfxm}
import javax.imageio.ImageIO
import java.net.URL
import javafx.embed.swing.SwingFXUtils

object MediaUtils {
  def jfxImage(url: URL): jfximg.Image = {
    url.getProtocol match {
      case "http" | "https" => jfxHTTPImage(url)
      case _ => new jfximg.Image(url.openStream())
    }
  }
  def jfxMedia(url: URL): jfxm.Media = {
    val media = new jfxm.Media(url.toString())
    media
  }
  
  private def jfxHTTPImage(url: URL): jfximg.Image = {
    val connection = url.openConnection()
    connection.setRequestProperty("User-Agent", "Mozilla")
    connection.connect()
    val bufferedImg = ImageIO.read(connection.getInputStream)
    val img = SwingFXUtils.toFXImage(bufferedImg, new jfximg.WritableImage(bufferedImg.getWidth, bufferedImg.getHeight))
    if (img.isError) throw new Exception(img.getException)
    img
  }
}