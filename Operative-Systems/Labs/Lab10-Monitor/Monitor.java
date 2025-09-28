package gelateria1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

	// private int numGelatai
	private int numGelataiLiberi, numClienti;
	private int postiOccupati, capienza;
	private int sospGelatai, sospClienti;

	private Lock lock = new ReentrantLock();
	private Condition ingressoGelataio = lock.newCondition();
	private Condition uscitaGelataio = lock.newCondition();
	private Condition ingressoCliente = lock.newCondition();
	// private Condition uscitaCliente = lock.newCondition();

	public Monitor(int capienza, int numGelatai) {
		this.capienza = capienza;
		// this.numGelatai = numGelatai;
		this.numGelataiLiberi = numGelatai;

		this.postiOccupati = 0;
		this.numClienti = 0;
		this.sospGelatai = 0;
		this.sospClienti = 0;
	}

	public void stato() {
		lock.lock();
		System.out.println("\n\n\n\n\nPosti occupati: " + this.postiOccupati);
		System.out.println("Gelatai liberi: " + this.numGelataiLiberi);
		System.out.println("Numero clienti dentro: " + this.numClienti);
		System.out.println("Numero clienti sospesi: " + this.sospClienti);
		System.out.println("Numero gelatai sospesi: " + this.sospGelatai);
		System.out.println("\n\n\n\n\n");
		lock.unlock();

	}

	public void entraCliente() throws InterruptedException {

		lock.lock();

		try {

			while (postiOccupati == capienza || numGelataiLiberi == 0 || numClienti == (capienza - 1)) {
				sospClienti++;
				System.out.println("Cliente sospeso");
				ingressoCliente.await();
				System.out.println("Cliente risvegliato");
				sospClienti--;
			}

			numClienti++; // evitare che capienza si riempa solo di clienti
			postiOccupati++;
			numGelataiLiberi--;

		} finally {
			lock.unlock();
		}
		return;
	}

	public void esceCliente() {
		lock.lock();
		postiOccupati--;
		numGelataiLiberi++;

		if (sospClienti > 0)
			ingressoCliente.signal();

		lock.unlock();
		return;
	}

	public void entraGelataio() throws InterruptedException {

		lock.lock();

		try {
			while (postiOccupati == capienza) {
				sospGelatai++;
				System.out.println("Gelataio sospeso in ingresso");
				ingressoGelataio.await();
				System.out.println("Gelataio risvegliato in ingresso");
				sospGelatai--;
			}

			postiOccupati++;
			numGelataiLiberi++;

			if (sospClienti > 0 && postiOccupati < capienza)
				ingressoCliente.signal();

		} finally {
			lock.unlock();
		}
		return;
	}

	public void esceGelataio() throws InterruptedException {

		lock.lock();

		try {
			while (numGelataiLiberi == 0) {
				sospGelatai++;
				System.out.println("Gelataio sospeso in uscita");
				uscitaGelataio.await();
				System.out.println("Gelataio risvegliato in uscita");
				sospGelatai--;
			}

			postiOccupati--;
			numGelataiLiberi--;

		} finally {
			lock.unlock();
		}
		return;
	}
}