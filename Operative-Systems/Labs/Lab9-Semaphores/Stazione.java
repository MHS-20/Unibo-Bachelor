package montagnerusse;

import java.util.concurrent.Semaphore;

public class Stazione {
	Trenino t;
	Semaphore SVI, SVU, ST, mutex;

	public Stazione(Trenino t) {
		this.t = t;
		SVI = new Semaphore(0);
		SVU = new Semaphore(0);
		ST = new Semaphore(0);
		mutex = new Semaphore(1);
	}

	public void accesso() {
		try {
			mutex.acquire();
			if (t.getFree() > t.getTmax() || t.getGiro()) {
				mutex.release();
				SVI.acquire();
				mutex.acquire();
			}
			t.accedi();
			mutex.release();
		} catch (InterruptedException e) {
		}

		if (t.getFree() < t.getTmax() / 2) //può partire
			ST.release();
	}

	public void uscita() {
		try {
			mutex.acquire();
			if (t.getGiro() == true) {
				mutex.release();
				SVU.acquire();
				mutex.acquire();
			}
			t.esci();
			SVI.release();
			mutex.release();
		} catch (InterruptedException e) {
		}
	}

	public void inizio_giro() {
		try {
			mutex.acquire();
			while (t.getFree() > t.getTmax() / 2) {
				mutex.release();
				ST.acquire();
			}
			t.iniziaGiro();
		} catch (InterruptedException e) {
		}
	}

	public void fine_giro() {

		try {
			mutex.acquire();
			t.fineGiro();
			for (int i = 0; i < SVU.getQueueLength(); i++)
				SVU.release();
			mutex.release();
		} catch (InterruptedException e) {
		}
	}
}
