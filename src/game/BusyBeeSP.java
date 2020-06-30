package game;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import entities.Player;
import entities.Sprite;
import guiElements.Pollen;
import guiElements.RecipePane;
import guiElements.ScorePanel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BusyBeeSP extends Application{
	
	private String pathPollen = "res/salamisound-5450416-tischglocke-einmal.mp3";  
	private String pathThunder = "res/salamisound-3039124-regen-mit-donner-langes.mp3";  
	private String pathEnemy = "res/salamisound-3569093-flachen-stein-in-wasser.mp3";  
	
	private Media mediaEnemy = new Media(new File(pathEnemy).toURI().toString()); 
	private Media mediaPollen = new Media(new File(pathPollen).toURI().toString());  
	private Media mediaThunder = new Media(new File(pathThunder).toURI().toString());
	
	private List<Sprite> spriteList = new ArrayList<Sprite>();
	private List<Pollen>pollenList = new ArrayList<Pollen>();
	private List<Sprite> toremoveSprites = new ArrayList<Sprite>();
	private List<ImageView>toRemovePollen = new ArrayList<ImageView>();
	private ArrayList<String> input = new ArrayList<String>();
	private HashMap<Character, String> recipe = new HashMap<>();
	
	private boolean isFalling1 = true;
	private boolean isFalling2 = true;
	private boolean gameOver = false;
	private boolean moveRight = false;
	private boolean moveLeft = false;
	private boolean moveUp = false;
	private boolean isRunning = false;
	private boolean isWind = false;
	private boolean eastWind = false;
	
	private AnchorPane mainPane;
	private AnimationTimer timer;
	private Player player;
	private Player computer;
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
	
	private int playerLife;
	private int computerLife;
	private int collectedPollenPlayer = 0;
	private int collectedPollenComputer = 0;
	private int enemySpeed = 3;
	private int playerSpeed;
	private int playerSpeedRun;
	private int regenRandom;
	private int vogelRandom = 50;
	private double startTime;
	
	private MediaPlayer thunder;
	private MediaPlayer enemyCollision;
	private MediaPlayer pollenCollision;
	
	@Override
	
	public void start(Stage arg0) throws Exception {
	
	mainPane = new AnchorPane();
	ImageView background = new ImageView(new Image("file:res/Background2.png"));
	background.fitHeightProperty().bind(mainPane.heightProperty());
	mainPane.getChildren().add(background);
	
	scene = new Scene(mainPane,985,588,Color.TRANSPARENT);
	Stage stage = new Stage();
	stage.setScene(scene);
	stage.show();
	createRecipe();
	RecipePane recipePane = new RecipePane(this);
	recipePane.setLayoutX(260);
	mainPane.getChildren().add(recipePane);
	scorePanel1 = new ScorePanel(recipePane.getPollen(),1);
	mainPane.getChildren().add(scorePanel1);
	scorePanel2 = new ScorePanel(recipePane.getPollen(),2);
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
		if(!input.contains(code)) {
			input.add(code);
		}
				});
	scene.setOnKeyReleased(event -> {
		String code = event.getCode().toString();
        input.remove( code );
	});
	
	player = new Player(GameLauncher.CHARACTER1);
	computer = new Player(GameLauncher.CHARACTER2);
	player.setLayoutX(500);
	computer.setLayoutX(0);
	mainPane.getChildren().addAll(player,computer);
	
	timer = new AnimationTimer() {
		
		@Override
		public void handle(long arg0) {
			updatePlayer();
			updateComputer();
			createPollen();
			createEnemies();
			checkBoundsComputer();
			checkBoundsPlayer();
			movePollen();
			moveRain();
			checkCollisionSprite();
			checkCollisionPollen();
			moveComputer();
			createWind();
			removeEnemy();
			if(!toRemovePollen.isEmpty()) {
				pollenList.removeAll(toRemovePollen);
				toRemovePollen.clear();
			}
			if(!toremoveSprites.isEmpty()) {
				spriteList.removeAll(toremoveSprites);
				toremoveSprites.clear();
			}
			createTau();
			moveTau();
			removeTau();
			setGravity();
			removePollen();
			
		}
	};
	timer.start();
}

private  void createRecipe() {
	toCollect = new char[6];
	for(int i = 0; i < 6; i++) {
		int random = (int) (Math.random()*6);
		switch (random) {
		case 0:
			toCollect[i] = 'l';
			recipe.put('l', GameLauncher.LINDE);
			break;
		case 1:
			toCollect[i]='r';
			recipe.put('r', GameLauncher.ROSE);
			break;
		case 2:
			toCollect[i]='s';
			recipe.put('s', GameLauncher.SCHNEEGLOECKCHEN);
			break;
		case 3:
			toCollect[i]='m';
			recipe.put('m', GameLauncher.MOHN);
			break;
		case 4:
			toCollect[i]='o';
			recipe.put('o', GameLauncher.SONNENBLUME);
			break;
		case 5:
			toCollect[i]='t';
			recipe.put('t', GameLauncher.TULPE);
			break;
		}
	}
	
}

public void createEnemies() {
	if(isWind) {
		regenRandom = 900;
	}
	int random = (int) (Math.random()* regenRandom);
	if (random == 100 || random == 300) {
		rain = new Sprite(GameLauncher.REGEN,5,5,20,20);
		setSpritePosition(rain,Math.random()*944,0);
		spriteList.add(rain);
		mainPane.getChildren().add(rain);
	}
	/*else if(random == 200) {
		bird = new Enemy(VOGEL,5,5,100,200);
		setSpritePosition(bird,854,Math.random()*200);
		enemyList.add(bird);
		mainPane.getChildren().add(bird);
	}*/
}

public void setSpritePosition(Sprite enemy, double x , double y) {
	  enemy.setLayoutY(y);
	  enemy.setLayoutX(x);
}

public void moveRain() {
	if(!spriteList.isEmpty() && spriteList.contains(rain)){
	spriteList.forEach(raindrop ->{
		raindrop.getAnimation().play();
		raindrop.moveY(enemySpeed);
		if(isWind) {
			if(eastWind) {
				raindrop.moveX(1);
			}else {
				raindrop.moveX(-1);
			}
		}
	});
	}
}
public void moveBird() {
	if(!spriteList.isEmpty() && spriteList.contains(bird)){
	spriteList.forEach(bird ->{
		bird.getAnimation().play();
		if(isWind) {
			bird.moveX(-2);
		}else {
			bird.moveX(-enemySpeed);
		}
	});
	}
}

public void createPollen() {
	int random = (int) (Math.random()*1200+1);
	switch(random) {
	case 100:{
		linde = new Pollen(new Image(GameLauncher.LINDE));
		pollenList.add(linde);
		setPositionPollen(linde);
		mainPane.getChildren().add(linde);
		break;
	}
	case 200:{
		rose = new Pollen(new Image(GameLauncher.ROSE));
		pollenList.add(rose);
		setPositionPollen(rose);
		mainPane.getChildren().add(rose);
		break;
	}
	case 300:{
		sonnenblume = new Pollen(new Image(GameLauncher.SONNENBLUME));
		pollenList.add(sonnenblume);
		setPositionPollen(sonnenblume);
		mainPane.getChildren().add(sonnenblume);
		break;
	}
	case 400:{
		mohn = new Pollen(new Image(GameLauncher.MOHN));
		pollenList.add(mohn);
		setPositionPollen(mohn);
		mainPane.getChildren().add(mohn);
		break;
	}
	case 500:{
		schneegloeckchen = new Pollen(new Image(GameLauncher.SCHNEEGLOECKCHEN));
		pollenList.add(schneegloeckchen);
		setPositionPollen(schneegloeckchen);
		mainPane.getChildren().add(schneegloeckchen);
		break;
	}
	case 600:{
		tulpe = new Pollen(new Image(GameLauncher.TULPE));
		pollenList.add(tulpe);
		setPositionPollen(tulpe);
		mainPane.getChildren().add(tulpe);
		break;
	}
	case 700:{
		punkt = new Pollen(new Image(GameLauncher.PUNKT));
		pollenList.add(punkt);
		setPositionPollen(punkt);
		mainPane.getChildren().add(punkt);
		break;
	}
	case 800:{
		punkt = new Pollen(new Image(GameLauncher.PUNKT));
		pollenList.add(punkt);
		setPositionPollen(punkt);
		mainPane.getChildren().add(punkt);
		break;
	}
	}
}

private void setPositionPollen(ImageView pollen) {
	pollen.setLayoutY(0);
	pollen.setLayoutX(Math.random()*944);
}
private void movePollen() {
	if(!pollenList.isEmpty()) {
	pollenList.forEach(pollen ->{
		pollen.setLayoutY(pollen.getLayoutY() + 3);
		pollen.setRotate(pollen.getRotate() + 4);
		if(isWind) {
			if(eastWind) {
				pollen.setLayoutX(pollen.getLayoutX() +1);
			}else {
				pollen.setLayoutX(pollen.getLayoutX() -1);
			}
		}
	});
	}
}

private void createTau() {
	int random = (int)(Math.random()*1000);
	if(random == 100) {
		tau = new Sprite(GameLauncher.TAU, 5, 5, 41, 41);
		setSpritePosition(tau, Math.random()*944, 0);
		spriteList.add(tau);
		mainPane.getChildren().add(tau);
	}
	
}

private void moveTau() {
	if(!spriteList.isEmpty() && spriteList.contains(tau)){
		spriteList.forEach(tautropfen ->{
			tautropfen.getAnimation().play();
			tautropfen.moveY(3);
			if(isWind) {
				if(eastWind) {
					tautropfen.moveX(1);
				}else {
					tautropfen.moveX(-1);
				}
			}
		});
		}
}

private void removeTau() {
	if(!spriteList.isEmpty() && spriteList.contains(tau)){
		spriteList.forEach(tautropfen ->{
			if(tautropfen.getTranslateY() > 486) {
				mainPane.getChildren().remove(tautropfen);
			}
		});
		}
}

private void updatePlayer() {
	if(isWind) {
		playerSpeed = 2;
		playerSpeedRun = 3;
	}
	else {
		playerSpeed = 4;
		playerSpeedRun = 8;
	}
	if (input.contains("RIGHT")) {
		player.getAnimation().play();
		player.getAnimation().setOffsetY(216);
		if(input.contains("M")) {
			player.moveX(playerSpeedRun);
		}else {
			player.moveX(playerSpeed);
		}
	} else if (input.contains("LEFT")) {
		player.getAnimation().play();
		player.getAnimation().setOffsetY(108);
		if(input.contains("M")) {
			player.moveX(-playerSpeedRun);
		}else {
			player.moveX(-playerSpeed);
		}
	} else if(input.contains("UP")) {
		player.getAnimation().play();
		player.getAnimation().setOffsetY(0);
		isFalling1 = false;
		if(input.contains("M")) {
			player.moveY(-playerSpeedRun);
		}else {
			player.moveY(-playerSpeed);
		}
	} 
	else {
		isFalling1 = true;
	}
}
private void updateComputer() {
	int randomMovement = (int) (Math.random() * 550);
	switch(randomMovement) {
	case 100:{
		moveLeft = true;
		moveRight = false;
		moveUp = false;
		break;
	}
	case 200:{
		moveLeft = false;
		moveRight = true;
		moveUp = false;
		break;
	}case 300:{
		moveLeft = false;
		moveRight = false;
		moveUp = true;
		break;
	}case 400:{
		isRunning = true;
	}case 500:{
		isRunning = false;
	}
	}
	
}

private void moveComputer() {
	if(moveLeft) {
		computer.getAnimation().setOffsetY(108);
		computer.getAnimation().play();
		if(isRunning) {
			computer.moveX(-playerSpeedRun);
		}else {
			computer.moveX(-playerSpeed);
		}
		if(computer.getTranslateX() < 0) {
			moveLeft = false;
		}
	}
	else if(moveRight) {
		computer.getAnimation().setOffsetY(216);
		computer.getAnimation().play();
		if(isRunning) {
			computer.moveX(playerSpeedRun);
		}else {
			computer.moveX(playerSpeed);
		}
		if(computer.getTranslateX() > 870) {
			moveLeft = false;
		}
	}
	else if(moveUp) {
		computer.getAnimation().setOffsetY(0);
		computer.getAnimation().play();
		isFalling2 = false;
		if(isRunning) {
			computer.moveY(-playerSpeedRun);
		}else {
			computer.moveY(-playerSpeed);
		}
		if(computer.getTranslateY() < 0) {
			moveUp = false;
		}
	}else {
		isFalling2 = true;
	}
}
private void checkCollisionSprite(){
	if(!spriteList.isEmpty()) {
	spriteList.forEach(sprite ->{
		if(player.checkCollision(sprite)) {
			toremoveSprites.add(sprite);
			mainPane.getChildren().remove(sprite);
			if(sprite.getImageURL().equals(GameLauncher.REGEN) ||sprite.getImageURL().equals(GameLauncher.VOGEL) ) {
				enemyCollision = new MediaPlayer(mediaEnemy);  
				enemyCollision.setAutoPlay(true);
				removeLife(player);
			} else if(sprite.getImageURL().equals(GameLauncher.TAU)) {
				scorePanel1.addPower();
			}
		}
		if(computer.checkCollision(sprite)) {
			toremoveSprites.add(sprite);
			mainPane.getChildren().remove(sprite);
			if(sprite.getImageURL().equals(GameLauncher.REGEN) ||sprite.getImageURL().equals(GameLauncher.VOGEL) ) {
				enemyCollision = new MediaPlayer(mediaEnemy);  
				enemyCollision.setAutoPlay(true);
				removeLife(computer);
			}
			else if(sprite.getImageURL().equals(GameLauncher.TAU)) {
				scorePanel2.addPower();
			}
		}
	});
}
}
private void setGravity() {
	if(isFalling1) {
	if(player.getTranslateY() < 486) {
		player.moveY(playerSpeed);
	}
	if(isWind) {
		if(eastWind) {
			player.moveX(2);
		}else {
			player.moveX(-2);
		}
	}
	}
	if(isFalling2) {
		if(computer.getTranslateY() < 486) {
			computer.moveY(playerSpeed);
		}
		if(isWind) {
			if(eastWind) {
				computer.moveX(2);
			}else {
				computer.moveX(-2);
			}
		}
	}
}
private void removeEnemy() {
	if(!spriteList.isEmpty()) {
		spriteList.forEach(enemy ->{
			if(enemy.equals(rain) && enemy.getTranslateY() > 588 - enemy.getHeight()) {
				toremoveSprites.add(enemy);
				mainPane.getChildren().remove(enemy);
			}
			else if(enemy.equals(bird) && enemy.getTranslateX() < - 944) {
				toremoveSprites.add(enemy);
				mainPane.getChildren().remove(enemy);
			}
			
		});
	}
}

private void checkCollisionPollen() {
	if(!pollenList.isEmpty()) {
		pollenList.forEach(pollen ->{
			if(player.checkCollision(pollen)) {
				mainPane.getChildren().remove(pollen);
				toRemovePollen.add(pollen);
				if(pollen.getImage().getUrl().equals(recipe.get(toCollect[collectedPollenPlayer]))) {

				    pollenCollision = new MediaPlayer(mediaPollen);  
					pollenCollision.setAutoPlay(true);
					scorePanel1.getPollenImageList().get(collectedPollenPlayer).setVisible(true);
					collectedPollenPlayer++;
				}else if (pollen.getImage().getUrl().equals(GameLauncher.PUNKT)) {
				    pollenCollision = new MediaPlayer(mediaPollen);  
					pollenCollision.setAutoPlay(true);
					Integer point = scorePanel1.getPoints()+100;
					scorePanel1.setPoints(point);
					scorePanel1.getCollectedPoints().setText(point.toString());
				}
			}
			if(computer.checkCollision(pollen)) {
				mainPane.getChildren().remove(pollen);
				toRemovePollen.add(pollen);
				if(pollen.getImage().getUrl().equals(recipe.get(toCollect[collectedPollenComputer]))) {
					pollenCollision = new MediaPlayer(mediaPollen);  
					pollenCollision.setAutoPlay(true);
					scorePanel2.getPollenImageList().get(collectedPollenComputer).setVisible(true);
					collectedPollenComputer++;
				}else if(pollen.getImage().getUrl().equals(GameLauncher.PUNKT)) {
					pollenCollision = new MediaPlayer(mediaPollen);  
					pollenCollision.setAutoPlay(true);
					Integer point = scorePanel2.getPoints()+100;
					scorePanel2.setPoints(point);
					scorePanel2.getCollectedPoints().setText(point.toString());
				}
			}
		});
	}
}

private void removePollen() {
	if(!pollenList.isEmpty()) {
		//System.out.println("position" + position);
		pollenList.forEach(pollen ->{
			if(pollen.getLayoutY() > pollen.getMaxTransition()) {
				toRemovePollen.add(pollen);
				mainPane.getChildren().remove(pollen);
			}
		});
	}
}
private void removeLife(Player player) {
	if(player.equals(player)) {
		playerLife--;
		scorePanel1.removeLive(playerLife);
	}else {
		computerLife--;
		scorePanel2.removeLive(computerLife);
	}
}

public char[] getToCollect() {
	return toCollect;
}

public void setToCollect(char[] toCollect) {
	this.toCollect = toCollect;
}

private void changeRandomnessAndSpeed() {
	if(System.currentTimeMillis() - startTime > 180000) {
		vogelRandom = 100;
		regenRandom = 120;
	}
	else if(System.currentTimeMillis() - startTime > 270000) {
		enemySpeed = 4;
	}
}
private void setGameOver() {
	if(scorePanel1.getPollenImageList().get(0).isVisible() && scorePanel1.getPollenImageList().get(1).isVisible() && scorePanel1.getPollenImageList().get(2).isVisible() && scorePanel1.getPollenImageList().get(3).isVisible() && scorePanel1.getPollenImageList().get(4).isVisible() && scorePanel1.getPollenImageList().get(5).isVisible()) {
		gameOver = true;
		player.setWon(true);
	}
	else if(scorePanel2.getPollenImageList().get(0).isVisible() && scorePanel2.getPollenImageList().get(1).isVisible() && scorePanel2.getPollenImageList().get(2).isVisible() && scorePanel2.getPollenImageList().get(3).isVisible() && scorePanel2.getPollenImageList().get(4).isVisible() && scorePanel2.getPollenImageList().get(5).isVisible()){
		gameOver = true;
		computer.setWon(true);
	}else if(playerLife == 0 && computerLife != 0) {
		gameOver = true;
		computer.setWon(true);
	}else if(computerLife == 0 && playerLife != 0) {
		gameOver = true;
		player.setWon(true);
	}else if (computerLife == 0 && playerLife == 0) {
		gameOver = true;
	}
	
}

private void isGameOver() {
	if (gameOver) {
		if(player.isWon() && computer.isWon()) {
			System.out.print("Tied");
		}else if(player.isWon() && !computer.isWon()) {
			System.out.print("Player1 won");
		}else if (!player.isWon() && computer.isWon()) {
			System.out.print("Player2 won");
		}else {
			System.out.print("Both lost");
		}
	}

}

private void checkBoundsPlayer() {
	if(player.getTranslateY() < 0 ) {
		player.setTranslateY(0);
	}
	if(player.getTranslateX() > 0) {
		player.setTranslateX(0);
	}
	if(player.getTranslateX() < -870) {
		player.setTranslateX(870);
	}
}

private void checkBoundsComputer() {
	if(computer.getTranslateY() < 0 ) {
		computer.setTranslateY(0);
	}
	if(computer.getTranslateX() < 0) {
		computer.setTranslateX(0);
	}
	if(computer.getTranslateX() > 870) {
		computer.setTranslateX(870);
	}
}

private void createWind() {
	if(!isWind) {
		int random = (int)(Math.random()*1000);
		if (random == 100) {
			thunder = new MediaPlayer(mediaThunder);  
			thunder.setCycleCount(MediaPlayer.INDEFINITE);
			thunder.setAutoPlay(true);
			System.out.println("createWinde");
			isWind = true;
			int direction = (int) (Math.random()*2);
			if(direction == 0) {
				eastWind = true;
			}else {
				eastWind = false;
			}
			
		}
		
	}
	else {
		int random = (int)(Math.random()*700);
		if(random == 100) {
			System.out.println("stopWind");
			thunder.stop();
			isWind = false;

		}
	}
	
}
public static void main(String[] args) {
	launch();
}

}
