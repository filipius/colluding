package clients;

import clients.MISResistant.PoolSizeNotSupportedException;
import util.MersenneRandom;

public class MixedMalicious extends ColludingMalicious {
	private boolean naive;
	private double naive_ratio;
	private double naive_attack;
	
	public MixedMalicious(int id, double naive_ratio, double naive_attack, double colluding_attack) {
		super(id, colluding_attack);
		this.naive_ratio = naive_ratio;
		this.naive_attack = naive_attack;
	}

	@Override
	public void prepareVote() throws PoolSizeNotSupportedException {
		if (MersenneRandom.nextDouble() < naive_ratio)
			this.naive = true;
		else {
			this.naive = false;
			super.prepareVote();
		}
	}

	@Override
	public int decideVote() throws PoolSizeNotSupportedException {
		if (this.naive)
			return MersenneRandom.nextDouble() < this.naive_attack ? super.getNextWrongVote() : VoteType.OK;
		else
			return super.decideVote();
	}
	
	@Override
	public ClientTypes getType() {
		return ClientTypes.MIXED_MALICIOUS;
	}

}
