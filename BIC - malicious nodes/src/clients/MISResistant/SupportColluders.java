package clients.MISResistant;

import clients.CountColluders;

public class SupportColluders extends CountColluders {
	private static int attackingsize = 3;
	int [] offendersids;
	int pos;
	boolean canattack, decisiontaken;
	
	public SupportColluders() {
		this.offendersids = new int[attackingsize];
		this.pos = 0;
		this.decisiontaken = false;
	}
	
	public int getOffenders() {
		return this.pos;
	}
	
	public void addMeAsOffender(int id) throws PoolSizeNotSupportedException {
		if (this.pos > 2)
			throw new PoolSizeNotSupportedException();
		this.offendersids[this.pos++] = id;
	}
	
	public boolean isOfender(int id) {
		return this.pos > 0 && offendersids[0] == id || this.pos > 1 && offendersids[1] == id
			|| this.pos > 2 && offendersids[2] == id;
	}
	
	public boolean canAttack() throws PoolSizeNotSupportedException {
		if (!this.decisiontaken) {
			this.decisiontaken = true;
			this.canattack = AttackGroups.getInstance().canAttack(offendersids);
			if (this.canattack)
				AttackGroups.getInstance().join(offendersids, this.pos);
		}
		return this.canattack;

	}
}
