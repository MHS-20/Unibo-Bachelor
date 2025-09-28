// PutFileServer Sequenziale

import java.io.*;
import java.net.*;

public class PutFileServerSeq {
	public static final int PORT = 54321; // porta default per server

	public static void main(String[] args) throws IOException {
		// Porta sulla quale ascolta il server
		int port = -1;
		int length = 0;
		boolean notEnded = true;

		Socket clientSocket = null;
		DataInputStream inSock = null;
		DataOutputStream outSock = null;

		/* controllo argomenti */
		try {
			if (args.length == 1) {
				port = Integer.parseInt(args[0]);
				// controllo che la porta sia nel range consentito 1024-65535
				if (port < 1024 || port > 65535) {
					System.out.println("Usage: java PutFileServerSeq or java PutFileServerSeq port");
					System.exit(1);
				}
			} else if (args.length == 0) {
				port = PORT;
			} else {
				System.out.println("Usage: java PutFileServerSeq or java PutFileServerSeq port");
				System.exit(1);
			}
		} // try
		catch (Exception e) {
			System.out.println("Problemi, i seguenti: ");
			e.printStackTrace();
			System.out.println("Usage: java PutFileServerSeq or java PutFileServerSeq port");
			System.exit(1);
		}

		/* preparazione socket e in/out stream */
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);
			System.out.println("PutFileServerSeq: avviato ");
			System.out.println("Creata la server socket: " + serverSocket);
		} catch (Exception e) {
			System.err.println("Problemi nella creazione della server socket: " + e.getMessage());
			e.printStackTrace();
			System.exit(2);
		}
		try {
			// ciclo infinito server
			while (true) {
				clientSocket = null;
				inSock = null;
				outSock = null;

				System.out.println("\nIn attesa di richieste...");
				try {
					clientSocket = serverSocket.accept();
					clientSocket.setSoTimeout(30000); // timeout altrimenti server sequenziale si sospende
					System.out.println("Connessione accettata: " + clientSocket + "\n");
				} catch (SocketTimeoutException te) {
					System.err.println("Non ho ricevuto nulla dal client per 30 sec., interrompo "
							+ "la comunicazione e accetto nuove richieste.");
					continue;
				} catch (Exception e) {
					System.err.println("Problemi nella accettazione della connessione: " + e.getMessage());
					e.printStackTrace();
					continue;
				}

				inSock = new DataInputStream(clientSocket.getInputStream());
				outSock = new DataOutputStream(clientSocket.getOutputStream());

				notEnded = true; 
				while (notEnded == true) { //una iterazione per ogni file
					// ricezione nome file
					String nomeFile;
					try {
						nomeFile = inSock.readUTF();
						System.out.println("Ricevuto nome: " + nomeFile);
					} catch (SocketTimeoutException ste) {
						System.out.println("Timeout scattato: ");
						ste.printStackTrace();
						//notEnded = false;
						continue;
					} catch (EOFException e) {
						//notEnded = false;
						continue;
					} catch (IOException e) {
						System.out.println("Problemi nella creazione degli stream di input/output " + "su socket: ");
						e.printStackTrace();
						//notEnded = false;
						continue;
					}

					// elaborazione e comunicazione esito
					FileOutputStream outFile = null;
					String esito;

					if (nomeFile == null) {
						System.out.println("Problemi nella ricezione del nome del file: ");
						continue;
					} else {
						File curFile = new File(nomeFile);
						if (curFile.exists()) { // controllo su file
							try {
								esito = "salta file";
								curFile.delete();
							} catch (Exception e) {
								System.out.println("Problemi nella notifica di file esistente: ");
								e.printStackTrace();
								continue;
							}
						} else{
							esito = "attiva";
							curFile.createNewFile();
						}

						outFile = new FileOutputStream(nomeFile);
					}

					try {
						outSock.writeUTF(esito);
						length = inSock.readInt();
					} catch (SocketTimeoutException ste) {
						System.out.println("Timeout scattato: ");
						ste.printStackTrace();
						continue;
					}

					// ricezione file
					try {
						System.out.println("Ricevo il file " + nomeFile + ": \n");
						notEnded = FileUtility.trasferisci_a_byte_file_binario(inSock, new DataOutputStream(outFile),
								length);
						System.out.println("\nRicezione del file " + nomeFile + " terminata\n");
						outFile.close(); // chiusura file
					} catch (SocketTimeoutException ste) {
						System.out.println("Timeout scattato: ");
						ste.printStackTrace();
						System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
						continue;
					} catch (Exception e) {
						System.err.println("\nProblemi durante la ricezione e scrittura del file: " + e.getMessage());
						e.printStackTrace();
						System.out.println("Terminata connessione con " + clientSocket);
						continue;
					}
				} // while not ended
				clientSocket.shutdownInput(); // chiusura socket (downstream)
				clientSocket.shutdownOutput(); // chiusura socket (upstream)
				System.out.println("\nTerminata connessione con " + clientSocket);
				clientSocket.close();
			} // while (true)

		}
		// qui catturo le eccezioni non catturate all'interno del while
		// in seguito alle quali il server termina l'esecuzione
		catch (Exception e) {
			e.printStackTrace();
			clientSocket.shutdownInput(); // chiusura socket (downstream)
			clientSocket.shutdownOutput(); // chiusura socket (upstream)
			System.out.println("\nTerminata connessione con " + clientSocket);
			clientSocket.close();
			System.out.println("Errore irreversibile, PutFileServerSeq: termino...");
			System.exit(3);
		}
	} // main
} // PutFileServerSeq