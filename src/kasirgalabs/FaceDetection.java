package kasirgalabs;

import java.util.Locale;
import java.util.Optional;
import javafx.application.Application;
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

            // set the proper behavior on closing the application
            primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {

                public void handle(WindowEvent we) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("EXIT");
                    alert.setHeaderText("Sınavdan çıkmak istiyor musunuz?");
                    

                    ButtonType buttonTypeYes = new ButtonType("Evet");
                    ButtonType buttonTypeNo = new ButtonType("Hayır", ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(buttonTypeYes,  buttonTypeNo);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeYes){
                       controller.setClosed();
                    } else {
                        
                    }
//                    JOptionPane optionPane = new JOptionPane();
//                    JDialog dialog = optionPane.createDialog(
//                            "EXIT");
//
//                    optionPane.setMessage("Sınavdan çıkmak istiyor musunuz?");
//                     
//                    optionPane.setOptions(new String[]{"Evet", "Hayır"});
//                   
//                    
//                    dialog.setAlwaysOnTop(true);
//                    dialog.setVisible(true);

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
        launch((String) null);

    }

}
