import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        loadFont();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
        Scene scene = new Scene(root, Color.LIGHTBLUE);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        Image icon = new Image("image/layout/logo.png");
        stage.getIcons().add(icon);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.setScene(scene);
        stage.show();
    }

    private void loadFont(){
        Font.loadFont(getClass().getResourceAsStream("/font/Xirod.otf"), 36);
        Font.loadFont(getClass().getResourceAsStream("/font/HN-Artukge-Script.ttf"), 36);
        //Font.getFamilies().forEach(System.out::println);
    }

    public static void main(String[] args) {
        launch();
    }
}