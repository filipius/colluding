package clients;

import util.MersenneRandom;

public class NaiveMalicious extends Client {
	// each naive casts a different vote in the set [startwrongvote, endwrongvote]
	// repetitions can occur accross different pools
	private double attack_ratio;
	
	public NaiveMalicious(int id) {
		super(id);
	}

	public double getAttack_ratio() {
		return attack_ratio;
	}

	public void setAttack_ratio(double attack_ratio) {
		this.attack_ratio = attack_ratio;
	}

	public NaiveMalicious(int id, double attack_ratio) {
		super(id);
		this.attack_ratio = attack_ratio;
	}

	@Override
	public int decideVote() {
		return MersenneRandom.nextDouble() < this.attack_ratio ? super.getNextWrongVote() : VoteType.OK;
	}

	@Override
	public void prepareVote() {}
	
	@Override
	public Client duplicate(int id) throws DuplicationUnsupportedException {
		NaiveMalicious nm = new NaiveMalicious(id);
		nm.setAttack_ratio(this.attack_ratio);
		nm.setAvg_life(this.getAvg_life());
		return nm;
	}

	@Override
	public ClientTypes getType() {
		return ClientTypes.NAIVE_MALICIOUS;
	}
	
}
