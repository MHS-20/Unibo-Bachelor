package media;

import media.filters.HasGenre;
import utils.StringUtils;

public class Ebook extends Media implements HasGenre {
	private String[] authors = null;
	private String genre = null;

	public Ebook(String title, int year, String[] authors, String genre) {
		super(year, title);
		this.genre = genre;
		this.authors = new String[authors.length];
		for (int i = 0; i < authors.length; i++) {
			this.authors[i] = authors[i];
		}
	}

	public Type getType() {
		return Type.EBOOK;
	}

	public String[] getAuthors() {
		String[] auth = new String[authors.length];
		for (int i = 0; i < authors.length; i++) {
			auth[i] = authors[i];

		}
		return auth;
	}

	public void setAuthors(String[] autori) {
		this.authors = new String[autori.length];
		for (int i = 0; i < autori.length; i++) {
			this.authors[i] = autori[i];
		}
	}

	public String getGenre() {
		return this.genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	// versione fino a java 13
	/*
	 * @Override public boolean equals(Object o) { if (!(o instanceof Ebook)) return
	 * false;
	 * 
	 * Ebook eb = (Ebook) o; return StringUtils.areEquivalent(getAuthors(),
	 * eb.getAuthors()) && this.genre.equals(eb.getGenre()) && super.equals(o); }
	 */

	//versione da Java 15 in poi va abilitata con –-enable-preview
	@Override
	public boolean equals(Object o) {
		if (o instanceof Ebook eb)
		{return StringUtils.areEquivalent(getAuthors(), eb.getAuthors()) && this.genre.equals(eb.getGenre())
				&& super.equals(o); }
		return false;
	}
	
	
	public String toString() {
		String str = new String(super.toString() + " autori: ");
		for (int i = 0; i < this.authors.length; i++) {
			str = str + authors[i] + " ";

		}
		str = str + "genere: " + getGenre() + "\n";
		return str;

	}

}
