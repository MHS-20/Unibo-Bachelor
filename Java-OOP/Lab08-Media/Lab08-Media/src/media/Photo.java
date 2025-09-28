package media;

import utils.StringUtils;

public class Photo extends Media {
	private String[] authors = null;

	public Photo(String titolo, int anno, String[] authors) {
		super(anno, titolo);
		this.authors = authors;
	}

	public Type getType() {
		return Type.PHOTO;
	}

	public String[] getAuthors() {
		return this.authors;
	}

	public void setAuthors(String[] authors) {
		this.authors = authors;
	}

	//versione prima di Java 15 
	/*
	 * @Override public boolean equals(Object o) { if (!(o instanceof Photo)) return
	 * false;
	 * 
	 * Photo f = (Photo) o; return StringUtils.areEquivalent(getAuthors(),
	 * f.getAuthors()) && super.equals(o); }
	 */
	
	//versione da Java 15 in poi va abilitata con –-enable-preview
	@Override
	public boolean equals(Object o) {
		if (o instanceof Photo f) {
		return StringUtils.areEquivalent(getAuthors(), f.getAuthors())
				&& super.equals(o);
		}
		return false;
	}

	@Override
	public String toString() {
		String str = super.toString() + " autori: ";
		for (int i = 0; i < this.authors.length; i++) {
			str += authors[i] + " ";
		}
		return str;
	}

}
