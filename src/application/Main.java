package application;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import javafx.scene.media.*;
import javafx.animation.*;


public class Main extends Application {
	
	private AnimationTimer gameLoop; // Animation timer for game loop
    private ImageView imageView; // Image view
    private Label logInInfoLabel; // Label for the login information
    private TextField usernameField; // Text field for the username input
    private PasswordField passwordField; // Password field for the password input
    private Button logInButton; // The Button for the login
    private Button playButton; // The Button for playing the game
    private Button Controls;  // The Button for accessing controls
   
    private long lastUpdate = 0;  // Variable to track last update time
	private static final long UPDATE = 200_000_000;
    Stage window; // the main Stage
    Scene scene1, scene2,scene3,scene4;  // All the scenes
    Pane root; // Root pane for the scenes
    Screen screen = Screen.getPrimary(); // Primary screen
    Rectangle2D bounds = screen.getVisualBounds(); // Bounds of the screen
    double screenWidth = bounds.getWidth(); // Width of the screen
    double screenHeight = bounds.getHeight(); // Height of the screen
    private int score;
    private Label scoreLabel;
    File soundFile = new File("src/themesong.mp3"); //getting the theme song
	Media media = new Media(soundFile.toURI().toString()); //converting it 
	MediaPlayer player = new MediaPlayer(media); 
    @Override
    public void start(Stage primaryStage) {
    	
    	
    	
    	music(); //Start to play the theme song     	
    	window = primaryStage;
    	GameBoard gameBoard = new GameBoard(screenWidth,screenHeight);
        // Creating the game scene
    	scene2 = new Scene(gameBoard);

    	window.setTitle("Frogger"); //setting the title
   	    Image icon = new Image("FroggerIcon.png"); //setting the icon
    	window.getIcons().add(icon); //adding the icon to the stage
        imageView = new ImageView(new Image("FroggerStartingScreen.png")); //adding the starter screen image
        
        // Creating the login elements 
        logInInfoLabel = new Label();
        logInInfoLabel.setTextFill(Color.BLACK);
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        
        logInButton = new Button("Log In");
      
        logInButton.setId("logInButton"); //setting the id for the login button
        logInButton.setOnAction(e -> userLogin()); //action for when you press the login
        EventHandler<KeyEvent> enterKeyPressHandler = event -> { //if you press enter the login works 
            if (event.getCode() == KeyCode.ENTER) {
               logInButton.fire();
            }
        };


        logInButton.setOnKeyPressed(enterKeyPressHandler);

        // Creating the game loop for animation
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	if (now - lastUpdate >= UPDATE) {
                    gameBoard.moveObject(); 
                    lastUpdate = now;
                }
            }
        };
        	    
	    
        root = new Pane(); //creating my root as a Pane
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); //setting the background of the root
        root.setPrefSize(500, 600); //setting the size of the root
        root.getChildren().addAll(imageView, logInInfoLabel, usernameField, passwordField, logInButton); //adding the necessities to the root
        
        //setting the images and buttons location and size
        imageView.setFitWidth(600);
        imageView.setFitHeight(600);
        logInInfoLabel.setLayoutX(30);
        logInInfoLabel.setLayoutY(450);
        usernameField.setLayoutX(28);
        usernameField.setLayoutY(472);
        passwordField.setLayoutX(28);
        passwordField.setLayoutY(505);
        logInButton.setLayoutX(67);
        logInButton.setLayoutY(544);

        // Creating and styling the label
      Label label = new Label("Frogger");
      Font fone = Font.loadFont("file:resources/fonts/font1.ttf", 60); //adding fonts
 
      label.setId("label"); //setting the label to have an ID for CSS
      label.setFont(fone); //setting the font to the label
      
      //adding a bit of DropShadow to the label 
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10);
    dropShadow.setOffsetX(0);
    dropShadow.setOffsetY(0);
    label.setEffect(dropShadow); //setting the effect of the label
    root.getChildren().add(label); //adding the label to the root
   

    BorderPane.setAlignment(label, Pos.CENTER); //setting the BorderPane to the center
    
    // Creating the transitions for label animation
    TranslateTransition translateIn = new TranslateTransition(Duration.seconds(2), label); //goes in from the left
    translateIn.setFromX(-300);
    translateIn.setToX(50);
    translateIn.setFromY(70);
    translateIn.setToY(70);
    TranslateTransition translateOut = new TranslateTransition(Duration.seconds(2), label); //comes out from the right
    translateOut.setFromX(50);
    translateOut.setToX(500);
    translateOut.setFromY(70);
    translateOut.setToY(70);
    RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), label); //duration it rotates for
    rotateTransition.setByAngle(360); // Rotate by 360 degrees
    rotateTransition.setCycleCount(1); // Rotate only once
    rotateTransition.setInterpolator(Interpolator.LINEAR); 
    rotateTransition.setAxis(Rotate.Y_AXIS); // Rotate around the Y-axis
 
    
    SequentialTransition sequentialTransition = new SequentialTransition( //making it come in and out after 1 second
            translateIn,
            new PauseTransition(Duration.seconds(1)),
            translateOut,
            new PauseTransition(Duration.seconds(1))
    );
    sequentialTransition.setOnFinished(event -> {
        sequentialTransition.play(); //restarting after it exits
    });
    
    sequentialTransition.play(); //playing the transition so it comes in and out of the screen
    rotateTransition.play(); //rotating the label
    

   
        scene1 = new Scene(root); //setting the first scene to the root
        scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); //applying CSS to the scene
        window.setScene(scene1); //setting the stage to the scene
        window.setResizable(false);
        window.show(); //showing the stage
    }
    
    private boolean userLogin() {
    	// if username and password  = "1", We have successfully logged in
      if(usernameField.getText().toString().equals("1") && passwordField.getText().toString().equals("1")) {

    	  // Creating the buttons for the starting screen
    	  playButton = new Button("Play");
    	  Controls = new Button("Controls");
         //  = new Button("High Score");
          
          //setting the buttons sizes
    	  playButton.setMinSize(10, 10); 
    	  playButton.setPrefSize(150, 40); 
    	  playButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    	  Controls.setMinSize(10, 10); 
          Controls.setPrefSize(150, 40); 
          Controls.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
     
    	  
    	  
          // Setting the buttons actions and positions
          playButton.setOnAction(e -> button1());
          playButton.setLayoutX(170);
          playButton.setLayoutY(200); 
          playButton.setFont(Font.font("System", FontWeight.BOLD, 14));
       
         
       
          Controls.setLayoutX(170);
          Controls.setLayoutY(340); 
          Controls.setFont(Font.font("System", FontWeight.BOLD, 14));
        
          // Setting the button IDs for CSS
          playButton.setId("playButton");
         
          Controls.setId("Controls");

          // Setting the button hover sounds
          playButton.setOnMouseEntered(event ->hoverSounds() );
         
          Controls.setOnMouseEntered(event -> hoverSounds() );
       // Setting the button action for Controls
          Controls.setOnAction(e -> button2());
          // Addding the buttons to the root pane
          root.getChildren().addAll(playButton,Controls);
    	  return true; // Return true to indicate successful login
    	}
      else if(usernameField.getText().isEmpty() && passwordField.getText().isEmpty()) {
    	  logInInfoLabel.setText("Please enter your data.");
    	  return false; // The username box and password box is empty
      }


      else {
    	// Invalid username or password
    	  logInInfoLabel.setText("Wrong username or password!");
    	  
    	  return false;// Return false to indicate login failure
      }

    }

    private void centerWindow(Stage window) { //centering stage function
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // Getting the bounds of the primary screen
        
        // Calculating the center coordinates of the window
        double centerX = (screenBounds.getWidth() - window.getWidth()) / 2;
        double centerY = (screenBounds.getHeight() - window.getHeight()) / 2;
        window.setX(centerX); //setting the X center coordinate 
        window.setY(centerY); //setting the Y center coordinate
    }

    private void button1() { //switch to scene 2 function
        window.setScene(scene2); //setting the stage to the gameboard
        window.setWidth(screenWidth); //setting the width of the stage
        window.setHeight(screenHeight); //setting the height of the stage
        
        centerWindow(window); // centering the stage
        gameLoop.start(); // starting the gameloop
    }
    private void button2() { //switching to the Controls function 
    	AnchorPane root1 = new AnchorPane(); // AnchorPane is the root container
        root1.setStyle("-fx-background-color: grey;");
    	root1.setId("root1");
    	// Creating the Label for the "Controls" text
        Label controlsLabel = new Label("Controls:");
        controlsLabel.setLayoutX(14.0);
        controlsLabel.setLayoutY(2.0);
        controlsLabel.setPrefSize(310.0, 88.0);
        controlsLabel.setTextFill(new LinearGradient(1.0, 0.17803033192952475, 1.0, 1.0, true, null, new Stop[] {
            new Stop(0, Color.BLACK),
            new Stop(1, Color.WHITE)
        }));
        controlsLabel.setFont(new Font(72.0));
        
     // Adding the controlsLabel to the root1 AnchorPane
        root1.getChildren().add(controlsLabel);
        
     // Creating the ImageView for the arrow key image
        ImageView arrowKeyImage = new ImageView(new Image("ArrowKey.png"));
        arrowKeyImage.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);
        arrowKeyImage.setFitWidth(301.0);
        arrowKeyImage.setFitHeight(301.0);
        arrowKeyImage.setLayoutY(99.0);
        arrowKeyImage.setPickOnBounds(true);
        arrowKeyImage.setPreserveRatio(true);
        arrowKeyImage.setEffect(new InnerShadow());
        root1.getChildren().add(arrowKeyImage);
        
     // Creating the ImageView for the WASD key image
        ImageView wasdKeyImage = new ImageView(new Image("WASDKey.png"));
        wasdKeyImage.setBlendMode(javafx.scene.effect.BlendMode.MULTIPLY);
        wasdKeyImage.setFitWidth(301.0);
        wasdKeyImage.setFitHeight(309.0);
        wasdKeyImage.setLayoutX(299.0);
        wasdKeyImage.setLayoutY(99.0);
        wasdKeyImage.setPickOnBounds(true);
        wasdKeyImage.setPreserveRatio(true);
        wasdKeyImage.setEffect(new InnerShadow());
        root1.getChildren().add(wasdKeyImage);
        
     // Creating the ImageView for the back arrow image
        ImageView backArrowImage = new ImageView(new Image("BackArrow.png"));
        backArrowImage.setBlendMode(javafx.scene.effect.BlendMode.DARKEN);
        backArrowImage.setFitWidth(168.0);
        backArrowImage.setFitHeight(61.0);
        backArrowImage.setLayoutX(330.0);
        backArrowImage.setLayoutY(14.0);
        backArrowImage.setPickOnBounds(true);
        backArrowImage.setPreserveRatio(true);

        root1.getChildren().add(backArrowImage);
     // Creating the Button for the "Back" button when you want to go back to the strting scene
        Button back = new Button("Back");
        back.setLayoutX(456.0);
        back.setLayoutY(19.0);
        back.setPrefSize(133.0, 49.0);
      
     // Giving the button a slight Lighting effect
        Lighting lighting2 = new Lighting();
     
        lighting2.setSurfaceScale(4.0);
        back.setEffect(lighting2);
     
        back.setOnAction(e->button3()); //functionality of the back button
        root1.getChildren().add(back); //adding the back button
        	
    	scene3 = new Scene(root1); // setting the scene to the root
    	
        window.setScene(scene3); //setting the stage to the controls
        
		window.setFullScreen(false); //not allowed to make the game fullscreen
 
        window.show(); //showing the window
        
     
    }
    private void button3() {
    	window.setScene(scene1); //switching to scene1
    }
	public void music() {
		
		player.setOnEndOfMedia(() -> {  // Setting up the media player to restart from the beginning when the music reaches the end
			player.seek(Duration.ONE);
		});
		player.play(); //playing the music
	}
	
	
	public void hoverSounds() {
		
		 File audioFile = new File("src/buttonHover.mp3"); //Defining the media path

       
         Media media = new Media(audioFile.toURI().toString());  // Create the media object from the audio file
        MediaPlayer mediaPlayer = new MediaPlayer(media);
         mediaPlayer.setOnEndOfMedia(()->{   // Setting up the media player to stop and restart when the audio finishes playing
        	 mediaPlayer.seek(Duration.ZERO);
        	 mediaPlayer.setOnEndOfMedia(mediaPlayer::stop);
         });

         playButton.setOnMouseExited(event -> { //when you exit your mouse the playButton stops playing the sound
             mediaPlayer.stop(); //stopping the sound
         });
         
         Controls.setOnMouseExited(event -> {//when you exit your mouse the Controls button stops playing the sound
             mediaPlayer.stop(); //stopping the sound
         });
         mediaPlayer.play(); //playing the sound
         
        
	}
	
    public static void main(String[] args) { //main function
        launch(args);
    }
   
}
