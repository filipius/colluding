package clients;


public class NormalClient extends Client {

	public NormalClient(int id) {
		super(id);
	}

	@Override
	public int decideVote() {
		return VoteType.OK;
	}

	@Override
	public void prepareVote() {}

	
	@Override
	public Client duplicate(int id) throws DuplicationUnsupportedException {
		NormalClient nc = new NormalClient(id);
		nc.setAvg_life(this.getAvg_life());
		return nc;
	}

	@Override
	public ClientTypes getType() {
		return ClientTypes.NORMAL_CLIENT;
	}
	
}
