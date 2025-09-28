package flightTracker.persistence;
import java.io.*;
import java.util.*;

import flightTracker.model.Flight;
import flightTracker.model.FlightPos;

import java.time.*;
import java.time.format.DateTimeParseException;

public class MyFlightReader implements FlightReader {

	/* Formato del file:
	UTC;Position;Altitude;Speed;Direction
	2019-05-12T00:22:38Z;40.640606,-73.79158;0;0;180
	2019-05-12T00:28:53Z;40.640301,-73.791969;0;15;225
	...	*/

	@Override
	public Flight readFlight(String id, BufferedReader reader) throws IOException, BadFileFormatException {
		ZonedDateTime timestamp;
		double latitude, longitude, altitude, speed;
		int direction;

		List<FlightPos> list = new ArrayList<>();
		String header = reader.readLine();
		validate(header);
		String line;
		while((line  =reader.readLine()) != null){
			String[] parti = line.split(";");
			try	{
				timestamp = ZonedDateTime.parse(parti[0].trim()); 
			} catch (DateTimeParseException e0) {
				throw new BadFileFormatException("bad timestamp in first column in line " + line);
			}
			try	{
				String[] latlong = parti[1].trim().split(",");
				latitude = Double.parseDouble(latlong[0].trim());
				longitude = Double.parseDouble(latlong[1].trim());
			} catch (NumberFormatException e1) {
				throw new BadFileFormatException("bad latitude or longitude in line " + line + ": " + e1);
			}
			try	{
				altitude = Double.parseDouble(parti[2].trim());
				speed = Double.parseDouble(parti[3].trim());
				direction = Integer.parseInt(parti[4].trim());
			} catch (NumberFormatException e2) {
				throw new BadFileFormatException("bad altitude, speed or direction in line " + line + ": " + e2);
			}
			list.add(new FlightPos(timestamp, latitude, longitude, altitude, speed, direction));
		}
		return new Flight(id, list);
	}

	private static void validate(String header) throws BadFileFormatException {
		String[] correctHeader = {"UTC","Position","Altitude","Speed","Direction"};
		String[] parti = header.split(";");
		if (parti.length<correctHeader.length) throw new BadFileFormatException("Wrong header line, missing one or more headers");
		if (parti.length>correctHeader.length) throw new BadFileFormatException("Wrong header line, extra headers in line");
		for(int i=0;i<parti.length; i++) {
			if(!parti[i].equals(correctHeader[i])) 
				throw new BadFileFormatException("Wrong header line, expected " + correctHeader[i] + ", found " + parti[i]);
		}
	}

}
