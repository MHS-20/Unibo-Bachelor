package montagnerusse;

public class GiroSulleMontagneRusse {

	public static void main(String args[]) {
		final int NT = 10;
		final int NV = 25;
		Visitatore[] visitatori = new Visitatore[NV];
		Trenino t = new Trenino(NT);
		Stazione s = new Stazione(t);

		for (int i = 0; i < NV; i++)
			visitatori[i] = new Visitatore(s);

		t.start();
		//s.inizio_giro();

		for (int i = 0; i < NV; i++)
			visitatori[i].start();

		for (;;) {
			s.inizio_giro();
			s.fine_giro();
		}
	}

}
