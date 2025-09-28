package flightTracker.model;
import java.util.*;
import java.time.*;

public class Flight {

	private List<FlightPos> tracks;
	private String id;

	public Flight(String id, List<FlightPos> tracks){
		if (id==null || id.equals("")) throw new IllegalArgumentException("null or empty flight ID");
		if (tracks==null || tracks.isEmpty()) throw new IllegalArgumentException("null or empty list of flight positions");
		this.tracks = tracks;
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public List<FlightPos> getPositions(){
		return tracks;
	}

	public Duration getDuration(){
		int lastPosIndex  = tracks.size()-1;
		int firstPosIndex = 0;
		return Duration.between(tracks.get(firstPosIndex).getTimestamp(), tracks.get(lastPosIndex).getTimestamp());
	}
}
