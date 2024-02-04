package warstwaInterfejsUzytkownika;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import warstwaDanych.Kontakt;
import warstwaLogiki.dataManager;

public class graphicUI extends Application {
	
	private ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
	private dataManager dm = new dataManager();
	@Override
	public void start(Stage stage) throws SQLException{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("kalendarz.fxml"));
	        Parent root = loader.load();
	        kalendarzController kalendarzController1 = loader.getController();
	        dm.odczytajDane();
	        kalendarzController1.setDataManager(dm);
	        Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("Glowna.css").toExternalForm());
			Image icon = new Image("calendar-icon.png");
			stage.getIcons().add(icon);
			stage.setTitle("Organizer by Filip Banasiak i Dawid Centkowski");
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
