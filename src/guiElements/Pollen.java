package guiElements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pollen extends ImageView {
	private double maxTransition;
	
	public Pollen(Image image) {
		this.setImage(image);
		int area = (int)(Math.random()*6);
		if(area == 0 || area == 1 || area == 2) {
			maxTransition = (int) (Math.random()*196)+196;
		}else if(area == 3 || area == 4) {
			maxTransition =(int) (Math.random()*196);
		}else {
			maxTransition =(int) (Math.random()*196)+392;
		}
	}

	public double getMaxTransition() {
		return maxTransition;
	}

	public void setMaxTransition(double maxTransition) {
		this.maxTransition = maxTransition;
	}
	

}
