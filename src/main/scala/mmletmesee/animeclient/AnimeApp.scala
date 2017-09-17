package mmletmesee.animeclient

object AnimeApp extends App {
  javafx.application.Application.launch(classOf[AnimeApp], args: _*)
  
  class AnimeApp extends javafx.application.Application {
    val factory = new AnimeFactory
    
    override def start(stage: javafx.stage.Stage) {
      stage.setScene(factory.animeView.scene)
      stage.show()
    }
  }
}