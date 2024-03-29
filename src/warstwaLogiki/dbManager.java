package warstwaLogiki;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	
	public void edytujKontakt(Kontakt k) throws SQLException{
		int id = k.getID();
		
		String sql = "UPDATE kontakt SET imie = '"+k.getImie()+"', nazwisko = '"+k.getNazwisko()+"', nr_tel = '"+k.getNr()+"', email = '"+k.getEmail()+"' WHERE ID_kontaktu = "+id+";\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunKontakt(Kontakt k) throws SQLException{
		int id = k.getID();
		
		String sql = "DELETE FROM kontakt WHERE ID_kontaktu = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunAssignKontakt(Kontakt k) throws SQLException{
		int id = k.getID();
		
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
				int id = result.getInt("ID_wydarzenia");
				Wydarzenia wo = new Wydarzenia(nazwa, miejsce, data, godzina);
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
		
		String sql = "INSERT INTO wydarzenia (ID_wydarzenia, nazwa, miejsce, data, godzina) VALUES ('"+id+"', '"+w.getNazwa()+"', '"+w.getMiejsce()+"', '"+w.getData()+"', '"+w.getGodzina()+"');\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void edytujWydarzenie(Wydarzenia w) throws SQLException{
		int id = w.getID();
		String sql = "UPDATE wydarzenia SET nazwa = '"+w.getNazwa()+"', miejsce = '"+w.getMiejsce()+"', data = '"+w.getData()+"', godzina = '"+w.getGodzina()+"' WHERE ID_wydarzenia = "+id+";\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunWydarzenie(Wydarzenia w) throws SQLException{
		int id = w.getID();
		String sql = "DELETE FROM wydarzenia WHERE ID_wydarzenia = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void usunAssignWydarzenie(Wydarzenia w) throws SQLException{
		int id = w.getID();
		String sql = "DELETE FROM assign WHERE ID_wydarzenia = '"+id+"';\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void przypiszKontaktdoWydarzenia(int k, int w) throws SQLException{
		String sql = "INSERT INTO assign (ID_kontaktu, ID_wydarzenia) VALUES ('"+k+"', '"+w+"');\n";
		licznikZapytan++;
		bufor.zapisZapytan(sql);
	}
	
	public void polaczAssign(ArrayList<Kontakt> k, ArrayList<Wydarzenia> w) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			String sql = "SELECT * FROM assign JOIN kontakt ON assign.ID_kontaktu = kontakt.ID_kontaktu JOIN wydarzenia ON assign.ID_wydarzenia = wydarzenia.ID_wydarzenia";
			
			ResultSet result = stat.executeQuery(sql);
			while(result.next()) {

				k.get(result.getInt(2)).setWydarzenie(w.get(result.getInt(3)));
				w.get(result.getInt(3)).setKontakt(k.get(result.getInt(2)));
			}
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się wczytać kontaktów z bazy. Spróbuj ponownie");
		}
	}
}
