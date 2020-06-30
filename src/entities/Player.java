package entities;

import game.GameLauncher;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Player extends AnchorPane {
	private ImageView imageView;

	private int count = 6;
	private int columns = 6;
	private int offsetX = 0;
	private int offsetY = 0;
	private int width = 81;
	private int height = 81;
	private SpriteAnimation animation;
	private AnimationTimer jumpTimer;
	Player character;
	double gravity;
	boolean won = false;
	private String name = " ";

	// Konstruktor erzeugt einen neue Spieler Animation, setzt Bild und formatiert Layout
	public Player(String imageName) {

		character = this;
		Image image = new Image(getClass().getResource(imageName).toExternalForm());
		imageView = new ImageView(image);
		imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
		imageView.setEffect(new DropShadow());
		imageView.fitHeightProperty().bind(this.heightProperty());
		imageView.setPreserveRatio(true);
		getChildren().add(imageView);

		animation = new SpriteAnimation(imageView, Duration.millis(200), count, columns, offsetX, offsetY, width,
				height);

	}

	// Methode zum Bewegen des Spielers entlang der X-Achse
	public void moveX(int x) {
		boolean right = x > 0 ? true : false;
		for (int i = 0; i < Math.abs(x); i++) {
			if (right) {
				this.setTranslateX(this.getTranslateX() + 1);
			} else {
				this.setTranslateX(this.getTranslateX() - 1);
			}
		}
	}

	// Methode zum Bewegn des Spielers entlang der Y Achse
	public void moveY(int y) {
		boolean down = y > 0 ? true : false;
		for (int i = 0; i < Math.abs(y); i++) {
			if (down) {
				this.setTranslateY(this.getTranslateY() + 1);
			} else {
				this.setTranslateY(this.getTranslateY() - 1);
			}
		}

	}
	
	public void moveTo(double x, double y) {
		while(this.getTranslateX() < x) {
			moveX(3);
		}
	}

	// Checkt ob sich der Spieler außerhalb des "Handys" befindet und falls ja, wird
	// der Spieler an der max. bzw. min. möglichen Stelle gesetzt
	public void checkBounds() {

		if(this.getTranslateY() < 0 ) {
			this.setTranslateY(0);
		}
		if(this.getTranslateX() < 0) {
			this.setTranslateX(0);
		}
		if(this.getTranslateX() > 870) {
			this.setTranslateX(870);
		}
		/*if (this.getTranslateX() > (Settings.sceneWidth * 0.25)) {
			this.setTranslateX(Settings.sceneWidth * 0.25);
		}
		if (this.getTranslateX() < -(Settings.sceneWidth * 0.377)) {
			this.setTranslateX(-(Settings.sceneWidth * 0.377));
		}*/

	}

	// Checkt Collision mit einem Gegner (Block/Feuerkugel), keine Pixelkollision
	// sondern die "Ränder" werden überprüft, ob sie sich überlappen.
	public boolean checkCollision(Sprite enemy) {
		if (this.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
			return true;
		}
		return false;

	}

	// Checkt Collision mit einem Bild, selbe Prinzip wie bei Gegner
	public boolean checkCollision(ImageView image) {
		if (this.getBoundsInParent().intersects(image.getBoundsInParent())) {
			return true;
		}
		return false;

	}

	// Benötigte Getters
	public double getHeightOfImage() {
		return height;
	}

	public double getWidthOfImage() {
		return width;
	}

	public SpriteAnimation getAnimation() {
		return animation;
	}

	public double getPositionY() {
		return this.getTranslateY();
	}

	public void setPositionY(double position) {
		this.setTranslateY(position);
	}

	public void stopTimer() {
		jumpTimer.start();
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
