package fondt2.tlc;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import fondt2.tlc.util.DayOfWeekHelper;

public class Band {
	private DayOfWeek[] combinedDays;
	private LocalTime startTime;
	private LocalTime endTime;
	private double costPerInterval;

	public Band(LocalTime startTime, LocalTime endTime, DayOfWeek[] days,
			double costPerInterval) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.combinedDays = Arrays.copyOf(days, days.length);
		this.costPerInterval = costPerInterval;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public DayOfWeek[] getCombinedDays() {
		return combinedDays;
	}

	public double getCostPerInterval() {
		return costPerInterval;
	}

	public boolean isInBand(LocalDateTime dateTime) {
		LocalTime timeFromDate = dateTime.toLocalTime();
		DayOfWeek dayFromDate = dateTime.getDayOfWeek();
		return DayOfWeekHelper.isDayIn(dayFromDate, combinedDays)
				&& startTime.compareTo(timeFromDate) <= 0
				&& endTime.compareTo(timeFromDate) >= 0;
	}

	public boolean isValid() {
		if (combinedDays != null && combinedDays.length > 0
				&& startTime != null && endTime != null  && costPerInterval>=0) {
			for (DayOfWeek day : combinedDays) {
				if (day == null) {
					return false;
				}
			}
			return startTime.isBefore(endTime);
		}
		return false;
	}
}
