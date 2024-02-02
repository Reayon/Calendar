package warstwaInterfejsUzytkownika;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import warstwaDanych.Kontakt;
import warstwaLogiki.dataManager;


public class kalendarzController {
	private dataManager dm;
	private YearMonth currentYearMonth;

    @FXML
    private Label miesiac;

    @FXML
    private Label rok;

    @FXML
    private GridPane kalendarz;
    
    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
    private kontaktyController kontaktyController;
    
    @FXML
    private void initialize() {
        currentYearMonth = YearMonth.now();
        createCalendarGrid();
        updateCalendar();
        centerGridPane();
        Platform.runLater(()->{
        	System.out.println();
        });
    }
    
    public void setDataManager(dataManager dm) {
        this.dm = dm;
    }
    
    private void createCalendarGrid() {
        kalendarz.getChildren().clear();

        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            kalendarz.add(dayLabel, i, 0);
        }

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Label dayLabel = new Label();
                kalendarz.add(dayLabel, col, row + 1);
            }
        }
    }

    private void updateCalendar() {
        if (currentYearMonth == null) {
            return;
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        LocalDate firstDayOfMonth = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), 1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        miesiac.setText(currentYearMonth.getMonth().toString());
        rok.setText(String.valueOf(currentYearMonth.getYear()));

        int dayCounter = 1;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Label dayLabel = (Label) kalendarz.getChildren().get(7 * row + col);

                if (row == 0 && col < dayOfWeek - 1) {
                    dayLabel.setText("");
                } else if (dayCounter <= daysInMonth) {
                    dayLabel.setText(Integer.toString(dayCounter));
                    dayCounter++;
                } else {
                    dayLabel.setText("");
                }
            }
        }
    }

    private void centerGridPane() {
    	int numRows = kalendarz.getRowConstraints().size();
    	int numCols = kalendarz.getColumnConstraints().size();

        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            for (int colIndex = 0; colIndex < numCols; colIndex++) {
                Label dayLabel = (Label) kalendarz.getChildren().get(rowIndex * numCols + colIndex);
                dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            }
        }
    }

    @FXML
    private void showPreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void showNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }
    
    public void switchToKontakty(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("kontakty.fxml"));
        Parent root = loader.load();
        kontaktyController kontaktyController = loader.getController();
        kontaktyController.setDataManager(dm);
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	scene = new Scene(root);
    	stage.setScene(scene);
    	stage.show();
    }
    
    public void switchToWydarzenia(ActionEvent event) throws IOException {
  	  	root = FXMLLoader.load(getClass().getResource("wydarzenia.fxml"));
  	  	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
  	  	scene = new Scene(root);
  	  	stage.setScene(scene);
  	  	stage.show();
  }
    
}