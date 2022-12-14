package flightTracker.ui.controller;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import flightTracker.model.Flight;
import flightTracker.model.FlightPos;
import flightTracker.model.Point;
import flightTracker.persistence.BadFileFormatException;
import flightTracker.persistence.FlightReader;




public class MyController extends Controller {
	
	public MyController( String[] availableFlightFiles) {
		super(availableFlightFiles);
	}

	@Override
	public List<Point> getPoints(Flight flight) {
		return flight.getPositions().stream().map(FlightPos::getPosition).collect(Collectors.toList());
	}
	
	@Override
	public Flight load(String flightId, Reader reader) throws IOException, BadFileFormatException {
		BufferedReader rdr;
		rdr = new BufferedReader(reader);
		return FlightReader.of().readFlight(flightId, rdr);
	}
	
}
