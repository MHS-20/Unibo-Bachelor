package stadiocalcio;
import java.util.Random;

public class Main {

	private final static int MAX_NUM = 20;
	
	public static void main(String[] args) {
		Random r = new Random(System.currentTimeMillis());
		String[] squadre = new String[3]; 
		int NT = r.nextInt(MAX_NUM);
		int capienza = MAX_NUM / 2;
		
		squadre[0] = "sq1";
		squadre[1] = "sq2";
		squadre[2] = "sq3";

		Stadio s = new Stadio(squadre, capienza);
		Tifoso[] Tif = new Tifoso[NT];
		
		for(int i=0; i<NT; i++){
			int sq = r.nextInt(2);
			Tif[i] = new Tifoso(s, squadre[sq], r.nextInt(100)); 
		}
		
		for(int i=0; i<NT; i++)
			Tif[i].start();
		
		for(int i=0; i<NT; i++){
			try{
				Tif[i].join();
			}catch(InterruptedException e){}
		}
		
		s.stampa(); 
}
}