import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.StringTokenizer;
//qui catturo le eccezioni non catturate all'interno del while

public class DiscoveryServer {

	// porta nel range consentito 1024-65535!
	// dichiarata come statica perche' caratterizza il server
	private static final int PORT = 4445;

	public static void main(String[] args) {

		System.out.println("DiscoveryServer: avviato");
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];

		int port = Integer.parseInt(args[0]);
		int indexFile = 0; 

		RowSwap rs;
		//Thread th[] = new Thread[args.length - 1];
		// Thread th;

		// Controllo argomenti
		// Se non ho un numero pari di argomenti, oppure non viene registrato nessun
		// server posso uscire
		if ((args.length-1) % 2 != 0 || args.length == 0) {
			System.out.println("Usage: java DiscoveryServer portaDiscoveryServer file1 port1 ... fileN portN"); 
			System.exit(1);
		}
		port = Integer.parseInt(args[0]);
		if (port <= 1024 || port > 65535) {
			System.out.println("The discovery server port is not valid: " + args[0]);
			System.exit(2);
		}

		int validArgs = 0;
		File fileCorr;
		int portCorr;

		// per ogni coppia verifico che il primo argomento sia un file esistente
		// e il secondo una porta valida
		for (int i = 1; i < args.length; i = i + 2) {
			if (!((fileCorr = new File(args[i])).exists())) {
				System.out.println("One of the file passed as args, does not exist, the following:\n"+ args[i]);
				continue;
			}
			try {
				portCorr = Integer.parseInt(args[i+1]);
				if (portCorr <= 1024 || portCorr > 65535) {
					System.out.println("The following port is not valid: " + args[i]);
					continue;
				} else { 
					// TODO: controllare (ciclo) che le porte successive siano diverse da quella corrente
					// se arrivo qui: (a) il file esiste (b) la porta e' corretta
					System.out.println("Aggiunto il seguente SwapServer - File: " + fileCorr + ",\t porta: " + portCorr);
					validArgs++;
				}
			} catch (NumberFormatException e) {
				System.out.println("The following port is not a valid integer: "+ args[i]);
				continue;
			}
		} // for

		// Inizializzazione e attivazione dei SwapServer
		for (int i = 1; i <= validArgs; i+=2) {
			rs = new RowSwap(args[i], Integer.parseInt(args[i+1]));
			rs.start();
			System.out.println("Server: generato thread:" + i);
		}

		// creo socket
		try {
			socket = new DatagramSocket(port);
			packet = new DatagramPacket(buf, buf.length);
			System.out.println("Creata la socket: " + socket);
		} catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			String nomeFile = null;
			int numLinea = -1;
			String richiesta = null, risposta = null; 
			ByteArrayInputStream biStream = null;
			DataInputStream diStream = null;
			ByteArrayOutputStream boStream = null;
			DataOutputStream doStream = null;
			byte[] data = null;

			// 1 ricezione richieste
			while (true) { // demone
				try {
					packet.setData(buf);
					socket.receive(packet);
					System.out.println("Server: Messaggio dal client ricevuto");
				} catch (IOException e) {
					System.err.println("Server: Problemi nella ricezione del datagramma: " + e.getMessage());
					e.printStackTrace();
					continue;
					// il server continua a fornire il servizio ricominciando
				}

				// 2 estraggo richiesta
				try {
					biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					diStream = new DataInputStream(biStream);
					richiesta = diStream.readUTF(); // nome file
					System.out.println("Server: richiesto il file " + richiesta);

				} catch (Exception e) {
					System.err.println("Server: Problemi nella lettura della richiesta: " + nomeFile + " " + numLinea);
					e.printStackTrace();
					continue;
					// il server continua a fornire il servizio ricominciando dall'inizio
					// del ciclo
				}

				// 3 invio della risposta (porta del thread)
				try {

					risposta = null;
					for (int i = 1; i <= validArgs; i+=2) {
						if (args[i].equals(richiesta)) {
						risposta = "" + args[i+1];
						break;
						}
					}

					// problemi lato server, i.e. non abbiamo trovato il file
					if (risposta == null) risposta = "File con nome: " + richiesta + " non trovato";
					System.out.println("Risposta: " + risposta);					

					boStream = new ByteArrayOutputStream();
					doStream = new DataOutputStream(boStream);
					doStream.writeUTF(risposta);
					data = boStream.toByteArray();
					data = boStream.toByteArray();
					packet.setData(data, 0, data.length);
					socket.send(packet);

				} catch (IOException e) {
					System.err.println("Problemi nell'invio della risposta: " + e.getMessage());
					e.printStackTrace();
					continue;
					// il server continua a fornire il servizio ricominciando dall'inizio
					// del ciclo
				}
			} // while

		}
		// qui catturo le eccezioni non catturate all'interno del while
		// in seguito alle quali il server termina l'esecuzione
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("LineServer: termino...");
		socket.close();
	}
}