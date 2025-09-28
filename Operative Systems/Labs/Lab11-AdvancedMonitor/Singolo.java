package newgrange;

import java.util.Random;
import static newgrange.Direzione.IN;
import static newgrange.Direzione.OUT;

public class Singolo extends Thread {

	private Monitor M;
	private Random r;

	public Singolo(Monitor m, Random r) {
		M = m;
		this.r = r;
	}

	public void run() {
		try {
			M.EntraSingolo(IN);
			sleep(r.nextInt(10 * 1000));
			M.EsceSingolo(IN);
			sleep(r.nextInt(10 * 1000));
			M.EntraSingolo(OUT);
			sleep(r.nextInt(10 * 1000));
			M.EsceSingolo(OUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}