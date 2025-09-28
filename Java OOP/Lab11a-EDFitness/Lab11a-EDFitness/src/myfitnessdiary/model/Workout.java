package myfitnessdiary.model;

import java.time.LocalDate;

public class Workout {

	private LocalDate date;
	private int duration;
	private Intensity intensity;
	private Activity activity;

	public Workout(LocalDate date, int duration, Intensity intensity, Activity activity) {
		if (date == null)
			throw new IllegalArgumentException("data errata");
		if (duration <= 0)
			throw new IllegalArgumentException("durata errata");
		if (intensity == null)
			throw new IllegalArgumentException("intensità  errata");
		if (activity == null)
			throw new IllegalArgumentException("attività  errata");

		this.date = date;
		this.duration = duration;
		this.intensity = intensity;
		this.activity = activity;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public int getDuration() {
		return this.duration;
	}

	public Intensity getIntensity() {
		return this.intensity;
	}

	public Activity getActivity() {
		return this.activity;

	}

	public int getBurnedCalories() {
		return duration * activity.getCalories(getIntensity());

	}

}
