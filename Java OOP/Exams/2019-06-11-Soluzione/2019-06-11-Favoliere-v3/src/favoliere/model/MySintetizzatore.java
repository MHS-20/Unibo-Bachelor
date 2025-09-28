package favoliere.model;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class MySintetizzatore implements Sintetizzatore {

	private List<Personaggio> personaggi;
	private List<Scenario> 	  scenari;
	private List<Azione> 	  azioni;
	private List<Conclusione> conclusioni;
      
	@Override
	public List<Personaggio> getPersonaggi() {
		return personaggi;
	}

	@Override
	public List<Scenario> getScenari() {
		return scenari;
	}

	@Override
	public List<Azione> getAzioni() {
		return azioni;
	}

	@Override
	public List<Conclusione> getConclusioni() {
		return conclusioni;
	}

	public MySintetizzatore(List<Personaggio> personaggi, List<Scenario> scenari, List<Azione> azioni,
			List<Conclusione> conclusioni) {
		
		this.personaggi = personaggi;
		this.scenari = scenari;
		this.azioni = azioni;
		this.conclusioni = conclusioni;
	}

	@Override
	public Favola generaFavola(FasciaEta eta, Impressionabilita livelloImpressionabilita) throws NoSuchTaleException {
		Set<Personaggio> buoni = sorteggia(personaggi, 2, Tipologia.POSITIVO);
		Set<Personaggio> cattivi = sorteggia(personaggi, 1, Tipologia.NEGATIVO);
			
		Esordio esordio = new Esordio(buoni, cattivi);
		Optional<Scenario> scenario = sorteggia(scenari,eta.getGradoComplessita());
		if (!scenario.isPresent()) throw new NoSuchTaleException("Non esistono scenari con il grado di complessità richiesto");
		
		Optional<Azione> azione = sorteggia(azioni,livelloImpressionabilita.getGradoDurezza());
		if (!azione.isPresent()) throw new NoSuchTaleException("Non esistono azioni con il grado di durezza richiesto");
		
		Conclusione conclusione = sorteggia(conclusioni);
		
		return new Favola(esordio,scenario.get(),azione.get(),conclusione);
	}

	private <T> T sorteggia(List<T> lista){
		Random random = new Random();
		int i = random.nextInt(lista.size());
		return lista.get(i);
	}
	
	private <T extends ConIndice> Optional<T> sorteggia(List<T> lista, int upperBound){
		Random random = new Random();
		T result = null;
		int k;
		
		/* ***** PSEUDO CODICE SENZA STREAM, DA INCAPSULARE IN OPPORTUNA FUNZIONE AUSILIARIA *****
		boolean exist=false;
		for(int i=0; i<lista.size(); i++)
			if (lista.get(i).getIndice()<=upperBound)
				exist=true;
		* *****						*****/
		
		Set<T> indiceMinoreUpperBound = lista.stream().filter(e -> e.getIndice()<=upperBound).collect(Collectors.toSet());
		if (indiceMinoreUpperBound.isEmpty()) return Optional.empty();
		
		do {
			k = random.nextInt(lista.size());
			if (lista.get(k).getIndice()<=upperBound) result = lista.get(k);
		} while(lista.get(k).getIndice()>upperBound);
		
		return Optional.ofNullable(result);
	}
	
	private Set<Personaggio> sorteggia(List<Personaggio> lista, int n, Tipologia tipo) throws NoSuchTaleException {
		Set<Personaggio> personaggiScelti = lista.stream().filter(e -> e.getTipologia()==tipo).collect(Collectors.toSet());
		if (personaggiScelti.size()<n) throw new NoSuchTaleException("Non ci sono sufficienti personaggi del tipo richiesto");
		List<Personaggio> listaPersonaggiScelti = personaggiScelti.stream().collect(Collectors.toList()); //ri-converto in lista per avere una struttura dati con indice
		
		Random random = new Random();
		
		return 
			random
				.ints(0, listaPersonaggiScelti.size()) 
				.distinct()
				.limit(n)
				.mapToObj(listaPersonaggiScelti::get) //qui mi serve la lista per estrarre l'indice del random selezionato, non posso usare il set
				.collect(Collectors.toSet());
			
		
		/* ***** CODICE SENZA STREAM ***** 
		return pickNRandomElements(listaPersonaggiScelti, n, random);	
		 *****						*****/	
	}
	
	/* ***** CODICE SENZA STREAM ***** 
	private static Set<Personaggio> pickNRandomElements(List<Personaggio> listaPersonaggiScelti, int n, Random random) {
		Set<Personaggio> selected = new TreeSet<Personaggio>();
	    int k;
	    do {
			k = random.nextInt(listaPersonaggiScelti.size());
			if (!selected.contains(listaPersonaggiScelti.get(k))) //qui mi serve la lista per estrarre l'indice del random selezionato, non posso usare il set
				selected.add(listaPersonaggiScelti.get(k));
		} while(selected.size()<n);
	    return selected;
	}
	 *****						*****/	

}
