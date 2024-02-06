package warstwaDanych;

import java.util.ArrayList;
import java.util.Objects;

import javafx.scene.paint.Color;

public class Wydarzenia implements Comparable<Wydarzenia> {
	
	private String nazwa;
	private String miejsce;
	private String data;
	private String godzina;
	private Color color;
	private ArrayList<Kontakt> kontakty;
	private Kategorie kategoria;
	private int id;
	

	public Wydarzenia(String nazwa, String miejsce, String data, String godzina) {
		this.nazwa = nazwa;
		this.miejsce = miejsce;
		this.data = data;
		this.godzina = godzina;
		kontakty = new ArrayList<Kontakt>();
	}
	public Wydarzenia(String nazwa, String miejsce, String data, String godzina, Color color) {
		this.nazwa = nazwa;
		this.miejsce = miejsce;
		this.data = data;
		this.godzina = godzina;
		this.color = color;
		kontakty = new ArrayList<Kontakt>();
	}
	public Wydarzenia(String nazwa, String miejsce, String data, String godzina, Color color, int id) {
		this.nazwa = nazwa;
		this.miejsce = miejsce;
		this.data = data;
		this.godzina = godzina;
		this.color = color;
		this.id = id;
		kontakty = new ArrayList<Kontakt>();
	}
	public Wydarzenia(String nazwa, String miejsce, String data, String godzina, Color color, int id, ArrayList<Kontakt> kontakty) {
		this.nazwa = nazwa;
		this.miejsce = miejsce;
		this.data = data;
		this.godzina = godzina;
		this.color = color;
		this.id = id;
		this.kontakty = kontakty;
	}

	// Settery
	public void setNazwa(String n) {
		nazwa = n;
	}

	public void setMiejsce(String m) {
		miejsce = m;
	}
	
	public void setData(String d) {
		data = d;
	}
	
	public void setGodzina(String g) {
		godzina = g;
	}
	
	public void setID(int i)
	{
		id = i;
	}
	
	public void setColor(Color c)
	{
		color = c;
	}
	
	public void setKontakt(Kontakt k)
	{
		this.kontakty.add(k);
	}
	public void setKategoria(Kategorie k)
	{
		kategoria = k;
	}

	// Gettery
	public String getNazwa() {
		return nazwa;
	}

	public String getMiejsce() {
		return miejsce;
	}
	
	public String getData() {
		return data;
	}
	
	public String getGodzina() {
		return godzina;
	}
	
	public int getID() {
		return id;
	}
	
	public Color getColor() {
		return color;
	}
	
	public ArrayList<Kontakt> getArrayKontakt() {
		return this.kontakty;
	}
	public Kategorie getKategoria() {
		return this.kategoria;
	}
	public String getKategoriaNazwa() {
		if(kategoria == null) {
			return "brak kategorii";
		}
		return kategoria.getNazwa();
	}
	
	public Kontakt getExactKontakt(int i)
	{
		return kontakty.get(i);
	}
	public void dropKategoria(Kategorie k)
	{
		kategoria = null;
	}
	
	public void dropEqualKontakt(Kontakt k)
	{
		for(int i=0;i<getKontaktySize(); i++) {
			if(this.kontakty.get(i).equals(k)) {
				kontakty.remove(i);
			}
		} 
	}
	
	public String getKontakty()
	{
		String tekst = "";
		if(kontakty.isEmpty())
		{
			return "brak kontaktów";
		}
		else
		{
			tekst = "\nPrzypisane kontakty: \n";
		for (int i = 0; i < kontakty.size(); i++) {
			tekst += Integer.toString(i+1)+ ". " + kontakty.get(i)+" ";
			tekst += "\n";
		} return tekst;
		}
	}
	
	public String getKontaktyImieNazwisko()
	{
		String tekst = "";
		if(kontakty.isEmpty())
		{
			return "brak kontaktów";
		}
		else
		{
		for (int i = 0; i < kontakty.size(); i++) {
			tekst += kontakty.get(i).toStringImieNazwisko();
			tekst += "\n";
		} return tekst;
		}
	}
	
	public int getKontaktySize()
	{
		return kontakty.size();
	}
	
	public String toString() {

		return nazwa + " " + miejsce + " " + data + " " + godzina + " " + id +" "+ getKontakty();
	}
	
	public String toStringNazwa() {

		return nazwa + " " + miejsce + " " + data + " " + godzina;
	}
	
	public String toStringCUI() {

		return nazwa + " " + miejsce + " " + data + " " + godzina + " " + id +" "+ getKontakty();
	}

	public int compareTo(Wydarzenia w) {
		int wynik = this.nazwa.compareTo(w.nazwa);
		if(wynik == 0) {
			wynik = this.miejsce.compareTo(w.miejsce); 
			return wynik;
		}
		return wynik;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wydarzenia other = (Wydarzenia) obj;
		return Objects.equals(data, other.data) && Objects.equals(godzina, other.godzina)
				&& Objects.equals(miejsce, other.miejsce) && Objects.equals(nazwa, other.nazwa);
	} 
	
	public boolean equalsKontakty(Object o) {
		Kontakt k = (Kontakt) o;
		for(int i=0;i<getKontaktySize(); i++) {
			if(this.kontakty.get(i).equals(k)) {
				return true;
			}
		} return false;
	} 
	public boolean equalsKategorie(Object o) {
		Kategorie k = (Kategorie) o;
		if(this.kategoria.equals(k)) {
				return true;
		}
		 return false;
	}
}
