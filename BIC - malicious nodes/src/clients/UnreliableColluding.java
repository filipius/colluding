package clients;

import clients.MISResistant.PoolSizeNotSupportedException;
import util.MersenneRandom;

public class UnreliableColluding extends ColludingMalicious {
	// the ratio of loyalty of the colluder, i.e., it 
	// percentage of times it promises to attack, and 
	// actually does so
	private double loyalty;

	public UnreliableColluding(int id, double colluding_attack, double doesntdefect) {
		super(id, colluding_attack);
		this.loyalty = doesntdefect;
	}

	@Override
	public int decideVote() throws PoolSizeNotSupportedException {
		int vote = super.decideVote();
		if (vote == VoteType.COLLUDER_BAD && MersenneRandom.nextDouble() > this.loyalty)
			vote = VoteType.OK;
		return vote;
	}

	@Override
	public ClientTypes getType() {
		return ClientTypes.UNRELIABLE_COLLUDER;
	}

}
