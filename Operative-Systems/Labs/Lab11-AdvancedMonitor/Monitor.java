package newgrange;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

	private Lock lock = new ReentrantLock();

	// Capienze
	private int maxGoing; // capienza stanza
	private int maxVisiting; // capienza passaggio

	// Contatori sul passaggio (1 per ogni direzione)
	private int singoli[] = new int[2];
	private int coppie[] = new int[2];
	private int guide[] = new int[2];

	// Contatori complessivi
	private int totGoing; // totale nel passaggio
	private int totVisiting; // totale nella stanza
	private int totGuideIN; // numero di guide nalla stanza

	// Condition (1 per ogni direzione)
	private Condition[] codaSingoli = new Condition[2];
	private Condition[] codaCoppie = new Condition[2];
	private Condition[] codaGuide = new Condition[2];

	// Contatori dei sospesi (1 per ogni direzione)
	private int[] sospSingoli = new int[2];
	private int[] sospCoppie = new int[2];
	private int[] sospGuide = new int[2];

	public Monitor(int maxGoing, int maxVisitng) {
		this.maxGoing = maxGoing;
		this.maxVisiting = maxVisitng;

		totGoing = 0;
		totVisiting = 0;
		totGuideIN = 0;

		for (int i = 0; i < 2; i++) {
			singoli[i] = 0;
			coppie[i] = 0;
			guide[i] = 0;

			codaSingoli[i] = lock.newCondition();
			codaCoppie[i] = lock.newCondition();
			codaGuide[i] = lock.newCondition();

			sospSingoli[i] = 0;
			sospCoppie[i] = 0;
			sospGuide[i] = 0;
		}
	}

	private int oppositeDir(Direzione d) {
		if (d.equals(Direzione.IN))
			return Direzione.OUT.ordinal();
		else
			return Direzione.IN.ordinal();

	}

	public void svegliaPerEntrare() { // sveglia chi deve entrare
		if (sospGuide[Direzione.IN.ordinal()] > 0)
			codaGuide[Direzione.IN.ordinal()].signal();
		else if (sospSingoli[Direzione.IN.ordinal()] > 0)
			codaSingoli[Direzione.IN.ordinal()].signal();
		else if (sospCoppie[Direzione.IN.ordinal()] > 0)
			codaCoppie[Direzione.IN.ordinal()].signal();
		else if (sospGuide[Direzione.OUT.ordinal()] > 0 || sospSingoli[Direzione.OUT.ordinal()] > 0
				|| sospCoppie[Direzione.OUT.ordinal()] > 0)
			svegliaPerUscire();
		return;
	}

	public void svegliaPerUscire() { // sveglia chi deve uscire
		if (sospCoppie[Direzione.OUT.ordinal()] > 0)
			codaCoppie[Direzione.OUT.ordinal()].signal();
		else if (sospSingoli[Direzione.OUT.ordinal()] > 0)
			codaSingoli[Direzione.OUT.ordinal()].signal();
		else if (sospGuide[Direzione.OUT.ordinal()] > 0)
			codaGuide[Direzione.OUT.ordinal()].signal();
		else if (sospGuide[Direzione.IN.ordinal()] > 0 || sospSingoli[Direzione.IN.ordinal()] > 0
				|| sospCoppie[Direzione.IN.ordinal()] > 0)
			svegliaPerUscire();
		return;
	}

	public void EntraSingolo(Direzione dir) throws InterruptedException {
		lock.lock();

		// guida prioritatiria nella stessa direzione
		// coppia prioritaria in direzione opposta

		while (totGoing == maxGoing || totVisiting == maxVisiting || sospGuide[dir.ordinal()] > 0
				|| sospCoppie[oppositeDir(dir)] > 0) {
			sospSingoli[dir.ordinal()]++;
			codaSingoli[dir.ordinal()].await();
			sospSingoli[dir.ordinal()]--;
		}

		singoli[dir.ordinal()]++;
		totGoing++;
		lock.unlock();
	}

	public void EsceSingolo(Direzione dir) throws InterruptedException {
		lock.lock();

		// coppia prioirataria nella stessa direzione
		// guida prioritaria in direzione opposta

		while (totGoing == maxGoing || sospCoppie[dir.ordinal()] > 0 || sospGuide[oppositeDir(dir)] > 0) {
			sospSingoli[dir.ordinal()]++;
			codaSingoli[dir.ordinal()].await();
			sospSingoli[dir.ordinal()]--;
		}

		singoli[dir.ordinal()]--;
		totGoing--;

		if (dir.equals(Direzione.IN)) {
			totVisiting++;
			svegliaPerUscire();
		} else {
			totVisiting--;
			svegliaPerEntrare();
		}

		lock.unlock();
	}

	public void EntraCoppia(Direzione dir) throws InterruptedException {
		lock.lock();
		while (totGoing == maxGoing || totVisiting == maxVisiting || sospSingoli[dir.ordinal()] > 0
				|| coppie[oppositeDir(dir)] > 0) {
			sospCoppie[dir.ordinal()]++;
			codaCoppie[dir.ordinal()].await();
			sospCoppie[dir.ordinal()]--;
		}

		coppie[dir.ordinal()]++;
		totGoing++;

		lock.unlock();
	}

	public void EsceCoppia(Direzione dir) throws InterruptedException {
		lock.lock();
		while (totGoing == maxGoing || sospSingoli[oppositeDir(dir)] > 0 || sospGuide[oppositeDir(dir)] > 0
				|| coppie[oppositeDir(dir)] > 0) {
			sospCoppie[dir.ordinal()]++;
			codaCoppie[dir.ordinal()].await();
			sospCoppie[dir.ordinal()]--;
		}

		coppie[dir.ordinal()]--;
		totGoing--;

		if (dir.equals(Direzione.IN)) {
			totVisiting++;
			svegliaPerUscire();
		} else {
			totVisiting--;
			svegliaPerEntrare();
		}

		lock.unlock();
	}

	public void EntraGuida(Direzione dir) {
		lock.lock();

		lock.unlock();
	}

	public void EsceGuida(Direzione dir) {
		lock.lock();

		lock.unlock();
	}
}