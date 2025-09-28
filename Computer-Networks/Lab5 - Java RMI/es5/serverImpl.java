import java.io.BufferedReader;
import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.PrintWriter;

public class ServerImpl extends UnicastRemoteObject implements RemOp {
	private BufferedReader in; 
	private StringTokenizer stk;
	private String[] res;  

	// Costruttore
	public ServerImpl() throws RemoteException {
		super();
	}

	public int conta_righe(String fileName, int wordNum) throws RemoteException {
		
		String line;
		int num = 0, numLine = 0; 

		try{
			in = new BufferedReader(new FileReader(fileName));
		System.out.println("File aperto: " + fileName);


			while((line = in.readLine()) != null){
			stk = new StringTokenizer(line); 
			num = stk.countTokens();

			if(num > wordNum)
			 numLine++;
			}
		}catch(Exception e){
		  throw new RemoteException(e.getMessage());
	}
		in.close();
		return numLine; 
	}

	public String[] elimina_riga(String fileName, int rowNum) throws RemoteException {
		String line;
		int count = 0; 
		File newf, old, temp; 

		try{
			in = new BufferedReader(new FileReader(fileName));
			System.out.println("File aperto: " + fileName);
			newf = new File("temp.txt");
			old = new File(fileName);
			PrintWriter pw = new PrintWriter(newf);

			while((line = in.readLine()) != null ){
				if(count != rowNum)
					pw.write(line);
				count++;
			}

			pw.close();
			old.delete();
			temp = new File(fileName);
			newf.renameTo(temp);
		}catch(Exception e){
			throw new RemoteException(e.getMessage());
		}

		res[0] = fileName; 
		res[1] = count + "";
		return res; 
	}

	// Avvio del Server RMI
	public static void main(String[] args) {

		final int REGISTRYPORT = 1099;
		String registryHost = "localhost";
		String serviceName = "RemOp"; // lookup name...

		// Registrazione del servizio RMI
		String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/" + serviceName;
		try {
			serverImpl serverRMI = new serverImpl();
			Naming.rebind(completeName, serverRMI);
			System.out.println("Server RMI: Servizio \"" + serviceName + "\" registrato");
		} catch (Exception e) {
			System.err.println("Server RMI \"" + serviceName + "\": " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
}