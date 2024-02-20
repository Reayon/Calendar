package warstwaInterfejsUzytkownika;

import java.io.IOException;
import java.sql.SQLException;
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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import warstwaDanych.Kategorie;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class kategorieController extends wydarzeniaController {
	ObservableList<Kategorie> listaKategorii = FXCollections.observableArrayList();
	
    @FXML
    private MenuItem Dodaj;

    @FXML
    private MenuItem Edytuj;

    @FXML
    private MenuItem Usun;

    @FXML
    private MenuItem aboutProgram;

    @FXML
    private TableColumn<Kategorie, String> kategoriaColumn;

    @FXML
    private Menu menuKontakt;

    @FXML
    private TableView<Kategorie> tabelaKategorii;

    @FXML
    private MenuItem wczytaj;

    @FXML
    private TableColumn<Kategorie, Wydarzenia> wydarzeniaColumn;
    
    public void initialize() {

    	wydarzeniaColumn.setCellValueFactory(new PropertyValueFactory<>("wydarzeniaNazwa"));
    	kategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
    	
    	Platform.runLater(()->{
        	
            
            for(int i = 0; i<dm.pobierzListeKategorii().size();i++) {
            	Kategorie kategoria = new Kategorie(dm.pobierzListeKategorii().get(i).getNazwa());
            			
            	for(int j=0; j<dm.pobierzListeKategorii().get(i).getWydarzenia().size(); j++) {
            		kategoria.setWydarzenie(dm.pobierzListeKategorii().get(i).getExactWydarzenie(j));
            	}
            	listaKategorii.add(kategoria);
            	
            }
            tabelaKategorii.setItems(listaKategorii);
            
            //tabelarow.getItem(listaWydarzen.get(i));
            });
    }
    
    @FXML
    private void onAddKategoriaButtonClicked() {
    	Dialog<Kategorie> dialog = new Dialog<>();
        dialog.setTitle("Dodaj nową kategorię");
        dialog.setHeaderText("Wprowadź nazwę nowej kategorii:");
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("calendar-icon.png"));
		
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField nazwaTextField = new TextField();
        nazwaTextField.setPromptText("Nazwa");
        
        ComboBox<Wydarzenia> wydarzeniaComboBox = new ComboBox<>();
        wydarzeniaComboBox.setPromptText("Wybierz wydarzenie");
        wydarzeniaComboBox.getItems().addAll(dm.pobierzListeWydarzen());

        wydarzeniaComboBox.setCellFactory(lv -> new ListCell<Wydarzenia>() {
            @Override
            protected void updateItem(Wydarzenia wydarzenia, boolean empty) {
                super.updateItem(wydarzenia, empty);
                setText(empty ? "" : wydarzenia.getNazwa());
            }
        });
        wydarzeniaComboBox.setButtonCell(new ListCell<Wydarzenia>() {
            @Override
            protected void updateItem(Wydarzenia wydarzenia, boolean empty) {
                super.updateItem(wydarzenia, empty);
                setText(empty ? "" : wydarzenia.getNazwa());
            }
        });

        dialogPane.setContent(new ScrollPane(new VBox(8, nazwaTextField, wydarzeniaComboBox)));

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        nazwaTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(newValue.trim().isEmpty()));
        
        wydarzeniaComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(newValue == null));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
            	Wydarzenia selectedWyd = wydarzeniaComboBox.getSelectionModel().getSelectedItem();
            	int nextId = dm.getNastepneIdKategorii();
                return new Kategorie(
                		nextId,
                        nazwaTextField.getText()
                		);
            }
            return null;
        });

        Optional<Kategorie> result = dialog.showAndWait();
        result.ifPresent(kategoria -> {
            listaKategorii.add(kategoria);
            try {
				dm.addKategoria(kategoria.getNazwa());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
    
    @FXML
    private void onEditKategoriaButtonClicked() {
        
    	Kategorie selectedKategoria = tabelaKategorii.getSelectionModel().getSelectedItem();
    	System.out.println(selectedKategoria.getID());
        if (selectedKategoria != null) {
            Dialog<Kategorie> dialog = new Dialog<>();
            dialog.setTitle("Edytuj wydarzenie");
            dialog.setHeaderText("Edytuj dane wydarzenia:");
            
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("calendar-icon.png"));
            
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField nazwaTextField = new TextField(selectedKategoria.getNazwa());
            nazwaTextField.setPromptText("Nazwa");

            dialogPane.setContent(new ScrollPane(new VBox(8, nazwaTextField)));

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);

            nazwaTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(newValue.trim().isEmpty()));
            
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return new Kategorie(
                    		nazwaTextField.getText()
                    ); 
                }
                return null;
            });
            Optional<Kategorie> result = dialog.showAndWait();
            result.ifPresent(editedKategoria -> {
                selectedKategoria.setNazwa(editedKategoria.getNazwa());
            	tabelaKategorii.refresh();
                try {
                    dm.editKategoria(editedKategoria.getNazwa(), selectedKategoria.getID()+1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    @FXML
    private void onUsunButtonClicked() {	
    	Kategorie selectedKategoria = tabelaKategorii.getSelectionModel().getSelectedItem();
        int selectedIdx = tabelaKategorii.getSelectionModel().getSelectedIndex();
        if (selectedKategoria != null) {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usunięcia");
            alert.setHeaderText("Czy na pewno chcesz usunąć wybrane wydarzenie?");
            alert.setContentText("Tej operacji nie można cofnąć.");

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    try {
                        dm.removeKategoria(selectedKategoria.getID()+1);
                        tabelaKategorii.getItems().remove(selectedIdx);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    public void switchToWydarzenia(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("wydarzenia.fxml"));
        Parent root = loader.load();
        wydarzeniaController = loader.getController();
        wydarzeniaController.setDataManager(dm);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

