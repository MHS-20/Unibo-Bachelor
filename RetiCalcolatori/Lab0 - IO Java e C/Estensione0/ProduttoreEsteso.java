import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProduttoreEsteso {
	public static void main(String[] args) {
		String inputl;
		int j = 0; 
		FileWriter[] fout = new FileWriter[args.length];
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		if (args.length == 0) {
			System.out.println("Nessun arogmento in ingresso");
			System.exit(0);
		}

		System.out.println("Inserire il contenuto del file fino a EOF");

		try {
			for (int i = 0; i < args.length; i++) {
				fout[i] = new FileWriter(args[i]);
			}

			//leggo tutti gl input
			while ((inputl = in.readLine()) != null) { // till EOF (CTRL+D)
				j = Integer.parseInt(inputl.substring(0, 1)); //leggo numero del file su cui devo scrivere
				System.out.println(j);
				fout[j - 1].write(inputl + "\n", 0, inputl.length() + 1); //j -1 perché parto a contare i file da 1 e non da 0
			}

			System.out.println("Raggiunto EOF");

			//chiudo tutto
			for (int i = 0; i < args.length; i++)
				fout[i].close();

		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}
}