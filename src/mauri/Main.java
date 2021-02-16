package mauri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

   private static Stage mainWindow;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainWindow = primaryStage;
        mainWindow.getIcons().add(new Image("/logo/0.5x/logo_05.png"));
        Parent login = FXMLLoader.load(getClass().getResource("login.fxml"));
        mainWindow.setTitle("Mauri");
        mainWindow.setScene(new Scene(login, 900, 700));
        mainWindow.show();
    }

    public static Stage getWindow(){
        return mainWindow;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
