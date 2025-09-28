package myfitnessdiary.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;

public class MyFitnessDiary implements FitnessDiary {
	private List<Workout> workouts;

	public MyFitnessDiary() {
		workouts = new ArrayList<Workout>();
	}

	public void addWorkout(Workout wo) {
		workouts.add(wo);
	}

	/*
	 * private List<Workout> filter(Predicate<Workout> predicate) { List<Workout>
	 * filtered = workouts.stream().filter(predicate).collect(Collectors.toList());
	 * return filtered; }
	 */
	
 
	
	public List<Workout> getWeekWorkouts(LocalDate date) {
		long distanceFromPastMonday = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
		LocalDate weekStartDate = date.minusDays(distanceFromPastMonday);
		LocalDate weekEndDate = weekStartDate.plusWeeks(1).minusDays(1);
		//return filter(p -> p.getDate().compareTo(weekStartDate) >= 0 && p.getDate().compareTo(weekEndDate) <= 0);
		List<Workout> filtered = new ArrayList<Workout>();
		for (Workout w: workouts){
			if((w.getDate().compareTo(weekStartDate)>=0) && (w.getDate().compareTo(weekEndDate)<=0)) {
				filtered.add(w);
			}
		}
		return filtered;	
	
	}

	public List<Workout> getDayWorkouts(LocalDate date) {
		//return filter(p -> p.getDate().equals(date));
		List<Workout> filtered = new ArrayList<Workout>();
		for (Workout w: workouts){
			if(w.getDate().equals(date)) {
				filtered.add(w);
			}
		}
		return filtered;
	}


	public int getDayWorkoutMinutes(LocalDate date) {
		List<Workout> woList = getDayWorkouts(date);
		return calcMinutes(woList);
	}

	
	public int getWeekWorkoutMinutes(LocalDate date) {
		List<Workout> woList = getWeekWorkouts(date);
		return calcMinutes(woList);
	}

	
	public int getDayWorkoutCalories(LocalDate date) {
		List<Workout> woList = getDayWorkouts(date);
		return calcCalories(woList);
	}


	public int getWeekWorkoutCalories(LocalDate date) {
		List<Workout> woList = getWeekWorkouts(date);
		return calcCalories(woList);
	}

	private int calcMinutes(List<Workout> woList) {
		int totaleMinuti = 0;
		for (Workout wo : woList) {
			totaleMinuti += wo.getDuration();
		}
		return totaleMinuti;
	}

	private int calcCalories(List<Workout> woList) {
		int totaleCalorie = 0;
		for (Workout wo : woList) {
			totaleCalorie += wo.getBurnedCalories();
		}
		return totaleCalorie;
	}

}
