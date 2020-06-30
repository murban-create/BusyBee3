package guiElements;

import game.BusyBeeMP;
import game.BusyBeeSP;
import game.GameLauncher;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RecipePane extends VBox{
	private BusyBeeMP busyBee;
	private BusyBeeSP busyBeeSP;
	
	private String[] pollen;
	private boolean isMP;
	private char[] recipe;
	
	public RecipePane(BusyBeeMP busyBee) {
		isMP = true;
		this.busyBee = busyBee;
		itemsToCollect();
	}
	public RecipePane(BusyBeeSP busyBee) {
		isMP = false;
		this.busyBeeSP = busyBee;
		itemsToCollect();
	}
	public void itemsToCollect() {
		Label itemsToCollectLabel = new Label("Your recipe is:");
		getChildren().add(itemsToCollectLabel); 
		HBox hbox = new HBox();
		getChildren().add(hbox);
		pollen = new String[6];
		if(isMP) {
		recipe = busyBee.getToCollect();
		}else {
		recipe = busyBeeSP.getToCollect();
		}
		for(int i = 0; i < 6; i++) {
			switch (recipe[i]) {
			case 'r':
				ImageView rose = new ImageView(new Image(getClass().getResource(GameLauncher.ROSE).toExternalForm()));
				pollen[i] = GameLauncher.ROSE;
				rose.setVisible(true);
				hbox.getChildren().add(rose);
				break;
			case 't':
				ImageView tulpe = new ImageView(new Image(getClass().getResource(GameLauncher.TULPE).toExternalForm()));
				pollen[i] = GameLauncher.TULPE;
				hbox.getChildren().add(tulpe);
				break;
			case 'o':
				ImageView sonnenblume = new ImageView(new Image(getClass().getResource(GameLauncher.SONNENBLUME).toExternalForm()));
				pollen[i] = GameLauncher.SONNENBLUME;
				hbox.getChildren().add(sonnenblume);
				break;
			case 's':
				ImageView schneegloeckchen = new ImageView(new Image(getClass().getResource(GameLauncher.SCHNEEGLOECKCHEN).toExternalForm()));
				pollen[i] = GameLauncher.SCHNEEGLOECKCHEN;
				hbox.getChildren().add(schneegloeckchen);
				break;
			case 'l':
				ImageView linde = new ImageView(new Image(getClass().getResource(GameLauncher.LINDE).toExternalForm()));
				pollen[i] = GameLauncher.LINDE;
				hbox.getChildren().add(linde);
				break;
			case 'm':
				ImageView mohn = new ImageView(new Image(getClass().getResource(GameLauncher.MOHN).toExternalForm()));
				pollen[i] = GameLauncher.MOHN;
				hbox.getChildren().add(mohn);
				break;
			}
		}
		
	}
	public String[] getPollen() {
		return pollen;
	}
	public void setPollen(String[] pollen) {
		this.pollen = pollen;
	}
	
}
