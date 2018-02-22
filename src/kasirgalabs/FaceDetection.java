package kasirgalabs;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author goksu
 */
 



public class FaceDetection extends Application implements Runnable
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("FaceDetection.fxml"));
			BorderPane root = (BorderPane) loader.load();
			// set a whitesmoke background
			root.setStyle("-fx-background-color: whitesmoke;");
			// create and style a scene
			Scene scene = new Scene(root, 600, 440);
			primaryStage.setTitle("Secure");
			primaryStage.setScene(scene);

			// show the GUI
			primaryStage.show();
			
			// init the controller
			FaceDetectionController controller = loader.getController();
			controller.init();

			// set the proper behavior on closing the application
			
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				
				public void handle(WindowEvent we)
				{
					controller.setClosed();
					/*
						ExamPrepFrame epf = new ExamPrepFrame();
						epf.caStop();
						epf.fdStop();
						epf.rpStop();
					
					

					LastFrame lf = new LastFrame();
					*/
					
				}
			
			}));
		}
		catch (Exception e)
		{
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
