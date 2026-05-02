import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
        Scene scene = new Scene(root, Color.LIGHTBLUE);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        Image icon = new Image("image/logo.png");
        stage.getIcons().add(icon);
        stage.setFullScreen(true);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}