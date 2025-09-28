package control;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import model.Appointment;
import model.AppointmentCollection;

public class MyCalendar {
	private AppointmentCollection allAppointments;

	public MyCalendar() {
		allAppointments = new AppointmentCollection(100);
	}

	// aggiungo un nuovo appuntamento
	public void add(Appointment app) {
		allAppointments.add(app);
	}

	public boolean remove(Appointment app) {
		int position = allAppointments.indexOf(app);
		if (position >= 0) {
			allAppointments.remove(position);
			return true;
		}
		return false;
	}

	private boolean isOverlapped(LocalDateTime start, LocalDateTime end, 
			LocalDateTime refStart, LocalDateTime refEnd) {
		return start.isBefore(refEnd) && end.isAfter(refStart);
	}

	private AppointmentCollection getAppointmentsIn(LocalDateTime start, LocalDateTime end) {
		AppointmentCollection appList = new AppointmentCollection(
				allAppointments.size());
		for (int i = 0; i < allAppointments.size(); i++) {
			Appointment app = allAppointments.get(i);
			if (isOverlapped(app.getFrom(), app.getTo(), start, end)) {
				appList.add(app);
			}
		}
		return appList;
	}

	public AppointmentCollection getDayAppointments(LocalDate date) {
		LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.of(0, 0));
		LocalDateTime dayEnd = dayStart.plusDays(1);
		return getAppointmentsIn(dayStart, dayEnd);
	}

	public AppointmentCollection getMonthAppointments(LocalDate date) {
		LocalDate firstDayOfMonth = date.withDayOfMonth(1);
		LocalDateTime monthStart = LocalDateTime.of(firstDayOfMonth, LocalTime.of(0, 0));
		LocalDateTime monthEnd = monthStart.plusMonths(1);
		return getAppointmentsIn(monthStart, monthEnd);
	}

	public AppointmentCollection getWeekAppointments(LocalDate date) {
		long distanceFromPastMonday = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
		LocalDate weekStartDate = date.minusDays(distanceFromPastMonday);
		LocalDateTime weekStart = LocalDateTime.of(weekStartDate, LocalTime.of(0, 0));
		LocalDateTime weekEnd = weekStart.plusWeeks(1);

		return getAppointmentsIn(weekStart, weekEnd);
	}

	public AppointmentCollection getAllAppointments() {
		AppointmentCollection appCollection = new AppointmentCollection(
				allAppointments.size());
		for (int i = 0; i < allAppointments.size(); i++) {
			appCollection.add(allAppointments.get(i));
		}
		
		
		return appCollection;
	}

}
