package kasirgalabs;

import java.util.Locale;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.opencv.core.Core;

/**
 *
 * @author goksu
 */
public class FaceDetection extends Application implements Runnable {

    @Override
    public void start(Stage primaryStage) {
        try {
            // load the FXML resource
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "FaceDetection.fxml"));
            BorderPane root = (BorderPane) loader.load();
            // set a whitesmoke background
            root.setStyle("-fx-background-color: whitesmoke;");
            // create and style a scene
            Scene scene = new Scene(root, 600, 440);
            primaryStage.setTitle("Secure");
            primaryStage.setScene(scene);

            // show the GUI
            primaryStage.show();
            primaryStage.toBack();
            // init the controller
            FaceDetectionController controller = loader.getController();
            controller.init();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    @Override
    public void handle(WindowEvent event) {
       
            event.consume();
            primaryStage.show(); // I tried without this line also.
        
    }
});

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch((String) null);

    }

    
}
