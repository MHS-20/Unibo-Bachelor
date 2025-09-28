import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class SwapClient {

	public static void main(String[] args) {

		InetAddress addr = null;
		int port = -1;
		String nomeFile = null;
		
		try {
			if (args.length == 3) { //localhost, porta, nome file
		    addr = InetAddress.getByName(args[0]);
		    port = Integer.parseInt(args[1]);
			nomeFile = args[2];

			} else {
				System.out.println("Errore negli argomenti del client");
			    System.exit(1);
			}
		} catch (UnknownHostException e) {
			System.out
		      .println("Problemi nella determinazione dell'endpoint del server : ");
			e.printStackTrace();
			System.out.println("SwapClient: interrompo...");
			System.exit(2);
		}

		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];
		int numl1, numl2, portTh = - 1; 

		// creazione della socket datagram, settaggio timeout di 30s
		// e creazione datagram packet
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(30000);
			packet = new DatagramPacket(buf, buf.length, addr, port);
			System.out.println("\nSwapClient: avviato");
			System.out.println("Creata la socket: " + socket);
		} catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.out.println("SwapClient: interrompo...");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out
			.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");

			ByteArrayOutputStream boStream = null;
			DataOutputStream doStream = null;
			byte[] data = null;
			int numLinea = -1, res; 
			String richiesta, risposta; 
			ByteArrayInputStream biStream = null;
			DataInputStream diStream = null;
			StringTokenizer stk; 

		try {
			boStream = new ByteArrayOutputStream();
			doStream = new DataOutputStream(boStream);
			
			// interazione con l'utente fino a CTRL+D
			while ((nomeFile = stdIn.readLine()) != null) {
				try {
					
					//richiesta al server
					doStream.writeUTF(nomeFile);
					data = boStream.toByteArray();
					packet.setData(data);
					socket.send(packet);		
					System.out.println("Richiesta inviata a " + addr + ", " + port);
					
					//ricezione dal server 
					packet.setData(buf);
					socket.receive(packet);
					biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					diStream = new DataInputStream(biStream);
					risposta = diStream.readUTF();
					System.out.println("Ricevuta porta del thread: " + portTh);
					System.out.println("Ricevuta porta del thread: " + portTh);
					
					if (risposta.equals("File non trovato")) {
						System.out.println("Errore, il seguente: ");
						System.out.println(risposta + "\nEsco...");
						continue;
					} else {
						portTh = Integer.parseInt(risposta);
					}
					
					//leggo i numeri delle righe da utente
					System.out.print("\nNumeri delle linee da scambiare? ");
					stk = new StringTokenizer(stdIn.readLine(), " "); 
					numl1 = Integer.parseInt(stk.nextToken());
					numl2 = Integer.parseInt(stk.nextToken());
				} catch (Exception e) {
					System.out.println("Problemi nell'interazione da console: ");
					e.printStackTrace();
					System.out
						.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
					continue;
				}

				buf = new byte[256];
				packet = new DatagramPacket(buf, buf.length, addr, portTh);
				boStream = new ByteArrayOutputStream();
				doStream = new DataOutputStream(boStream);

				// invio richiesta a thread
				try {
					doStream.writeInt(numl1);
					doStream.writeInt(numl2);
					data = boStream.toByteArray();
					packet.setData(data);
					socket.send(packet);
					System.out.println("Richiesta inviata al thread " + addr + ", " + port);
				} catch (IOException e) {
					System.out.println("Problemi nell'invio della richiesta: ");
					e.printStackTrace();
					System.out
				      .print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
					continue;
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}

				try {
					// ricezione della risposta del thread
					packet.setData(buf);
					socket.receive(packet); // sospensiva solo per i millisecondi indicati, dopodiche' SocketException
				} catch (IOException e) {
					System.out.println("Problemi nella ricezione del datagramma: ");
					e.printStackTrace();
					System.out
						.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
					continue;
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}
				try {
					biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					diStream = new DataInputStream(biStream);
					res = diStream.readInt();
					System.out.println("Risposta del thread: " + res);
				} catch (IOException e) {
					System.out.println("Problemi nella lettura della risposta: ");
					e.printStackTrace();
					System.out
				      .print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
					continue;
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}
			
				// tutto ok, pronto per nuova richiesta
				System.out
			    	.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
			} // while
		}
		// qui catturo le eccezioni non catturate all'interno del while
		// in seguito alle quali il client termina l'esecuzione
		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("LineClient: termino...");
		socket.close();
	}
}