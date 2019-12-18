package clients;

import clients.MISResistant.PoolSizeNotSupportedException;
import util.MersenneRandom;

public class ColludingMalicious extends Client {
	protected boolean honest;
	private double attack_ratio;

	public ColludingMalicious(int id) {
		super(id);
		this.attack_ratio = 1;
	}

	public ColludingMalicious(int id, double attack_ratio) {
		super(id);
		this.attack_ratio = attack_ratio;
	}

	@Override
	public void prepareVote() throws PoolSizeNotSupportedException {
		CountColluders c = (CountColluders) super.thepool.getPlaceholder();
		if (c == null) {
			c = new CountColluders();
			super.thepool.setPlaceholder(c);
		}
		c.incCount();
		if (MersenneRandom.nextDouble() < this.attack_ratio) {
			c.incOffenders();
			this.honest = false;
		}
		else
			this.honest = true;
	}

	@Override
	public int decideVote() throws PoolSizeNotSupportedException {
		CountColluders c = (CountColluders) super.thepool.getPlaceholder();
		//System.out.println("Colluder. c = " + c);
		if (c.getCount() == super.thepool.getPoolSize() || !this.honest && c != null && c.getOffenders() > super.thepool.getPoolSize() / 2)
			return VoteType.COLLUDER_BAD;
		else
			return VoteType.OK;
	}

	public double getAttack_ratio() {
		return attack_ratio;
	}

	public void setAttack_ratio(double attack_ratio) {
		this.attack_ratio = attack_ratio;
	}
	
	@Override
	public Client duplicate(int id) throws DuplicationUnsupportedException {
		ColludingMalicious cm = new ColludingMalicious(id, this.attack_ratio);
		cm.setAvg_life(this.getAvg_life());
		return cm;
	}

	@Override
	public ClientTypes getType() {
		return ClientTypes.COLLUDING_MALICIOUS;
	}

	
	
}
