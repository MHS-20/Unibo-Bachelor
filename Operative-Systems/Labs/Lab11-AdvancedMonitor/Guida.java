package newgrange;

import static newgrange.Direzione.IN;
import static newgrange.Direzione.OUT;

import java.util.Random;
import static newgrange.Direzione.IN;
import static newgrange.Direzione.OUT;

public class Guida extends Thread {

	private Monitor M;
	private Random r;

	public Guida(Monitor m, Random r) {
		M = m;
		this.r = r;
	}

	public void run() {
		try {
			M.EntraGuida(IN);
			sleep(r.nextInt(10 * 1000));
			M.EsceGuida(IN);
			sleep(r.nextInt(10 * 1000));
			M.EntraGuida(OUT);
			sleep(r.nextInt(10 * 1000));
			M.EsceGuida(OUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
