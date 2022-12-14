
/**
 * Interfaccia remota di servizio
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemOp extends Remote {

	public int conta_righe(String fileName, int wordNum) throws RemoteException;

	public String[] elimina_riga(String fileName, int rowNum) throws RemoteException;
}