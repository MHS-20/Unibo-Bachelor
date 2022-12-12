import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Consumatore e' un filtro
public class Consumatore {
	public static void main(String[] args) {
		FileReader r = null;
		char ch; 
		String filterprefix = args[0]; 
		int x;
		
		if (args.length != 2){
			System.out.println("Utilizzo: consumatore <inputFilename>");
			System.exit(0);
		}
			  
		try {
			r = new FileReader(args[1]);
		} catch(FileNotFoundException e){
			System.out.println("File non trovato");
			System.exit(1);
		}
		try {

			while ((x = r.read()) >= 0) { 
				ch = (char) x;
				
				if(filterprefix.indexOf(ch) == -1)
				System.out.print(ch);
			}
			r.close();
		} catch(IOException ex){
			System.out.println("Errore di input");
			System.exit(2);
		}
}}