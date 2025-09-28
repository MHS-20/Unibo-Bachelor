package media;

import media.filters.HasDuration;
import media.filters.HasGenre;

public class Song extends Media implements HasGenre, HasDuration {
	private String singer = null;
	private int duration = -1;
	private String genre;

	public Song(String titolo, int anno, String singer, int duration, String genre) {
		super(anno, titolo);
		this.singer = singer;
		this.duration = duration;
		this.genre = genre;
	}

	public Type getType() {
		return Type.SONG;
	}

	public String getSinger() {
		return this.singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
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

	/*
	 * //versione prima di Java 15 
	 * 
	 * @Override public boolean equals(Object o) { if (!(o instanceof Song)) return
	 * false;
	 * 
	 * Song c = (Song) o; return getSinger().equals(c.getSinger()) && getDuration()
	 * == c.getDuration() && getGenre() == c.getGenre() && super.equals(o); }
	 */
	
	//versione da Java 15 in poi va abilitata con –-enable-preview
	@Override
	public boolean equals(Object o) {
		if (o instanceof Song c)
		{	
		return getSinger().equals(c.getSinger()) && 
				getDuration() == c.getDuration() &&
				getGenre() == c.getGenre() &&
				super.equals(o);
		}
		return false;
	}

	public String toString() {
		return super.toString() + " cantante: " + getSinger() + " durata: minuti " + getDuration() + "\n";
	}

}
