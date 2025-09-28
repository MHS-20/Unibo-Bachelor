package fondt2.tlc;

import java.util.Arrays;

public class PhonePlan {
	private String name;
	private Rate[] rates;

	public PhonePlan(String name, Rate[] rates) {
		this.name = name;
		this.rates = Arrays.copyOf(rates, rates.length);
	}

	public String getName() {
		return name;
	}
	
	public double getCallCost(PhoneCall call) {
		Rate selected = getRate(call);
		if (selected == null) {
			return -1;
		}		
		return selected.getCallCost(call);	
	}

	public boolean isValid() {
		for (Rate rate : rates) {
			if (!rate.isValid()) {
				return false;
			}
		}
		return true;
	}

	private Rate getRate(PhoneCall phoneCall) {
		String number = phoneCall.getDestNumber();
		for (Rate r : rates) {
			if (r.isApplicableTo(number)) {
				return r;
			}
		}
		return null;
	}
}
