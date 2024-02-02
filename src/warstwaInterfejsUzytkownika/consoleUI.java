package warstwaInterfejsUzytkownika;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import java.util.Scanner;

import warstwaLogiki.bufor;
import warstwaLogiki.dataManager;

public class consoleUI {
	
	private dataManager dm;
	
	public consoleUI(dataManager dm) {
		this.dm = dm;
	}
	
	public void showCUI(Scanner sc) throws SQLException, FileNotFoundException{
		Scanner sc1 = new Scanner(System.in);
		Scanner skaner = new Scanner(System.in).useDelimiter("\n");;
		
		while(true)
		{
		System.out.println("Kalendarz");
		System.out.println("1 Dodaj kontakt");
		System.out.println("2 Usuń kontakt");
		System.out.println("3 Edytuj kontakty");
		System.out.println("4 Posortuj kontakty");
		System.out.println("5 Dodaj wydarzenie");
		System.out.println("6 Usuń wydarznie");
		System.out.println("7 Edytuj wydarzenia");
		System.out.println("8 Posortuj wydarzenia");
		System.out.println("9 Przypisanie kontaktu do wydarzenia");
		System.out.println("0 Wyjdz");
		
		int wybor1 = sc1.nextInt();
		int w = 0;
		switch(wybor1)
		{
		case 1: 
			System.out.println("Podaj imie");
			String imie = sc.next();
			System.out.println("Podaj nazwisko");
			String nazwisko= sc.next();
			System.out.println("Podaj nr");
			int numer = sc1.nextInt();
			System.out.println("Podaj email");
			String email= sc.next();
			dm.addKontakt(imie, nazwisko, numer, email);
			break;
		case 2: 
			System.out.println(dm.wyswietlKontakty());
			w = sc1.nextInt();
			dm.removeKontakt(w);
			break;
		case 3: 
			System.out.println(dm.wyswietlKontakty());
			System.out.println("Czy chcesz edytować kontakty?");
			System.out.println("1 Tak");
			System.out.println("2 Nie");
			wybor1 = sc1.nextInt();
			switch(wybor1) {
				case 1:
					System.out.println(dm.wyswietlKontakty());
					System.out.println("Który kontakt chcesz zedytowac?");
					wybor1 = sc1.nextInt();
					System.out.println("Podaj imie");
					String imie1 = sc.next();
					System.out.println("Podaj nazwisko");
					String nazwisko1= sc.next();
					System.out.println("Podaj nr");
					int numer1 = sc1.nextInt();
					System.out.println("Podaj email");
					String email1 = sc.next();
					dm.editKontakt(imie1, nazwisko1, numer1, email1, wybor1);
					break;
				case 2:
					break;
			}
			break;
		case 4: 
			System.out.println("Po czy chcesz posortowac?");
			System.out.println("1. imie");
			System.out.println("2. nazwisko");
			int wybor2 = sc1.nextInt();
			dm.sortujKontakty(wybor2);
			break;
		case 5: 
			System.out.println("Podaj nazwe wydarzenia");
			String nazwa = sc.next();
			System.out.println("Podaj miejsce wydarzenia");
			String miejsce = sc.next();
			System.out.println("Podaj date (dd.mm.yyyy)");
			String data = sc.next();
			System.out.println("Podaj godzine (h:min)");
			String godzina = sc.next();
			dm.addWydarzenie(nazwa, miejsce, data, godzina);
			break;
		case 6: 
			System.out.println(dm.wyswietlWydarzenia());
			w = sc1.nextInt();
			dm.removeWydarzenie(w);
			break;
		case 7: 
			System.out.println(dm.wyswietlWydarzenia());
			System.out.println("Czy chcesz edytować wydarzenia?");
			System.out.println("1 Tak");
			System.out.println("2 Nie");
			wybor1 = sc1.nextInt();
			switch(wybor1) {
			case 1:
				System.out.println(dm.wyswietlKontakty());
				System.out.println("Który wydarzenie chcesz zedytowac?");
				wybor1 = sc1.nextInt();
				System.out.println("Podaj nazwe wydarzenia");
				String nazwa1 = sc.next();
				System.out.println("Podaj miejsce wydarzenia");
				String miejsce1 = sc.next();
				System.out.println("Podaj date (dd.mm.yyyy)");
				String data1 = sc.next();
				System.out.println("Podaj godzine (h:min)");
				String godzina1 = sc.next();
				dm.editWydarzenie(nazwa1, miejsce1, data1, godzina1, wybor1);
				break;
			case 2:
				break;
		}
			break;
		case 8: 
			System.out.println("Po czy chcesz posortowac?");
			System.out.println("1. nazwie");
			System.out.println("2. miejscu");
			int wybor3 = sc1.nextInt();
			dm.sortujWydarzenia(wybor3);
			break;
		case 9:
			System.out.println(dm.wyswietlKontakty());
			System.out.println("Podaj nr id kontaktu");
			int nr1 = sc1.nextInt();
			System.out.println(dm.wyswietlWydarzenia());
			System.out.println("Podaj nr id wydarzenia");
			int nr2 = sc1.nextInt();
			dm.assignKontaktToWydarzenia(nr1, nr2);
			break;
		case 0: 
			System.out.println("Do widzenia");
			sc.close();
			sc1.close();
			System.exit(0);
			break;
			}	
		}
	}
}
