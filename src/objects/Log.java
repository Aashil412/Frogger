package objects;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Log extends GameObject {
    private int direction;

    public Log(int row, int col, String label) {
        super(row, col, label);
        setPrefSize(50, 50);

        Image image = new Image("steamroller.png");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, backgroundSize);
        setBackground(new Background(backgroundImage));

        // Set the initial direction of the log
        direction = 1; // or -1 depending on your desired initial direction
    }
    private int movementCount;

    public int getMovementCount() {
        return movementCount;
    }

    public void setMovementCount(int movementCount) {
        this.movementCount = movementCount;
    }
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}