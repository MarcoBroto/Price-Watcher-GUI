import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Views/ItemSelect.fxml"));
        primaryStage.setTitle("Price Watcher");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        Parent seed = FXMLLoader.load(getClass().getResource("Views/BasicView.fxml"));
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Price Watcher");
        secondaryStage.setScene(new Scene(seed));
        secondaryStage.setResizable(false);
        secondaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
