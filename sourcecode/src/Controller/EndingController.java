package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EndingController{
    @FXML private Label winnerNameLabel;
    @FXML private ImageView winnerAvatarView;
    private final SceneNavigator navigator = SceneNavigator.getInstance();

    @FXML
    public void goHome(ActionEvent event) {
        navigator.goHome(event);
    }

    @FXML
    public void goPlay(ActionEvent event) {
        navigator.goPlay(event);
    }

    public void displayWinner(String name, String avatarPath) {
        if (winnerNameLabel != null) {
            if ("It's a Tie!".equals(name)) {
                winnerNameLabel.setText(name);
            } else {
                winnerNameLabel.setText("Player " + name + " wins!");
            }
        }

        if (winnerAvatarView != null) {
            if (avatarPath != null && !avatarPath.trim().isEmpty()) {
                try {
                    Image avatarImage = new Image(getClass().getResourceAsStream(avatarPath));
                    winnerAvatarView.setImage(avatarImage);
                    winnerAvatarView.setVisible(true); 
                } catch (Exception e) {
                    System.err.println("Error: Failed to load winner avatar from " + avatarPath);
                }
            } else {
                winnerAvatarView.setVisible(false);
            }
        }
    }
}