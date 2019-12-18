package clients;

import pools.Pool;
import clients.MISResistant.PoolSizeNotSupportedException;
import util.MersenneRandom;

/**
 * 
 * @author Jorge Farinha jfarATstudent.dei.uc.pt
 * 09/2008
 *
 */
public abstract class Client{
	/*
	public final static int NORMAL=0;
	public final static int NAIVE_MALICIOUS=1;
	public final static int COLLUDING_MALICIOUS=2;
	public final static int MIXED_MALICIOUS=3;
	public final static int UNRELIABLE_COLLUDING_MALICIOUS=3;
	public final static String names[] = {"NORMAL", "NAIVE", "COLLUDING", "MIXED", "UNR_COLLUDING"};
	public final static String initials[] = {"R", "N", "C", "M", "U"};
	*/

	protected boolean die = false;
	private double avg_life;  // this uses a geometric distrbiution. Not used by default
	protected int id;
	protected int participations;
	//protected int type;
	protected double score;
	protected double defeats;
	protected double weighteddefeats;
	// each naive casts a different vote in the set [startwrongvote, endwrongvote]
	// repetitions can occur accross different pools
	static private final int startwrongvote = -2000, endwrongvote = -1000;
	static private int wrongvote = startwrongvote;
	//stores the sizes of the groups where the node participated
	//e.g., [1] stores the number of times the node ran alone [2] the 
	// number it went together with another node, etc.
	protected int [] particpationsinpools;
	protected Pool thepool;
	
	public Client(int id) {
		this.id = id;
		this.participations = 0;
		this.particpationsinpools = null;
		this.score = 0;
		this.defeats = 0;
		this.weighteddefeats = 0;
		this.avg_life = 0;
	}

	
	/*
	 * XXX: this should rather be in NaiveMalicious, but two different classes need this
	 * and the second (MixedMalicious) cannot descend from NaiveMalicious
	 */
	protected static int getNextWrongVote() {
		int ret = wrongvote++;
		if (wrongvote > endwrongvote)
			wrongvote = startwrongvote;
		return ret;
	}


	public Pool getThepool() {
		return thepool;
	}

	public void setThepool(Pool thepool) {
		this.thepool = thepool;
	}

	/*
	public Client(int id, int type){
		this.id=id;
		this.type=type;
		this.score=0;
	}
	
	public Client (Client c){
		this.id=c.getId();
		this.score=c.getScore();
		this.type=c.getType();
	}
	*/
	
	public final String toString() 
	{
		return "Client: "+id+" "+ ClientTypes.getSuffix(this.getType());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public abstract ClientTypes getType();

	public static int getTypesofClients() {
		return ClientTypes.values().length;
	}
	
	public final String typeString() {
		return ClientTypes.getName(this.getType());
	}
	
	/*
	{
		return Client.names[this.type];
	}
	*/
	
	/*
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	*/

	public double getScore() {
		return score;
	}

	public void setScore(double votes) {
		this.score = votes;
	}
	
	public double addScore(double score){
			//this.score=(this.score+(1-winnersAverage))/2;
			this.score+=score;
		return this.score;
	}
	
	public double getDefeats() {
		return defeats;
	}

	public void setDefeats(double d) {
		this.defeats = d;
	}

	public void incDefeats() {
		this.defeats++;
	}

	public int getParticipations() {
		return participations;
	}

	public void setParticipations(int participations) {
		this.participations = participations;
	}

	public void incParticipations() {
		this.participations += 1;
	}
	
	public abstract void prepareVote() throws PoolSizeNotSupportedException, VoteOperationNotSupportedException;
	
	public abstract int decideVote() throws PoolSizeNotSupportedException, VoteOperationNotSupportedException;
	
	public void addWeightedScore(double val) {
		this.weighteddefeats += val;
	}
	
	public double getWeightedScore() {
		return this.weighteddefeats;
	}
	
	public void setWeightedScore(double val) {
		this.weighteddefeats = val;
	}
	
	public int getVote() throws PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		this.participations += 1;
		return this.decideVote();
	}
	
	public void setParticipationInGroups(int size) {
		if (this.particpationsinpools == null)
			this.particpationsinpools = new int[size];
		else
			if (size > this.particpationsinpools.length) {
				int [] newarray = new int[size];
				System.arraycopy(this.particpationsinpools, 0, newarray, 0, this.particpationsinpools.length);
				this.particpationsinpools = newarray;
			}
		this.particpationsinpools[size - 1]++;
	}

	public int getVoteGroupsize() {
		int randnbr = (int) (MersenneRandom.nextDouble() * this.participations + 1);
		int sum = 0;
		int i = 1;
		for (; i <= this.particpationsinpools.length; i++) {
			sum += this.particpationsinpools[i - 1];
			if (sum >= randnbr)
				break;
		}
		if (i > 3) {
			//System.err.println("problems at client" + this.id);
			i = 3;
		}
		//System.out.println("group size: " + i);
		return i;
	}

	public double getAvg_life() {
		return avg_life;
	}

	public void setAvg_life(double avg_life) {
		this.avg_life = avg_life;
	}


	public void setout(boolean state) {
		this.die = state;
	}
	
	public boolean isout() {
		if (!this.die && this.avg_life != 0) {
			double rand = MersenneRandom.nextDouble();
			this.die = rand < (1 / this.avg_life);
		}
		return this.die;
	}

	
	public Client duplicate(int i) throws DuplicationUnsupportedException {
		throw new DuplicationUnsupportedException();
	}

}
