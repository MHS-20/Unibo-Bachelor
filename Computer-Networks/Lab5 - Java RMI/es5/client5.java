import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;

class client5 {

	public static void main(String[] args) {
		final int REGISTRYPORT = 1099;
		String registryHost = null; // host remoto con registry
		String serviceName = "";
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		// Controllo dei parametri della riga di comando
		if (args.length != 2) {
			System.out.println("Sintassi: RMI_Registry_IP ServiceName");
			System.exit(1);
		}
		registryHost = args[0];
		serviceName = args[1];

		System.out.println("Invio richieste a " + registryHost + " per il servizio di nome " + serviceName);

		// Connessione al servizio RMI remoto
		try {
			String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;
			RemOp serverRMI = (RemOp) Naming.lookup(completeName);
			System.out.println("ClientRMI: Servizio \"" + serviceName + "\" connesso");

			System.out.println("\nRichieste di servizio fino a fine file");

			String service;
			System.out.print("Servizio (C=Conta Righe, E=Elimina riga): ");

			int wordNum, rowNum, res;
			String fileName;
			String[] results;

			/* ciclo accettazione richieste utente */
			while ((service = stdIn.readLine()) != null) {

				if (service.equalsIgnoreCase("c")) {
					System.out.println("Nome file: ");
					fileName = stdIn.readLine();

					System.out.println("Numero di parole per riga: ");
					wordNum = Integer.parseInt(stdIn.readLine());
					res = serverRMI.conta_righe(fileName, wordNum);
					System.out.println("Risultato conta righe: " + res);
					// conta righe
				} else if (service.equalsIgnoreCase("e")) {
					System.out.println("Nome file: ");
					fileName = stdIn.readLine();

					System.out.println("Numero della riga da eliminare: ");
					rowNum = Integer.parseInt(stdIn.readLine());
					results = serverRMI.elimina_riga(fileName, rowNum);
					System.out.println("Risultato conta righe: " + results);
				} else // elimina file
					System.out.print("Servizio non disponibile");

				System.out.print("Servizio (C=Conta Righe, E=Elimina riga): ");
			} // while (!EOF), fine richieste utente
		} catch (Exception e) {
			System.err.println("ClientRMI: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
}