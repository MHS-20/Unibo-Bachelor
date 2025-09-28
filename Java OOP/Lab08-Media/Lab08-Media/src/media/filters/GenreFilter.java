package media.filters;

import media.*;

public class GenreFilter implements Filter {
	private String genre = null;

	public GenreFilter(String genre) {
		setGenre(genre);
	}

	public void setGenre(String genre) {
		this.genre = genre.trim();
	}

     //versione prima di Java 15
	/*
	 * @Override public boolean filter(Media media) { return media instanceof
	 * HasGenre && (this.genre.isEmpty() ||
	 * ((HasGenre)media).getGenre().equals(this.genre) ); }
	 */
	
	// versione da Java 15 in poi
	@Override
	public boolean filter(Media media) {
		 if (media instanceof HasGenre hg) 
		 {	 
		 return this.genre.isEmpty() || (hg.getGenre().equals(this.genre));
		 }
		 return false;
	}
}
