package stadiocalcio;

public class Tifoso extends Thread {
	private Stadio stadio;
	private String squadra;
	private int eta;
	private static final int tentativi = 5;

	public Tifoso(Stadio stadio, String squadra, int eta) {
		this.stadio = stadio;
		this.squadra = squadra;
		this.eta= eta;
	}

	public String getSquadra() {
		return squadra;
	}

	public int getFasciaeta() {
		return eta;
	}

	public void run() {
		boolean entrato = false; 
		
		for(int i=0; i<tentativi && entrato == false; i++){
			entrato = stadio.entra(squadra, eta);

			try{
			if (entrato){
				this.sleep(2);
				stadio.esci();
			}
			else this.sleep(2);
		}catch(InterruptedException e){}
}
}
}