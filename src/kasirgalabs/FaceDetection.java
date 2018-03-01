package kasirgalabs;

import java.util.Locale;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

            // set the proper behavior on closing the application
            primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {

                public void handle(WindowEvent we) {
                    JOptionPane optionPane = new JOptionPane();
                    JDialog dialog = optionPane.createDialog("Title");
               
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                    int reply = optionPane.showConfirmDialog(
                            JOptionPane.getRootFrame(),
                            "Sınavdan çıkmak istiyor musunuz?",
                            "EXIT", JOptionPane.YES_NO_OPTION);

                    if(reply == JOptionPane.YES_OPTION) {
                        primaryStage.hide();
                        controller.setClosed();
                    }
                    else {

                        primaryStage.show();
                    }
                    // controller.setClosed();

                }

            }));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(null);

    }

}
