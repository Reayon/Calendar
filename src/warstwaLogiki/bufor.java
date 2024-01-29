package warstwaLogiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class bufor {
	
	private File plik = new File("./src/zapytanie.txt");
	
	public boolean isEmpty() {
		try (BufferedReader br = new BufferedReader(new FileReader(plik))) {
	        if (br.readLine() == null) {
	              	return true;
	        	}return false;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	}
	
	public void zapisZapytan(String jakiś_napis) {
        String tekstDoZapisania = new String(jakiś_napis);

        try{
            plik.createNewFile();
            FileWriter strumienZapisu = new FileWriter(plik, true);
            strumienZapisu.write(tekstDoZapisania);
            strumienZapisu.write("\n");
            strumienZapisu.close();
        }
        catch (IOException io){
        	System.out.println(io.getMessage());
        }
        catch (Exception se){
        	System.err.println("blad sec");
        }
	}
	
	public void czyscPlik() {
        String tekstDoZapisania = new String();

        try{
            plik.createNewFile();
            FileWriter strumienZapisu = new FileWriter(plik);
            strumienZapisu.write(tekstDoZapisania);
            strumienZapisu.close();
        }
        catch (IOException io){
        	System.out.println(io.getMessage());
        }
        catch (Exception se){
        	System.err.println("blad sec");
        }
	}
	
	public String odczytZapytan() throws FileNotFoundException {
		String txt = "";
		Scanner sc = new Scanner(plik);
		while (sc.hasNextLine())
			txt += sc.nextLine();
		sc.close();
		return txt;
	}
}
