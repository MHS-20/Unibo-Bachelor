import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Figliconsumatore implements Runnable {

	private BufferedReader in;
	private String filterString;
	private String filename;

	public Figliconsumatore(BufferedReader in, String filterString, String filename) {
		this.in = in;
		this.filterString = filterString;
		this.filename = filename;
	}

	public void run() {
		char read;
		int found, c;

		try {
			// creo il file di appoggio
			File childtemp = new File("childtemp.txt");
			File inputfile = new File(filename);

			childtemp.createNewFile();
			FileWriter fw = new FileWriter(childtemp);

			found = 0;
			while ((c = in.read()) > 0) {
				read = (char) c;
				for (int i = 0; i < filterString.length(); i++) {
					if (read == filterString.charAt(i)) {
						found = 1;
					}
				}
				if (found == 0) {
					System.out.print(read);
					fw.append(read);
				}
				found = 0;
			} // fine while

			Files.copy(childtemp.toPath(), inputfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			childtemp.delete();
			fw.flush();
			fw.close();

		} catch (IOException | NullPointerException ex) {
			ex.printStackTrace();
			System.out.println("Errore di input");
			System.exit(2);
		}

	}
}