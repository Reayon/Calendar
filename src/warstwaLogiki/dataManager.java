package warstwaLogiki;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.scene.paint.Color;
import warstwaDanych.Kategorie;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class dataManager {
	
	private XML xml = new XML();
	private dbManager db = new dbManager();
	private ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
	private ArrayList<Wydarzenia> wydarzenia = new ArrayList<Wydarzenia>();
	private ArrayList<Kategorie> kategorie = new ArrayList<Kategorie>();
	
	public dataManager() {
	    this.kontakty = new ArrayList<>();
	    this.wydarzenia = new ArrayList<>();
	    this.kategorie = new ArrayList<>();
	}
	
	public dataManager(ArrayList<Kontakt> kontakty, ArrayList<Wydarzenia> wydarzenia, ArrayList<Kategorie> kategorie) {
		this.kontakty = kontakty;
		this.wydarzenia = wydarzenia;
		this.kategorie = kategorie;
	}
	
	public ArrayList<Kontakt> pobierzListeKontaktow() {
		return kontakty;
    }
	public ArrayList<Wydarzenia> pobierzListeWydarzen() {
		return wydarzenia;
    }
	public ArrayList<Kategorie> pobierzListeKategorii() {
		return kategorie;
    }
	
	public void wykonajZBufora() throws FileNotFoundException {
		if(db.getLicznikZapytan() > 10) {
			db.wykonajZapytania();
			db.wyzerujLicznikZapytan();
		}
	}
	
	public void odczytajDane() throws SQLException, FileNotFoundException {
		if(db.czyPolaczone()) {
			if(db.getBufor().isEmpty()==false) {
			db.wykonajZapytania();
			db.odczytajKontakty(kontakty);
			db.odczytajWydarzenia(wydarzenia);
			db.polaczAssign(kontakty, wydarzenia);
			db.odczytajKategorie(kategorie);
			db.polaczAssignKategorie(wydarzenia, kategorie);
			} else {
				db.odczytajKontakty(kontakty);
				db.odczytajWydarzenia(wydarzenia);
				db.polaczAssign(kontakty, wydarzenia);
				db.odczytajKategorie(kategorie);
				db.polaczAssignKategorie(wydarzenia, kategorie);
			}
		} else {
			xml.odczytKontaktowXML(kontakty);
			xml.odczytWydarzenXML(wydarzenia);
		}
	}
	
	public String wyswietlKontakty() throws SQLException {
		String tekst = "";
		for (int i = 0; i < kontakty.size(); i++) {
			tekst += i+1+". ";
			tekst += kontakty.get(i).toStringZWydarzeniem()+" ";
			tekst += "\n";
		}
		return tekst;
	}
	
	public String wyswietlWydarzenia() throws SQLException {
		String tekst = "";
		for (int i = 0; i < wydarzenia.size(); i++) {
			tekst += i+1+". ";
			tekst += wydarzenia.get(i)+" ";
			tekst += "\n";
		}
		return tekst;
	}
	
	public String wyswietlKategorie() throws SQLException {
		String tekst = "";
		for (int i = 0; i < wydarzenia.size(); i++) {
			tekst += i+1+". ";
			tekst += wydarzenia.get(i)+" ";
			tekst += "\n";
		}
		return tekst;
	}
	
	private boolean equalsArrayListKontakty(Kontakt k) throws SQLException {	
		for (int i = 0; i < kontakty.size(); i++) {
			if(kontakty.get(i).equals(k)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean equalsArrayListWydarzenia(Wydarzenia w) throws SQLException {
		for (int i = 0; i < wydarzenia.size(); i++) {
			if(wydarzenia.get(i).equals(w)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean equalsArrayListKategorie(Kategorie k) throws SQLException {
		for (int i = 0; i < kategorie.size(); i++) {
			if(kategorie.get(i).equals(k)) {
				return true;
			}
		}
		return false;
	}
	
	public void addKontakt(String imie, String nazwisko, int numer, String email) throws SQLException {
		
		if(equalsArrayListKontakty(new Kontakt(imie, nazwisko, numer, email))==false) {
			Kontakt kon = new Kontakt(imie, nazwisko, numer, email);
			db.dodajKontakt(kontakty, kon);
			kontakty.add(kon);
			xml.zapisKontaktowDoXML(kontakty);
		} else {
			System.out.println("Taki kontakt już istnieje");
		}
	}
	
	public void editKontakt(String imie, String nazwisko, int numer, String email, int nr) throws SQLException {
		
		kontakty.get(nr-1).setImie(imie);
		kontakty.get(nr-1).setNazwisko(nazwisko);
		kontakty.get(nr-1).setNr(numer);
		kontakty.get(nr-1).setEmail(email);
		
		db.edytujKontakt(kontakty.get(nr-1));
		xml.zapisKontaktowDoXML(kontakty);
		
		System.out.println("Pomyślnie edytowano kontakt");
	}
	public void editKontaktGUI(String imie, String nazwisko, int numer, String email, int id) throws SQLException {
	    // Znajdź kontakt o zadanym numerze
	    Optional<Kontakt> optionalKontakt = kontakty.stream()
	            .filter(k -> k.getID() == id-1)
	            .findFirst();

	    if (optionalKontakt.isPresent()) {
	        Kontakt kontakt = optionalKontakt.get();
	        kontakt.setImie(imie);
	        kontakt.setNazwisko(nazwisko);
	        kontakt.setNr(numer);
	        kontakt.setEmail(email);

	        db.edytujKontakt(kontakt);
	        xml.zapisKontaktowDoXML(kontakty);

	        System.out.println("Pomyślnie edytowano kontakt");
	    } else {
	        System.out.println("Nie można znaleźć kontaktu do edycji.");
	    }
	}
	
	public void removeKontakt(int nr) throws SQLException {
		
		db.usunAssignKontakt(kontakty.get(nr-1).getID());
		db.usunKontakt(kontakty.get(nr-1).getID());
		for(int i=0;i<wydarzenia.size();i++) {
            for(int j=0;j<kontakty.get(nr-1).getWydarzeniaSize();j++) {
                if(wydarzenia.get(i).equals(kontakty.get(nr-1).getExactWydarzenie(j))) {
                    wydarzenia.get(i).dropEqualKontakt(kontakty.get(nr-1));
                }
            }
        }
		kontakty.remove(nr-1);
		xml.zapisKontaktowDoXML(kontakty);
		
		System.out.println("Pomyślnie usunięto kontakt");
	}
	public void removeKontaktGUI(int id) throws SQLException {
	    // Find the contact by ID
	    Optional<Kontakt> optionalKontakt = kontakty.stream()
	            .filter(k -> k.getID() == id)
	            .findFirst();

	    if (optionalKontakt.isPresent()) {
	        Kontakt kontakt = optionalKontakt.get();

	        // Remove the contact from the database and related entities
	        db.usunAssignKontakt(kontakt.getID());
	        db.usunKontakt(kontakt.getID());

	        // Remove the contact from the list
	        kontakty.remove(kontakt);

	        // Remove the contact from XML
	        xml.zapisKontaktowDoXML(kontakty);

	        System.out.println("Pomyślnie usunięto kontakt");
	    } else {
	        System.err.println("Nie znaleziono kontaktu o id: " + id);
	    }
	}
	
	
	public void addWydarzenie(String nazwa, String miejsce, String data, String godzina) throws SQLException {
		
		if(equalsArrayListWydarzenia(new Wydarzenia(nazwa, miejsce, data, godzina))==false) {
			Wydarzenia wo = new Wydarzenia(nazwa, miejsce, data, godzina);
			db.dodajWydarzenie(wydarzenia, wo);
			wydarzenia.add(wo);
			xml.zapisWydarzeniaDoXML(wydarzenia);
			
		} else {
			System.out.println("Takie wydarzenie już istnieje");
		}
	}
	
	public void addWydarzenieZKolorem(String nazwa, String miejsce, String data, String godzina, Color color) throws SQLException {
		
		if(equalsArrayListWydarzenia(new Wydarzenia(nazwa, miejsce, data, godzina, color))==false) {
			Wydarzenia wo = new Wydarzenia(nazwa, miejsce, data, godzina, color);
			db.dodajWydarzenie(wydarzenia, wo);
			wydarzenia.add(wo);
			xml.zapisWydarzeniaDoXML(wydarzenia);
			
		} else {
			System.out.println("Takie wydarzenie już istnieje");
		}
	}
	
	public void addWydarzenieZKoloremZKontaktem(String nazwa, String miejsce, String data, String godzina, Color color, ArrayList<Kontakt> kontakty, int id) throws SQLException {
		
		if(equalsArrayListWydarzenia(new Wydarzenia(nazwa, miejsce, data, godzina, color, id, kontakty))==false) {
			Wydarzenia wo = new Wydarzenia(nazwa, miejsce, data, godzina, color, id ,kontakty);
			db.dodajWydarzenie(wydarzenia, wo);
			wydarzenia.add(wo);
			xml.zapisWydarzeniaDoXML(wydarzenia);
			for(int k = 0;k<kontakty.size();k++)
            {
            	db.przypiszKontaktdoWydarzenia(kontakty.get(k).getID(), wo.getID());
            }
		} else {
			System.out.println("Takie wydarzenie już istnieje");
		}
	}
	
	public void editWydarzenie(String nazwa, String miejsce, String data, String godzina, int nr) throws SQLException {
		
		wydarzenia.get(nr-1).setNazwa(nazwa);
		wydarzenia.get(nr-1).setMiejsce(miejsce);
		wydarzenia.get(nr-1).setData(data);
		wydarzenia.get(nr-1).setGodzina(godzina);
		
		db.edytujWydarzenie(wydarzenia.get(nr-1));
		xml.zapisWydarzeniaDoXML(wydarzenia);
		
		System.out.println("Pomyślnie edytowano wydarzenie");
	}
	
	public void editWydarzenieZKolorem(String nazwa, String miejsce, String data, String godzina, Color color, int id) throws SQLException {
	    // Znajdź wydarzenie o zadanym ID
	    Optional<Wydarzenia> optionalWydarzenie = wydarzenia.stream()
	            .filter(w -> w.getID() == id)
	            .findFirst();

	    if (optionalWydarzenie.isPresent()) {
	        Wydarzenia wydarzenie = optionalWydarzenie.get();
	        wydarzenie.setNazwa(nazwa);
	        wydarzenie.setMiejsce(miejsce);
	        wydarzenie.setData(data);
	        wydarzenie.setGodzina(godzina);
	        wydarzenie.setColor(color);

	        db.edytujWydarzenie(wydarzenie);
	        xml.zapisWydarzeniaDoXML(wydarzenia);

	        System.out.println("Pomyślnie edytowano wydarzenie");
	    } else {
	        System.out.println("Nie można znaleźć wydarzenia do edycji.");
	    }
	}
	
	public void removeWydarzenie(int nr) throws SQLException {
		
		db.usunAssignWydarzenie(wydarzenia.get(nr-1).getID());
		db.usunWydarzenie(wydarzenia.get(nr-1).getID());
		for(int i=0;i<kontakty.size();i++) {
            for(int j=0;j<wydarzenia.get(nr-1).getKontaktySize();j++) {
                if(kontakty.get(i).equals(wydarzenia.get(nr-1).getExactKontakt(j))) {
                    kontakty.get(i).dropExactWydarzenie(wydarzenia.get(nr-1));
                }
            }
        }
		wydarzenia.remove(nr-1);
		xml.zapisWydarzeniaDoXML(wydarzenia);
		
		System.out.println("Pomyślnie edytowano wydarzenie");
	}
	public void removeWydarzenieGUI(int id) throws SQLException {
	    Optional<Wydarzenia> optionalWydarzenie = wydarzenia.stream()
	            .filter(w -> w.getID() == id)
	            .findFirst();

	    if (optionalWydarzenie.isPresent()) {
	        Wydarzenia wydarzenie = optionalWydarzenie.get();
	        db.usunAssignWydarzenie(wydarzenie.getID());
	        db.usunWydarzenie(wydarzenie.getID());
	        for (int i = 0; i < kontakty.size(); i++) {
	            for (int j = 0; j < wydarzenie.getKontaktySize(); j++) {
	                if (kontakty.get(i).equals(wydarzenie.getExactKontakt(j))) {
	                    kontakty.get(i).dropExactWydarzenie(wydarzenie);
	                }
	            }
	        }
	        wydarzenia.remove(wydarzenie);
	        xml.zapisWydarzeniaDoXML(wydarzenia);

	        System.out.println("Pomyślnie edytowano wydarzenie");
	    } else {
	        System.err.println("Nie znaleziono wydarzenia o id: " + id);
	    }
	}
	
	public int getDayOfMonthW(int i) {
	    Wydarzenia wydarzenie = wydarzenia.get(i);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	    LocalDate wydarzenieDate = LocalDate.parse(wydarzenie.getData(), formatter);
        return wydarzenieDate.getDayOfMonth();
	}
	
	public int getMonthW(int i) {
	    Wydarzenia wydarzenie = wydarzenia.get(i);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	    LocalDate wydarzenieDate = LocalDate.parse(wydarzenie.getData(), formatter);
        return wydarzenieDate.getMonth().getValue();
	}
	
	public int getYearW(int i) {
	    Wydarzenia wydarzenie = wydarzenia.get(i);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	    LocalDate wydarzenieDate = LocalDate.parse(wydarzenie.getData(), formatter);
        return wydarzenieDate.getYear();
	}
	
	public ArrayList<String> getWydarzeniaDanegoDnia(LocalDate date) {
	    ArrayList<String> wydarzeniaDanegoDnia = new ArrayList<>();

	    for (int i = 0; i < wydarzenia.size(); i++) {
	        Wydarzenia wydarzenie = wydarzenia.get(i);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	        LocalDate wydarzenieDate = LocalDate.parse(wydarzenie.getData(), formatter);

	        if (wydarzenieDate.equals(date)) {
	            String eventInfo = String.format(
	                "Nazwa: " + wydarzenie.getNazwa() + "\nMiejsce: " + wydarzenie.getMiejsce() + "\nData: " + wydarzenie.getData() + "\nGodzina: " + wydarzenie.getGodzina() + "\n"
	            );

	            wydarzeniaDanegoDnia.add(eventInfo);
	        }
	    }

	    return wydarzeniaDanegoDnia;
	}
	
	public Color getKolorWydarzenia(String nazwaWydarzenia) {
	    for (int i = 0; i < wydarzenia.size(); i++) {
	        Wydarzenia wydarzenie = wydarzenia.get(i);
	        if (wydarzenie.getNazwa().equals(nazwaWydarzenia)) {
	            return wydarzenie.getColor();
	        }
	    }
	    return null;
	}
	
	public void addKategoria(String nazwa) throws SQLException {
		
		if(equalsArrayListKategorie(new Kategorie(nazwa))==false) {
			Kategorie ka = new Kategorie(nazwa);
			db.dodajKategorie(kategorie, ka);
			kategorie.add(ka);
		} else {
			System.out.println("Taka kategoria już istnieje");
		}
	}
	
	public void editKategoria(String nazwa, int nr) throws SQLException {
		
		kategorie.get(nr-1).setNazwa(nazwa);
		
		db.edytujKategorie(kategorie.get(nr-1));
		
		System.out.println("Pomyślnie edytowano wydarzenie");
	}
	
	public void removeKategoria(int nr) throws SQLException {
		
		db.usunAssignKategorie(kategorie.get(nr-1).getID());
		db.usunKategorie(kategorie.get(nr-1).getID());
		for(int i=0;i<wydarzenia.size();i++) {
            for(int j=0;j<kategorie.get(nr-1).getWydarzenia().size();j++) {
                if(wydarzenia.get(i).equals(kategorie.get(nr-1).getExactWydarzenie(j))) {
                    wydarzenia.get(i).dropKategoria(kategorie.get(nr-1));
                }
            }
        }
		kategorie.remove(nr-1);
		
		System.out.println("Pomyślnie edytowano wydarzenie");
	}
	
	public void assignKontaktToWydarzenia(int nr1, int nr2) throws SQLException {
		
		if(kontakty.get(nr1-1).equalsWydarzenia(wydarzenia.get(nr2-1))==false) {
			kontakty.get(nr1-1).setWydarzenie(wydarzenia.get(nr2-1));
			wydarzenia.get(nr2-1).setKontakt(kontakty.get(nr1-1));
			db.przypiszKontaktdoWydarzenia(kontakty.get(nr1-1).getID(), wydarzenia.get(nr2-1).getID());
			xml.zapisKontaktowDoXML(kontakty);
			xml.zapisWydarzeniaDoXML(wydarzenia);
		} else {
			System.out.println("Ten kontakt został już dodany do tego wydarzenia");
		}
	}
	
	public void assignWydarzenieToKategorie(int nr1, int nr2) throws SQLException {
		if(wydarzenia.get(nr1-1).equalsKategorie(kategorie.get(nr2-1))==false) {
			wydarzenia.get(nr1-1).setKategoria(kategorie.get(nr2-1));
			kategorie.get(nr2-1).setWydarzenie(wydarzenia.get(nr1-1));
			db.przypiszWydarzeniedoKategorii(wydarzenia.get(nr1-1).getID(), kategorie.get(nr2-1).getID());
			xml.zapisKategoriiDoXML(kategorie);
			xml.zapisWydarzeniaDoXML(wydarzenia);
			
		} else {
			System.out.println("Ten kontakt został już dodany do tego wydarzenia");
		}
	}
	
	public void sortujKontakty(int wybor) throws SQLException {
		switch(wybor)
		{
			case 1:
				Collections.sort(kontakty);
				System.out.println("Sortowanie..");
				System.out.println(wyswietlKontakty());
				break;
			case 2:
				Collections.sort(kontakty,new KontaktNazwiskoComparator());
				System.out.println("Sortowanie..");
				System.out.println(wyswietlKontakty());
				break;
		}
	}
	
	public void sortujWydarzenia(int wybor) throws SQLException {
		switch(wybor)
		{
			case 1:
				Collections.sort(wydarzenia);
				System.out.println("Sortowanie..");
				System.out.println(wyswietlWydarzenia());
				break;
			case 2:
				Collections.sort(wydarzenia,new WydarzenieMiejsceComparator());
				System.out.println("Sortowanie..");
				System.out.println(wyswietlWydarzenia());
				break;
		}
	}
}

