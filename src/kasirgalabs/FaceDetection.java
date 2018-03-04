package kasirgalabs;

import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

/**
 *
 * @author goksu
 */
public class FaceDetection extends Application implements Runnable {

    ReportWriting rw = new ReportWriting();
    FaceDetectionController controller;

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
            controller = loader.getController();

            controller.init(rw);
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

    public void stop() {
        controller.setClosed();
        rw.submitText();
        rw.closed();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch((String) null);

    }
    private static final Logger LOG = Logger.getLogger(
            FaceDetection.class.getName());

}
