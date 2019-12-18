package matrix;

public class AgainstVoteTie implements Comparable<T> {
	private int node1, node2;
	private double vote;
	
	public AgainstVoteTie(int i, int j, double good, double bad) {
		this.node1 = i;
		this.node2 = j;
		this.vote = bad / (good + bad);
	}

	public int getNode1() {
		return node1;
	}
	
	public void setNode1(int node1) {
		this.node1 = node1;
	}
	
	public int getNode2() {
		return node2;
	}
	
	public void setNode2(int node2) {
		this.node2 = node2;
	}
	
	public double getVote() {
		return vote;
	}
	
	public void setVote(double vote) {
		this.vote = vote;
	}

}
