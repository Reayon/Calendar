package warstwaInterfejsUzytkownika;

import java.io.IOException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import warstwaDanych.Kontakt;
import warstwaLogiki.dataManager;

public class kalendarzController {
    protected dataManager dm = new dataManager();
    private YearMonth currentYearMonth;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Button aboutButton;
    
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

    protected Stage stage;
    protected Scene scene;
    @FXML
    private kontaktyController kontaktyController;
    @FXML
	protected wydarzeniaController wydarzeniaController;

    // Metoda wywoływana podczas inicjalizacji kontrolera.
    @FXML
    private void initialize() {
        currentYearMonth = YearMonth.now();
        createCalendarGrid();
        updateCalendar();
        centerGridPane();
        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(this::handleDatePickerAction);
        Platform.runLater(() -> {
            System.out.println();
        });
        aboutButton.setOnAction(event -> showAboutDialog());
        
    }
    private void handleDatePickerAction(ActionEvent event) {
        currentYearMonth = YearMonth.from(datePicker.getValue());
        updateCalendar();
    }
    // Ustawienie obiektu DataManager, który zarządza danymi.
    public void setDataManager(dataManager dm) {
        this.dm = dm;
    }

    // Utworzenie siatki kalendarza.
    private void createCalendarGrid() {
        kalendarz.getChildren().clear();
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        // Pętle zagnieżdżone tworzące etykiety dla każdego dnia w siatce 6x7.
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Label dayLabel = new Label();
                kalendarz.add(dayLabel, col, row);
                dayLabel.setAlignment(Pos.CENTER);
                dayLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));

                // Dodanie obsługi zdarzenia kliknięcia na etykietę.
                dayLabel.setOnMouseClicked(event -> handleDayLabelClick(dayLabel.getText()));
            }
        }
        updateCalendar();
    }
    
    // Obsługa zdarzenia kliknięcia na etykietę z dniem.
    private void handleDayLabelClick(String dayText) {
        if (!dayText.isEmpty()) {
            String dayInfo = getDayInformation(dayText);
            showDayInfoDialog(dayText, dayInfo);
        }
    }
    
    // Pobranie informacji o wybranym dniu.
    private String getDayInformation(String dayText) {
        int selectedDay = Integer.parseInt(dayText);
        
        System.out.println(selectedDay);
        LocalDate selectedDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), selectedDay);
        

        ArrayList<String> wydarzeniaDanegoDnia = dm.getWydarzeniaDanegoDnia(selectedDate);
        StringBuilder dayInfoBuilder = new StringBuilder();

        if (!wydarzeniaDanegoDnia.isEmpty()) {
            dayInfoBuilder = new StringBuilder("Wydarzenia na " + selectedDate + ":\n");
            for (String event : wydarzeniaDanegoDnia) {
                dayInfoBuilder.append("").append(event).append("\n");
            }
        } else {
            dayInfoBuilder.append("Brak wydarzeń dla tego dnia.\n");
        }
        return dayInfoBuilder.toString();
    }

    // Wyświetlenie okna dialogowego z informacjami o wybranym dniu.
    private void showDayInfoDialog(String dayText, String dayInfo) {
        Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("calendar-icon.png"));
        alert.setTitle("Informacje o dniu");
        alert.setHeaderText("Informacje dla dnia " + dayText);
        alert.setContentText(dayInfo);
        alert.showAndWait();
    }
   
 // Aktualizacja widoku kalendarza.
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
        int blankDays = 0;
        // Pętle zagnieżdżone aktualizujące etykiety w kalendarzu.
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Label dayLabel = (Label) kalendarz.getChildren().get(7 * row + col);
                int dayIndex = 7 * row + col - (dayOfWeek - 1);

                // Sprawdzenie, czy komórka powinna być pusta (przed pierwszym dniem lub po ostatnim dniu).
                if (dayIndex < 0 || dayIndex >= daysInMonth) {
                    dayLabel.setText("");
                    if(dayCounter <= 1) {
                    	blankDays++;
                    }
                } else {
                    dayLabel.setText(Integer.toString(dayCounter));
                    dayCounter++;
                }
                final int blank = blankDays;
                // Podświetlenie obecnego dnia niebieskim obramowaniem.
                if (LocalDate.now().getMonth().equals(currentYearMonth.getMonth()) &&
                        LocalDate.now().getYear() == currentYearMonth.getYear() &&
                        dayLabel.getText().equals(Integer.toString(LocalDate.now().getDayOfMonth()))) {
                	//dayLabel.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
                	dayLabel.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null)));
                } else {
                    dayLabel.setStyle("");
                    dayLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
                }
                
                Platform.runLater(()->{   
                    for(int i=0; i<dm.pobierzListeWydarzen().size(); i++) {
                    	if(dm.getDayOfMonthW(i) == dayIndex && dm.getMonthW(i) == currentYearMonth.getMonth().getValue() && dm.getYearW(i) == currentYearMonth.getYear()) {
                    		
                    		kalendarz.getChildren().get(dayIndex+blank-1).setStyle("-fx-background-color: "+toCssColor(dm.pobierzListeWydarzen().get(i).getColor())+";");
                    	}
                    }
                 });
            }
        }
    }

    // Wyrównanie siatki kalendarza.
    private void centerGridPane() {
        int numRows = kalendarz.getRowConstraints().size();
        int numCols = kalendarz.getColumnConstraints().size();
        int totalCells = numRows * numCols;

        // Ustawienie maksymalnych rozmiarów dla każdej etykiety w siatce.
        for (int i = 0; i < totalCells && i < kalendarz.getChildren().size(); i++) {
            Label dayLabel = (Label) kalendarz.getChildren().get(i);
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }

    // Przejście do poprzedniego miesiąca.
    @FXML
    private void showPreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    // Przejście do następnego miesiąca.
    @FXML
    private void showNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }
    @FXML
    protected void showAboutDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("calendar-icon.png"));
        alert.setTitle("O programie ''Kalendarz''");
        alert.setHeaderText("Informacje ogólne: ");
        alert.setContentText("Wersja: 1.0.0\n"
        		+ "Autorzy: Filip Banasiak i Dawid Centkowski\n"
        		+ "Grupa: 3\n");
        alert.showAndWait();
    }

    // Przełączenie na widok kontakty.
    public void switchToKontakty(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("kontakty.fxml"));
        Parent root = loader.load();
        kontaktyController kontaktyController = loader.getController();
        kontaktyController.setDataManager(dm);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Przełączenie na widok wydarzenia.
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
    public static String toCssColor(Color color) {
        return "rgba(" + Math.round(255 * color.getRed()) + ","
                + Math.round(255 * color.getGreen()) + ","
                + Math.round(255 * color.getBlue()) + ","
                + color.getOpacity() + ")";
    }
}
