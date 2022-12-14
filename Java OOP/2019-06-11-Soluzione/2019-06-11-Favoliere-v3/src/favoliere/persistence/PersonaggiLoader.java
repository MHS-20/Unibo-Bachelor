package favoliere.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import favoliere.model.Personaggio;
import favoliere.model.Tipologia;

public class PersonaggiLoader implements SectionLoader<Personaggio> {

	private List<Personaggio> personaggi;
	
	@Override
	public void loadAllItems(Reader baseReader) throws IOException, BadFileFormatException {
		personaggi = new ArrayList<>();
		BufferedReader reader = new BufferedReader(baseReader);
		String riga = null;
		while ((riga = reader.readLine()) != null) {
			String[] items = riga.split(":");
			if(items.length!=3) {
				throw new BadFileFormatException("Formato riga errato: non ci sono le tre parti previste" + riga);
			}
			if (!items[0].trim().equals("POSITIVO") && !items[0].trim().equals("NEGATIVO")){
				throw new BadFileFormatException("Mancanza tipologia in personaggio: " + riga);
			}
			if (items[1].trim().isEmpty()){
				throw new BadFileFormatException("Mancanza nome in personaggio: " + riga);
			}
			if (items[2].trim().isEmpty()){
				throw new BadFileFormatException("Mancanza descrizione in personaggio: " + riga);
			}
			personaggi.add(new Personaggio(items[1].trim(), Tipologia.valueOf(items[0].trim()), items[2].trim()));
		}
	}
	
	@Override
	public List<Personaggio> getItems() {
		return personaggi;
	}

}
