package montagnerusse;

public class Trenino extends Thread {
	private int tmax, free;
	private boolean giro;

	public Trenino(int tmax) {
		this.tmax = tmax;
		this.free = tmax;
		this.giro = false;
	}

	public int getTmax() {
		return tmax;
	}

	public int getFree() {
		return free;
	}

	public void accedi() {
		free--;
	}

	public void esci() {
		free++;
	}

	public boolean getGiro() {
		return giro;
	}

	public void iniziaGiro() {
		giro = true;
	}

	public void fineGiro() {
		System.out.println("Fine del giro");
		giro = false;
	}

	public void run() {

		while (true) {
			if (giro == true) {
				try {
					System.out.println("Inizio del giro");
					sleep(5); // tempo di viaggio
				} catch (InterruptedException e) {
				}
			}
		}
	}
}