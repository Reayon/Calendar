package warstwaInterfejsUzytkownika;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import warstwaDanych.Kontakt;

public class kontaktyController {
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
        nrTelColumn.setCellValueFactory(new PropertyValueFactory<>("nr_tel"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        ObservableList<Kontakt> listaKontaktow = FXCollections.observableArrayList(new Kontakt("Marek","Marecki",123141241,"MarekM@wp.pl"),
        																			new Kontakt("Adam","Darecki",234598236,"AdamD@wp.pl"),
        																			new Kontakt("Filip","Banasiak",19234867,"Filip@gmail.com"),
        																			new Kontakt("Kacper","Jozwiak",394058734,"Juzba@wp.pl"),
        																			new Kontakt("Dawid","Centkowski",345876238,"Dawid@wp.pl"));
        tabelaKontaktow.setItems(listaKontaktow);
    }
}
