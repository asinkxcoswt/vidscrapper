package mmletmesee.animeclient.default_;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class MediaControl extends BorderPane {
	@FXML private MediaView playerView;
	@FXML private Slider slider;
	@FXML private Polygon playButton;
	@FXML private Rectangle stopButton;
	
	@FXML public void play() {
		System.out.println("play");
		final MediaPlayer player = playerView.getMediaPlayer();
		if (player != null) player.play();
	}
	
	@FXML public void pause() {
		System.out.println("stop");
		final MediaPlayer player = playerView.getMediaPlayer();
		if (player != null) player.pause();
	}
}
