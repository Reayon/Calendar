package warstwaLogiki;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import warstwaDanych.Kategorie;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;
import warstwaInterfejsUzytkownika.consoleUI;
import warstwaInterfejsUzytkownika.graphicUI;

public class Glowna {

	public static Scanner sc = new Scanner(System.in).useDelimiter("\n");
	public static Scanner sc1 = new Scanner(System.in);
	
	public static void main(String[] args) throws SQLException, FileNotFoundException{
		launch(args);
		ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
		ArrayList<Wydarzenia> wydarzenia = new ArrayList<Wydarzenia>();
		ArrayList<Kategorie> kategorie = new ArrayList<Kategorie>();
		
		dataManager dm = new dataManager(kontakty, wydarzenia,kategorie);
		
		dm.odczytajDane();
		
		//consoleUI cui = new consoleUI(dm);
		
		//graphicUI gui = new graphicUI();
		//cui.showCUI(sc);
		Application.launch(graphicUI.class,args);
		/*while(true) {
			System.out.println("Wybierz interfejs: ");
			System.out.println("1 Konsolowy");
			System.out.println("2 Graficzny");
			
			int wybor = sc1.nextInt();
			switch(wybor) {
			case 1:
				cui.showCUI(sc);
				wybor = sc1.nextInt();
			case 2:
				Application.launch(graphicUI.class,args);
				System.out.println("Nie działa");
			}
		}*/
	}

	private static void launch(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
