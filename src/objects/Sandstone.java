package objects;

import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Sandstone extends GameObject {

	public Sandstone(int row, int col,String l) {
		super(row, col, "Sandstone");
		setPrefSize(50, 50); 
		
		Image image = new Image("sandstone1.png");
		BackgroundSize backgroundSize = new BackgroundSize(0, 0, true, true, true, false);

		BackgroundImage backgroundImage = new BackgroundImage(image,
		        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
		        BackgroundPosition.DEFAULT, backgroundSize);
		setBackground(new Background(backgroundImage));
        //setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
	}
}
