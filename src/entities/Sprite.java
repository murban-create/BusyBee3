package entities;

import game.GameLauncher;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Sprite extends AnchorPane {
	private ImageView imageView;
	private int offsetX = 0;
	private int offsetY = 0;
	private SpriteAnimation animation;
	private String imageURL;

	//Konstruktor
	public Sprite(String imageName, int count, int columns, int width, int height) {
		this.imageURL = imageName;
		Image image = new Image(getClass().getResource(imageName).toExternalForm());
		imageView = new ImageView(image);
		imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
		imageView.setPreserveRatio(true);
		imageView.setEffect(new DropShadow());
		imageView.fitHeightProperty().bind(this.heightProperty());
		getChildren().add(imageView);
		animation = new SpriteAnimation(imageView, Duration.millis(200), count, columns, offsetX, offsetY, width,
				height);

	}

	//Methode zum Bewegen entlang der Y Achse des Gegners
	public void moveY(double speed) {
		for (int i = 0; i < Math.abs(speed); i++) {
			this.setTranslateY(this.getTranslateY() + 1);
		}
	}
	
	public void moveX(double speed) {
		boolean right = speed > 0 ? true : false;
		for (int i = 0; i < Math.abs(speed); i++) {
			if (right) {
				this.setTranslateX(this.getTranslateX() + 1);
			} else {
				this.setTranslateX(this.getTranslateX() - 1);
			}
		}
	}

	// Getter und Setter
	public SpriteAnimation getAnimation() {
		return animation;
	}

	public void setAnimation(SpriteAnimation animation) {
		this.animation = animation;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

}
