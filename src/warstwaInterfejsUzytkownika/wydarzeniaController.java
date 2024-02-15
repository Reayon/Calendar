package warstwaInterfejsUzytkownika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import warstwaDanych.Kategorie;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;
import warstwaLogiki.WydarzenieMiejsceComparator;

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
    private TableColumn<Wydarzenia, Kategorie> kategoriaColumn;

    @FXML
    private MenuItem usunW;
    
    @FXML
    private MenuItem editW;
    
    @FXML
    private TextField szukaj;
    
    @FXML
    private MenuItem przypiszK;
    
    @FXML
    protected MenuItem aboutProgram;
    
    @FXML
    private ComboBox<Kontakt> kontaktyComboBox;

    private kategorieController kategorieController;
    
    public void initialize() {
    	// Wyłączenie opcji edycji i usunięcia na starcie
    	editW.setDisable(true);
    	usunW.setDisable(true);
    	przypiszK.setDisable(true);
    	
    	aboutProgram.setOnAction(event -> showAboutDialog());
    	
    	 // Nasłuchiwanie na zdarzenia związane z zaznaczeniem wiersza w tabeli
    	tabelaWydarzen.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editW.setDisable(newValue == null);
            usunW.setDisable(newValue == null);
            przypiszK.setDisable(newValue == null);
        });
    	
    	 // Nasłuchiwanie podwójnego kliknięcia LPM na wiersz tabeli
    	tabelaWydarzen.setRowFactory(tv -> {
            TableRow<Wydarzenia> row = new TableRow<>();

            // Kontekstowe menu dla wiersza tabeli
            ContextMenu contextMenu = new ContextMenu();

            // Tworzenie pozycji w kontekstowym menu
            MenuItem przypiszKontaktMenuItem = new MenuItem("Przypisz kontakt");
            przypiszKontaktMenuItem.setOnAction(event -> przypiszKontaktGUI());
            MenuItem edytujWydarzenieMenuItem = new MenuItem("Edytuj Wydarzenie");
            edytujWydarzenieMenuItem.setOnAction(event -> edytujWydarzenieGUI());
            //MenuItem usunKontaktZWydarzeniaMenuItem = new MenuItem("Usun przypisane kontakty");
            //usunKontaktZWydarzeniaMenuItem.setOnAction(event -> usunKontaktZWydarzeniaGUI());

            // Dodawanie pozycji do kontekstowego menu
            contextMenu.getItems().add(przypiszKontaktMenuItem);
            contextMenu.getItems().add(edytujWydarzenieMenuItem);
            //contextMenu.getItems().add(usunKontaktZWydarzeniaMenuItem);

            // Obsługa zdarzenia dla kliknięcia prawym przyciskiem myszy
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    // Wyświetlenie kontekstowego menu w miejscu kliknięcia
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
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
        kategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("kategoriaNazwa"));
        
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
            	for(int j=0; j<dm.pobierzListeWydarzen().get(i).getKontaktySize(); j++) {
            		wydarzenie.setKontakt(dm.pobierzListeWydarzen().get(i).getExactKontakt(j));
            	}
            	if(dm.pobierzListeWydarzen().get(i).getKategoria()!= null) {
        			wydarzenie.setKategoria(dm.pobierzListeWydarzen().get(i).getKategoria());
        		}
            	listaWydarzen.add(wydarzenie);
            }
            tabelaWydarzen.setItems(listaWydarzen);
            filtrujWydarzenia();
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
                int nextId = dm.getNastepneIdWydarzenia();
                return new Wydarzenia(
                        nazwaTextField.getText(),
                        miejsceTextField.getText(),
                        formattedDate,
                        godzinaTextField.getText(),
                        selectedColor,
                        nextId
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
    private void przypiszKontaktGUI() {
    	Wydarzenia selectedWydarzenie = tabelaWydarzen.getSelectionModel().getSelectedItem();
        if (selectedWydarzenie != null) {
            Dialog<Wydarzenia> dialog = new Dialog<>();
            dialog.setTitle("Przypisz kontakt do wydarzenia");
            dialog.setHeaderText("Wybierz kontakt, który chcesz przypisać do wydarzenia:");

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("calendar-icon.png"));

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            ComboBox<Kontakt> kontaktyComboBox = new ComboBox<>();
            kontaktyComboBox.setPromptText("Wybierz kontakty");
            kontaktyComboBox.getItems().addAll(dm.pobierzListeKontaktow());

            kontaktyComboBox.setCellFactory(lv -> new ListCell<Kontakt>() {
                @Override
                protected void updateItem(Kontakt kontakt, boolean empty) {
                    super.updateItem(kontakt, empty);
                    setText(empty ? "" : kontakt.getImie() + " " + kontakt.getNazwisko());
                }
            });
            kontaktyComboBox.setButtonCell(new ListCell<Kontakt>() {
                @Override
                protected void updateItem(Kontakt kontakt, boolean empty) {
                    super.updateItem(kontakt, empty);
                    setText(empty ? "" : kontakt.getImie() + " " + kontakt.getNazwisko());
                }
            });

            dialogPane.setContent(new ScrollPane(new VBox(8, kontaktyComboBox)));

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);

            // Nasłuchiwanie zmian w wybieraczu kontaktów
            kontaktyComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                    okButton.setDisable(newValue == null));
            dialog.setResultConverter(dialogButton -> {
            	
                if (dialogButton == ButtonType.OK) {
                	Kontakt selectedContact = kontaktyComboBox.getSelectionModel().getSelectedItem();
                        
                        // Odświeżenie widoku tabeli
                        tabelaWydarzen.refresh();

                        try {
							dm.dodajKontaktDoWydarzeniaGUI(selectedContact, selectedWydarzenie);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }
            return null;
            });
            dialog.showAndWait();
        }
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
            colorPick.setValue(selectedWydarzenie.getColor());

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
                    return new Wydarzenia(
                    		nazwaTextField.getText(),
                            miejsceTextField.getText(),
                            formattedDate,
                            godzinaTextField.getText(),
                            selectedColor,
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
    
    //---------------------------------Wczytywanie i zapisywanie-------------------------------------
    
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
                
                
                Wydarzenia wydarzenie = new Wydarzenia(nazwa, miejsce, data, godzina, Color.valueOf(kolor));

                // Pobieranie informacji o kontaktach
                NodeList contactNodes = eventElement.getElementsByTagName("Kontakt"+i);
                ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
                for (int j = 0; j < contactNodes.getLength(); j++) {
                    Element contactElement = (Element) contactNodes.item(j);
                    String id = eventElement.getElementsByTagName("ID").item(0).getTextContent();
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
                    dm.addWydarzenieZKoloremZKontaktem(nazwa, miejsce, data, godzina, Color.valueOf(kolor), kontakty, wydarzenie.getID());
                    for(int k = 0;k<kontakty.size();k++)
                    {
                    	System.out.println(kontakty.get(k));
                    	dm.assignKontaktToWydarzenia(k+1, wydarzenie.getID()+1);
                    }
                }
            }
        }
    }
    
    @FXML
    private void filtrujWydarzenia() {
    	FilteredList<Wydarzenia> filteredData = new FilteredList<>(listaWydarzen, p -> true);
		
		// 2. Set the filter Predicate whenever the filter changes.
        szukaj.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(wydarzenie -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (wydarzenie.getNazwa().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				} else if (wydarzenie.getMiejsce().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				} else if (wydarzenie.getData().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				} else if (wydarzenie.getGodzina().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name. 
				}
				return false; // Does not match.
			});
		});
        
        tabelaWydarzen.setItems(filteredData);
    }
    
    @FXML
    private void zapiszDoPlikuXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki XML", "*.xml"));
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try {
                zapiszWydarzenDoXML(selectedFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void zapiszWydarzenDoXML(File file) {
    	try {
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(file), "utf-8");
			
			writer.writeStartDocument("1.0");
			
			writer.writeStartElement("ZapisaneWydarzenia");
			for (int i = 0; i < listaWydarzen.size(); i++) {
				Wydarzenia aktualneWydarzenie = listaWydarzen.get(i);
				writer.writeStartElement("Wydarzenie");
				writer.writeStartElement("Nazwa");
				writer.writeCharacters(aktualneWydarzenie.getNazwa());
				writer.writeEndElement();
				writer.writeStartElement("Miejsce");
				writer.writeCharacters(aktualneWydarzenie.getMiejsce());
				writer.writeEndElement();
				writer.writeStartElement("Data");
				writer.writeCharacters(aktualneWydarzenie.getData());
				writer.writeEndElement();
				writer.writeStartElement("Godzina");
				writer.writeCharacters(aktualneWydarzenie.getGodzina());
				writer.writeEndElement();
				writer.writeStartElement("Kolor");
	            writer.writeCharacters(aktualneWydarzenie.getColor().toString());
	            writer.writeEndElement();
				writer.writeStartElement("ZapisaneKontakty");
				for (int j = 0; j < listaWydarzen.get(i).getKontaktySize(); j++) {
					Kontakt aktualnyKontakt = listaWydarzen.get(i).getExactKontakt(j);
					writer.writeStartElement("Kontakt"+i);
					writer.writeStartElement("ID");
					writer.writeCharacters(Integer.toString(aktualnyKontakt.getID()));
					writer.writeEndElement();
					writer.writeStartElement("Imie");
					writer.writeCharacters(aktualnyKontakt.getImie());
					writer.writeEndElement();
					writer.writeStartElement("Nazwisko");
					writer.writeCharacters(aktualnyKontakt.getNazwisko());
					writer.writeEndElement();
					writer.writeStartElement("NumerTelefonu");
					writer.writeCharacters(Integer.toString(aktualnyKontakt.getNr()));
					writer.writeEndElement();
					writer.writeStartElement("Email");
					writer.writeCharacters(aktualnyKontakt.getEmail());
					writer.writeEndElement();
					writer.writeEndElement();
				}
				writer.writeEndElement();
				writer.writeEndElement();
			}
			writer.writeEndElement();
			writer.flush();
			writer.close();
			System.out.println("Dane zapisane do pliku.");
		}catch(Exception e) {
			System.out.println(e.getMessage());
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
    public void switchToKategorie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("kategorie.fxml"));
        Parent root = loader.load();
        kategorieController = loader.getController();
        kategorieController.setDataManager(dm);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
