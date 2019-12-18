package clients;

public class CountColluders {
	private int countmalicious;
	private int countoffenders;
	
	public CountColluders() {
		this.countoffenders = 0;
		this.countmalicious = 0;
	}
	
	public void incOffenders() {
		this.countoffenders++;
	}
	
	public int getOffenders() {
		return this.countoffenders;
	}
	
	public void incCount() {
		this.countmalicious++;
	}
	
	public int getCount() {
		return this.countmalicious;
	}

	public String toString() {
		return "CountColluders. Nbr of colluders = " + this.countoffenders;
	}
}
