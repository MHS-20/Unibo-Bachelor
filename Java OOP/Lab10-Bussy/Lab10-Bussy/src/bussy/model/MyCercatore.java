package bussy.model;
import java.util.*;
//import java.util.stream.Collectors;

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
//		SortedSet<Percorso> percorsiDiretti = mappaLinee.values().stream()
//													.map(linea -> linea.getPercorso(fermataDa, fermataA))
//													.filter(p -> p.isPresent()).map(p -> p.get())
//													.filter(p -> p.getDurata()>0)
//													.collect(Collectors.toCollection(TreeSet::new));

//		SortedSet<Percorso> percorsiDirettiNonPiuLunghiDiTot = 
//				durataMax.isPresent() 
//					? percorsiDiretti.stream().filter(p -> p.getDurata()<=durataMax.getAsInt()).collect(Collectors.toCollection(TreeSet::new))
//					: percorsiDiretti;
		
//		return percorsiDirettiNonPiuLunghiDiTot;

		SortedSet<Percorso> percorsiDiretti = new TreeSet<Percorso>();
		for (Linea linea : mappaLinee.values()) {
			Optional<Percorso> percOptional = linea.getPercorso(fermataDa, fermataA);
			if (percOptional.isPresent()) {
				Percorso percorso = percOptional.get();
				if (percorso.getDurata() > 0) {
					percorsiDiretti.add(percorso);
				}
			}
		}

		SortedSet<Percorso> percorsiDirettiNonPiuLunghiDiTot;
		if (durataMax.isPresent()) {
			percorsiDirettiNonPiuLunghiDiTot = new TreeSet<Percorso>();
			for (Percorso percorso : percorsiDiretti) {
				if (percorso.getDurata()<=durataMax.getAsInt()) {
					percorsiDirettiNonPiuLunghiDiTot.add(percorso);
				}
			}
		} else {
			percorsiDirettiNonPiuLunghiDiTot = percorsiDiretti;
		}

		return percorsiDirettiNonPiuLunghiDiTot;
	}

	public Map<String, Linea> getMappaLinee() {
		return mappaLinee;
	}

}
