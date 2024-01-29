package warstwaLogiki;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;
import warstwaInterfejsUzytkownika.consoleUI;
import warstwaInterfejsUzytkownika.graphicUI;

public class Glowna {

	public static Scanner sc = new Scanner(System.in).useDelimiter("\n");
	public static Scanner sc1 = new Scanner(System.in);
	
	public static void main(String[] args) throws SQLException{
		
		ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
		ArrayList<Wydarzenia> wydarzenia = new ArrayList<Wydarzenia>();
		
		dbManager db = new dbManager();
		//XML xml = new XML();
		
		db.odczytajKontakty(kontakty);
		db.odczytajWydarzenia(wydarzenia);
		db.polaczAssign(kontakty, wydarzenia);
		
		dataManager dm = new dataManager(kontakty, wydarzenia);
		
		consoleUI c = new consoleUI(dm);
		
		graphicUI gui = new graphicUI(dm);
		
		while(true) {
			System.out.println("Wybierz skąd chcesz pobrać dane: ");
			System.out.println("1 Baza danych");
			System.out.println("2 Plik XML");
			
			int wybor = sc1.nextInt();
			switch(wybor) {
			case 1:
				System.out.println("Wybierz interfejs: ");
				System.out.println("1 Konsolowy");
				System.out.println("2 Graficzny");
				wybor = sc1.nextInt();
					switch(wybor) {
						case 1:
							c.showCUI(sc);
							break;
						case 2:
							gui.showGUI();
							break;
					} break;
			case 2:
				System.out.println("Nie działa");
			}
		}
	}
}
