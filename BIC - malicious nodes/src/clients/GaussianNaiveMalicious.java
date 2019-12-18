package clients;


import util.MersenneRandom;


public class GaussianNaiveMalicious extends NaiveMalicious {

	public GaussianNaiveMalicious(int id, double gnm_wrong_avg, double gnm_wrong_stdev) {
		super(id);
		//Random random = new Random();
		//double rand = random.nextGaussian();
		double rand = MersenneRandom.nextGaussianDouble();
		double attack_ratio = gnm_wrong_stdev * rand + gnm_wrong_avg;
		if (attack_ratio < 0)
			attack_ratio = 0;
		else
			if (attack_ratio > 1)
				attack_ratio = 1;
		//System.out.println("attack ratio: " + attack_ratio);
		super.setAttack_ratio(attack_ratio);
	}

	/*
	@Override
	public int decideVote() {
		double rand = this.random.nextGaussian();  // this is avg 0 and stdev 1
		return this.stddev * rand + this.avgattackrate < threshold ? VoteType.NAIVE_RANDOM : VoteType.OK;
	}
	*/

	@Override
	public ClientTypes getType() {
		return ClientTypes.GAUSSIAN_NAIVE_MALICIOUS;
	}

}
