// PutFileClient.java

import java.net.*;
import java.io.*;

public class PutFileClient {

	public static void main(String[] args) throws IOException {

		InetAddress addr = null;
		int port = -1;

		try { // check args
			if (args.length == 2) {
				addr = InetAddress.getByName(args[0]);
				port = Integer.parseInt(args[1]);
			} else {
				System.out.println("Usage: java PutFileClient serverAddr serverPort");
				System.exit(1);
			}
		} // try
			// Per esercizio si possono dividere le diverse eccezioni
		catch (Exception e) {
			System.out.println("Problemi, i seguenti: ");
			e.printStackTrace();
			System.out.println("Usage: java PutFileClient serverAddr serverPort");
			System.exit(2);
		}

		// oggetti utilizzati dal client per la comunicazione e la lettura del file
		// locale
		Socket socket = null;
		FileInputStream inFile = null;
		DataInputStream inSock = null;
		DataOutputStream outSock = null;
		String nomeDir, nomeFile, attivo;
		File dir = null;
		File[] files = null;

		// creazione stream di input da tastiera
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out
				.print("PutFileClient Started.\n\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome direttorio: ");

		try {
			while ((nomeDir = stdIn.readLine()) != null) {
				// se il dir esiste creo la socket
				dir = new File(nomeDir);
				if (dir.exists() && dir.isDirectory()) {
					// creazione socket
					try {
						socket = new Socket(addr, port);
						socket.setSoTimeout(30000);
						System.out.println("Creata la socket: " + socket);
					} catch (Exception e) {
						System.out.println("Problemi nella creazione della socket: ");
						e.printStackTrace();
						System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome direttorio: ");
						continue;
						// il client continua l'esecuzione riprendendo dall'inizio del ciclo
					}

					// creazione stream di input/output su socket
					try {
						inSock = new DataInputStream(socket.getInputStream());
						outSock = new DataOutputStream(socket.getOutputStream());
					} catch (IOException e) {
						System.out.println("Problemi nella creazione degli stream su socket: ");
						e.printStackTrace();
						System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome direttorio: ");
						continue;
					}
				}
				// se la richiesta non e' corretta non proseguo
				else {
					System.out.println("Direttorio non trovato");
					System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome file: ");
					continue;
				}

				/* Invio file richiesto e attesa esito dal server */
				// creazione stream di input da file
				files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					nomeFile = files[i].getAbsolutePath();

					try {
						inFile = new FileInputStream(files[i]);
					} catch (FileNotFoundException e) {
						System.out.println("Problemi nella creazione dello stream di input da " + nomeFile + ": ");
						e.printStackTrace();
						System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome file: ");
						continue;
					}

					// trasmissione del nome
					try {
						outSock.writeUTF(files[i].getAbsolutePath());
						System.out.println("Inviato il nome del file " + nomeFile);
					} catch (Exception e) {
						System.out.println("(1) Problemi nell'invio del nome di " + nomeFile + ": ");
						e.printStackTrace();
						System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome file: ");
						continue;
					}

					// attivo o salta file
					try {
						attivo = inSock.readUTF();
						if (attivo.compareToIgnoreCase("salta file") != 0)
							continue;
					} catch (SocketTimeoutException | EOFException e) { // server non risponde
						e.printStackTrace();
						continue; 
					}

					// scrivo la lunghezza del file
					outSock.writeLong(files[i].length());
					System.out.println("Inizio la trasmissione di " + nomeFile);

					// trasferimento file
					try {
						FileUtility.trasferisci_a_byte_file_binario(new DataInputStream(inFile), outSock,
								files[i].length());
						inFile.close(); // chiusura file
						System.out.println("Trasmissione di " + nomeFile + " terminata ");
					} catch (SocketTimeoutException ste) {
						System.out.println("Timeout scattato: ");
						ste.printStackTrace();
						System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome file: ");
						continue;
					} catch (Exception e) {
						System.out.println(" (2) Problemi nell'invio di " + nomeFile + ": ");
						e.printStackTrace();
						System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, oppure immetti nome file: ");
						// il client continua l'esecuzione riprendendo dall'inizio del ciclo
						continue;
					}
				} // fine for

				// chiudo la socket
				socket.shutdownOutput();
				socket.shutdownInput();
				System.out.println("Terminata la chiusura della socket: " + socket);
			} // fine while
			socket.close();
			System.out.println("PutFileClient: termino...");
		}
		// qui catturo le eccezioni non catturate all'interno del while
		// quali per esempio la caduta della connessione con il server
		// in seguito alle quali il client termina l'esecuzione
		catch (Exception e) {
			System.err.println("Errore irreversibile, il seguente: ");
			e.printStackTrace();
			System.err.println("Chiudo!");
			System.exit(3);
		}
	} // main
} // PutFileClient
