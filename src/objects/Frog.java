package objects;

import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Frog extends GameObject {

	public Frog(int row, int col) {
		super(row, col, "FROG");
		setPrefSize(50, 50); 
		
		Image image = new Image("frog.png");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image,
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
		        BackgroundPosition.DEFAULT, backgroundSize);
		setBackground(new Background(backgroundImage));
        //setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
	}
}
