package guiElements;



import game.GameLauncher;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class NamePane extends AnchorPane{

	private final String BEE=" ";
	private final String BUMBLEBEE = " ";
	private String nameOne = " ";
	private String nameTwo = " ";
	private ImageView bee;
	private ImageView bumble;
	private Scene scene;
	private Button next;
	
	public NamePane(Scene scene) {
		scene.setRoot(this);
		//bee = new ImageView(new Image(getClass().getResource(GameLauncher.BEE).toExternalForm()));
		//bumble = new ImageView(new Image(getClass().getResource(GameLauncher.BUMBELBEE).toExternalForm()));
		next = new Button();
		next.setGraphic(new ImageView(new Image(getClass().getResource(GameLauncher.NEXT).toExternalForm())));
		next.setId("button-next");
		next.setLayoutX(924);
		next.setLayoutY(527);
		this.getChildren().add(next);
	}
	
	public void initGuiMP() {
		VBox vboxBee = new VBox();
		VBox vboxBumble = new VBox();
		vboxBumble.setLayoutX(460);
		this.getChildren().add(vboxBumble);
		this.getChildren().add(vboxBee);
		
		Label playerOne = new Label("Player One: Buzzzy Bee");
		vboxBee.getChildren().add(playerOne);
		Label playerTwo = new Label("Player Two: Humble Bumble");
		vboxBumble.getChildren().add(playerTwo);
		
		TextField textfieldOne = new TextField();
		textfieldOne.setPromptText("Please enter your nickname!");
		vboxBee.getChildren().add(textfieldOne);
		TextField textfieldTwo = new TextField();
		textfieldTwo.setPromptText("Please enter your nickname!");
		vboxBumble.getChildren().add(textfieldTwo);
		
		next.setOnMouseClicked(e ->{
			if((textfieldOne.getText() != null && !textfieldOne.getText().isEmpty())){
				GameLauncher.namePlayerOne = textfieldOne.getText();
			}
			if((textfieldTwo.getText() != null && !textfieldTwo.getText().isEmpty())){
				GameLauncher.namePlayerTwo = textfieldTwo.getText();
			}
		});
		
	}
	
	public void initGuiSP() {
		Label character = new Label("Do you wanna be?");
		this.getChildren().add(character);
		bee.setOnMouseEntered(e ->{
			bee.setEffect(new DropShadow());
		});
		bee.setOnMouseExited(e ->{
			bee.setEffect(null);
		});
		bee.setOnMouseClicked(e ->{
			bee.setEffect(new DropShadow());
			GameLauncher.chosenCharacter = "BEE";
		});
		bumble.setOnMouseEntered(e ->{
			bumble.setEffect(new DropShadow());
		});
		bumble.setOnMouseExited(e ->{
			bumble.setEffect(null);
		});
		bumble.setOnMouseClicked(e ->{
			bumble.setEffect(new DropShadow());
			GameLauncher.chosenCharacter = "BUMBLE";
		});
		TextField textfieldName = new TextField();
		textfieldName.setPromptText("Please enter your nickname!");
		this.getChildren().add(textfieldName);
		next.setOnMouseClicked(e ->{
			if((textfieldName.getText() != null && !textfieldName.getText().isEmpty())){
				GameLauncher.namePlayerOne = textfieldName.getText();
			}
			GameLauncher.namePlayerTwo = "Computer";
		});
	}
}
