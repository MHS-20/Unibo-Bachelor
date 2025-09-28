package myfitnessdiary.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import myfitnessdiary.model.FitnessDiary;
import myfitnessdiary.model.Workout;
import myfitnessdiary.persistence.ActivityRepository;
import myfitnessdiary.persistence.ReportWriter;

public class MyController extends Controller {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.ITALY);
	
	public MyController(FitnessDiary diary, ActivityRepository repository, ReportWriter reportWriter) {
		super(diary, repository, reportWriter);
	}

	public String getDayWorkout(LocalDate date) {
		StringBuilder str = new StringBuilder();
		str.append("Allenamento di ");
		str.append(date.format(formatter));
		str.append("\n");
		
		for (Workout wo : getDayWorkouts(date)) {
			str.append(wo.getActivity().getName() + " minuti: " + wo.getDuration() + "  calorie bruciate: "
					+ wo.getBurnedCalories() + "\n");
		}
		str.append("\nMinuti totali allenamento: " + getFitnessDiary().getDayWorkoutMinutes(date) + " \nCalorie totali bruciate: "
				+ getFitnessDiary().getDayWorkoutCalories(date) + "\n");
		return str.toString();
	}

}
