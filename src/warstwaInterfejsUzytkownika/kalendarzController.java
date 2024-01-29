package warstwaInterfejsUzytkownika;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;


public class kalendarzController {
	private YearMonth currentYearMonth;
    // Kalendarz Tab
    @FXML
    private Tab kalendarzTab;

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

    // Kontakty Tab
    @FXML
    private Tab kontaktyTab;

    @FXML
    private VBox vboxKontakty;

    @FXML
    private Label imie;

    @FXML
    private Label nazwisko;

    @FXML
    private Label nrtel;

    @FXML
    private Label email;

    // Wydarzenia Tab
    @FXML
    private Tab wydarzeniaTab;

    @FXML
    private VBox vboxWydarzenia;

    @FXML
    private Label nazwa;

    @FXML
    private Label miejsce;

    @FXML
    private Label data;

    @FXML
    private Label godzina;
    
    @FXML
    private void initialize() {
        currentYearMonth = YearMonth.now();
        createCalendarGrid();  // Utwórz siatkę kalendarza
        updateCalendar();
        centerGridPane();
    }

    private void createCalendarGrid() {
        kalendarz.getChildren().clear();  // Wyczyść istniejące etykiety przed utworzeniem nowej siatki

        // Days of the week labels
        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            kalendarz.add(dayLabel, i, 0);
        }

        // Days of the month labels
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Label dayLabel = new Label();
                kalendarz.add(dayLabel, col, row + 1);  // Dodaj 1, aby zacząć od wiersza 1
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

        // Aktualizacja etykiet z miesiącem i rokiem
        miesiac.setText(currentYearMonth.getMonth().toString());
        rok.setText(String.valueOf(currentYearMonth.getYear()));

        int dayCounter = 1;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                Label dayLabel = (Label) kalendarz.getChildren().get(7 * row + col);

                if (row == 0 && col < dayOfWeek - 1) {
                    // Dni przed pierwszym dniem miesiąca
                    dayLabel.setText("");
                } else if (dayCounter <= daysInMonth) {
                    // Dni w miesiącu
                    dayLabel.setText(Integer.toString(dayCounter));
                    dayCounter++;
                } else {
                    // Dni po ostatnim dniu miesiąca
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
}