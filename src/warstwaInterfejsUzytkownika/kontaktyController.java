package warstwaInterfejsUzytkownika;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
