package clients.MISResistant;

import clients.ClientTypes;
import clients.ColludingMalicious;
import clients.VoteType;

public class MISResistantColluder extends ColludingMalicious {
	//HashSet<Integer> mypeers; // other colluders that voted in collusion with me
	AttackGroups agroups;

	public MISResistantColluder(int id) {
		super(id);
		//this.mypeers = new HashSet<Integer>();
		this.agroups = AttackGroups.getInstance();
		// id == 0? Means new set of clients. Clear the Colluder Information
		if (id == 0)
			this.agroups.clear();
	}


	@Override
	public void prepareVote() throws PoolSizeNotSupportedException {
		if (super.thepool.getPoolSize() != 3)
			throw new PoolSizeNotSupportedException();
		SupportColluders c = (SupportColluders) super.thepool.getPlaceholder();
		if (c == null) {
			c = new SupportColluders();
			super.thepool.setPlaceholder(c);
		}
		c.addMeAsOffender(this.id);
	}

	@Override
	public int decideVote() throws PoolSizeNotSupportedException {
		//Client [] peers = super.thepool.getClients();
		SupportColluders ec = (SupportColluders) super.thepool.getPlaceholder();
		if (ec.getOffenders() < super.thepool.getPoolSize()) {
			if (ec.getOffenders() < 2 || !ec.canAttack())
				return VoteType.OK; // cannot attack...
			/*
			for (Client c : peers) {
				int peerid = c.getId();
				if (peerid != this.id) {
					if (this.mypeers.contains(peerid))
						return VoteType.OK;
					else
						if (ec.isOfender(peerid))
							this.mypeers.add(peerid);
				}
			}
			*/
		}
		return VoteType.COLLUDER_BAD;
	}

	@Override
	public ClientTypes getType() {
		return ClientTypes.MISRESISTANT_COLLUDER;
	}

}
