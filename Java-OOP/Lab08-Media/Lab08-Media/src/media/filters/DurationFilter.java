package media.filters;

import media.*;

public class DurationFilter implements Filter {
	private int duration = -1;

	public DurationFilter(int durata) {
		setDuration(durata);
	}

	public void setDuration(int durata) {
		this.duration = durata;
	}

	// versione prima di Java 15
	/*
	 * @Override public boolean filter(Media media) { 
	 * return media instanceof HasDuration && 
	 * (this.duration==0 || 
	 * ((HasDuration)media).getDuration() <= this.duration ); 
	 * }
	 */
	
	//versione dopo Java 15
	@Override
	public boolean filter(Media media) {
		if	(media instanceof HasDuration m) {
			return	(this.duration==0 || (m.getDuration() <= this.duration));
	   }
		return false;
    }
}

