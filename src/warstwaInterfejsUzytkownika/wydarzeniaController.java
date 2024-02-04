package warstwaInterfejsUzytkownika;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;
import warstwaLogiki.dataManager;

public class wydarzeniaController extends kalendarzController {
	ObservableList<Wydarzenia> listaWydarzen = FXCollections.observableArrayList();
	@FXML
    private TableView<Wydarzenia> tabelaWydarzen;

    @FXML
    private TableColumn<Wydarzenia, String> nazwaColumn;

    @FXML
    private TableColumn<Wydarzenia, String> miejsceColumn;

    @FXML
    private TableColumn<Wydarzenia, String> dataColumn;

    @FXML
    private TableColumn<Wydarzenia, String> godzinaColumn;
    
    @FXML
    private TableColumn<Wydarzenia, ArrayList<Kontakt>> kontaktColumn;
    
    @FXML
    private MenuItem usunW;
    
    @FXML
    private MenuItem editW;

    public void initialize() {
    	editW.setDisable(true);
    	usunW.setDisable(true);
    	
    	tabelaWydarzen.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Kliknięcie lewym przyciskiem myszy
                // Sprawdzenie, czy kliknięto pusty obszar
                if (tabelaWydarzen.getSelectionModel().isEmpty()) {
                    // Odznaczanie zaznaczonego wiersza
                	tabelaWydarzen.getSelectionModel().clearSelection();
                }
            }
        });
    	
    	tabelaWydarzen.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editW.setDisable(newValue == null);
            usunW.setDisable(newValue == null);
        });
    	
    	nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
    	miejsceColumn.setCellValueFactory(new PropertyValueFactory<>("miejsce"));
    	dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
    	godzinaColumn.setCellValueFactory(new PropertyValueFactory<>("godzina"));
    	kontaktColumn.setCellValueFactory(new PropertyValueFactory<>("kontaktyImieNazwisko"));
        
        Platform.runLater(()->{
        	
        for(int i = 0; i<dm.pobierzListeWydarzen().size();i++) {
        	Wydarzenia wydarzenie = new Wydarzenia(dm.pobierzListeWydarzen().get(i).getNazwa(),
        			dm.pobierzListeWydarzen().get(i).getMiejsce(),
        			dm.pobierzListeWydarzen().get(i).getData(),
        			dm.pobierzListeWydarzen().get(i).getGodzina());
        	for(int j=0; j<dm.pobierzListeWydarzen().get(i).getKontaktySize(); j++) {
        		wydarzenie.setKontakt(dm.pobierzListeWydarzen().get(i).getExactKontakt(j));
        	}
        	listaWydarzen.add(wydarzenie);
        }
        tabelaWydarzen.setItems(listaWydarzen);
        });
    }
    
    @FXML
    private void onAddWydarzenieButtonClicked() {
    	Dialog<Wydarzenia> dialog = new Dialog<>();
        dialog.setTitle("Dodaj nowy kontakt");
        dialog.setHeaderText("Wprowadź dane nowego kontaktu:");
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("calendar-icon.png"));
		
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nazwaTextField = new TextField();
        nazwaTextField.setPromptText("Nazwa");

        TextField miejsceTextField = new TextField();
        miejsceTextField.setPromptText("Miejsce");

        TextField dataTextField = new TextField();
        dataTextField.setPromptText("Data");

        TextField godzinaTextField = new TextField();
        godzinaTextField.setPromptText("Godzina");

        dialogPane.setContent(new ScrollPane(new VBox(8, nazwaTextField, miejsceTextField, dataTextField, godzinaTextField)));

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        nazwaTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(newValue.trim().isEmpty() || miejsceTextField.getText().isEmpty() ||
        		dataTextField.getText().isEmpty() || godzinaTextField.getText().isEmpty()));

        miejsceTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(nazwaTextField.getText().isEmpty() || newValue.trim().isEmpty() ||
        		dataTextField.getText().isEmpty() || godzinaTextField.getText().isEmpty()));

        dataTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
                newValue.trim().isEmpty() || godzinaTextField.getText().isEmpty()));

        godzinaTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
        		dataTextField.getText().isEmpty() || newValue.trim().isEmpty()));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Wydarzenia(
                        nazwaTextField.getText(),
                        miejsceTextField.getText(),
                        dataTextField.getText(),
                        godzinaTextField.getText()
                );
            }
            return null;
        });

        Optional<Wydarzenia> result = dialog.showAndWait();
        result.ifPresent(wydarzenie -> {
            listaWydarzen.add(wydarzenie);
            try {
				dm.addWydarzenie(wydarzenie.getNazwa(), wydarzenie.getMiejsce(), wydarzenie.getData(), wydarzenie.getGodzina());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
    
    @FXML
    private void onEditWydarzenieButtonClicked() {
        
    	Wydarzenia selectedWydarzenie = tabelaWydarzen.getSelectionModel().getSelectedItem();
        if (selectedWydarzenie != null) {
            Dialog<Wydarzenia> dialog = new Dialog<>();
            dialog.setTitle("Edytuj wydarzenie");
            dialog.setHeaderText("Edytuj dane wydarzenia:");
            
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("calendar-icon.png"));
            
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField nazwaTextField = new TextField(selectedWydarzenie.getNazwa());
            nazwaTextField.setPromptText("Nazwa");

            TextField miejsceTextField = new TextField(selectedWydarzenie.getMiejsce());
            miejsceTextField.setPromptText("Miejsce");

            TextField dataTextField = new TextField(String.valueOf(selectedWydarzenie.getData()));
            dataTextField.setPromptText("Data");

            TextField godzinaTextField = new TextField(selectedWydarzenie.getGodzina());
            godzinaTextField.setPromptText("Godzina");

            dialogPane.setContent(new ScrollPane(new VBox(8, nazwaTextField, miejsceTextField, dataTextField, godzinaTextField)));

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);

            nazwaTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(newValue.trim().isEmpty() || miejsceTextField.getText().isEmpty() ||
                    		dataTextField.getText().isEmpty() || godzinaTextField.getText().isEmpty()));

            miejsceTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(nazwaTextField.getText().isEmpty() || newValue.trim().isEmpty() ||
                    		dataTextField.getText().isEmpty() || godzinaTextField.getText().isEmpty()));

            dataTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
                            newValue.trim().isEmpty() || godzinaTextField.getText().isEmpty()));

            godzinaTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
                    		dataTextField.getText().isEmpty() || newValue.trim().isEmpty()));

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return new Wydarzenia(
                    		nazwaTextField.getText(),
                    		miejsceTextField.getText(),
                    		dataTextField.getText(),
                    		godzinaTextField.getText()
                    );
                }
                return null;
            });

            Optional<Wydarzenia> result = dialog.showAndWait();
            result.ifPresent(editedWydarzenie -> {
                listaWydarzen.remove(selectedWydarzenie);
                listaWydarzen.add(editedWydarzenie);
                try {
                    dm.editWydarzenie(editedWydarzenie.getNazwa(), editedWydarzenie.getMiejsce(), editedWydarzenie.getData(), editedWydarzenie.getGodzina(), tabelaWydarzen.getSelectionModel().getSelectedIndex() + 2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    @FXML
    private void onUsunButtonClicked() {	
    	Wydarzenia selectedWydarzenie = tabelaWydarzen.getSelectionModel().getSelectedItem();
        int selectedIdx = tabelaWydarzen.getSelectionModel().getSelectedIndex();
        if (selectedWydarzenie != null) {
        	usunW.setDisable(true);
        	Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usunięcia");
            alert.setHeaderText("Czy na pewno chcesz usunąć wybrane wydarzenie?");
            alert.setContentText("Tej operacji nie można cofnąć.");

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    try {
                        dm.removeWydarzenie(selectedIdx+1);
                        tabelaWydarzen.getItems().remove(selectedIdx);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    public void unselect() {
    	
    	tabelaWydarzen.getSelectionModel().select(null);
    }
    
    public void switchToKalendarz(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("kalendarz.fxml"));
        Parent root = loader.load();
        kalendarzController kalendarzController = loader.getController();
        kalendarzController.setDataManager(dm);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	scene = new Scene(root);
    	stage.setScene(scene);
    	stage.show();
    }
}
