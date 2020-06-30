package game;

import java.io.File;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import entities.Sprite;
import entities.Player;
import guiElements.Pollen;
import guiElements.RecipePane;
import guiElements.ScorePanel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BusyBeeMP extends Application {

	private String pathPollen = "res/salamisound-5450416-tischglocke-einmal.mp3";
	private String pathThunder = "res/salamisound-3039124-regen-mit-donner-langes.mp3";
	private String pathEnemy = "res/salamisound-3569093-flachen-stein-in-wasser.mp3";

	private Media mediaEnemy = new Media(new File(pathEnemy).toURI().toString());
	private Media mediaPollen = new Media(new File(pathPollen).toURI().toString());
	private Media mediaThunder = new Media(new File(pathThunder).toURI().toString());

	private MediaPlayer thunder;
	private MediaPlayer enemyCollision;
	private MediaPlayer pollenCollision;

	private List<Sprite> spriteList = new ArrayList<Sprite>();
	private List<Pollen> pollenList = new ArrayList<Pollen>();
	private List<Sprite> toremoveSprites = new ArrayList<Sprite>();
	private List<ImageView> toRemovePollen = new ArrayList<ImageView>();
	private ArrayList<String> input = new ArrayList<String>();
	private HashMap<Character, String> recipe = new HashMap<>();

	private boolean isFalling1 = true;
	private boolean isFalling2 = true;
	private boolean gameOver = false;
	private boolean isWind = false;
	private boolean eastWind = false;

	private AnchorPane mainPane;
	private AnimationTimer timer;
	private Player player1;
	private Player player2;
	private Scene scene;
	private ScorePanel scorePanel1;
	private ScorePanel scorePanel2;

	private char[] toCollect;

	private Sprite bird;
	private Sprite rain;
	private Sprite tau;

	private Pollen tulpe;
	private Pollen rose;
	private Pollen schneegloeckchen;
	private Pollen mohn;
	private Pollen linde;
	private Pollen sonnenblume;
	private Pollen punkt;

	private int playerLife1 = 3;
	private int playerLife2 = 3;
	private int collectedPollen1 = 0;
	private int collectedPollen2 = 0;
	private int enemySpeed = 3;
	private int playerSpeed;
	private int playerSpeedRun;
	private int regenRandom = 1000;
	private double startTime;

	@Override

	public void start(Stage arg0) throws Exception {
		mainPane = new AnchorPane();
		ImageView background = new ImageView(new Image("file:res/Background2.png"));
		background.fitHeightProperty().bind(mainPane.heightProperty());
		mainPane.getChildren().add(background);

		scene = new Scene(mainPane, 985, 588, Color.TRANSPARENT);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		createRecipe();
		RecipePane recipePane = new RecipePane(this);
		recipePane.setLayoutX(260);
		mainPane.getChildren().add(recipePane);
		scorePanel1 = new ScorePanel(recipePane.getPollen(), 1);
		mainPane.getChildren().add(scorePanel1);
		scorePanel2 = new ScorePanel(recipePane.getPollen(), 2);
		scorePanel1.setLayoutX(700);
		mainPane.getChildren().add(scorePanel2);
		initGame();
	}

	public void initGame() {
		String path = "res/bensound-littleidea.mp3";
		Media media = new Media(new File(path).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.setAutoPlay(true);

		startTime = System.currentTimeMillis();
		scene.setOnKeyPressed(event -> {
			String code = event.getCode().toString();
			if (!input.contains(code)) {
				input.add(code);
			}
		});
		scene.setOnKeyReleased(event -> {
			String code = event.getCode().toString();
			input.remove(code);
		});

		player1 = new Player(GameLauncher.CHARACTER1);
		player2 = new Player(GameLauncher.CHARACTER2);
		player1.setLayoutX(908);
		player2.setLayoutX(0);
		mainPane.getChildren().addAll(player1, player2);

		timer = new AnimationTimer() {

			@Override
			public void handle(long arg0) {
				updatePlayer1();
				updatePlayer2();
				setGravity();
				checkBounds();
				createPollen();
				createSprites();
				createWind();
				movePollen();
				moveSprites();
				checkCollisionSprite();
				checkCollisionPollen();
				removeSprites();
				removePollen();
				System.out.println(player1.getTranslateX());
				if (!toRemovePollen.isEmpty()) {
					pollenList.removeAll(toRemovePollen);
					toRemovePollen.clear();
				}
				if (!toremoveSprites.isEmpty()) {
					spriteList.removeAll(toremoveSprites);
					toremoveSprites.clear();
				}
				changeRandomnessAndSpeed();
				setGameOver();
				isGameOver();
			}
		};
		timer.start();
	}

	private void createRecipe() {
		toCollect = new char[6];
		for (int i = 0; i < 6; i++) {
			int random = (int) (Math.random() * 6);
			switch (random) {
			case 0:
				toCollect[i] = 'l';
				recipe.put('l', getClass().getResource(GameLauncher.LINDE).toExternalForm());
				break;
			case 1:
				toCollect[i] = 'r';
				recipe.put('r', getClass().getResource(GameLauncher.ROSE).toExternalForm());
				break;
			case 2:
				toCollect[i] = 's';
				recipe.put('s', getClass().getResource(GameLauncher.SCHNEEGLOECKCHEN).toExternalForm());
				break;
			case 3:
				toCollect[i] = 'm';
				recipe.put('m', getClass().getResource(GameLauncher.MOHN).toExternalForm());
				break;
			case 4:
				toCollect[i] = 'o';
				recipe.put('o', getClass().getResource(GameLauncher.SONNENBLUME).toExternalForm());
				break;
			case 5:
				toCollect[i] = 't';
				recipe.put('t', getClass().getResource(GameLauncher.TULPE).toExternalForm());
				break;
			}
		}

	}

	private void updatePlayer1() {
		if (isWind) {
			playerSpeed = 2;
			playerSpeedRun = 3;
		} else {
			playerSpeed = 4;
			playerSpeedRun = 5;
		}
		if (input.contains("RIGHT")) {
			player1.getAnimation().play();
			player1.getAnimation().setOffsetY(162);
			if (input.contains("M")) {
				player1.moveX(playerSpeedRun);
			} else {
				player1.moveX(playerSpeed);
			}
		} else if (input.contains("LEFT")) {
			player1.getAnimation().play();
			player1.getAnimation().setOffsetY(81);
			if (input.contains("M")) {
				player1.moveX(-playerSpeedRun);
			} else {
				player1.moveX(-playerSpeed);
			}
		} else if (input.contains("UP")) {
			player1.getAnimation().play();
			player1.getAnimation().setOffsetY(0);
			isFalling1 = false;
			if (input.contains("M")) {
				player1.moveY(-playerSpeedRun);
			} else {
				player1.moveY(-playerSpeed);
			}
		} else {
			isFalling1 = true;
		}
	}

	private void updatePlayer2() {
		if (isWind) {
			playerSpeed = 2;
			playerSpeedRun = 3;
		} else {
			playerSpeed = 4;
			playerSpeedRun = 5;
		}
		if (input.contains("W")) {
			player2.getAnimation().play();
			player2.getAnimation().setOffsetY(0);
			isFalling2 = false;
			if (input.contains("M")) {
				player2.moveY(-playerSpeedRun);
			} else {
				player2.moveY(-playerSpeed);
			}
		} else if (input.contains("A")) {
			player2.getAnimation().play();
			player2.getAnimation().setOffsetY(81);
			if (input.contains("M")) {
				player2.moveX(-playerSpeedRun);
			} else {
				player2.moveX(-playerSpeed);
			}
		} else if (input.contains("D")) {
			player2.getAnimation().play();
			player2.getAnimation().setOffsetY(162);
			if (input.contains("M")) {
				player2.moveX(playerSpeedRun);
			} else {
				player2.moveX(playerSpeed);
			}
		} else {

			isFalling2 = true;
		}
	}

	private void checkBounds() {
		if (player2.getTranslateY() < 0) {
			player2.setTranslateY(0);
		}
		if (player2.getTranslateX() < 0) {
			player2.setTranslateX(0);
		}
		if (player2.getTranslateX() > 908) {
			player2.setTranslateX(908);
		}
		if (player1.getTranslateY() < 0) {
			player1.setTranslateY(0);
		}
		if (player1.getTranslateX() > 0) {
			player1.setTranslateX(0);
		}
		if (player1.getTranslateX() < -908) {
			player1.setTranslateX(-908);
		}
	}

	private void setGravity() {
		if (isFalling1) {
			if (player1.getTranslateY() < 507) {
				player1.moveY(playerSpeed);
				if (isWind) {
					if (eastWind) {
						player1.moveX(2);
					} else {
						player1.moveX(-2);
					}
				}
			}
		}
		if (isFalling2) {
			if (player2.getTranslateY() < 507) {
				player2.moveY(playerSpeed);
				if (isWind) {
					if (eastWind) {
						player2.moveX(2);
					} else {
						player2.moveX(-2);
					}
				}
			}
		}
	}

	private void createSprites() {
		if (isWind) {
			regenRandom = 700;
		}
		int random = (int) (Math.random() * regenRandom);
		if (random == 100 || random == 300) {
			rain = new Sprite(GameLauncher.REGEN, 5, 5, 20, 20);
			setSpritePosition(rain, Math.random() * 944, 0);
			spriteList.add(rain);
			mainPane.getChildren().add(rain);
		} else if (random == 200) {
			bird = new Sprite(GameLauncher.VOGEL, 4, 4, 81, 81);
			setSpritePosition(bird, 854, Math.random() * 200);
			spriteList.add(bird);
			mainPane.getChildren().add(bird);
		} else if (random == 400) {
			tau = new Sprite(GameLauncher.TAU, 5, 5, 41, 41);
			setSpritePosition(tau, Math.random() * 944, 0);
			spriteList.add(tau);
			mainPane.getChildren().add(tau);
		}
	}

	private void createPollen() {
		int random = (int) (Math.random() * 1200 + 1);
		switch (random) {
		case 100: {
			linde = new Pollen(new Image(getClass().getResource(GameLauncher.LINDE).toExternalForm()));
			pollenList.add(linde);
			setPositionPollen(linde);
			mainPane.getChildren().add(linde);
			break;
		}
		case 200: {
			rose = new Pollen(new Image(getClass().getResource(GameLauncher.ROSE).toExternalForm()));
			pollenList.add(rose);
			setPositionPollen(rose);
			mainPane.getChildren().add(rose);
			break;
		}
		case 300: {
			sonnenblume = new Pollen(new Image(getClass().getResource(GameLauncher.SONNENBLUME).toExternalForm()));
			pollenList.add(sonnenblume);
			setPositionPollen(sonnenblume);
			mainPane.getChildren().add(sonnenblume);
			break;
		}
		case 400: {
			mohn = new Pollen(new Image(getClass().getResource(GameLauncher.MOHN).toExternalForm()));
			pollenList.add(mohn);
			setPositionPollen(mohn);
			mainPane.getChildren().add(mohn);
			break;
		}
		case 500: {
			schneegloeckchen = new Pollen(
					new Image(getClass().getResource(GameLauncher.SCHNEEGLOECKCHEN).toExternalForm()));
			pollenList.add(schneegloeckchen);
			setPositionPollen(schneegloeckchen);
			mainPane.getChildren().add(schneegloeckchen);
			break;
		}
		case 600: {
			tulpe = new Pollen(new Image(getClass().getResource(GameLauncher.TULPE).toExternalForm()));
			pollenList.add(tulpe);
			setPositionPollen(tulpe);
			mainPane.getChildren().add(tulpe);
			break;
		}
		case 700: {
			punkt = new Pollen(new Image(getClass().getResource(GameLauncher.PUNKT).toExternalForm()));
			pollenList.add(punkt);
			setPositionPollen(punkt);
			mainPane.getChildren().add(punkt);
			break;
		}
		case 800: {
			punkt = new Pollen(new Image(getClass().getResource(GameLauncher.PUNKT).toExternalForm()));
			pollenList.add(punkt);
			setPositionPollen(punkt);
			mainPane.getChildren().add(punkt);
			break;
		}
		}
	}

	private void createWind() {
		if (!isWind) {
			int random = (int) (Math.random() * 1000);
			if (random == 100) {
				thunder = new MediaPlayer(mediaThunder);
				thunder.setCycleCount(MediaPlayer.INDEFINITE);
				thunder.setAutoPlay(true);
				isWind = true;
				int direction = (int) (Math.random() * 2);
				if (direction == 0) {
					eastWind = true;
				} else {
					eastWind = false;
				}

			}

		}
	}

	private void setSpritePosition(Sprite enemy, double x, double y) {
		enemy.setLayoutY(y);
		enemy.setLayoutX(x);
	}

	private void setPositionPollen(ImageView pollen) {
		pollen.setLayoutY(0);
		pollen.setLayoutX(Math.random() * 944);
	}

	private void moveSprites() {
		if (!spriteList.isEmpty()) {
			spriteList.forEach(sprite -> {
				if (sprite.getImageURL().equals(GameLauncher.REGEN) || sprite.getImageURL().equals(GameLauncher.TAU)) {
					sprite.getAnimation().play();
					sprite.moveY(enemySpeed);
					if (isWind) {
						if (eastWind) {
							sprite.moveX(1);
						} else {
							sprite.moveX(-1);
						}
					}
				} else if (sprite.getImageURL().equals(GameLauncher.VOGEL)) {
					sprite.getAnimation().play();
					sprite.moveX(-enemySpeed);
					if (isWind) {
						if (eastWind) {
							sprite.moveX(-2);
						} else {
							sprite.moveX(-5);
						}
					}
				}

			});
		}
	}

	private void movePollen() {
		if (!pollenList.isEmpty()) {
			pollenList.forEach(pollen -> {
				pollen.setLayoutY(pollen.getLayoutY() + 3);
				pollen.setRotate(pollen.getRotate() + 4);
				if (isWind) {
					if (eastWind) {
						pollen.setLayoutX(pollen.getLayoutX() + 1);
					} else {
						pollen.setLayoutX(pollen.getLayoutX() - 1);
					}
				}
			});
		}
	}

	private void checkCollisionSprite() {
		spriteList.forEach(sprite -> {
			if (player1.checkCollision(sprite)) {
				toremoveSprites.add(sprite);
				mainPane.getChildren().remove(sprite);
				if (sprite.getImageURL().equals(GameLauncher.REGEN)
						|| sprite.getImageURL().equals(GameLauncher.VOGEL)) {
					enemyCollision = new MediaPlayer(mediaEnemy);
					enemyCollision.setAutoPlay(true);
					removeLife(player1);
				} else if (sprite.getImageURL().equals(GameLauncher.TAU)) {
					scorePanel1.addPower();
				}
			}
			if (player2.checkCollision(sprite)) {
				toremoveSprites.add(sprite);
				mainPane.getChildren().remove(sprite);
				if (sprite.getImageURL().equals(GameLauncher.REGEN)
						|| sprite.getImageURL().equals(GameLauncher.VOGEL)) {
					enemyCollision = new MediaPlayer(mediaEnemy);
					enemyCollision.setAutoPlay(true);
					removeLife(player2);
				} else if (sprite.getImageURL().equals(GameLauncher.TAU)) {
					scorePanel2.addPower();
				}
			}
		});
	}

	private void checkCollisionPollen() {
		if (!pollenList.isEmpty()) {
			pollenList.forEach(pollen -> {
				if (player1.checkCollision(pollen)) {
					mainPane.getChildren().remove(pollen);
					toRemovePollen.add(pollen);
					if (pollen.getImage().getUrl().equals(recipe.get(toCollect[collectedPollen1]))) {
						pollenCollision = new MediaPlayer(mediaPollen);
						pollenCollision.setAutoPlay(true);
						scorePanel1.getPollenImageList().get(collectedPollen1).setVisible(true);
						collectedPollen1++;
					} else if (pollen.getImage().getUrl()
							.equals(getClass().getResource(GameLauncher.PUNKT).toExternalForm())) {
						pollenCollision = new MediaPlayer(mediaPollen);
						pollenCollision.setAutoPlay(true);
						Integer point = scorePanel1.getPoints() + 100;
						scorePanel1.setPoints(point);
						scorePanel1.getCollectedPoints().setText(point.toString());
					}
				}
				if (player2.checkCollision(pollen)) {
					mainPane.getChildren().remove(pollen);
					toRemovePollen.add(pollen);
					if (pollen.getImage().getUrl().equals(recipe.get(toCollect[collectedPollen2]))) {
						pollenCollision = new MediaPlayer(mediaPollen);
						pollenCollision.setAutoPlay(true);
						scorePanel2.getPollenImageList().get(collectedPollen2).setVisible(true);
						collectedPollen2++;
					} else if (pollen.getImage().getUrl().equals(GameLauncher.PUNKT)) {
						pollenCollision = new MediaPlayer(mediaPollen);
						pollenCollision.setAutoPlay(true);
						Integer point = scorePanel2.getPoints() + 100;
						scorePanel2.setPoints(point);
						scorePanel2.getCollectedPoints().setText(point.toString());
					}
				}
			});
		}
	}

	private void removeSprites() {
		if (!spriteList.isEmpty()) {
			spriteList.forEach(sprite -> {
				if (sprite.getImageURL().equals(GameLauncher.REGEN) && sprite.getTranslateY() > 567) {
					toremoveSprites.add(sprite);
					mainPane.getChildren().remove(sprite);
				} else if (sprite.getImageURL().equals(GameLauncher.VOGEL) && sprite.getTranslateX() < -944) {
					toremoveSprites.add(sprite);
					mainPane.getChildren().remove(sprite);
				} else if (sprite.getImageURL().equals(GameLauncher.TAU) && sprite.getTranslateY() > 547) {
					toremoveSprites.add(sprite);
					mainPane.getChildren().remove(sprite);
				}

			});
		}
	}

	private void removePollen() {
		if (!pollenList.isEmpty()) {
			pollenList.forEach(pollen -> {
				if (pollen.getLayoutY() > pollen.getMaxTransition()) {
					toRemovePollen.add(pollen);
					mainPane.getChildren().remove(pollen);
				}
			});
		}
	}

	private void removeLife(Player player) {
		if (player.equals(player1)) {
			playerLife1--;
			scorePanel1.removeLive(playerLife1);
		} else {
			playerLife2--;
			scorePanel2.removeLive(playerLife2);
		}
	}

	private void changeRandomnessAndSpeed() {
		if (System.currentTimeMillis() - startTime > 180000) {
			regenRandom = 900;
		} else if (System.currentTimeMillis() - startTime > 270000) {
			enemySpeed = 4;
		}
	}

	private void setGameOver() {
		if (scorePanel1.getPollenImageList().get(0).isVisible() && scorePanel1.getPollenImageList().get(1).isVisible()
				&& scorePanel1.getPollenImageList().get(2).isVisible()
				&& scorePanel1.getPollenImageList().get(3).isVisible()
				&& scorePanel1.getPollenImageList().get(4).isVisible()
				&& scorePanel1.getPollenImageList().get(5).isVisible()) {

			gameOver = true;
			player1.setWon(true);
		} else if (scorePanel2.getPollenImageList().get(0).isVisible()
				&& scorePanel2.getPollenImageList().get(1).isVisible()
				&& scorePanel2.getPollenImageList().get(2).isVisible()
				&& scorePanel2.getPollenImageList().get(3).isVisible()
				&& scorePanel2.getPollenImageList().get(4).isVisible()
				&& scorePanel2.getPollenImageList().get(5).isVisible()) {

			gameOver = true;
			player2.setWon(true);
		} else if (scorePanel1.getPollenImageList().get(0).isVisible()
				&& scorePanel1.getPollenImageList().get(1).isVisible()
				&& scorePanel1.getPollenImageList().get(2).isVisible()
				&& scorePanel1.getPollenImageList().get(3).isVisible()
				&& scorePanel1.getPollenImageList().get(4).isVisible()
				&& scorePanel1.getPollenImageList().get(5).isVisible()
				&& scorePanel2.getPollenImageList().get(0).isVisible()
				&& scorePanel2.getPollenImageList().get(1).isVisible()
				&& scorePanel2.getPollenImageList().get(2).isVisible()
				&& scorePanel2.getPollenImageList().get(3).isVisible()
				&& scorePanel2.getPollenImageList().get(4).isVisible()
				&& scorePanel2.getPollenImageList().get(5).isVisible()) {

			gameOver = true;
			player1.setWon(true);
			player2.setWon(true);
		} else if (playerLife1 == 0 && playerLife2 != 0) {
			gameOver = true;
			player2.setWon(true);
		} else if (playerLife2 == 0 && playerLife1 != 0) {
			gameOver = true;
			player1.setWon(true);
		} else if (playerLife2 == 0 && playerLife1 == 0) {
			gameOver = true;
		}

	}

	private void isGameOver() {
		if (gameOver) {
			timer.stop();
			scorePanel1.stopTimer();
			scorePanel2.stopTimer();
			player1.getAnimation().stop();
			player2.getAnimation().stop();
			VBox endState = new VBox();
			mainPane.getChildren().add(endState);
			Label winner = new Label();
			endState.getChildren().add(winner);
			Label pointsPlayerOne = new Label(
					"POINTS " + GameLauncher.namePlayerOne.toUpperCase() + ": " + scorePanel1.getPoints());
			endState.getChildren().add(pointsPlayerOne);
			Label pointsPlayerTwo = new Label(
					"POINTS " + GameLauncher.namePlayerTwo.toUpperCase() + ": " + scorePanel2.getPoints());
			endState.getChildren().add(pointsPlayerTwo);

			if (player1.isWon() && player2.isWon()) {
				winner.setText("YOU TIED!");
			} else if (player1.isWon() && !player2.isWon()) {
				winner.setText(GameLauncher.namePlayerOne.toUpperCase() + " WON!");
			} else if (!player1.isWon() && player2.isWon()) {
				winner.setText(GameLauncher.namePlayerTwo.toUpperCase() + " WON!");
			} else {
				winner.setText("YOU BOTH LOST");
			}
		}

	}

	public void setToCollect(char[] toCollect) {
		this.toCollect = toCollect;
	}

	public char[] getToCollect() {
		return toCollect;
	}

	public static void main(String[] args) {
		launch();
	}

}
