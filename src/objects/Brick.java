package objects;

import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/*
 * This is a derived class that extends its parent class GameObject
 * The purpose of this class is to create a better look for the background of the matrix
 */
public class Brick extends GameObject {

	public Brick(int row, int col,String l) {
		super(row, col, "Brick");
		//created the size of the Brick based on its scale
		setPrefSize(50, 50); 
		
		//Imported an image for the Brick and set it as the image for the object
		Image image = new Image("brick.png");
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);

		BackgroundImage backgroundImage = new BackgroundImage(image,
		        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
		        BackgroundPosition.DEFAULT, backgroundSize);
		setBackground(new Background(backgroundImage));
//        setBackground(new Background(new BackgroundFill(null, null, null)));
	}
}
