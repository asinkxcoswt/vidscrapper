package mmletmesee.animeclient

import java.net.URL
import mmletmesee.animeclient.serviceimpl.GogoAnimeService

object AnimeConfig {
  def AnimeServiceClass: Class[_ <: AnimeService] = classOf[GogoAnimeService]
}