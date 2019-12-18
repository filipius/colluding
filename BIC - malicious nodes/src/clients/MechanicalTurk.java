package clients;

import clients.MISResistant.PoolSizeNotSupportedException;

public class MechanicalTurk extends Client {

	public MechanicalTurk(int id) {
		super(id);
	}

	@Override
	public void prepareVote() throws PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		throw new VoteOperationNotSupportedException();
	}

	@Override
	public int decideVote() throws PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		throw new VoteOperationNotSupportedException();
	}

	@Override
	public ClientTypes getType() {
		return ClientTypes.MECHANICAL_TURK;
	}

}
