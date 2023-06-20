package objects;

import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Turtle extends GameObject {

	public Turtle(int row, int col, String l) {
		super(row, col, l);
		setPrefSize(100, 100); 
		
		Image image = new Image("turtle.png");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image,
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
		        BackgroundPosition.DEFAULT, backgroundSize);
		setBackground(new Background(backgroundImage));
        //setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
	}
}