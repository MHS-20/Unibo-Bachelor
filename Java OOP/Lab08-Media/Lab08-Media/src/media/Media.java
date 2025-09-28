package media;

import media.filters.HasType;

public abstract class Media implements HasType {
	private int year = -1;
	private String title = null;

	public Media(int year, String title) {
		this.title = title;
		this.year = year;
	}

	public abstract Type getType(); // astratto!

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String titolo) {
		this.title = titolo;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int anno) {
		this.year = anno;
	}

	// versione prima di java 15
	/*
	 * @Override 
	 * public boolean equals(Object o) 
	 * { if (!(o instanceof Media)) 
	 * return false; 
	 * Media m = (Media) o; 
	 * return (this.title.equals(m.getTitle()) && this.year == m.getYear()); }
	 */
	
	//versione da Java 15 in poi va abilitata con –-enable-preview
	@Override
	public boolean equals(Object o) {
		if (o instanceof Media m)
		{
		
		return (this.title.equals(m.getTitle()) && this.year == m.getYear());
		}
		return false;
	}

	public String toString() {
		return "Media di tipo " + getType() + " con titolo: " + getTitle() + " anno: " + getYear();

	}
}
