package warstwaInterfejsUzytkownika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class ustawieniaController extends kalendarzController {
	
	@FXML
    private Button loadKontakty;
	@FXML
    protected kontaktyController kontaktyController;
    @FXML
	protected wydarzeniaController wydarzeniaController;
    
    protected Stage stage;
    protected Scene scene;
    @FXML
    private void initialize() {

    }
    
    //TU WYŁĄCZANIE ALERTÓW I POWIADOMIENIA
    
    public void switchToKalendarz(ActionEvent event) throws SQLException{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("kalendarz.fxml"));
	        Parent root = loader.load();
	        kalendarzController kalendarzController1 = loader.getController();
	        kalendarzController1.setDataManager(dm);
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
