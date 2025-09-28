package fondt2.tlc;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import fondt2.tlc.util.DayOfWeekHelper;

public class Rate {
	private String name;
	private Band[] bands;
	private double startCallCost;
	private int intervalInMillis;
	private String numberRoot;

	public Rate(String name, Band[] bands, int intervalInMillis, double startCallCost, String numberRoot) {
		this.name = name;
		this.bands = Arrays.copyOf(bands, bands.length);
		this.intervalInMillis = intervalInMillis;
		this.startCallCost = startCallCost;
		this.numberRoot = numberRoot;
	}

	public String getName() {
		return name;
	}

	public Band[] getBands() {
		return bands;
	}

	public boolean isApplicableTo(String destinationNumber) {
		return destinationNumber.startsWith(numberRoot);
	}

	public boolean isValid() {
		for (Band band : bands) {
			if (!band.isValid()) {
				return false;
			}
		}
		for (DayOfWeek day : DayOfWeek.values()) {
			if (!validateDay(day)) {
				return false;
			}
		}
		return true;
	}

	public double getCallCost(PhoneCall call) {
		double costPerInterval = getCostPerInterval(call.getStart());
		if (costPerInterval == -1) {
			return -1;
		}
		
		long difference = Duration.between(call.getStart(),  call.getEnd()).toMillis();
		
		// difference / intervalInMillis
		// Problema divisione tra interi, il risultato sarà un intero
		// se sono a metà intervallo DEVO arrotondare per eccesso
		
		//SOLUZIONE 1: forzare divisione tra double e poi arrotondare per eccesso
		int intervalCount = (int)Math.ceil(((double)difference) / intervalInMillis);

		
		//SOLUZIONE 2: se la divisione non da resto 0 aggiiungo un intervallo
		/*int intervalCount = (int) (difference / intervalInMillis); // Ok divisione intera
		//ma se sono a metà intervallo arrotondo per eccesso
				if((difference % intervalInMillis)!=0) 
					intervalCount = intervalCount+1;
		*/

		return startCallCost + intervalCount * costPerInterval;	
	}

	private boolean validateDay(DayOfWeek day) {
		if (day != null) {
			Band[] bandsInDay = selectBandsInDay(day);
			sortBandsByStartTime(bandsInDay);
			return validateBandsInDay(bandsInDay);
		} else {
			return false;
		}
	}

	private boolean validateBandsInDay(Band[] bandsInDay) {
		// Le bande devono essere adiacenti e coprire l'intera giornata
		// L'ordinamento serve per semplificare l'algoritmo di controllo
		for (int i = 0; i < bandsInDay.length - 1; i++) {
			LocalTime firstBandEndTime = bandsInDay[i].getEndTime();
			LocalTime secondBandStartTime = bandsInDay[i + 1].getStartTime();
			
			if (!firstBandEndTime.plusNanos(1).equals(secondBandStartTime)) {
				return false;
			}
		}
		return bandsInDay.length > 0
				&& bandsInDay[0].getStartTime().equals(LocalTime.MIN)
				&& bandsInDay[bandsInDay.length - 1].getEndTime().equals(
						LocalTime.MAX);
	}

	private Band[] selectBandsInDay(DayOfWeek day) {
		int count = 0;
		for (Band band : bands) {
			if (DayOfWeekHelper.isDayIn(day, band.getCombinedDays())) {
				count++;
			}
		}

		Band[] result = new Band[count];
		int j = 0;
		for (int i = 0; i < bands.length; i++) {
			if (DayOfWeekHelper.isDayIn(day, bands[i].getCombinedDays())) {
				result[j++] = bands[i];
			}
		}

		return result;
	}

	private void sortBandsByStartTime(Band[] bands) {
		for (int i = 0; i < bands.length; i++) {
			for (int j = i + 1; j < bands.length; j++) {
				if (bands[i].getStartTime().compareTo(bands[j].getStartTime()) > 0) {
					Band temp = bands[i];
					bands[i] = bands[j];
					bands[j] = temp;
				}
			}
		}
	}

	private double getCostPerInterval(LocalDateTime dateTime) {
		for (Band b : bands) {
			if (b.isInBand(dateTime)) {
				return b.getCostPerInterval();
			}
		}
		return -1;
	}
}
