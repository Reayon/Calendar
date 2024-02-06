package warstwaDanych;

import java.util.ArrayList;
import java.util.Objects;

public class Kategorie {
	private int id;
	private String nazwa;
	private ArrayList<Wydarzenia> wydarzenia = new ArrayList<Wydarzenia>();
	
	public Kategorie(String nazwa) {
		this.nazwa = nazwa;
	}
	
	public Kategorie(int id, String nazwa) {
		this.id = id;
		this.nazwa = nazwa;
	}

	public int getID() {
		return id;
	}

	public ArrayList<Wydarzenia> getWydarzenia() {
		return wydarzenia;
	}
	
	public void setWydarzenie(Wydarzenia w) {
		this.wydarzenia.add(w);
	}

	public void setWydarzenia(ArrayList<Wydarzenia> wydarzenia) {
		this.wydarzenia = wydarzenia;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	
	public String toString() {
		return id + " " + nazwa;
	}
	
	public Wydarzenia getExactWydarzenie(int i)
	{
		return wydarzenia.get(i);
	}
	
	public void dropExactWydarzenie(Wydarzenia w) {
		for(int i=0;i<wydarzenia.size(); i++) {
			if(this.wydarzenia.get(i).equals(w)) {
				wydarzenia.remove(i);
			}
		} 
	}
	
	public String getWydarzeniaNazwa()
	{
		String tekst = "";
		if(wydarzenia.isEmpty())
		{
			return "brak wydarzeń";
		}
		else
		{
		for (int i = 0; i < wydarzenia.size(); i++) {
			tekst += wydarzenia.get(i).toStringNazwa();
			tekst += "\n";
		} return tekst;
		}
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kategorie other = (Kategorie) obj;
		return Objects.equals(nazwa, other.nazwa) && Objects.equals(wydarzenia, other.wydarzenia);
	}
	
	public boolean equalsWydarzenia(Object o) {
		Wydarzenia w = (Wydarzenia) o;
		for(int i=0;i<wydarzenia.size(); i++) {
			if(this.wydarzenia.get(i).equals(w)) {
				return true;
			}
		} return false;
	} 
}
