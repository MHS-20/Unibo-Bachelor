/*
 * Classe usata per definire alcuni metodi utili nella selezione delle linee di
 * un file
 *
 */

// IN RICEZIONE
//st = new StringTokenizer(richiesta);
//nomeFile = st.nextToken();
//numLinea = Integer.parseInt(st.nextToken());
//System.out.println("Richiesta linea " + numLinea + " del file " + nomeFile);

import java.io.*;
import java.net.*;

public class RowSwap extends Thread{

	private DatagramSocket socket = null;
	private DatagramPacket packet = null;
	private byte[] buf = new byte[256];
	private byte[] data = null;

	private String richiesta = null;
	private ByteArrayInputStream biStream = null;
	private DataInputStream diStream = null;
	private ByteArrayOutputStream boStream = null;
	private DataOutputStream doStream = null;

	private PrintWriter pw;
	private BufferedReader bReader;
	private File old, temp;

	private int numl1, numl2;
	private String l1, l2, line;
	private int i, port;
	private String nomefile;

	private InetAddress clientaddr;
	private int clientport;

	public RowSwap(String f, int p) {
		this.nomefile = f;
		this.port = p; 
	}

	public void run() {

		// creo socket e stream out
		try {
			socket = new DatagramSocket(port);
			packet = new DatagramPacket(buf, buf.length);
			System.out.println("Thread: creata la socket: " + socket);
			boStream = new ByteArrayOutputStream();
			doStream = new DataOutputStream(boStream);
		} catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Thread: entro nel ciclo");

		while (true) {
			// estraggo richiesta
			try {
				packet.setData(buf);
				socket.receive(packet);
				biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
				diStream = new DataInputStream(biStream);
				System.out.println("Thread: ricevuto il messaggio dal client");
				
				numl1 = diStream.readInt(); // numero riga 1
				numl2 = diStream.readInt(); // numero riga 2
				System.out.println(numl1);

				l1 = getLine(nomefile, numl1); // estraggo linea 1
				l2 = getLine(nomefile, numl2); // estraggo linea 2
				System.out.println(l2);

				clientaddr = packet.getAddress();
				clientport = packet.getPort();
				System.out.println(clientaddr);
			} catch (Exception e) {
				System.err.println("Problemi nella lettura della richiesta: " + nomefile);
				e.printStackTrace();
				failure();
				continue;
			}

			// scrivo sul nuovo file
			try {
				System.out.println("Thread: modifico il file");
				old = new File(nomefile);
				temp = new File("temp.txt");
				bReader = new BufferedReader(new FileReader(nomefile));
				pw = new PrintWriter(new File("temp.txt"));
			} catch (FileNotFoundException e) {
				System.err.println("Problemi nell'apertura del file " + nomefile);
				e.printStackTrace();
				failure();
				continue;
			}

			try {
				i = 1;
				while ((line = bReader.readLine()) != null) {
					if (i == numl1)
						pw.write(l2);
					else if (i == numl2)
						pw.write(l1);
					else
						pw.write(line);
				}
			} catch (IOException e) {
				System.err.println("Problemi nell'apertura del file " + nomefile);
				e.printStackTrace();
				continue;
			}

			pw.flush();
			pw.close();
			old.delete();
			temp.renameTo(old);

			// invio della risposta
			try {
				System.out.println("Thread: invio la risposta al client");
				boStream = new ByteArrayOutputStream();
				doStream = new DataOutputStream(boStream);
				//packet = new DatagramPacket(buf, buf.length, clientaddr, clientport);
				doStream.writeInt(1); // 1 = success
				data = boStream.toByteArray();
				packet.setData(data, 0, data.length);
				socket.send(packet);
			} catch (IOException e) {
				System.err.println("Problemi nell'invio della risposta: " + e.getMessage());
				e.printStackTrace();
				failure();
				continue;
				// il server continua a fornire il servizio ricominciando dall'inizio
				// del ciclo
			}
		} // fine while
	}

	/*
	 * Metodo per comunicare al cliente il fallimento
	 */
	public void failure() {
		try {
			doStream.writeInt(-1); // - 1 = failure
			data = boStream.toByteArray();
			packet.setData(data, 0, data.length);
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Problemi nella scrittura del risultato");
			e.printStackTrace();
		}
	}

	/**
	 * metodo per recuperare una certa linea di un certo file
	 * 
	 * @param nomeFile
	 * @param numLinea
	 * @return linea letta o "Linea non trovata..."
	 * 
	 */
	static String getLine(String nomeFile, int numLinea) {
		String linea = null;
		BufferedReader in = null;

		if (numLinea <= 0)
			return linea = "Linea non trovata: numero linea maggiore di 0.";
		// associazione di uno stream di input al file da cui estrarre le linee
		try {
			in = new BufferedReader(new FileReader(nomeFile));
			System.out.println("File aperto: " + nomeFile);
		} catch (FileNotFoundException e) {
			System.out.println("File non trovato: ");
			e.printStackTrace();
			return linea = "File non trovato";
		}
		try {
			for (int i = 1; i <= numLinea; i++) {
				linea = in.readLine();
				if (linea == null) {
					linea = "Linea non trovata";
					in.close();
					return linea;
				}
			}
		} catch (IOException e) {
			System.out.println("Linea non trovata: ");
			e.printStackTrace();
			return linea = "Linea non trovata";
		}
		System.out.println("Linea selezionata: " + linea);

		try {
			in.close();
		} catch (IOException e) {
			System.out.println("Errore nella chiusura del reader");
			e.printStackTrace();
		}
		return linea;
	} // getLine

	/**
	 * metodo per recuperare la linea successiva di un file aperto in precedenza
	 * 
	 * @param in
	 * @return linea
	 */
	static String getNextLine(BufferedReader in) {
		String linea = null;
		try {
			if ((linea = in.readLine()) == null) {
				in.close();
				linea = "Nessuna linea disponibile";
			}
		} catch (FileNotFoundException e) {
			System.out.println("File non trovato: ");
			e.printStackTrace();
			return linea = "File non trovato";
		} catch (IOException e) {
			System.out.println("Problemi nell'estrazione della linea: ");
			e.printStackTrace();
			linea = "Nessuna linea disponibile";
		}
		return linea;
	} // getNextLine
}