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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import warstwaDanych.Kontakt;
import warstwaLogiki.dataManager;

public class kontaktyController extends kalendarzController {
	ObservableList<Kontakt> listaKontaktow = FXCollections.observableArrayList();
	@FXML
    private TableView<Kontakt> tabelaKontaktow;

    @FXML
    private TableColumn<Kontakt, String> imieColumn;

    @FXML
    private TableColumn<Kontakt, String> nazwiskoColumn;

    @FXML
    private TableColumn<Kontakt, Integer> nrTelColumn;

    @FXML
    private TableColumn<Kontakt, String> emailColumn;
	
    public void initialize() {
    	imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        nrTelColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        Platform.runLater(()->{
        	
        for(int i = 0; i<dm.pobierzListeKontaktow().size();i++) {
        	Kontakt kontakt = new Kontakt(dm.pobierzListeKontaktow().get(i).getImie(),
        			dm.pobierzListeKontaktow().get(i).getNazwisko(),
        			dm.pobierzListeKontaktow().get(i).getNr(),
        			dm.pobierzListeKontaktow().get(i).getEmail());
        			listaKontaktow.add(kontakt);
        }
        tabelaKontaktow.setItems(listaKontaktow);
        });
    }
        @FXML
        private void onAddContactButtonClicked() {
        	Dialog<Kontakt> dialog = new Dialog<>();
            dialog.setTitle("Dodaj nowy kontakt");
            dialog.setHeaderText("Wprowadź dane nowego kontaktu:");
            
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("calendar-icon.png"));
			
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField imieTextField = new TextField();
            imieTextField.setPromptText("Imię");

            TextField nazwiskoTextField = new TextField();
            nazwiskoTextField.setPromptText("Nazwisko");

            TextField nrTextField = new TextField();
            nrTextField.setPromptText("Numer telefonu");

            TextField emailTextField = new TextField();
            emailTextField.setPromptText("Adres email");

            dialogPane.setContent(new ScrollPane(new VBox(8, imieTextField, nazwiskoTextField, nrTextField, emailTextField)));

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);
            
            imieTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(newValue.trim().isEmpty() || nazwiskoTextField.getText().isEmpty() ||
                            nrTextField.getText().isEmpty() || emailTextField.getText().isEmpty()));

            nazwiskoTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(imieTextField.getText().isEmpty() || newValue.trim().isEmpty() ||
                            nrTextField.getText().isEmpty() || emailTextField.getText().isEmpty()));

            nrTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(imieTextField.getText().isEmpty() || nazwiskoTextField.getText().isEmpty() ||
                            newValue.trim().isEmpty() || emailTextField.getText().isEmpty()));

            emailTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(imieTextField.getText().isEmpty() || nazwiskoTextField.getText().isEmpty() ||
                            nrTextField.getText().isEmpty() || newValue.trim().isEmpty()));

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return new Kontakt(
                            imieTextField.getText(),
                            nazwiskoTextField.getText(),
                            Integer.parseInt(nrTextField.getText()),
                            emailTextField.getText()
                    );
                }
                return null;
            });

            Optional<Kontakt> result = dialog.showAndWait();
            result.ifPresent(kontakt -> {
                listaKontaktow.add(kontakt);
                try {
					dm.addKontakt(kontakt.getImie(), kontakt.getNazwisko(), kontakt.getNr(), kontakt.getEmail());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            });
        }
        
        @FXML
        private void onEditContactButtonClicked() {
            Kontakt selectedContact = tabelaKontaktow.getSelectionModel().getSelectedItem();
            if (selectedContact != null) {
                Dialog<Kontakt> dialog = new Dialog<>();
                dialog.setTitle("Edytuj kontakt");
                dialog.setHeaderText("Edytuj dane kontaktu:");
                
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("calendar-icon.png"));
                
                DialogPane dialogPane = dialog.getDialogPane();
                dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                TextField imieTextField = new TextField(selectedContact.getImie());
                imieTextField.setPromptText("Imię");

                TextField nazwiskoTextField = new TextField(selectedContact.getNazwisko());
                nazwiskoTextField.setPromptText("Nazwisko");

                TextField nrTextField = new TextField(String.valueOf(selectedContact.getNr()));
                nrTextField.setPromptText("Numer telefonu");

                TextField emailTextField = new TextField(selectedContact.getEmail());
                emailTextField.setPromptText("Adres email");

                dialogPane.setContent(new ScrollPane(new VBox(8, imieTextField, nazwiskoTextField, nrTextField, emailTextField)));

                Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
                okButton.setDisable(true);

                imieTextField.textProperty().addListener((observable, oldValue, newValue) ->
                        okButton.setDisable(newValue.trim().isEmpty() || nazwiskoTextField.getText().isEmpty() ||
                                nrTextField.getText().isEmpty() || emailTextField.getText().isEmpty()));

                nazwiskoTextField.textProperty().addListener((observable, oldValue, newValue) ->
                        okButton.setDisable(imieTextField.getText().isEmpty() || newValue.trim().isEmpty() ||
                                nrTextField.getText().isEmpty() || emailTextField.getText().isEmpty()));

                nrTextField.textProperty().addListener((observable, oldValue, newValue) ->
                        okButton.setDisable(imieTextField.getText().isEmpty() || nazwiskoTextField.getText().isEmpty() ||
                                newValue.trim().isEmpty() || emailTextField.getText().isEmpty()));

                emailTextField.textProperty().addListener((observable, oldValue, newValue) ->
                        okButton.setDisable(imieTextField.getText().isEmpty() || nazwiskoTextField.getText().isEmpty() ||
                                nrTextField.getText().isEmpty() || newValue.trim().isEmpty()));

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        return new Kontakt(
                                imieTextField.getText(),
                                nazwiskoTextField.getText(),
                                Integer.parseInt(nrTextField.getText()),
                                emailTextField.getText()
                        );
                    }
                    return null;
                });

                Optional<Kontakt> result = dialog.showAndWait();
                result.ifPresent(editedContact -> {
                    listaKontaktow.remove(selectedContact);
                    listaKontaktow.add(editedContact);
                    try {
                        dm.editKontakt(editedContact.getImie(), editedContact.getNazwisko(), editedContact.getNr(), editedContact.getEmail(), tabelaKontaktow.getSelectionModel().getSelectedIndex() + 2);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
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
