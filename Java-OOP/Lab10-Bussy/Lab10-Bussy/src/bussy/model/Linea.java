package bussy.model;

import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public abstract class Linea {
	private String id;
	private Map<Integer, Fermata> orariPassaggioAlleFermate;
	
	public Linea(String id, Map<Integer, Fermata> orariPassaggioAlleFermate) {
		if (id==null || id.contentEquals("") || orariPassaggioAlleFermate==null)
			throw new IllegalArgumentException("arg invalidi in ctor linea");
		this.id = id;
		this.orariPassaggioAlleFermate = orariPassaggioAlleFermate;
	}
	
	public String getId() {
		return id;
	}
	
	public Map<Integer, Fermata> getOrariPassaggioAlleFermate() {
		return orariPassaggioAlleFermate;
	}
	
	public int getOrarioPassaggioAllaFermata(String nomeFermata) throws IllegalArgumentException {
//		Optional<Map.Entry<Integer,Fermata>> optionalEntry = orariPassaggioAlleFermate.entrySet().stream().filter( e -> e.getValue().getNome().equals(nomeFermata)).findFirst();
		Optional<Map.Entry<Integer,Fermata>> optionalEntry = Optional.empty();
		for (Map.Entry<Integer,Fermata> entry : orariPassaggioAlleFermate.entrySet()) {
			if (entry.getValue().getNome().equals(nomeFermata)) {
				optionalEntry = Optional.of(entry);
				break;
			}
		}
		//
		if (!optionalEntry.isPresent()) throw new IllegalArgumentException("Non esiste " + nomeFermata + " nella linea " + this.getId());
		return optionalEntry.get().getKey();
	}
	
	public Entry<Integer,Fermata> getCapolineaIniziale() throws IllegalArgumentException {
		// Optional<Entry<Integer,Fermata>> entryIniziale = this.getOrariPassaggioAlleFermate().entrySet().stream().min(Map.Entry.comparingByKey());
		// OPPURE, in due passi separati:
		// Comparator<Map.Entry<Fermata,Integer>> myComparator = Map.Entry.comparingByValue();
		// return this.getOrariPassaggioAlleFermate().entrySet().stream().min(myComparator);

		Optional<Entry<Integer,Fermata>> entryIniziale = Optional.empty();
		for (Entry<Integer,Fermata> entry : this.getOrariPassaggioAlleFermate().entrySet()) {
			if (!entryIniziale.isPresent() || entry.getKey() < entryIniziale.get().getKey()) {
				entryIniziale = Optional.of(entry);
			}
		}

		if (entryIniziale.isPresent()) return entryIniziale.get();
		else throw new IllegalArgumentException("lista fermate vuota o illegale");
	}

	public Entry<Integer,Fermata> getCapolineaFinale() throws IllegalArgumentException {
		// Optional<Entry<Integer,Fermata>> entryFinale = this.getOrariPassaggioAlleFermate().entrySet().stream().max(Map.Entry.comparingByKey());
		// OPPURE, in due passi separati:
		// Comparator<Map.Entry<Fermata,Integer>> myComparator = Map.Entry.comparingByValue();
		// return this.getOrariPassaggioAlleFermate().entrySet().stream().max(myComparator);

		Optional<Entry<Integer,Fermata>> entryFinale = Optional.empty();
		for (Entry<Integer,Fermata> entry : this.getOrariPassaggioAlleFermate().entrySet()) {
			if (!entryFinale.isPresent() || entry.getKey() > entryFinale.get().getKey()) {
				entryFinale = Optional.of(entry);
			}
		}

		if (entryFinale.isPresent()) return entryFinale.get();
		else throw new IllegalArgumentException("lista fermate vuota o illegale");
	}
	
	public boolean isCapolineaIniziale(String fermata) {
		return fermata.equals(getCapolineaIniziale().getValue().getNome());
	}
	
	public boolean isCapolineaFinale(String fermata) {
		return fermata.equals(getCapolineaFinale().getValue().getNome());
	}

	public boolean isCircolare() {
		return getCapolineaIniziale().getValue().getNome().equals(getCapolineaFinale().getValue().getNome());
	}
	
	public abstract Optional<Percorso> getPercorso(String fermataDa, String fermataA) ;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orariPassaggioAlleFermate == null) ? 0 : orariPassaggioAlleFermate.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linea other = (Linea) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orariPassaggioAlleFermate == null) {
			if (other.orariPassaggioAlleFermate != null)
				return false;
		} else if (!orariPassaggioAlleFermate.equals(other.orariPassaggioAlleFermate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Linea [id=" + id + ", orariPassaggioAlleFermate=" + orariPassaggioAlleFermate + "]";
	}

}
