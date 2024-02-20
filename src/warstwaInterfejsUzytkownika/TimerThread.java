package warstwaInterfejsUzytkownika;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimerThread extends Thread {
	
	LocalTime lt = LocalTime.now();
	private int godzina;
	private int minuta;
	
	@Override
	public void run() {
        
		while(true) {
		LocalTime lt = LocalTime.now();
    	System.out.println(lt.format(DateTimeFormatter.
                ofLocalizedTime(FormatStyle.MEDIUM)));
    	godzina = lt.getHour();
    	minuta = lt.getMinute();
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
    }
	
	public LocalTime getTime() {
		return lt;
	}
	
}
