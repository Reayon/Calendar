package warstwaInterfejsUzytkownika;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import warstwaDanych.Kontakt;
import warstwaLogiki.dataManager;

public class kontaktyController {
	private dataManager dm = new dataManager();
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

    public void setDataManager(dataManager dm) {
        this.dm = dm;
    }
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
}
