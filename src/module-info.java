module Organizer {
	requires java.xml;
	requires java.sql;
	requires java.desktop;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.controls;
	
	exports warstwaInterfejsUzytkownika to javafx.graphics, javafx.fxml;
	opens warstwaDanych to javafx.base;
	opens warstwaInterfejsUzytkownika to javafx.fxml;
}