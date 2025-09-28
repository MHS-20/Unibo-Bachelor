package gelateria1;

public class Cliente extends Thread {

	private Monitor m;

	public Cliente(Monitor m) {
		this.m = m;
	}

	public void run() {

		try {

			for (int i = 0; i < 5; i++) { // 5 gelati al giorno
				System.out.println("Cliente: provo ad entrare" + this.getName());
				m.entraCliente();
				System.out.println("Cliente: sono entrato" + this.getName());
				sleep(500);
				System.out.println("Cliente: provo ad uscire" + this.getName());
				m.esceCliente();
				System.out.println("Cliente: sono uscito" + this.getName());

			}
		} catch (InterruptedException e) {
		}
	}
}