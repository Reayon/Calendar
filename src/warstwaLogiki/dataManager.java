package warstwaLogiki;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class dataManager {
	
	private XML xml = new XML();
	private dbManager db = new dbManager();
	private ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
	private ArrayList<Wydarzenia> wydarzenia = new ArrayList<Wydarzenia>();
	
	public dataManager() {
	    this.kontakty = new ArrayList<>();
	    this.wydarzenia = new ArrayList<>();
	}
	
	public dataManager(ArrayList<Kontakt> kontakty, ArrayList<Wydarzenia> wydarzenia) {
		this.kontakty = kontakty;
		this.wydarzenia = wydarzenia;
	}
	
	public ArrayList<Kontakt> pobierzListeKontaktow() {
		return kontakty;
    }
	public ArrayList<Wydarzenia> pobierzListeWydarzen() {
		return wydarzenia;
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
			} else {
				db.odczytajKontakty(kontakty);
				db.odczytajWydarzenia(wydarzenia);
				db.polaczAssign(kontakty, wydarzenia);
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
	
	public void removeKontakt(int nr) throws SQLException {
		
		db.usunAssignKontakt(kontakty.get(nr-1));
		db.usunKontakt(kontakty.get(nr-1));
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
	
	public void editWydarzenie(String nazwa, String miejsce, String data, String godzina, int nr) throws SQLException {
		
		wydarzenia.get(nr-1).setNazwa(nazwa);
		wydarzenia.get(nr-1).setMiejsce(miejsce);
		wydarzenia.get(nr-1).setData(data);
		wydarzenia.get(nr-1).setGodzina(godzina);
		
		db.edytujWydarzenie(wydarzenia.get(nr-1));
		xml.zapisWydarzeniaDoXML(wydarzenia);
		
		System.out.println("Pomyślnie edytowano wydarzenie");
	}
	
	public void removeWydarzenie(int nr) throws SQLException {
		
		db.usunAssignWydarzenie(wydarzenia.get(nr-1));
		db.usunWydarzenie(wydarzenia.get(nr-1));
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
		
		
		/*kontakty.get(nr1-1).setWydarzenie(wydarzenia.get(nr2-1));

		if(wydarzenia.get(nr2-1).equalsKontakty(kontakty.get(nr1-1))==false) {
		wydarzenia.get(nr2-1).setKontakt(kontakty.get(nr1-1));
		db.przypiszKontaktdoWydarzenia(kontakty.get(nr1-1).getID(), wydarzenia.get(nr2-1).getID());
		for(int i=0; i<wydarzenia.size(); i++) {
			if(wydarzenia.get(i).equalsKontakty(kontakty.get(nr1-1))==true && i != nr2-1) {
				if(db.polacz()!=null) {
					db.usunAssignKontakt(kontakty.get(nr1-1));
					wydarzenia.get(i).dropEqualKontakt(kontakty.get(nr1-1));
					db.przypiszKontaktdoWydarzenia(kontakty.get(nr1-1).getID(), wydarzenia.get(nr2-1).getID());
					xml.zapisKontaktowDoXML(kontakty);
					xml.zapisWydarzeniaDoXML(wydarzenia);
				} else {
					wydarzenia.get(i).dropEqualKontakt(kontakty.get(nr1-1));
					xml.zapisKontaktowDoXML(kontakty);
					xml.zapisWydarzeniaDoXML(wydarzenia);
				}
			}
		}
		xml.zapisKontaktowDoXML(kontakty);
		xml.zapisWydarzeniaDoXML(wydarzenia);
		} else {
			System.out.println("Ten kontakt został już dodany do tego wydarzenia");
			for(int i=0; i<wydarzenia.size(); i++) {
				if(wydarzenia.get(i).equalsKontakty(kontakty.get(nr1-1))==true && i != nr2-1) {
					wydarzenia.get(i).dropEqualKontakt(kontakty.get(nr1-1));
					xml.zapisKontaktowDoXML(kontakty);
					xml.zapisWydarzeniaDoXML(wydarzenia);
				}
			}
			xml.zapisKontaktowDoXML(kontakty);
	xml.zapisWydarzeniaDoXML(wydarzenia);
		}*/
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

