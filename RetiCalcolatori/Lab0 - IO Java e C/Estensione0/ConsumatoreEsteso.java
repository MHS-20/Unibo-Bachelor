import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsumatoreEsteso {

	public static void main(String[] args) {
		BufferedReader in[] = new BufferedReader[args.length];
		Thread t[] = new Thread[args.length]; 
		Figliconsumatore child[] = new Figliconsumatore[args.length]; 
		String filterString = args[0];

		int found, c;
		char read;

		if (args.length == 0) {
			System.out.println("Nessun arogmento in ingresso");
			System.exit(0);
		}
			
			try {
				for (int i = 0; i < args.length - 1; i++) { // -1 perché perchè 0 è la stringa filtro
					in[i] = new BufferedReader(new FileReader(args[i + 1])); //+ 1 perchè 0 è la stringa filtro
					System.out.println("File trovato. Filtraggio del file...");

					child[i] = new Figliconsumatore(in[i], filterString, args[i + 1]);
					t[i] = new Thread(child[i]);
					t[i].start();
				}
				
			}catch(FileNotFoundException e){
				System.out.println("File non trovato");
				System.exit(1);
			}
		}
}