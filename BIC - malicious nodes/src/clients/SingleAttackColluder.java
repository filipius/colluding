package clients;

import clients.MISResistant.PoolSizeNotSupportedException;

public class SingleAttackColluder extends ColludingMalicious {

	public SingleAttackColluder(int id, double attack_ratio) {
		super(id, attack_ratio);
	}

	@Override
	public int decideVote() throws PoolSizeNotSupportedException {
		int vote = super.decideVote();
		if (vote == VoteType.COLLUDER_BAD)
			super.setout(true);
		return vote;
	}

	@Override
	public Client duplicate(int id) throws DuplicationUnsupportedException {
		return new SingleAttackColluder(id, this.getAttack_ratio());
	}

	@Override
	public ClientTypes getType() {
		return ClientTypes.SINGLE_ATTACK_COLLUDER;
	}

}
