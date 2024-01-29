package warstwaDanych;

public class Kontakt implements Comparable<Kontakt> {

	private String imie;
	private String nazwisko;
	private int nr_tel;
	private String email;
	private Wydarzenia wydarzenie;
	private int id;
	
	public Kontakt(String imie, String nazwisko, int nr_tel, String email) {
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.nr_tel = nr_tel;
		this.email = email;
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
		this.wydarzenie = w;
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
	
	public Wydarzenia getWydarzenie()
	{
		return this.wydarzenie;
	}
	
	public void dropWydarzenie()
	{
		this.wydarzenie = null;
	}
	
	public String getStringWydarzenie()
	{
		if(wydarzenie==null)
		{
			return "Nie przypisano wydarzenia";
		}
		else
		{
			return wydarzenie.getNazwa()+" "+ wydarzenie.getData();
		}
	}

	public String toString() {
		return imie + " " + nazwisko + " " + Integer.toString(nr_tel) + " " + email;
	}
	
	public String toStringZWydarzeniem() {
		return toString()+" "+getStringWydarzenie();
	}
	
	public boolean equals(Object other) {
		if(other instanceof Kontakt) {
			Kontakt k = (Kontakt) other;
			if(this.imie.equals(k.imie)) {
				if(this.nazwisko.equals(k.nazwisko)) {
					if(this.nr_tel == k.nr_tel) {
						if(this.email.equals(k.email)) {
							if(this.id == k.id) {
								return true;
							}
						}
					}
				}
			}
		}return false;
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

