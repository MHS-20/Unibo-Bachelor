package bussy.model;

import java.util.Map;
import java.util.Optional;

public class LineaCircolare extends Linea {

	public LineaCircolare(String id, Map<Integer,Fermata> orariPassaggioAlleFermate) throws BadLineException {
		super(id, orariPassaggioAlleFermate);
		Fermata fermataIniziale = this.getCapolineaIniziale().getValue();
		Fermata fermataFinale = this.getCapolineaFinale().getValue();
		if (!isCircolare()) throw new BadLineException("La linea non è circolare: " + fermataIniziale.getId() + "/" + fermataFinale.getId());
	}
		
	private boolean isCapolinea(String fermata) {
		return this.isCapolineaFinale(fermata) && this.isCapolineaIniziale(fermata);
	}
	
	@Override
	public Optional<Percorso> getPercorso(String fermataDa, String fermataA) {
		int durata;
		try {
			// caso 0a: da capolinea iniziale a fermataA (!= capolinea finale)
			if(isCapolinea(fermataDa) && !isCapolinea(fermataA)) {
				durata = this.getOrarioPassaggioAllaFermata(fermataA);  // se non c'è, lancia eccezione
				//System.out.println("caso 0a");
			}
			else
			// caso 0b: da fermataDa (!= capolinea iniziale) a capolinea finale
			if(isCapolinea(fermataA) && !isCapolinea(fermataDa)) {
				durata =  this.getCapolineaFinale().getKey()-this.getOrarioPassaggioAllaFermata(fermataDa);  // se non c'è, lancia eccezione
				//System.out.println("caso 0b");
			}
			else
			// caso 0c: da capolinea a capolinea (giro completo)
			if(isCapolinea(fermataDa) && isCapolinea(fermataA)) {
				durata =  this.getCapolineaFinale().getKey();
				//System.out.println("caso 0c");
			}
			else {
				int a = this.getOrarioPassaggioAllaFermata(fermataDa); // se non c'è, lancia eccezione
				int b = this.getOrarioPassaggioAllaFermata(fermataA);  // se non c'è, lancia eccezione
				if (b>a) { 
					// caso 1: fermataDa <  fermataA (b-a>0) --> percorso diretto, come in linea PaP
					durata = b-a;
					//System.out.println("caso 1");
				}
				else {
					// caso 2: fermataDa >= fermataA (b-a<=0)--> percorso circolare
					durata = (b-a)+this.getCapolineaFinale().getKey();
					//System.out.println("caso 2, da " + fermataDa + " a " + fermataA + ", tempo " + durata);
				}
			}
			
			return Optional.of(new Percorso(fermataDa, fermataA, this, durata));
		}
		catch (NoSuchStopException e) {
			return Optional.empty(); 
		}
	}

}
