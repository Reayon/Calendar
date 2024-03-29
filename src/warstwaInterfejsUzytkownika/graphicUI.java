package warstwaInterfejsUzytkownika;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class graphicUI extends Application {
	@Override
	public void start(Stage stage){
		try {
			Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("Glowna.css").toExternalForm());
			Image icon = new Image("calendar-icon.png");
			stage.getIcons().add(icon);
			stage.setTitle("Organizer by Filip Banasiak i Dawid Centkowski");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
