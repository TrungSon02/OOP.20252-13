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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ending.fxml"));
        Scene scene = new Scene(root, Color.LIGHTBLUE);
        scene.getStylesheets().add(getClass().getResource("/asset/css/application.css").toExternalForm());

        Image icon = new Image("asset/image/layout/logo.png");
        stage.getIcons().add(icon);
        stage.setFullScreen(true);

        stage.setScene(scene);
        stage.show();
    }

    private void loadFont(){
        Font.loadFont(getClass().getResourceAsStream("/asset/font/Xirod.otf"), 36);
        Font.loadFont(getClass().getResourceAsStream("/asset/font/HN-Artukge-Script.ttf"), 36);
        Font.loadFont(getClass().getResourceAsStream("/asset/font/Super Beatpop.ttf"), 36);
        Font.loadFont(getClass().getResourceAsStream("/asset/font/SuperMaples.ttf"), 36);
        //asset/font.getFamilies().forEach(System.out::println);
        
    }

    public static void main(String[] args) {
        launch();
    }
}