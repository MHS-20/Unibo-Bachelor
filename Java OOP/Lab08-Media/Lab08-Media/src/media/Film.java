package media;

import media.filters.HasDuration;
import media.filters.HasGenre;

public class Film extends Media implements HasGenre, HasDuration {
	private String[] actors = null;
	private String director = null;
	private int duration = -1;
	private String genre = null;

	public Film(String titolo, int anno, String regista, int duration, String[] attori, String genre) {
		super(anno, titolo);
		this.director = regista;
		this.duration = duration;
		this.actors = new String[attori.length];
		for (int i = 0; i < attori.length; i++) {
			this.actors[i] = attori[i];
		}
		this.genre = genre;
	}

	public Type getType() {
		return Type.FILM;
	}

	public String getDirector() {
		return this.director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String[] getActors() {
		String[] actors = new String[this.actors.length];
		for (int i = 0; i < this.actors.length; i++) {
			actors[i] = this.actors[i];
		}
		return actors;
	}

	public void setActors(String[] attori) {
		this.actors = new String[attori.length];
		for (int i = 0; i < attori.length; i++) {
			this.actors[i] = attori[i];
		}
	}

	
	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	
	public String getGenre() {
		return genre;
	}
	
	public void setGenre(String value) {
		this.genre = value;
	}
 
	// versione sino a java 14
	/*
	 * @Override public boolean equals(Object o) { 
	 * if (!(o instanceof Film)) 
	 *   return false;
	 * 
	 * Film f = (Film) o; 
	 * return this.getDirector().equals(f.getDirector()) &&
	 *         this.getDuration() == f.getDuration() && 
	 *         this.getGenre() == f.getGenre() &&
	 *         super.equals(o); 
	 * }
	 */
	
	//versione da Java 15 in poi va abilitata con –-enable-preview
	@Override
	public boolean equals(Object o) {
		if (o instanceof Film f){
		return this.getDirector().equals(f.getDirector()) && 
				this.getDuration() == f.getDuration() &&
				this.getGenre() == f.getGenre() &&
				super.equals(o);}
		return false;
	}

	public String toString() {
		String str = super.toString() + " regista: " + getDirector() + " durata: minuti " + getDuration() + " attori: ";
		for (int i = 0; i < this.actors.length; i++) {
			str += actors[i];
			if (i < this.actors.length - 1) {
				str += ", ";
			}
		}
		str += "\n";
		return str;
	}

}
