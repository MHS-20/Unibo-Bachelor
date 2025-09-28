package model;

public class AppointmentCollection {
	private static final int DEFAULT_PHYSICAL_SIZE = 10;
	private static final int DEFAULT_GROWTH_FACTOR = 2;
	
	private Appointment[] innerContainer;
	private int size;

	public AppointmentCollection(int capacity) {
		innerContainer = new Appointment[capacity];
		size = 0;
	}
	
	public AppointmentCollection() {
		this(DEFAULT_PHYSICAL_SIZE);
	}


	public void add(Appointment appointment) {
		if (innerContainer.length == size) {
			Appointment[] newContainer = new Appointment[size * DEFAULT_GROWTH_FACTOR];
			for (int i = 0; i < innerContainer.length; ++i) {
				newContainer[i] = innerContainer[i];
			}
			innerContainer = newContainer;
		}
		innerContainer[size++] = appointment;
	}

	public int indexOf(Appointment appointment) {
		for (int i = 0; i < size; i++) {
			if (get(i).equals(appointment)) {
				return i;
			}
		}
		return -1;
	}

	public void remove(int index) {
		if (index < 0 || index >= size)
			return;
		
		for (int i = index; i < size - 1; ++i) {
			innerContainer[i] = innerContainer[i+1];
		}
		size--;
	}

	public int size() {
		return size;
	}

	public Appointment get(int index) {
		if (index < 0 || index >= size)
			return null;
	
		return innerContainer[index];
	}
}
