package warstwaDanych;

import java.util.ArrayList;

public class Kontakt implements Comparable<Kontakt> {

	private String imie;
	private String nazwisko;
	private int nr_tel;
	private String email;
	private ArrayList<Wydarzenia> wydarzenia;
	private int id;
	
	public Kontakt(String imie, String nazwisko, int nr_tel, String email) {
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.nr_tel = nr_tel;
		this.email = email;
		wydarzenia = new ArrayList<Wydarzenia>();
	}

	// Settery
	public void setImie(String i) {
		imie = i;
	}

	public void setNazwisko(String n) {
		nazwisko = n;
	}

	public void setNr(int n) {
		nr_tel = n;
	}
	
	public void setEmail(String e) {
		email = e;
	}
	
	public void setID(int i) {
		id = i;
	}
	
	public void setWydarzenie(Wydarzenia w) {
		this.wydarzenia.add(w);
	}

	// Gettery
	public String getImie() {
		return imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public int getNr() {
		return nr_tel;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getID() {
		return id;
	}
	
	public Wydarzenia getExactWydarzenie(int i) {
		return wydarzenia.get(i);
	}
	
	public int getWydarzeniaSize() {
		return wydarzenia.size();
	}
	
	public void dropExactWydarzenie(Wydarzenia w) {
		for(int i=0;i<getWydarzeniaSize(); i++) {
			if(this.wydarzenia.get(i).equals(w)) {
				wydarzenia.remove(i);
			}
		} 
	}
	
	public String getStringWydarzenia()
	{
		String tekst = "";
		if(wydarzenia.isEmpty())
		{
			return "brak przypisanych wydarzeń";
		}
		else
		{
			tekst = "\nPrzypisane wydarzenia: \n";
		for (int i = 0; i < getWydarzeniaSize(); i++) {
			tekst += Integer.toString(i+1)+ ". " + wydarzenia.get(i)+" ";
			tekst += "\n";
		} return tekst;
		}
	}

	public String toString() {
		return imie + " " + nazwisko + " " + Integer.toString(nr_tel) + " " + email + " id: " +id;
	}
	
	public String toStringZWydarzeniem() {
		return toString()+" "+getStringWydarzenia();
	}
	
	public boolean equals(Object other) {
		if(other instanceof Kontakt) {
			Kontakt k = (Kontakt) other;
			if(this.imie.equals(k.imie)) {
				if(this.nazwisko.equals(k.nazwisko)) {
					if(this.nr_tel == k.nr_tel) {
						if(this.email.equals(k.email)) {
							return true;
						}
					}
				}
			}
		}return false;
	}
	
	public boolean equalsWydarzenia(Object o) {
		Wydarzenia w = (Wydarzenia) o;
		for(int i=0;i<getWydarzeniaSize(); i++) {
			if(this.wydarzenia.get(i).equals(w)) {
				return true;
			}
		} return false;
	} 

	public int compareTo(Kontakt k) {
		int wynik = this.imie.compareTo(k.imie);
		if(wynik == 0) {
			wynik = this.nazwisko.compareTo(k.nazwisko);
			return wynik;
		}
		return wynik;
	}

}

