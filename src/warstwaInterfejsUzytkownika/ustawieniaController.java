package warstwaInterfejsUzytkownika;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ustawieniaController extends kalendarzController {
	
	@FXML
    protected kontaktyController kontaktyController;
    @FXML
	protected wydarzeniaController wydarzeniaController;
    @FXML
    private ComboBox<String> przypomnij;
    @FXML
    private Button zapiszButton;  // Dodany przycisk zapisu
    
    protected Stage stage;
    protected Scene scene;
    
    private String wybranaOpcjaPrzypomnienia;
    @FXML
    private void initialize() {
        // Initialize ComboBox options
        ObservableList<String> opcjePrzypomnienia = FXCollections.observableArrayList(
               "5", "15", "30", "45"
        );
        przypomnij.setItems(opcjePrzypomnienia);
        // Dodanie listenera do ComboBoxa, który zapisuje wybraną opcję
        przypomnij.setOnAction(event -> {
            wybranaOpcjaPrzypomnienia = przypomnij.getValue();
        });

        // Dodanie obsługi zdarzenia dla przycisku zapisu
        zapiszButton.setOnAction(event -> {
            zapiszOpcjePrzypomnienia();
        });
    }

    // Dodana metoda do zapisywania opcji przypomnienia
    private void zapiszOpcjePrzypomnienia() {
    	File plik = new File("./src/ustawienia.txt");

    	try{
    		plik.createNewFile();                    
    		FileWriter strumienZapisu = new FileWriter(plik);    
    		strumienZapisu.write(wybranaOpcjaPrzypomnienia);        
    		strumienZapisu.close();                 
    	}

		// Instrukcje lapiace wyjatki
		catch (IOException io)                                               
		{System.out.println(io.getMessage());}

		catch (Exception se)
		{System.err.println("blad sec");}
    }
    
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
