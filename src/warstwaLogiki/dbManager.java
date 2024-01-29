package warstwaLogiki;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class dbManager {
	
	public Connection polacz() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/organizer", "root", "");
			return con;
		} catch(Exception e)	{
		System.err.println("Brak połączenia z bazą danych");
		return null;
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
		try {
			Statement stat = polacz().createStatement();
			int id = 0;
			if(ko.isEmpty()) {
				k.setID(id);
			} else {
				id = ko.get((ko.size()-1)).getID()+1;
				k.setID(id);
			}
			String sql = "INSERT INTO kontakt (ID_kontaktu, imie, nazwisko, nr_tel, email) VALUES ('"+id+"', '"+k.getImie()+"', '"+k.getNazwisko()+"', "+k.getNr()+", '"+k.getEmail()+"')";
			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się dodać kontaktu do bazy. Spróbuj ponownie");
		}
	}
	
	public void edytujKontakt(Kontakt k) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			int id = k.getID();
			String sql = "UPDATE kontakt SET imie = '"+k.getImie()+"', nazwisko = '"+k.getNazwisko()+"', nr_tel = '"+k.getNr()+"', email = '"+k.getEmail()+"' WHERE ID_kontaktu = "+id+" ";
			
			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się edytować kontaktu z bazy. Spróbuj ponownie");
		}
	}
	
	public void usunKontakt(Kontakt k) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			int id = k.getID();
			
			String sql = "DELETE FROM kontakt WHERE ID_kontaktu = '"+id+"' ";
			
			stat.execute(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się usunąć kontaktu z bazy. Spróbuj ponownie");
		}
	}
	
	public void usunAssignKontakt(Kontakt k) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			int id = k.getID();
			
			String sql = "DELETE FROM assign WHERE ID_kontaktu = '"+id+"'; ";
			
			stat.execute(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się usunąć kontaktu z bazy. Spróbuj ponownie");
		}
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
		try {
			Statement stat = polacz().createStatement();
			
			int id = 0;
			if(wy.isEmpty()) {
				w.setID(id);
			} else {
				id = wy.get((wy.size()-1)).getID()+1;
				w.setID(id);
			}
			
			String sql = "INSERT INTO wydarzenia (ID_wydarzenia, nazwa, miejsce, data, godzina) VALUES ('"+id+"', '"+w.getNazwa()+"', '"+w.getMiejsce()+"', '"+w.getData()+"', '"+w.getGodzina()+"')";
			
			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się dodać wydarzenia do bazy. Spróbuj ponownie");
		}
	}
	
	public void edytujWydarzenie(Wydarzenia w) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			int id = w.getID();
			String sql = "UPDATE wydarzenia SET nazwa = '"+w.getNazwa()+"', miejsce = '"+w.getMiejsce()+"', data = '"+w.getData()+"', godzina = '"+w.getGodzina()+"' WHERE ID_wydarzenia = "+id;
			
			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się edytować kontaktu z bazy. Spróbuj ponownie");
		}
	}
	
	public void usunWydarzenie(Wydarzenia w) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			int id = w.getID();
			String sql = "DELETE FROM wydarzenia WHERE ID_wydarzenia = '"+id+"' ";

			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się usunąć kontaktu z bazy. Spróbuj ponownie");
		}
	}
	
	public void usunAssignWydarzenie(Wydarzenia w) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			int id = w.getID();
			String sql = "DELETE FROM assign WHERE ID_wydarzenia = '"+id+"' ";
			
			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się usunąć kontaktu z bazy. Spróbuj ponownie");
		}
	}
	
	public void przypiszKontaktdoWydarzenia(int k, int w) throws SQLException{
		try {
			Statement stat = polacz().createStatement();
			
			String sql = "INSERT INTO assign (ID_kontaktu, ID_wydarzenia) VALUES ('"+k+"', '"+w+"')";
			
			stat.executeUpdate(sql);
			stat.close();
			polacz().close();
		} catch(SQLException e) {
			System.err.println("Nie udało się przypisać kontaktu do wydarzenia w bazie. Spróbuj ponownie");
		}
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
}
