
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import objects.Car;
import objects.Frog;
import objects.GameObject;
import objects.Grass;
import objects.Log;
import objects.Lily;
import objects.Street;
import objects.Truck;
import objects.Brick;
import objects.Sandstone;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * GameBoard class where we implement the game
 * @author James, James, Ashwin, Aashil
 *
 */
public class GameBoard extends Pane implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ROWS = 21;
	private static final int COLS = 15;
	private double initialWidth;
	private double initialHeight;
	private double cellWidth;
	private double cellHeight;
	private GameObject[][] matrix;
	private GridPane grid;
	private Timeline timeline;
	private boolean gameOver;
	private StackPane pauseMenu;
	private boolean keyPressed = false;
	private List<GameObject> groundObjects;
	Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();
	double screenWidth = bounds.getWidth();
	double screenHeight = bounds.getHeight();

	/**
	 * GameBoard constructor that creates a new instance of our game with a 21 x 15
	 * matrix Spawns all the objects onto the board and implements pause menu and
	 * win/death screens
	 * 
	 * @param initialWidth
	 * @param initialHeight
	 */
	public GameBoard(double initialWidth, double initialHeight) {
		matrix = new GameObject[ROWS][COLS];
		cellWidth = Screen.getPrimary().getVisualBounds().getWidth() / COLS;
		cellHeight = Screen.getPrimary().getVisualBounds().getHeight() / ROWS;
		groundObjects = new ArrayList<>();
		grid = new GridPane();
		grid.prefWidthProperty().bind(widthProperty());
		grid.prefHeightProperty().bind(heightProperty());
		getChildren().add(grid);
		initPauseMenu();
		initMatrix();
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
			moveObject();
			if (gameOver) {
				endGame();
				return;
			}
			spawnObject();
			if (gameOver) {
				endGame();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		setFocusTraversable(true);
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				// check the state of the game. If it is paused, game over or if a key is being
				// pressed
				if (gameOver || timeline.getStatus() == Timeline.Status.PAUSED || keyPressed) {
					return;
				}
				keyPressed = true;
				if (event.getCode() == KeyCode.ESCAPE) {
					if (timeline.getStatus() == Timeline.Status.RUNNING) {
						timeline.pause();
						getChildren().add(pauseMenu);
					} else {
						getChildren().remove(pauseMenu);
						timeline.play();
					}
				} else {
					Frog frog = null;
					int frogRow = 0;
					int frogCol = 0;
					for (int row = 0; row < ROWS; row++) {
						for (int col = 0; col < COLS; col++) {
							if (matrix[row][col] instanceof Frog) {
								frog = (Frog) matrix[row][col];
								frogRow = row;
								frogCol = col;
								break;
							}
						}
					}

					if (frog == null) {
						endGame();
						return;
					}

					int newRow = frogRow;
					int newCol = frogCol;
					// The controls allowing the player to move
					if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
						frog.setRotate(0);
						newRow--;
					} else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
						frog.setRotate(-90);
						newCol--;
					} else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
						frog.setRotate(-180);
						newRow++;
					} else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
						frog.setRotate(90);
						newCol++;
					}
					if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS) {

						// if the frog moves off the grid end the game

						matrix[frogRow][frogCol] = null;
						matrix[newRow][newCol] = frog;
						if (frog != null) {
							GridPane.setRowIndex(frog, newRow);
							GridPane.setColumnIndex(frog, newCol);
						}

					} else {
						matrix[frogRow][frogCol] = null;
						grid.getChildren().remove(frog);
						gameOver = true;
					}
				}
			}
		});
		setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				keyPressed = false;
			}
		});
		setOnMouseClicked(e -> requestFocus());
		requestFocus();
	}

	/**
	 * Function to spawn the car and truck objects in certain rows
	 */
	public void spawnObject() {

		Random random = new Random();

		for (int row = 0; row < ROWS; row++) {
			if (row == 9 || row == 11 || row == 13 || row == 15 || row == 19 || row == 17) {
				if (random.nextDouble() < 0.1) {
					Car car = new Car(row, COLS - 1, "CAR");
					car.setPrefSize(cellWidth * 2, cellHeight);
					matrix[row][COLS - 1] = car;
					grid.add(car, COLS - 1, row);
				}
			}
			if (row == 8 || row == 10 || row == 12 || row == 16 || row == 18) {
				if (random.nextDouble() < 0.1) {
					Truck truck = new Truck(row, 0, "TRUCK");
					truck.setPrefSize(cellWidth * 3, cellHeight);
					matrix[row][0] = truck;
					grid.add(truck, 0, row);
				}
			}
		}
	}

	/**
	 * Initializing the matrix with the ground objects Adds them to the gridpane as
	 * well Row 0 and 14 has Lily Objects / Row 7 has grass / rows 1-6 has sandstone
	 * / rows 15-20 has stonestone / rows 8-13 has brick
	 */
	private void initMatrix() {
		for (int row = 0; row < ROWS; row++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / ROWS);
			grid.getRowConstraints().add(rowConstraints);
			for (int col = 0; col < COLS; col++) {
				GameObject object = null;
				if (row == 0 || row == 7) {
					object = new Grass(row, col, "GRASS");
				}
				if (row == 0) {
					object = new Lily(row, col, "LILY");
				}
				if (row == 14) {
					object = new Lily(row, col, "LILY");
				}
				if (row >= 1 && row <= 6) {
					object = new Sandstone(row, col, "SANDSTONE");
				}
				if (row >= 15 && row <= 20) {
					object = new Street(row, col, "STREET");
				}
				if (row >= 8 && row <= 13) {
					object = new Brick(row, col, "BRICK");
				}
				if (object != null) {
					matrix[row][col] = object;
					grid.add(object, col, row);

					// Saves to groundObjects list to help with loading
					if (object instanceof Grass || object instanceof Lily || object instanceof Street
							|| object instanceof Sandstone || object instanceof Brick) {
						groundObjects.add(object);
					}
				}
				if (row == 0) {
					ColumnConstraints columnConstraints = new ColumnConstraints();
					columnConstraints.setPercentWidth(100.0 / COLS);
					grid.getColumnConstraints().add(columnConstraints);
				}
			}

		}
		Frog frog = new Frog(ROWS - 1, COLS / 2);
		matrix[ROWS - 1][COLS / 2] = frog;
		grid.add(frog, COLS / 2, ROWS - 1);
	}

	private Random random = new Random();
	private List<Log> activeLogs = new ArrayList<>();
	private int maxLogs = 5;

	/**
	 * Void method that gets called in moveObjects Moves the logs in both directions
	 * and don't get removed from the screen Move back and forth
	 */
	public void moveLogs() {
		List<Log> logsToRemove = new ArrayList<>();
		for (Log log : activeLogs) {
			int col = log.getCol();
			int newCol = col + log.getDirection();

			// Check if the log is going out of bounds
			if (newCol < 0 || newCol >= COLS) {
				// Reverse the direction of the log
				log.setDirection(-log.getDirection());
				newCol = col + log.getDirection();
			}

			// Remove the log from the current column in the grid
			grid.getChildren().remove(log);
			matrix[log.getRow()][col] = null;

			// Move the log to the new column
			if (newCol >= 0 && newCol < COLS) {
				matrix[log.getRow()][newCol] = log;
				log.setCol(newCol);
				grid.add(log, newCol, log.getRow());
			} else {
				logsToRemove.add(log);
			}
		}

		// Remove logs that have moved out of bounds
		for (Log log : logsToRemove) {
			activeLogs.remove(log);
		}

		// Randomly spawn new logs if the active logs are below the maximum limit
		if (activeLogs.size() < maxLogs && random.nextDouble() < 0.5) {
			spawnLog();
		}
	}

	/**
	 * Spawn log method that randomly spawns them in the rows 1-6 and randomizes a
	 * direction
	 */
	private void spawnLog() {
		int row = random.nextInt(6) + 1; // Spawn logs between rows 1 and 6
		int col = random.nextInt(COLS);
		Log log = new Log(row, col, "LOG");
		matrix[row][col] = log;
		log.setCol(col);
		log.setRow(row);
		grid.add(log, col, row);
		activeLogs.add(log);

		int direction = random.nextInt(2) == 0 ? 1 : -1; // Randomly choose a direction
		log.setDirection(direction);
	}

	/**
	 * Move object that gets called in the timeline to constantly move the objects
	 * in their given patterns Cars move from right to left Trucks move from left to
	 * right
	 */
	public void moveObject() {
		if (timeline.getStatus() == Timeline.Status.PAUSED) {
			return;
		}
		// Move cars
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				GameObject object = matrix[row][col];
				if (object instanceof Car) {
					int newCol = col - 1;
					if (newCol < 0) {
						matrix[row][col] = null;
						grid.getChildren().remove(object);
					} else {
						matrix[row][col] = null;
						matrix[row][newCol] = object;
						object.setCol(newCol);
						GridPane.setColumnIndex(object, newCol);
						if (isFrogCollided(row, newCol)) {
							gameOver = true;
							endGame();
						}
					}
				}
			}
		}
		// Move trucks
		for (int row = 0; row < ROWS; row++) {
			for (int col = COLS - 1; col >= 0; col--) {
				GameObject object = matrix[row][col];
				if (object instanceof Truck) {
					int newCol = col + 1;
					if (newCol >= COLS) {
						matrix[row][col] = null;
						grid.getChildren().remove(object);
					} else {
						matrix[row][col] = null;
						matrix[row][newCol] = object;
						object.setCol(newCol);
						GridPane.setColumnIndex(object, newCol);
						if (isFrogCollided(row, newCol)) {
							gameOver = true;
							endGame();
						}
					}
				}
			}
		}
		moveLogs();

		@SuppressWarnings("unused")
		Frog frog = null;
		int frogRow = -1;
		@SuppressWarnings("unused")
		int frogCol = -1;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (matrix[row][col] instanceof Frog) {
					frog = (Frog) matrix[row][col];
					frogRow = row;
					frogCol = col;
					break;
				}
			}
		}
		// Condition that checks if the frog has won
		if (frogRow == 0) {
			winGame();
			return;
		}
	}

	/**
	 * Method that returns a boolean to check if the frog has collided with an
	 * object
	 * 
	 * @param row
	 * @param col
	 * @return true or false
	 */
	private boolean isFrogCollided(int row, int col) {
		if (matrix[row][col] instanceof Frog) {
			gameOver = true;
			return true;
		}
		return false;
	}

	/**
	 * Pause menu that includes pause button, resume button, restart button, save
	 * button
	 */
	private void initPauseMenu() {
		VBox menuItems = new VBox(20);
		menuItems.setAlignment(Pos.CENTER);

		Label pauseLabel = new Label("GAME PAUSED");
		pauseLabel.setFont(new Font("Times New Roman", 40));
		pauseLabel.setTextFill(Color.WHITE);
		menuItems.getChildren().add(pauseLabel);

		Button resumeButton = new Button("RESUME");
		resumeButton.setFont(new Font("Times New Roman", 20));
		resumeButton.setOnAction(event -> {
			getChildren().remove(pauseMenu);
			requestFocus();
			timeline.play();
		});
		Button restartButton = new Button("RESTART");
		restartButton.setFont(new Font("Times New Roman", 20));
		restartButton.setOnAction(event -> {
			restartGame();
		});

		Button saveButton = new Button("SAVE");
		saveButton.setFont(new Font("Times New Roman", 20));
		saveButton.setOnAction(event -> {
			saveGame();
		});

		Button loadButton = new Button("LOAD");
		loadButton.setFont(new Font("Times New Roman", 20));
		loadButton.setOnAction(event -> {
			loadGame();
		});
		menuItems.getChildren().add(resumeButton);
		menuItems.getChildren().add(restartButton);
		menuItems.getChildren().add(saveButton);
		menuItems.getChildren().add(loadButton);
		pauseMenu = new StackPane(menuItems);
		pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
		pauseMenu.prefWidthProperty().bind(widthProperty());
		pauseMenu.prefHeightProperty().bind(heightProperty());
	}

	/**
	 * Restarts the game fresh
	 */
	private void restartGame() {
		GameBoard newGameBoard = new GameBoard(initialWidth, initialHeight);
		Scene newGameScene = new Scene(newGameBoard);

		Stage currentStage = (Stage) grid.getScene().getWindow();
		currentStage.setScene(newGameScene);
		currentStage.setFullScreen(true);
		newGameBoard.requestFocus();
	}

	/**
	 * Win game condition if you reach the final row it plays a sound and has a play
	 * again button
	 */
	private void winGame() {
		timeline.stop();
		AnchorPane root2;
		MediaPlayer mediaPlayer;
		File soundFile = new File("src/YouWon.mp3");
		Media sound = new Media(soundFile.toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		root2 = new AnchorPane();
		root2.setStyle("-fx-background-color: grey;");
		root2.setPrefWidth(640);
		root2.setPrefHeight(640);
		ImageView imageView = new ImageView();
		Image image = new Image("YouWonImage.png");
		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		imageView.setPickOnBounds(true);
		root2.getChildren().add(imageView);
		Button playAgainButton = new Button("Play Again");
		playAgainButton.setId("PA");
		playAgainButton.setFont(new Font("Times New Roman", 20));
		playAgainButton.setVisible(false);
		playAgainButton.setStyle("-fx-background-color: linear-gradient(#8b0000, #0073ad);" + "-fx-text-fill: white;"
				+ "-fx-font-family: \"Arial\";" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;"
				+ "-fx-background-radius: 20px;");
		playAgainButton.setPadding(new Insets(10));
		playAgainButton.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.3)));
		playAgainButton.setOnMouseEntered(event -> {
			playAgainButton.setStyle("-fx-background-color: linear-gradient(#8b0000, #0073ad);"
					+ "-fx-text-fill: white;" + "-fx-font-family: \"Arial\";" + "-fx-font-size: 14px;"
					+ "-fx-font-weight: bold;" + "-fx-background-radius: 20px;");
			playAgainButton.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.5)));
		});
		playAgainButton.setOnMouseExited(event -> {
			playAgainButton.setStyle("-fx-background-color: linear-gradient(#8b0000, #0073ad);"
					+ "-fx-text-fill: white;" + "-fx-font-family: \"Arial\";" + "-fx-font-size: 14px;"
					+ "-fx-font-weight: bold;" + "-fx-background-radius: 20px;");
			playAgainButton.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.3)));
		});

		playAgainButton.setOnAction(event -> {
			mediaPlayer.stop();
			GameBoard newGameBoard = new GameBoard(initialWidth, initialHeight);
			Scene newGameScene = new Scene(newGameBoard);

			Stage currentStage = (Stage) playAgainButton.getScene().getWindow();

			currentStage.setScene(newGameScene);
			currentStage.setFullScreen(true);
			newGameBoard.requestFocus();
		});

		VBox gameOverPane = new VBox(20);
		gameOverPane.setAlignment(Pos.CENTER);
		gameOverPane.getChildren().add(playAgainButton);

		root2.getChildren().add(gameOverPane);
		AnchorPane.setTopAnchor(gameOverPane, 0.0);
		AnchorPane.setBottomAnchor(gameOverPane, 0.0);
		AnchorPane.setLeftAnchor(gameOverPane, 0.0);
		AnchorPane.setRightAnchor(gameOverPane, 0.0);
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
			playAgainButton.setVisible(true);
		}));
		gameOverPane.setAlignment(Pos.CENTER);
		timeline.play();
		AnchorPane.setTopAnchor(gameOverPane, 0.0);
		AnchorPane.setBottomAnchor(gameOverPane, 0.0);
		AnchorPane.setLeftAnchor(gameOverPane, 0.0);
		AnchorPane.setRightAnchor(gameOverPane, 0.0);

		Scene currentScene = grid.getScene();
		Stage currentStage = (Stage) currentScene.getWindow();
		Scene gameOverScene = new Scene(root2);
		if (currentStage != null) {
			mediaPlayer.setOnReady(() -> {
				Duration stopTime = Duration.seconds(5);
				mediaPlayer.setStopTime(stopTime);
				mediaPlayer.play();
			});
			mediaPlayer.setOnEndOfMedia(() -> {
				mediaPlayer.stop();
			});
			currentStage.setScene(gameOverScene);
			currentStage.setWidth(640);
			currentStage.setHeight(640);
			
			currentStage.show();
		}

	}

	/**
	 * Ends the game when the frog leaves the screen or collides with an object
	 */
	private void endGame() {
		// Adds sound to the end screen
		timeline.stop();
		AnchorPane root2;
		MediaPlayer mediaPlayer;
		 // Play the sound for 5 seconds
        File soundFile = new File("src/DSD.mp3");
        Media sound = new Media(soundFile.toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnReady(() -> {
            Duration stopTime = Duration.seconds(5);
            mediaPlayer.setStopTime(stopTime);
            mediaPlayer.play();
        });

        // Stop the media player after 5 seconds
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
        });
		
		 root2 = new AnchorPane();
	        root2.setStyle("-fx-background-color: black;");
	        root2.setPrefSize(600, 400);

	        // Create an ImageView and set its properties
	        ImageView imageView = new ImageView();
	        imageView.setLayoutX(650);
	        imageView.setLayoutY(320);
	        imageView.setPreserveRatio(true);
	        imageView.setPickOnBounds(true);
	        // Load the image
	        Image image = new Image("DSYDI.png"); // Replace with the actual path to your image
	        imageView.setImage(image);
	        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(5), imageView);
	        scaleTransition.setToX(4.0); // New X scale (double the original size)
	        scaleTransition.setToY(4.0); // New Y scale (double the original size)
	        scaleTransition.setCycleCount(1); // Run the animation once

	        // Start the animation
	        scaleTransition.play();
	        
	        // Center the ImageView within the AnchorPane
//	        AnchorPane.setTopAnchor(imageView, (root2.getHeight() - imageView.getFitHeight()) / 2);
//	        AnchorPane.setLeftAnchor(imageView, (root2.getWidth() - imageView.getFitWidth()) / 2);

	        // Add the ImageView to the AnchorPane
	        root2.getChildren().add(imageView);
	        
	        Button playAgainButton = new Button("Play Again");
	       playAgainButton.setId("PA");
	        playAgainButton.setFont(new Font("Times New Roman", 20));
	        playAgainButton.setTranslateX(710);
	        playAgainButton.setTranslateY(470);
	        playAgainButton.setVisible(false); // Set the button initially invisible
	     
	        playAgainButton.setStyle("-fx-background-color: linear-gradient(#8b0000, #FFFFFF);" +
	                "-fx-text-fill: white;" +
	                "-fx-font-family: \"Arial\";" +
	                "-fx-font-size: 14px;" +
	                "-fx-font-weight: bold;"+
	                "-fx-background-radius: 20px;");
	        playAgainButton.setPadding(new Insets(10));
	        playAgainButton.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.3)));

	        playAgainButton.setOnMouseEntered(event -> {
	        	playAgainButton.setStyle("-fx-background-color: linear-gradient(#8b0000, #FFFFFF);" +
	                    "-fx-text-fill: white;" +
	                    "-fx-font-family: \"Arial\";" +
	                    "-fx-font-size: 14px;" +
	                    "-fx-font-weight: bold;"+
	                    "-fx-background-radius: 20px;");
	            playAgainButton.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.5)));
	        });

	        playAgainButton.setOnMouseExited(event -> {
	        	playAgainButton.setStyle("-fx-background-color: linear-gradient(#8b0000, #FFFFFF);" +
	                    "-fx-text-fill: white;" +
	                    "-fx-font-family: \"Arial\";" +
	                    "-fx-font-size: 14px;" +
	                    "-fx-font-weight: bold;"+
	                    "-fx-background-radius: 20px;");
	          playAgainButton.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.3)));
	        });

	        Timeline timeline = new Timeline(
	                new KeyFrame(Duration.seconds(2), event -> playAgainButton.setVisible(true))
	        );
	        timeline.play(); // Start the timeline
	        
	        
	        playAgainButton.setOnAction(event -> {
	        	mediaPlayer.stop();
	        	GameBoard newGameBoard = new GameBoard(initialWidth, initialHeight);
				Scene newGameScene = new Scene(newGameBoard);
	
				Stage currentStage = (Stage) playAgainButton.getScene().getWindow();
				currentStage.setScene(newGameScene);
				currentStage.setFullScreen(true);
//				currentStage.setWidth(screenWidth);
			//	currentStage.setHeight(screenHeight);
				newGameBoard.requestFocus();
				 
	        });

	        VBox gameOverPane = new VBox(20, playAgainButton);
	        gameOverPane.setAlignment(Pos.CENTER);


	        root2.getChildren().add(gameOverPane);

	      
	        Scene currentScene = grid.getScene();
	        Stage currentStage = (Stage) currentScene.getWindow();
	        if (currentStage != null) {
	        Scene gameOverScene = new Scene(root2);
	        currentStage.setScene(gameOverScene);
	        //currentStage.setFullScreen(true);
	        currentStage.show();
	        }
	}

	/**
	 * Method that saves the game when using our save button
	 */
	private void saveGame() {
		try {
			FileOutputStream fos = new FileOutputStream("game_data.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			GridData gridData = new GridData(matrix.length, matrix[0].length);

			for (GameObject groundObject : groundObjects) {
				ObjectData objectData = new ObjectData(groundObject.getClass().getSimpleName(), groundObject.getRow(),
						groundObject.getCol());
				gridData.addObject(objectData);
			}

			for (int row = 0; row < matrix.length; row++) {
				for (int col = 0; col < matrix[row].length; col++) {
					GameObject gameObject = matrix[row][col];
					if (gameObject != null) {
						ObjectData objectData = new ObjectData(gameObject.getClass().getSimpleName(), row, col);
						gridData.addObject(objectData);
					}
				}
			}

			// Save the frog's position
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (matrix[row][col] instanceof Frog) {
						gridData.setFrogRow(row);
						gridData.setFrogCol(col);
						break;
					}
				}
			}
			oos.writeObject(gridData);
			oos.close();
			fos.close();
			System.out.println("Game saved successfully!");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Method that loads the game when using our load button
	 */
	private void loadGame() {
		try {
			FileInputStream fis = new FileInputStream("game_data.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);

			GridData gridData = (GridData) ois.readObject();

			clearMatrix();

			// Load ground objects
			for (ObjectData objectData : gridData.getObjects()) {
				String objectType = objectData.getType();
				int row = objectData.getRow();
				int col = objectData.getCol();

				if (objectType.equals("Grass")) {
					GameObject gameObject = new Grass(row, col, "GRASS");
					matrix[row][col] = gameObject;
					grid.add(gameObject, col, row);
				} else if (objectType.equals("Lily")) {
					GameObject gameObject = new Lily(row, col, "LILY");
					matrix[row][col] = gameObject;
					grid.add(gameObject, col, row);
				} else if (objectType.equals("Street")) {
					GameObject gameObject = new Street(row, col, "STREET");
					matrix[row][col] = gameObject;
					grid.add(gameObject, col, row);
				} else if (objectType.equals("Brick")) {
					GameObject gameObject = new Brick(row, col, "BRICK");
					matrix[row][col] = gameObject;
					grid.add(gameObject, col, row);
				} else if (objectType.equals("Sandstone")) {
					GameObject gameObject = new Sandstone(row, col, "SANDSTONE");
					matrix[row][col] = gameObject;
					grid.add(gameObject, col, row);
				}
			}
			// Load back cars
			for (ObjectData objectData : gridData.getObjects()) {
				String objectType = objectData.getType();
				int row = objectData.getRow();
				int col = objectData.getCol();

				if (objectType.equals("Car")) {
					GameObject gameObject = createObject(objectType, row, col);
					if (gameObject != null) {
						matrix[row][col] = gameObject;
						grid.add(gameObject, col, row);
					}
				}
			}
			// Load back trucks
			for (ObjectData objectData : gridData.getObjects()) {
				String objectType = objectData.getType();
				int row = objectData.getRow();
				int col = objectData.getCol();

				if (objectType.equals("Truck")) {
					GameObject gameObject = createObject(objectType, row, col);
					if (gameObject != null) {
						matrix[row][col] = gameObject;
						grid.add(gameObject, col, row);
					}
				}
			}
			// Load back logs
			for (ObjectData objectData : gridData.getObjects()) {
				String objectType = objectData.getType();
				int row = objectData.getRow();
				int col = objectData.getCol();

				if (objectType.equals("Log")) {
					GameObject gameObject = createObject(objectType, row, col);
					if (gameObject != null) {
						matrix[row][col] = gameObject;
						grid.add(gameObject, col, row);
					}
				}
			}

			int frogRow = gridData.getFrogRow();
			int frogCol = gridData.getFrogCol();
			if (frogRow >= 0 && frogRow < ROWS && frogCol >= 0 && frogCol < COLS) {
				GameObject frog = createObject("Frog", frogRow, frogCol);
				if (frog != null) {
					matrix[frogRow][frogCol] = frog;
					grid.add(frog, frogCol, frogRow);
				}
			}

			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create object for loading the game
	 * 
	 * @param objectType
	 * @param row
	 * @param col
	 * @return gameobject
	 */
	private GameObject createObject(String objectType, int row, int col) {
		if (objectType.equals("GRASS")) {
			return new Grass(row, col, "GRASS");
		} else if (objectType.equals("GRASS")) {
			return new Grass(row, col, "Grass");
		} else if (objectType.equals("Street")) {
			return new Street(row, col, "STREET");
		} else if (objectType.equals("Log")) {
			return new Log(row, col, "LOG");
		} else if (objectType.equals("Car")) {
			return new Car(row, col, "CAR");
		} else if (objectType.equals("Truck")) {
			return new Truck(row, col, "TRUCK");
		} else if (objectType.equals("Lily")) {
			return new Lily(row, col, "LILY");
		} else if (objectType.equals("Sandstone")) {
			return new Sandstone(row, col, "SANDSTONE");
		} else if (objectType.equals("Brick")) {
			return new Brick(row, col, "BRICK");
		} else if (objectType.equals("Frog")) {
			return new Frog(row, col);
		} else {
			return null;
		}

	}

	/**
	 * Clears the initial matrix
	 */
	private void clearMatrix() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				GameObject object = matrix[row][col];
				if (object != null) {
					grid.getChildren().remove(object);
					matrix[row][col] = null;
				}
			}
		}
	}
}