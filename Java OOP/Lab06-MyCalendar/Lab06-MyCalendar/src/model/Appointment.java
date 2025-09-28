package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Appointment {
	private LocalDateTime from;
	private LocalDateTime to;
	private String description;
	
	public Appointment(String description, LocalDateTime from, LocalDateTime to){
		setDescription(description); 
		setFrom(from); 
		setTo(to);
	}

	public Appointment(String description, LocalDateTime from, Duration duration){
		setDescription(description); 
		setFrom(from); 
		setDuration(duration);
	}

	public void setFrom(LocalDateTime from) {
		this.from = from;
	}

	public LocalDateTime getFrom() {
		return from;
	}

	public void setTo(LocalDateTime to) {
		this.to = to;
	}

	public LocalDateTime getTo() {
		return to;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Duration getDuration() {
		return Duration.between(from, to);
	}

	public void setDuration(Duration duration) {
		to = from.plus(duration);
	}

	@Override
	public String toString() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
		return ("Impegno: " + description + " con inizio "
				+ dateFormatter.format(from) + " e fine " + dateFormatter.format(to));
	}

	public boolean equals(Appointment app) {
		return from.equals(app.getFrom()) && to.equals(app.getTo())
				&& description.equals(app.getDescription());
	}

}
