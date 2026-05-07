import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class Controller extends AnchorPane {
    @FXML
    private Button btn;
    public Controller() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sourcecode/src/design/Player_interact.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        	Controller controller = new Controller();
        	controller.btn.setOnAction(e -> {
                System.out.println("Button clicked!");
            });
    }
}
