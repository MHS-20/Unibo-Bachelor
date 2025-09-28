package newgrange;

import static newgrange.Direzione.IN;
import static newgrange.Direzione.OUT;

import java.util.Random;

public class Coppia extends Thread {

	private Monitor M;
	private Random r;

	public Coppia(Monitor m, Random r) {
		M = m;
		this.r = r;
	}

	public void run() {
		try {
			M.EntraCoppia(IN);
			sleep(r.nextInt(10 * 1000));
			M.EsceCoppia(IN);
			sleep(r.nextInt(10 * 1000));
			M.EntraCoppia(OUT);
			sleep(r.nextInt(10 * 1000));
			M.EsceCoppia(OUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}