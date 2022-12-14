package favoliere.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import favoliere.model.Scenario;

public class ScenariLoader implements SectionLoader<Scenario> {

	private List<Scenario> entities;

	@Override
	public void loadAllItems(Reader baseReader) throws IOException, BadFileFormatException {
		entities = new ArrayList<>();
		BufferedReader reader = new BufferedReader(baseReader);
		String riga = null;
		while ((riga = reader.readLine()) != null) {
			String[] items = riga.trim().split("#");
			int index;
			try {
				index = Integer.parseInt(items[1]);
			} catch (NumberFormatException e) {
				throw new BadFileFormatException("indice non numerico in riga");
			}
			if (items[0].trim().equals("")) {
				throw new BadFileFormatException("descrizione vuota in riga");
			}
			entities.add(supply(items[0].trim(), index));
		}
	}

	@Override
	public List<Scenario> getItems() {
		return entities;
	}

	protected Scenario supply(String s, int index) {
		return new Scenario(s, index);
	}

}
