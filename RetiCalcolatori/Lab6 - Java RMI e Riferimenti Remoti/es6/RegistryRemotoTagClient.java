
/**
 *  Interfaccia remota Registry per il Cliente.
 *  	Cerca = ottiene il primo riferimento al server registrato con 'nomeLogico'.
 *  	CercaTutti = ottiene tutti i riferimenti ai server registrati con 'nomeLogico'.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegistryRemotoTagClient extends Remote {
	public Remote cerca(String nomeLogico) throws RemoteException;

	public Remote[] cercaTutti(String nomeLogico) throws RemoteException;

	public Remote[] ricercaPerTag(String tag) throws RemoteException;
}