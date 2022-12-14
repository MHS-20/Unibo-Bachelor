package bussy.model;
import java.util.*;
import java.util.stream.Collectors;

public class MyCercatore implements Cercatore {

	private Map<String, Linea> mappaLinee;

	public MyCercatore(Map<String, Linea> mappaLinee){
		if (mappaLinee==null || mappaLinee.isEmpty()) throw new IllegalArgumentException("mappa linee nulla o vuota");
		this.mappaLinee = mappaLinee;
	}

	public SortedSet<Percorso> cercaPercorsi(String fermataDa, String fermataA, OptionalInt durataMax){
		if (fermataDa==null) throw new IllegalArgumentException("fermata iniziale nulla");
		if (fermataA==null) throw new IllegalArgumentException("fermata finale nulla");
		//cerca SOLO percorsi diretti senza cambi
		SortedSet<Percorso> percorsiDiretti = mappaLinee.values().stream()
													.map(linea -> linea.getPercorso(fermataDa, fermataA))
													.filter(p -> p.isPresent()).map(p -> p.get())
													.filter(p -> p.getDurata()>0)
													.collect(Collectors.toCollection(TreeSet::new));
		SortedSet<Percorso> percorsiDirettiNonPiuLunghiDiTot = 
				durataMax.isPresent() 
					? percorsiDiretti.stream().filter(p -> p.getDurata()<=durataMax.getAsInt()).collect(Collectors.toCollection(TreeSet::new))
					: percorsiDiretti;
		return percorsiDirettiNonPiuLunghiDiTot;
	}

	public Map<String, Linea> getMappaLinee() {
		return mappaLinee;
	}

}
