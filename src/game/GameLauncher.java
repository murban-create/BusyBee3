package game;


import guiElements.NamePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameLauncher extends Application{

	public final static String LINDE = "/linde.png";
	public final static String TULPE = "/tulpe.png";
	public final static String ROSE = "/rose.png";
	public final static String SONNENBLUME = "/sonnenblume.png";
	public final static String SCHNEEGLOECKCHEN = "/schneeglöckchen.png";
	public final static String MOHN = "/mohn.png";
	public final static String PUNKT = "/point.png";
	public final static String CHARACTER1 = "/bee.png";
	public final static String CHARACTER2 = "/bee3.png";
	public final static String REGEN = "/rain.png";
	public final static String VOGEL = "/bird.png";
	public final static String TAU = "/tau.png";
	public final static String BACKGROUND = "/Background2.png";
	public final static String NEXT ="/next.png";
	public final static String HEART="/heartBee.png";
	
	public static String namePlayerOne = "Player 1";
	public static String namePlayerTwo = "Player 2";
	public static String chosenCharacter;
	private Scene scene;
	private AnchorPane mainPane;
	@Override
	public void start(Stage arg0) throws Exception {
		mainPane = new AnchorPane();
		Label title = new Label("BuZZZZZyBee & HumbleBumble");
		title.setLayoutY(20);
		title.setId("title-label");
		mainPane.getChildren().add(title);
		Button multiPlayer = new Button("Multiplayer Mode");
		multiPlayer.setLayoutX(325);
		multiPlayer.setLayoutY(230);
		multiPlayer.setOnMouseClicked(e ->{
			NamePane namePaneMP = new NamePane(scene);
			namePaneMP.initGuiMP();
		});
		Button singlePlayer = new Button ("Ego Booster Mode");
		singlePlayer.setLayoutX(325);
		singlePlayer.setLayoutY(350);
		singlePlayer.setOnMouseClicked(e ->{
			NamePane namePaneSP = new NamePane(scene);
			namePaneSP.initGuiSP();
		});
		mainPane.getChildren().addAll(multiPlayer, singlePlayer);
		scene = new Scene(mainPane,985,588,Color.TRANSPARENT);
		scene.getStylesheets().add(getClass().getResource("/CSS/stylesheet.css").toExternalForm());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
	}
	public static void main(String[] args) {
		launch();
	}

}
