package warstwaLogiki;

import warstwaDanych.Kontakt;

import java.util.Comparator;

public class KontaktNazwiskoComparator implements Comparator<Kontakt> {
	public int compare(Kontakt k1, Kontakt k2) {
		int wynik = k1.getNazwisko().compareTo(k2.getNazwisko());
		return wynik;
	}
	
}
