package montagnerusse;

public class Visitatore extends Thread {
	Stazione s;
	boolean intreno;

	public Visitatore(Stazione s) {
		this.s = s;
		this.intreno = false;
	}

	public void run() {
		System.out.println("Provo ad accedere");
		s.accesso();
		System.out.println("Son salito");
		s.uscita();
		System.out.println("Sono sceso");
	}
}
