package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EndingController extends BaseController{
    @FXML private Label winnerNameLabel;
    @FXML private Label winnerScoreLabel;
    @FXML private ImageView winnerAvatarView;

    @FXML
    public void initialize(){
        //PHUC
        //TODO: Get the input param: player name and player avatar then update the text label + image to show that
        System.out.println("Final results screen initialized.");
    }

    public void displayWinner(String name, String avatarPath) {
        if (winnerNameLabel != null) {
            winnerNameLabel.setText("Player " + name + " wins!");
        }

        try {
            if (winnerAvatarView != null && avatarPath != null) {
                Image avatarImage = new Image(getClass().getResourceAsStream(avatarPath));
                winnerAvatarView.setImage(avatarImage);
            }
        } catch (Exception e) {
            System.err.println("Error: Failed to load winner avatar from " + avatarPath);
        }
    }
}