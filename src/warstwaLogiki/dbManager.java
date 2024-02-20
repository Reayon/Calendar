package warstwaLogiki;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import warstwaDanych.Kategorie;
import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class dbManager {
	
	public bufor bufor = new bufor();
	public int licznikZapytan;
	
	public Connection polacz() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/organizer?allowMultiQueries=true", "root", "");
			return con;
		} catch(Exception e)	{
		System.err.println("Brak połączenia z bazą danych");
		return null;
		}
	}
	
	public bufor getBufor() {
		return this.bufor;
	}
	
	public boolean czyPolaczone() {
		if(polacz()==null) {
			return false;
		} return true;
	}
	
	public int getLicznikZapytan() {
		return this.licznikZapytan;
	}
	
	public void wyzerujLicznikZapytan() {
		licznikZapytan = 0;
	}
	
	public void wykonajZapytania() throws FileNotFoundException {
		try {
			Statement stat = polacz().createStatement();
			String sql = bufor.odczytZapytan();
			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
			bufor.czyscPlik();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void odczytajKontakty(ArrayList<Kontakt> k) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			String sql = "SELECT * FROM kontakt";
			
			ResultSet result = stat.executeQuery(sql);
			while(result.next()) {
				String imie = result.getString("imie");
				String nazwisko = result.getString("nazwisko");
				int numer = result.getInt("nr_tel");
				String email = result.getString("email");
				int id = result.getInt("ID_kontaktu");
				Kontakt ko = new Kontakt(imie, nazwisko, numer, email);
				ko.setID(id);
				k.add(ko);
				}
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się wczytać kontaktów z bazy. Spróbuj ponownie");
		}
	}
	
	public void dodajKontakt(ArrayList<Kontakt> ko, Kontakt k) throws SQLException{
		int id = 0;
		if(ko.isEmpty()) {
			k.setID(id);
		} else {
			id = ko.get((ko.size()-1)).getID()+1;
			k.setID(id);
		}
		String sql = "INSERT INTO kontakt (ID_kontaktu, imie, nazwisko, nr_tel, email) VALUES ('"+id+"', '"+k.getImie()+"', '"+k.getNazwisko()+"', "+k.getNr()+", '"+k.getEmail()+"');\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void dodajKontaktgui(ArrayList<Kontakt> ko, Kontakt k) throws SQLException{
		Statement stat = polacz().createStatement();
		int id = 0;
		if(ko.isEmpty()) {
			k.setID(id);
		} else {
			id = ko.get((ko.size()-1)).getID()+1;
			k.setID(id);
		}
		String sql = "INSERT INTO kontakt (ID_kontaktu, imie, nazwisko, nr_tel, email) VALUES ('"+id+"', '"+k.getImie()+"', '"+k.getNazwisko()+"', "+k.getNr()+", '"+k.getEmail()+"');\n";
		stat.executeUpdate(sql);
		stat.close();
        polacz().close();
	}
	
	public void edytujKontakt(Kontakt k) throws SQLException{
		int id = k.getID();
		
		String sql = "UPDATE kontakt SET imie = '"+k.getImie()+"', nazwisko = '"+k.getNazwisko()+"', nr_tel = '"+k.getNr()+"', email = '"+k.getEmail()+"' WHERE ID_kontaktu = "+id+";\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunKontakt(int id) throws SQLException{
		String sql = "DELETE FROM kontakt WHERE ID_kontaktu = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunAssignKontakt(int id) throws SQLException{
		String sql = "DELETE FROM assign WHERE ID_kontaktu = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void odczytajWydarzenia(ArrayList<Wydarzenia> w) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			String sql = "SELECT * FROM wydarzenia";
			
			ResultSet result = stat.executeQuery(sql);
			while(result.next()) {
				String nazwa = result.getString("nazwa");
				String miejsce = result.getString("miejsce");
				String data = result.getString("data");
				String godzina = result.getString("godzina");
				String c = result.getString("kolor");
				int id = result.getInt("ID_wydarzenia");
				Wydarzenia wo = new Wydarzenia(nazwa, miejsce, data, godzina, Color.valueOf(c));
				wo.setID(id);
				w.add(wo);
			}
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się wczytać wydarzeń z bazy. Spróbuj ponownie");
		}
	}
	
	public void dodajWydarzenie(ArrayList<Wydarzenia> wy, Wydarzenia w) throws SQLException{
		int id = 0;
		if(wy.isEmpty()) {
			w.setID(id);
		} else {
			id = wy.get((wy.size()-1)).getID()+1;
			w.setID(id);
		}
		
		String sql = "INSERT INTO wydarzenia (ID_wydarzenia, nazwa, miejsce, data, godzina, kolor) VALUES ('"+id+"', '"+w.getNazwa()+"', '"+w.getMiejsce()+"', '"+w.getData()+"', '"+w.getGodzina()+"', '"+w.getColor()+"');\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void dodajWydarzeniegui(ArrayList<Wydarzenia> wy, Wydarzenia w) throws SQLException{
		Statement stat = polacz().createStatement();
		int id = 0;
		if(wy.isEmpty()) {
			w.setID(id);
		} else {
			id = wy.get((wy.size()-1)).getID()+1;
			w.setID(id);
		}
		
		String sql = "INSERT INTO wydarzenia (ID_wydarzenia, nazwa, miejsce, data, godzina, kolor) VALUES ('"+id+"', '"+w.getNazwa()+"', '"+w.getMiejsce()+"', '"+w.getData()+"', '"+w.getGodzina()+"', '"+w.getColor()+"');\n";
		stat.executeUpdate(sql);
		stat.close();
        polacz().close();
	}
	
	public void edytujWydarzenie(Wydarzenia w) throws SQLException{
		int id = w.getID();
		String sql = "UPDATE wydarzenia SET nazwa = '"+w.getNazwa()+"', miejsce = '"+w.getMiejsce()+"', data = '"+w.getData()+"', godzina = '"+w.getGodzina()+"', kolor = '"+w.getColor()+"' WHERE ID_wydarzenia = "+id+";\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunWydarzenie(int id) throws SQLException{
		String sql = "DELETE FROM wydarzenia WHERE ID_wydarzenia = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void odczytajKategorie(ArrayList<Kategorie> kategorie) {
		try {
			Statement stat = polacz().createStatement();
			
			String sql = "SELECT * FROM kategorie";
			
			ResultSet result = stat.executeQuery(sql);
			while(result.next()) {
				String nazwa = result.getString("nazwa");
				int id = result.getInt("ID_kategorii");
				Kategorie k = new Kategorie(nazwa);
				k.setID(id);
				kategorie.add(k);
				}
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się wczytać kontaktów z bazy. Spróbuj ponownie");
		}
	}
	
	public void dodajKategorie(ArrayList<Kategorie> kategorie, Kategorie k) {
		int id = 0;
		if(kategorie.isEmpty()) {
			k.setID(id);
		} else {
			id = kategorie.get((kategorie.size()-1)).getID()+1;
			k.setID(id);
		}
		
		String sql = "INSERT INTO kategorie (ID_kategorii, nazwa) VALUES ('"+id+"', '"+k.getNazwa()+"');\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void edytujKategorie(Kategorie k) {
		int id = k.getID();
		String sql = "UPDATE kategorie SET nazwa = '"+k.getNazwa()+"' WHERE ID_kategorii = "+id+";\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunKategorie(int id) throws SQLException{
		usunAssignKategorie(id);
		String sql = "DELETE FROM kategorie WHERE ID_kategorii = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunAssignWydarzenie(int id) throws SQLException{
		String sql = "DELETE FROM assign WHERE ID_wydarzenia = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunAssignKategorieWydarzenie(int id) throws SQLException{
		String sql = "DELETE FROM assignkategorie WHERE ID_wydarzenia = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunAssignKategorie(int id) throws SQLException{
		String sql = "DELETE FROM assignkategorie WHERE ID_kategorii = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void przypiszKontaktdoWydarzenia(int k, int w) throws SQLException{
		String sql = "INSERT INTO assign (ID_kontaktu, ID_wydarzenia) VALUES ('"+k+"', '"+w+"');\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunKontaktZWydarzenia(int idk, int idw) throws SQLException{
		String sql = "DELETE FROM assign WHERE ID_wydarzenia = '"+idw+"' AND ID_kontaktu = '"+idk+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void przypiszWydarzeniedoKategorii(int w, int k) throws SQLException{
		String sql = "INSERT INTO assignkategorie (ID_wydarzenia, ID_kategorii) VALUES ('"+w+"', '"+k+"');\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	
	public void polaczAssign(ArrayList<Kontakt> k, ArrayList<Wydarzenia> w) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			String sql = "SELECT * FROM assign JOIN kontakt ON assign.ID_kontaktu = kontakt.ID_kontaktu JOIN wydarzenia ON assign.ID_wydarzenia = wydarzenia.ID_wydarzenia";
			
			ResultSet result = stat.executeQuery(sql);
			while(result.next()) {
				int idK = result.getInt("assign.ID_kontaktu");
				int idW = result.getInt("assign.ID_wydarzenia");
				for(int i=0;i<k.size();i++) {
					if(k.get(i).getID()==idK) {
						for(int j=0;j<w.size();j++) {
							if(w.get(j).getID()==idW) {
								k.get(i).setWydarzenie(w.get(j));
								w.get(j).setKontakt(k.get(i));
							}
						}
					}
				}
			}
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się wczytać kontaktów z bazy. Spróbuj ponownie");
		}
	}
	public void polaczAssignKategorie(ArrayList<Wydarzenia> w, ArrayList<Kategorie> k) throws SQLException{
        try {
            Statement stat = polacz().createStatement();

            String sql = "SELECT * FROM assignkategorie JOIN wydarzenia ON assignkategorie.ID_wydarzenia = wydarzenia.ID_wydarzenia JOIN kategorie ON assignkategorie.ID_kategorii = kategorie.ID_kategorii";

            ResultSet result = stat.executeQuery(sql);
            while(result.next()) {
                int idK = result.getInt("assignkategorie.ID_kategorii");
                int idW = result.getInt("assignkategorie.ID_wydarzenia");
                for(int i=0;i<w.size();i++) {
                    if(w.get(i).getID()==idW) {
                        for(int j=0;j<k.size();j++) {
                            if(k.get(j).getID()==idK) {
                                w.get(i).setKategoria(k.get(j));
                                k.get(j).setWydarzenie(w.get(i));
                            }
                        }
                    }
                }
            }
            stat.close();
            polacz().close();
        } catch(SQLException e) {
            System.err.println("Nie udało się wczytać kontaktów z bazy. Spróbuj ponownie");
        }
    }
}
