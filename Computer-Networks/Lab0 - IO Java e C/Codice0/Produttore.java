import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

// Produttore, e' un filtro
public class Produttore {
	public static void main(String[] args) {		
		BufferedReader in = null;
		
		if (args.length != 1){
			System.out.println("Utilizzo: produttore <inputFilename>");
			System.exit(0);
		}
		
        in = new BufferedReader(new InputStreamReader(System.in));
		FileWriter fout;
		String inputl = null;
		
		try {
			fout = new FileWriter(args[0]);
			while((inputl = in.readLine()) != null) {
				fout.write(inputl, 0, inputl.length());
				System.out.println("Inserisci la nuova riga");
			}
			fout.close();	
		} 
		catch (NumberFormatException nfe) { 
			nfe.printStackTrace(); 
			System.exit(1); 
		}
	    catch (IOException e) { 
			e.printStackTrace();
			System.exit(2); 
	}
}}