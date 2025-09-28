package gelateria1;

public class Gelataio extends Thread {

	private Monitor m;

	public Gelataio(Monitor m) {
		this.m = m;
	}

	public void run() {

		while (true) {
			try {
				System.out.println("Gelataio: provo ad entrare " + this.getName());
				m.entraGelataio();
				System.out.println("Gelataio: sono entrato " + this.getName());
				sleep(500);
				System.out.println("Gelataio: provo ad uscire " + this.getName());
				m.esceGelataio();
				System.out.println("Gelataio: sono uscito " + this.getName());
			} catch (InterruptedException e) {
			}
		}
	}
}