/* FileUtility.java */

import java.io.*;

public class FileUtility {

	/**
	 * Nota: sorgente e destinazione devono essere correttamente aperti e chiusi
	 * da chi invoca questa funzione.
	 *  
	 */
	static public boolean trasferisci_a_byte_file_binario(DataInputStream src,
			DataOutputStream dest, long length) throws IOException {
	
		// ciclo di lettura da sorgente e scrittura su destinazione
	    int buffer;
	    
	    try {
	    	// esco dal ciclo alla lettura di un valore negativo -> EOF
	    	// N.B.: la funzione consuma l'EOF
	    	for(int i = 0; i < length; i ++){
	    		buffer = src.read();
	    		
	    		if(buffer >= 0)
	    			dest.write(buffer);
	    		else 
	    			return false; 
	    	}
	    	dest.flush();
			return true; 
	    }
	    catch (EOFException e) {
	    	System.out.println("Problemi, i seguenti: ");
	    	e.printStackTrace();
			return false; 
	    }
	}
}