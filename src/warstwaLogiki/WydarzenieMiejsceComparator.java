package warstwaLogiki;

import java.util.Comparator;

import warstwaDanych.Wydarzenia;

public class WydarzenieMiejsceComparator implements Comparator<Wydarzenia> {
	public int compare(Wydarzenia w1, Wydarzenia w2) {
		int wynik = w1.getMiejsce().compareTo(w2.getMiejsce());
		return wynik;
	}
}
