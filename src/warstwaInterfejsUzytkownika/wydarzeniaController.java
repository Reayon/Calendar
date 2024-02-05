package warstwaInterfejsUzytkownika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class wydarzeniaController extends kalendarzController {
	// Lista obiektów klasy Wydarzenia
	ObservableList<Wydarzenia> listaWydarzen = FXCollections.observableArrayList();
	
	@FXML
	private TableColumn<Wydarzenia, Integer> idColumn;

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
    private TableColumn<Wydarzenia, String> colorColumn;

    @FXML
    private MenuItem usunW;
    
    @FXML
    private MenuItem editW;
    
    @FXML
    protected MenuItem aboutProgram;

    
    
    public void initialize() {
    	// Wyłączenie opcji edycji i usunięcia na starcie
    	editW.setDisable(true);
    	usunW.setDisable(true);
    	
    	aboutProgram.setOnAction(event -> showAboutDialog());
    	
    	 // Nasłuchiwanie na zdarzenia związane z zaznaczeniem wiersza w tabeli
    	tabelaWydarzen.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editW.setDisable(newValue == null);
            usunW.setDisable(newValue == null);
        });
    	
    	 // Nasłuchiwanie podwójnego kliknięcia na wiersz tabeli
    	tabelaWydarzen.setRowFactory(tv -> {
            TableRow<Wydarzenia> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                	edytujWydarzenieGUI();
                }
            });
            return row;
        });
    	
    	// Inicjalizacja kolumn tabeli
    	nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
    	miejsceColumn.setCellValueFactory(new PropertyValueFactory<>("miejsce"));
    	dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
    	godzinaColumn.setCellValueFactory(new PropertyValueFactory<>("godzina"));
    	kontaktColumn.setCellValueFactory(new PropertyValueFactory<>("kontaktyImieNazwisko"));
    	colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idColumn.setVisible(false);
        
        // Wypełnienie tabeli danymi z bazy danych
        Platform.runLater(()->{
        	for(int i = 0; i<dm.pobierzListeWydarzen().size();i++) {
            	Wydarzenia wydarzenie = new Wydarzenia(
            			dm.pobierzListeWydarzen().get(i).getNazwa(),
            			dm.pobierzListeWydarzen().get(i).getMiejsce(),
            			dm.pobierzListeWydarzen().get(i).getData(),
            			dm.pobierzListeWydarzen().get(i).getGodzina(),
            			dm.pobierzListeWydarzen().get(i).getColor(),
            			dm.pobierzListeWydarzen().get(i).getID());
            	System.out.println("Wybrane Kolor: " + dm.pobierzListeWydarzen().get(i).getColor());
            	for(int j=0; j<dm.pobierzListeWydarzen().get(i).getKontaktySize(); j++) {
            		wydarzenie.setKontakt(dm.pobierzListeWydarzen().get(i).getExactKontakt(j));
            	}
            	listaWydarzen.add(wydarzenie);
            }
            tabelaWydarzen.setItems(listaWydarzen);
            });
        	// Ustawienia dla tabeli
            tabelaWydarzen.setStyle("-fx-background-color: transparent;");
    }
    
    @FXML
    private void dodajWydarzenieGUI() {
    	// Utworzenie okna dialogowego
    	Dialog<Wydarzenia> dialog = new Dialog<>();
        dialog.setTitle("Dodaj nowy kontakt");
        dialog.setHeaderText("Wprowadź dane nowego kontaktu:");
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("calendar-icon.png"));
		
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Pola do wprowadzania danych
        TextField nazwaTextField = new TextField();
        nazwaTextField.setPromptText("Nazwa");

        TextField miejsceTextField = new TextField();
        miejsceTextField.setPromptText("Miejsce");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Data");

        TextField godzinaTextField = new TextField();
        godzinaTextField.setPromptText("Godzina");
        
        ColorPicker colorPick = new ColorPicker();
        colorPick.setPromptText("Kolor");

        dialogPane.setContent(new ScrollPane(new VBox(8, nazwaTextField, miejsceTextField, datePicker, godzinaTextField, colorPick)));

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        // Nasłuchiwanie zmian w polach tekstowych i wybieraczu koloru
        nazwaTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(newValue.trim().isEmpty() || miejsceTextField.getText().isEmpty() ||
        		datePicker.getValue() == null || godzinaTextField.getText().isEmpty()));

        miejsceTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(nazwaTextField.getText().isEmpty() || newValue.trim().isEmpty() ||
        		datePicker.getValue() == null || godzinaTextField.getText().isEmpty()));

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = newValue != null ? newValue.format(formatter) : "";

            okButton.setDisable(
                    nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
                            formattedDate.isEmpty() || godzinaTextField.getText().isEmpty()
            );
        });

        godzinaTextField.textProperty().addListener((observable, oldValue, newValue) ->
        okButton.setDisable(nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
        		datePicker.getValue() == null || newValue.trim().isEmpty()));
        
        // Konwersja koloru na szesnastkowy i weryfikacja, czy jest wybrany kolor
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate selectedDate = datePicker.getValue();
             // Jeśli selectedDate nie jest null, to sformatuj datę przy użyciu podanego formatera,
             // w przeciwnym razie ustaw formattedDate na pusty ciąg znaków.
                String formattedDate = selectedDate != null ? selectedDate.format(formatter) : "";
                Color selectedColor = colorPick.getValue();
                String hexColor = String.format("#%02X%02X%02X",
                        (int) (selectedColor.getRed() * 255),
                        (int) (selectedColor.getGreen() * 255),
                        (int) (selectedColor.getBlue() * 255));

                return new Wydarzenia(
                        nazwaTextField.getText(),
                        miejsceTextField.getText(),
                        formattedDate,
                        godzinaTextField.getText(),
                        hexColor
                );
            }
            return null;
        });

        Optional<Wydarzenia> result = dialog.showAndWait();
        result.ifPresent(wydarzenie -> {
            listaWydarzen.add(wydarzenie);
            try {
				dm.addWydarzenieZKolorem(wydarzenie.getNazwa(), wydarzenie.getMiejsce(), wydarzenie.getData(), wydarzenie.getGodzina(), wydarzenie.getColor());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
    
    
    @FXML
    private void edytujWydarzenieGUI() {
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
            
            // Założenie, że selectedWydarzenie.getData() zwraca String
            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("Data");

            // Założenie, że selectedWydarzenie.getData() zwraca String
            LocalDate selectedDate = LocalDate.parse(selectedWydarzenie.getData(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            datePicker.setValue(selectedDate);

            
            TextField godzinaTextField = new TextField(selectedWydarzenie.getGodzina());
            godzinaTextField.setPromptText("Godzina");
            
            ColorPicker colorPick = new ColorPicker();
            colorPick.setValue(Color.web(selectedWydarzenie.getColor() != null ? selectedWydarzenie.getColor() : "#000000"));

            dialogPane.setContent(new ScrollPane(new VBox(8, nazwaTextField, miejsceTextField, datePicker, godzinaTextField, colorPick)));

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);
            
            // Nasłuchiwanie zmian w polach tekstowych, wybieraczu daty i wybieraczu koloru
            nazwaTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(newValue.trim().isEmpty() || miejsceTextField.getText().isEmpty() ||
                    		 datePicker.getValue() == null || godzinaTextField.getText().isEmpty()));

            miejsceTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(nazwaTextField.getText().isEmpty() || newValue.trim().isEmpty() ||
                    		 datePicker.getValue() == null || godzinaTextField.getText().isEmpty()));

            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String formattedDate = newValue != null ? newValue.format(formatter) : "";

                okButton.setDisable(
                        nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
                                formattedDate.isEmpty() || godzinaTextField.getText().isEmpty()
                );
            });

            godzinaTextField.textProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
                    		datePicker.getValue() == null || newValue.trim().isEmpty()));

            colorPick.valueProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(
                        nazwaTextField.getText().isEmpty() || miejsceTextField.getText().isEmpty() ||
                        datePicker.getValue() == null || godzinaTextField.getText().isEmpty() || newValue == null
                );
            });
            //colorPick.isPressed().okButton.setDisable();
            
            dialog.setResultConverter(dialogButton -> {
            	
                if (dialogButton == ButtonType.OK) {
                	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                     String formattedDate = selectedDate != null ? selectedDate.format(formatter) : "";
                     Color selectedColor = colorPick.getValue();
                     String hexColor = String.format("#%02X%02X%02X",
                             (int) (selectedColor.getRed() * 255),
                             (int) (selectedColor.getGreen() * 255),
                             (int) (selectedColor.getBlue() * 255));
                    return new Wydarzenia(
                    		nazwaTextField.getText(),
                            miejsceTextField.getText(),
                            formattedDate,
                            godzinaTextField.getText(),
                            hexColor,
                            selectedWydarzenie.getID()
                    ); 
                }
                return null;
            });
            Optional<Wydarzenia> result = dialog.showAndWait();
            result.ifPresent(editedWydarzenie -> {
            	// Aktualizacja danych wydarzenia
                selectedWydarzenie.setNazwa(editedWydarzenie.getNazwa());
                selectedWydarzenie.setMiejsce(editedWydarzenie.getMiejsce());
                selectedWydarzenie.setData(editedWydarzenie.getData());
                selectedWydarzenie.setGodzina(editedWydarzenie.getGodzina());
                selectedWydarzenie.setColor(editedWydarzenie.getColor());
                
                // Odświeżenie widoku tabeli
                tabelaWydarzen.refresh();
                // Aktualizacja danych w bazie danych
                try {
                	dm.editWydarzenieZKolorem(editedWydarzenie.getNazwa(), editedWydarzenie.getMiejsce(), editedWydarzenie.getData(), editedWydarzenie.getGodzina(), editedWydarzenie.getColor(),selectedWydarzenie.getID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    @FXML
    private void usunWydarzenieGUI() {	
    	Wydarzenia selectedWydarzenie = tabelaWydarzen.getSelectionModel().getSelectedItem();
        int selectedIdx = tabelaWydarzen.getSelectionModel().getSelectedIndex();
        if (selectedWydarzenie != null) {
        	usunW.setDisable(true);
        	
        	// Okno dialogowe potwierdzające usunięcie
        	Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usunięcia");
            alert.setHeaderText("Czy na pewno chcesz usunąć wybrane wydarzenie?");
            alert.setContentText("Tej operacji nie można cofnąć.");

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    try {
                    	// Usunięcie wydarzenia z bazy danych
                        dm.removeWydarzenieGUI(selectedWydarzenie.getID());
                        
                        // Usunięcie wydarzenia z listy i odświeżenie tabeli
                        tabelaWydarzen.getItems().remove(selectedIdx);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    @FXML
    private void wczytajPlik(ActionEvent event) {
    	// Okno wyboru pliku XML
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki XML", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        // Wczytanie wydarzeń z pliku, jeśli został wybrany plik
        if (selectedFile != null) {
            try {
                wczytajWydarzenia(selectedFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void wczytajWydarzenia(File file) throws ParserConfigurationException, SAXException, IOException, SQLException {
        try (FileInputStream in = new FileInputStream(file)) {
        	 // Tworzenie parsera XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            
            // Pobieranie listy węzłów reprezentujących wydarzenia
            NodeList eventNodes = doc.getElementsByTagName("Wydarzenie");
            
            // Przetwarzanie każdego węzła reprezentującego wydarzenie
            for (int i = 0; i < eventNodes.getLength(); i++) {
                Element eventElement = (Element) eventNodes.item(i);
                
                String nazwa = eventElement.getElementsByTagName("Nazwa").item(0).getTextContent();
                String miejsce = eventElement.getElementsByTagName("Miejsce").item(0).getTextContent();
                String data = eventElement.getElementsByTagName("Data").item(0).getTextContent();
                String godzina = eventElement.getElementsByTagName("Godzina").item(0).getTextContent();
                String kolor = eventElement.getElementsByTagName("Kolor").item(0).getTextContent();
                
                Wydarzenia wydarzenie = new Wydarzenia(nazwa, miejsce, data, godzina, kolor);

                // Pobieranie informacji o kontaktach
                NodeList contactNodes = eventElement.getElementsByTagName("Kontakt"+i);
                ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
                for (int j = 0; j < contactNodes.getLength(); j++) {
                    Element contactElement = (Element) contactNodes.item(j);
                    String id = eventElement.getElementsByTagName("ID"+j).item(0).getTextContent();
                    int ID = Integer.parseInt(id);
                    String imie = contactElement.getElementsByTagName("Imie").item(0).getTextContent();
                    String nazwisko = contactElement.getElementsByTagName("Nazwisko").item(0).getTextContent();
                    String nu = contactElement.getElementsByTagName("NumerTelefonu").item(0).getTextContent();
                    int numer = Integer.parseInt(nu);
                    String email = contactElement.getElementsByTagName("Email").item(0).getTextContent();

                    Kontakt kontakt = new Kontakt(imie, nazwisko, numer, email, ID);
                    kontakty.add(kontakt);
                    wydarzenie.setKontakt(kontakt);
                }
                
                // Sprawdzenie, czy wydarzenie już istnieje w liście
                boolean eventExists = listaWydarzen.contains(wydarzenie);
                
                // Dodanie wydarzenia do listy i bazy danych, jeśli nie istnieje
                if (!eventExists) {
                    listaWydarzen.add(wydarzenie);
                    dm.addWydarzenieZKoloremZKontaktem(nazwa, miejsce, data, godzina, kolor, kontakty, wydarzenie.getID());
                    for(int k = 0;k<kontakty.size();k++)
                    {
                    	System.out.println(kontakty.get(k));
                    	dm.assignKontaktToWydarzenia(k+1, wydarzenie.getID()+1);
                    }
                }
            }
        }
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
