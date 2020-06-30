package guiElements;

import java.util.ArrayList;
import java.util.List;


import game.GameLauncher;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ScorePanel extends VBox {

	private double time;
	private Integer points = 0;
	private ProgressBar power;
	private Timeline timeline;
	private Label collectedPoints;
	private Label name;

	private String[] pollen;
	private ImageView[] lifes;
	private List<ImageView> pollenImageList;

	public ScorePanel(String[] pollen, int playerNum) {
		this.pollen = pollen;
		if (playerNum == 1) {
			name = new Label(GameLauncher.namePlayerOne);
		} else {
			name = new Label(GameLauncher.namePlayerTwo);
		}
		getChildren().add(name);
		addCollectedItems();
		addLive();
		addTimer();
		runSimultation();
		addPoints();
	}

	public void addCollectedItems() {
		Label collectedItems = new Label("Collected Items:");
		getChildren().add(collectedItems);
		pollenImageList = new ArrayList<ImageView>();
		HBox hbox2 = new HBox();
		getChildren().add(hbox2);
		for (int i = 0; i < pollen.length; i++) {
			ImageView item = new ImageView(new Image(getClass().getResource(pollen[i]).toExternalForm()));
			item.setVisible(false);
			pollenImageList.add(item);
		}
		hbox2.getChildren().addAll(pollenImageList);
	}

	public void addLive() {
		Label livelabel = new Label("Lifes:");
		getChildren().add(livelabel);
		HBox hbox = new HBox();
		this.getChildren().add(hbox);
		lifes = new ImageView[3];
		lifes[0] = new ImageView(new Image(getClass().getResource(GameLauncher.HEART).toExternalForm()));
		lifes[1] = new ImageView(new Image(getClass().getResource(GameLauncher.HEART).toExternalForm()));
		lifes[2] = new ImageView(new Image(getClass().getResource(GameLauncher.HEART).toExternalForm()));
		for (int i = 0; i < lifes.length; i++) {
			hbox.getChildren().add(lifes[i]);
		}
	}

	public void addPoints() {
		Label pointsLabel = new Label("Points:");
		getChildren().add(pointsLabel);
		collectedPoints = new Label();
		collectedPoints.setText(points.toString());
		getChildren().add(collectedPoints);
	}

	public void removeLive(int lifes) {
		switch (lifes) {
		case 2: {
			this.lifes[2].setVisible(false);
			break;
		}
		case 1: {
			this.lifes[1].setVisible(false);
			break;
		}
		case 0: {
			this.lifes[0].setVisible(false);
			break;
		}
		}
	}

	public void addTimer() {
		Label timerlabel = new Label("Power left:");
		getChildren().add(timerlabel);
		power = new ProgressBar();
		power.setMinSize(100, 30);
		power.setMaxSize(100, 30);
		getChildren().add(power);
	}

	public void runSimultation() {
		timeline = new Timeline(new KeyFrame(Duration.millis(0), new KeyValue(power.progressProperty(), 0)),
				new KeyFrame(Duration.minutes(3), new KeyValue(power.progressProperty(), 1)));

		power.progressProperty().addListener((obs, oldProgress, newProgress) -> {

			time = newProgress.doubleValue();
			if (newProgress.doubleValue() < 0.25) {
				power.setStyle("-fx-accent: darkgreen;");

			} else if (newProgress.doubleValue() < 0.5) {
				power.setStyle("-fx-accent: darkslateblue;");

			} else if (newProgress.doubleValue() < 0.75) {
				power.setStyle("-fx-accent: tomato;");
			} else {
				power.setStyle(" -fx-accent: red;");
			}
		});

		timeline.setCycleCount(1);
		timeline.play();
	}

	public void addPower() {
		timeline.playFrom(timeline.getCurrentTime().subtract(Duration.millis(30000)));
	}

	public void stopTimer() {
		timeline.stop();
	}

	public List<ImageView> getPollenImageList() {
		return pollenImageList;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Label getCollectedPoints() {
		return collectedPoints;
	}

}
