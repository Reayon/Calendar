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

    public void initialize() {
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
