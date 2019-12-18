package matrix;

public class MatrixElement {
	private double good;
	private double bad;
	
	public void addGood() {
		this.good++;
	}
	
	public void addBad() {
		this.bad++;
	}
	
	public double getGood() {
		return good;
	}
	
	public void setGood(double good) {
		this.good = good;
	}
	
	public double getBad() {
		return bad;
	}
	
	public void setBad(double bad) {
		this.bad = bad;
	}

	
	
}
