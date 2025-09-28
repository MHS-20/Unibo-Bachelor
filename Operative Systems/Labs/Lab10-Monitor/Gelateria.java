package gelateria1;

public class Gelateria {

	private final static int capienza = 10;
	private final static int numGelatai = 5;
	private final static int numClienti = 15;

	public static void main(String[] args) {
		Monitor m = new Monitor(10, 5);
		Cliente[] c = new Cliente[numClienti];
		Gelataio[] g = new Gelataio[numGelatai];

		m.stato();

		for (int i = 0; i < numGelatai; i++) {
			g[i] = new Gelataio(m);
			g[i].start();
		}

		m.stato();

		for (int i = 0; i < numClienti; i++) {
			c[i] = new Cliente(m);
			c[i].start();
		}

		m.stato();
	}
}