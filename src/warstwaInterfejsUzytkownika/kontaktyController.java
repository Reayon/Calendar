package warstwaInterfejsUzytkownika;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class kontaktyController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
    private Tab kontaktyTab;

    @FXML
    private VBox vboxKontakty;

    @FXML
    private Label imie;

    @FXML
    private Label nazwisko;

    @FXML
    private Label nrtel;

    @FXML
    private Label email;
    
    
}
