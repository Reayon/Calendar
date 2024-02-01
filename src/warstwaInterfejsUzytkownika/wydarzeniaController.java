package warstwaInterfejsUzytkownika;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class wydarzeniaController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
    @FXML
    private Tab wydarzeniaTab;

    @FXML
    private VBox vboxWydarzenia;

    @FXML
    private Label nazwa;

    @FXML
    private Label miejsce;

    @FXML
    private Label data;

    @FXML
    private Label godzina;
    
    public void switchToKontakty(ActionEvent e) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("kontakty.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
}
