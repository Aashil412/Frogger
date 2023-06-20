package objects;

import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Street extends GameObject {

	public Street(int row, int col,String l) {
		super(row, col, l);
		setPrefSize(50, 50); 
		
		Image image = new Image("street12.png");
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);

		BackgroundImage backgroundImage = new BackgroundImage(image,
		        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
		        BackgroundPosition.DEFAULT, backgroundSize);
		setBackground(new Background(backgroundImage));
        //setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
	}
}
