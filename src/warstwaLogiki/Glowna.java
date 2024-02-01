package warstwaLogiki;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;
import warstwaInterfejsUzytkownika.consoleUI;
import warstwaInterfejsUzytkownika.graphicUI;

public class Glowna {

	public static Scanner sc = new Scanner(System.in).useDelimiter("\n");
	public static Scanner sc1 = new Scanner(System.in);
	
	public static void main(String[] args) throws SQLException, FileNotFoundException{
		ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
		ArrayList<Wydarzenia> wydarzenia = new ArrayList<Wydarzenia>();
		
		dataManager dm = new dataManager(kontakty, wydarzenia);
		
		dm.odczytajDane();
		
		consoleUI cui = new consoleUI(dm);
				
		while(true) {
			System.out.println("Wybierz interfejs: ");
			System.out.println("1 Konsolowy");
			System.out.println("2 Graficzny");
			
			int wybor = sc1.nextInt();
			switch(wybor) {
			case 1:
				cui.showCUI(sc);
			case 2:
				Application.launch(graphicUI.class,args);
				break;
			}
		}
	}
}
