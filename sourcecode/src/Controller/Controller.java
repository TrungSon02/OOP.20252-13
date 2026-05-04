package Controller;
import java.io.IOException;

import Model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Controller {
    private Game game;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button playButton;

    @FXML
    private Button instructionButton;

    @FXML
    private Button exitButton;

    @FXML
    private ImageView playerOneAvatar;

    @FXML
    public void initialize() {
        playerOneAvatar.setImage(new Image("/image/avatar/Pattrick_2.png"));
    }

    public void playGame(){
        //PHUC:
        //TODO: Load main_game.fxml scene. NOTE: remember to add application.css (same command as in App.java)
        //Call initializeBoard
    }

    public void initializeBoard(){
        //PHUC
        //TODO: Called once when game starts
        //Set up initial gem count for each square (5 small gems each, 1 big gem in each Quan)
        //Set up initial images for all squares
        //Initialize scores to 0
    }

    public void showInstruction(){
        //PHUC
        //TODO: Load instruction.fxml scene. NOTE: remember to add application.css (same command as in App.java)
    }

    public void showResult(){
        //PHUC
        //TODO: Load ending.fxml scene. NOTE: remember to add application.css (same command as in App.java)
        //TODO 2: Show which player won
        //Note: I havent done ending.fxml yet :)
    }

    public void exit(){
        //PHUC
        //TODO: Exit the game (Like alt f4). (Watch the Code Bro YT vid for the sample code for exiting game)
    }

    public void goHome(){
        //PHUC
        //TODO: Load menu.fxml scene. NOTE: remember to add application.css (same command as in App.java)
    }

    public void getInput(){
        //NAM
        //TODO: Handle player input via one of two methods:
        //First method: Drag and Drop
        //Second method: Select a square -> then select direction
        //NOTE: Check player's turn for the available squares
        //After getting input, call onPlayerMove(square, direction)
    }

    public void onPlayerMove(){
        //Son 
        //TODO: Already done on paper, write it back here
    }

    public void animateMove(){
        //Hieu Anh 
        //TODO 1: Make a new folder in image, try to make the collection of images containing 1,2,3,4,... gems (make several collection)
        //TODO 2: Based on the list of move (parameter of this method) change the images of each square 
        //TODO 3: Add a number indicating the total gems on each square then update that number after each move
    }
    
    public void updateScoreUI(){
        //Hieu Anh
        //TODO: Based on the param, Update the score Label for the given player
    }

    



}
