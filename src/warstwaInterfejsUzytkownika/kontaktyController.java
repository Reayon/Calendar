package warstwaInterfejsUzytkownika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

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
    @FXML
    private TableColumn<Kontakt, Integer> idColumn;
    
    @FXML
    private	MenuItem Edytuj;

    @FXML
    private MenuItem Usun;
    @FXML
    protected MenuItem aboutProgram;
	
    public void initialize() {

    	Edytuj.setDisable(true);
        Usun.setDisable(true);

        tabelaKontaktow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        	Edytuj.setDisable(newValue == null);
            Usun.setDisable(newValue == null);
        });
        tabelaKontaktow.setRowFactory(tv -> {
            TableRow<Kontakt> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    editKontakt();
                }
            });
            return row;
        });
    	imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
        nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        nrTelColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setVisible(false); // Ukryj kolumnę identyfikatora
        aboutProgram.setOnAction(event -> showAboutDialog());
        
        Platform.runLater(()->{
        	
        for(int i = 0; i<dm.pobierzListeKontaktow().size();i++) {
        	Kontakt kontakt = new Kontakt(dm.pobierzListeKontaktow().get(i).getImie(),
        			dm.pobierzListeKontaktow().get(i).getNazwisko(),
        			dm.pobierzListeKontaktow().get(i).getNr(),
        			dm.pobierzListeKontaktow().get(i).getEmail(),
        			dm.pobierzListeKontaktow().get(i).getID());
        			listaKontaktow.add(kontakt);
        }
        tabelaKontaktow.setItems(listaKontaktow);
        });
        tabelaKontaktow.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                if (!tabelaKontaktow.getSelectionModel().isEmpty()) {
                    Kontakt selectedKontakt = tabelaKontaktow.getSelectionModel().getSelectedItem();
                    int kontaktID = selectedKontakt.getID();
                    System.out.println("Wybrane ID: " + kontaktID);
                }
            }
        });
    }
        @FXML
        private void addKontakt() {
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
        private void editKontakt() {
        	
            Kontakt selectedKontakt = tabelaKontaktow.getSelectionModel().getSelectedItem();
            if (selectedKontakt != null) {
                Dialog<Kontakt> dialog = new Dialog<>();
                dialog.setTitle("Edytuj kontakt");
                dialog.setHeaderText("Edytuj dane kontaktu:");
                
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("calendar-icon.png"));
                
                DialogPane dialogPane = dialog.getDialogPane();
                dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                TextField imieTextField = new TextField(selectedKontakt.getImie());
                imieTextField.setPromptText("Imię");

                TextField nazwiskoTextField = new TextField(selectedKontakt.getNazwisko());
                nazwiskoTextField.setPromptText("Nazwisko");

                TextField nrTextField = new TextField(String.valueOf(selectedKontakt.getNr()));
                nrTextField.setPromptText("Numer telefonu");

                TextField emailTextField = new TextField(selectedKontakt.getEmail());
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
                                emailTextField.getText(),
                                selectedKontakt.getID()
                        );
                    }
                    return null;
                });

                Optional<Kontakt> result = dialog.showAndWait();
                result.ifPresent(editedKontakt -> {
                    selectedKontakt.setImie(editedKontakt.getImie());
                    selectedKontakt.setNazwisko(editedKontakt.getNazwisko());
                    selectedKontakt.setNr(editedKontakt.getNr());
                    selectedKontakt.setEmail(editedKontakt.getEmail());
                    tabelaKontaktow.refresh();
                    try {
                        dm.editKontaktGUI(editedKontakt.getImie(), editedKontakt.getNazwisko(), editedKontakt.getNr(), editedKontakt.getEmail(), selectedKontakt.getID()+1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }else {
            	showSelectionAlert("Edytuj kontakt", "Wybierz kontakt do edycji.");
            }
        }
        
        @FXML
        private void deleteKontakt() {
        	Kontakt selectedKontakt = tabelaKontaktow.getSelectionModel().getSelectedItem();
            int selectedIdx = tabelaKontaktow.getSelectionModel().getSelectedIndex();
            if (selectedKontakt != null) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                alert.setTitle("Potwierdzenie usunięcia");
                alert.setHeaderText("Czy na pewno chcesz usunąć wybrany kontakt?");
                alert.setContentText("Tej operacji nie można cofnąć.");

                Optional<ButtonType> result = alert.showAndWait();
                result.ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        try {
                            dm.removeKontaktGUI(selectedKontakt.getID());
                            tabelaKontaktow.getItems().remove(selectedIdx);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        
        private void showSelectionAlert(String title, String content) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }
        @FXML
        private void wczytajPlik(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki XML", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                	wczytajKontakty(selectedFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        private void wczytajKontakty(File file) throws ParserConfigurationException, SAXException, IOException, SQLException {
        	try (InputStream in = new FileInputStream(file)) {
        	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	    Document doc = dBuilder.parse(in);

        	    NodeList contactNodes = doc.getElementsByTagName("Kontakt");

        	    for (int i = 0; i < contactNodes.getLength(); i++) {
        	        Element contactElement = (Element) contactNodes.item(i);

        	        String imie = contactElement.getElementsByTagName("Imie").item(0).getTextContent();
        	        String nazwisko = contactElement.getElementsByTagName("Nazwisko").item(0).getTextContent();
        	        String nu = contactElement.getElementsByTagName("NumerTelefonu").item(0).getTextContent();
        	        int numer = Integer.parseInt(nu);
        	        String email = contactElement.getElementsByTagName("Email").item(0).getTextContent();

        	        Kontakt kontakt = new Kontakt(imie, nazwisko, numer, email);

        	        boolean contactExists = listaKontaktow.contains(kontakt);

        	        if (!contactExists) {
        	            listaKontaktow.add(kontakt);
        	            dm.addKontakt(imie, nazwisko, numer, email);
        	        }
        	    }
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
