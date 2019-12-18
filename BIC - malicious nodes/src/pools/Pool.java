package pools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import clients.Client;
import clients.ColludingMalicious;
import clients.MechanicalTurk;
import clients.NormalClient;
import clients.VoteOperationNotSupportedException;
import clients.VoteType;
import clients.MISResistant.PoolSizeNotSupportedException;




/**
 * 
 * @author Jorge Farinha jfarATstudent.dei.uc.pt
 * 09/2008 
 *
 */


public class Pool {
	protected static int alldifferent, allequal, twoone;
	protected List<Client> clients;
	protected List<Integer> results;
	protected int finalResult;
	//protected int addedClients = 0;
	/*
	protected double cmWrong;
	protected double nmWrong;
	protected double mmNaiveRatio;
	 */
	//private int numClientsAgreePool = 0;
	protected int id;
	/*
	 * This enables clients to manipulate their own vote
	 */
	protected Object placeholder;

	//public Pool(int id, int nClients, double nmWrong, double cmWrong, double mmNaiveRatio) {
	public Pool(int id) {
		this.clients = new ArrayList<>();//Client[nClients];
		this.results = new ArrayList<>();
		/*
		this.nmWrong = nmWrong;
		this.cmWrong = cmWrong;
		this.mmNaiveRatio=mmNaiveRatio;
		 */
		this.id=id;
		this.placeholder = null;
	}


	public int add(Client client) {
		if (!this.clients.contains(client)) {
			this.clients.add(client);
			client.setThepool(this);
		}
		return this.clients.size();
	}

	public void add(int vote) {
		this.results.add(vote);
	}

	/**
	 * os clientes decidem qual o resultado que hão-de devolver
	 * @throws PoolSizeNotSupportedException 
	 * @throws VoteOperationNotSupportedException 
	 */
	public void vote() throws PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		for (Client cl : this.clients)
			cl.prepareVote();
		for (Client cl : this.clients)
			results.add(cl.getVote());
		
		decide_vote();
	}


	public void decide_vote() {
		HashMap<Integer,Integer> votes = new HashMap<Integer,Integer>();
		for (int i = 0; i < this.clients.size(); i++) {
			int vote = this.results.get(i);
			int nbr;
			Integer nbrvotes = votes.get(vote);
			if (nbrvotes == null)
				nbrvotes = 0;
			nbr = nbrvotes + 1;
			votes.put(vote, nbr);
		}
		int max = 0;
		int whichvote = 0;
		Iterator<Integer> it = votes.keySet().iterator();
		while (it.hasNext()) {
			int vote = it.next();
			int nbrvotes = votes.get(vote);
			if (nbrvotes > max) {
				max = nbrvotes;
				whichvote = vote;
			}
		}

		if (max > this.clients.size() / 2)// && whichvote != VoteType.NAIVE_RANDOM)
			this.finalResult = whichvote;
		else
			this.finalResult = VoteType.INVALID;
	}

	public void infervote() {
		throw new UnsupportedOperationException();

		/*
		if (this.clients.size() != 3)
			throw new UnsupportedOperationException();

		int gsize1 = clients.get(0).getVoteGroupsize();
		int gsize2 = clients.get(1).getVoteGroupsize();
		int gsize3 = clients.get(2).getVoteGroupsize();

		for (Client cl : this.clients)
			cl.incParticipations();

		results.set(0,  -1);
		results.set(1,  -1);
		results.set(2,  -1);

		// are all votes different?
		if (gsize1 == 1 && gsize2 == 1 || gsize1 == 1 && gsize3 == 1 || gsize2 == 1 && gsize3 == 1) {
			results.set(0,  1);
			results.set(1,  2);
			results.set(2,  3);
			alldifferent++;
			this.setFinalResult();
			return;
		}

		// are all alike?
		if (gsize1 == 3 && gsize2 == 3 && gsize3 > 1 || gsize1 == 3 && gsize3 == 3 && gsize2 > 1 || gsize2 == 3 && gsize3 == 3 && gsize1 > 1
				|| gsize1 == 2 && gsize2 == 2 && gsize3 == 2) {
			results.set(0,  1);
			results.set(1,  1);
			results.set(2,  1);
			allequal++;
			this.setFinalResult();
			return;
		}

		twoone++;
		// now, do we have groups of 2? Must run after the previous check.
		if ((gsize1 == 1 || gsize1 == 3) && gsize2 >= 2 && gsize3 >= 2) {
			results.set(0,  1);
			results.set(1,  2);
			results.set(2,  2);
			this.setFinalResult();
			return;
		}

		if ((gsize2 == 1 || gsize2 == 3) && gsize1 >= 2 && gsize3 >= 2) {
			results.set(0, 2);
			results.set(1,  1);
			results.set(2,  2);
			this.setFinalResult();
			return;
		}

		if ((gsize3 == 1 || gsize3 == 3) && gsize1 >= 2 && gsize2 >= 2) {
			results.set(0,  2);
			results.set(1,  2);
			results.set(2,  1);
			this.setFinalResult();
			return;
		}

		if (results.get(0) < 0 || results.get(1) < 0 || results.get(2) < 0) {
			System.err.println("Wrong situation in Pool.infervote():");
			System.err.println("gsize1 = " + gsize1 + " gsize2 = " + gsize2 + " gsize3 = " + gsize3);
			System.exit(1);
		}

		 */
	}

	/*
	public void infervote() {
		HashMap<Integer, ArrayList<Integer>> groups = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> group;

		for (int i = 0; i < addedClients; i++) {
			int votegsize = clients[i].getVoteGroupsize();
			group = groups.get(votegsize);
			if (group == null) {
				group = new ArrayList<Integer>();
				groups.put(votegsize, group);
			}
			group.add(i);
		}
		int vote = 0;
		for (int size = 1; size < addedClients; size++) {
			group = groups.get(size);
			Iterator<Integer> it = group.iterator();
			int count = 0;
			while (it.hasNext()) {

			}
		}
	}
	 */

	/*
	public void vote(Matrix m) {
		for (int i = 0; i < addedClients; i++) {
			int otherClients[] = new int[addedClients - 1];
			int pos = 0;
			for (int j = 0; j < addedClients; j++)
				if (i != j)
					otherClients[pos++] = this.clients[j].getId();
			results[i] = m.getVote(clients[i].getId(), otherClients);
		}
	}
	 */

	public int getVotesAgainst(int pos) {
		int total = 0;
		for (int i = 0; i < this.clients.size(); i++)
			if (this.results.get(i) != this.results.get(pos))
				total++;
		return total;
	}

	public int getVotesTogether(int pos) {
		int total = 1;
		for (int i = 0; i < this.clients.size(); i++)
			if (i != pos && this.results.get(i) == this.results.get(pos))
				total++;
		return total;
	}

	public boolean votedTogether(int pos1, int pos2) {
		return this.results.get(pos1) == this.results.get(pos2);
	}

	public double getWeightedVotesAgainst(int pos) {
		double total = 0;
		for (int i = 0; i < this.clients.size(); i++)
			if (this.results.get(i) != this.results.get(pos)) {
				total += (this.clients.get(i).getScore() / this.clients.get(i).getParticipations());
			}
		return total;
	}


	public boolean isDefeated(int pos) {
		return (this.finalResult != VoteType.INVALID && this.results.get(pos) != this.finalResult);
	}


	public int getPoolSize() {
		return this.clients.size();
	}

	public boolean allAgree() {
		int val=results.get(0);

		for(int i=1;i<this.clients.size();i++){
			if (val!=results.get(i))
				return false;
		}
		return true;		
	}

	public String toString() {
		String s = "Pool (" + id + "):\t";
		for (int i = 0; i < this.clients.size(); i++) {
			s += clients.get(i).typeString() + "\t" + clients.get(i).getId() + "\t";
		}

		for (int i = 0; i < this.clients.size(); i++) {
			s += VoteType.name(results.get(i)) + "\t";
		}
		s+="\t-\t"+ VoteType.name(finalResult);
		return s;
	}


	public int getVote(int pos) {
		return this.results.get(pos);
	}

	public Object getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(Object placeholder) {
		this.placeholder = placeholder;
	}


	public static int getAlldifferent() {
		return alldifferent;
	}


	public static void setAlldifferent(int alldifferent) {
		Pool.alldifferent = alldifferent;
	}


	public static int getAllequal() {
		return allequal;
	}


	public static void setAllequal(int allequal) {
		Pool.allequal = allequal;
	}


	public static int getTwoone() {
		return twoone;
	}


	public static void setTwoone(int twoone) {
		Pool.twoone = twoone;
	}


	public Client getClient(int j) {
		return this.clients.get(j);
	}

	public static void main(String[] args) throws PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		Pool p = new Pool(0);
		p.add(new ColludingMalicious(0));
		p.add(new ColludingMalicious(1));
		p.add(new NormalClient(2));
		p.vote();
		if (p.getVote(0) != p.getVote(1) || p.getVote(0) == p.getVote(2) || 
				!p.isDefeated(2) ||
				!p.votedTogether(0, 1) ||
				p.getVotesAgainst(0) != 1 || p.getVotesAgainst(1) != 1 || p.getVotesAgainst(2) != 2 ||
				p.getVotesTogether(0) != 2 || p.getVotesTogether(1) != 2 || p.getVotesTogether(2) != 1)
			System.err.println("Something wrong in the votes");

		p = new Pool(0);
		p.add(new MechanicalTurk(0));
		p.add(new MechanicalTurk(1));
		p.add(new MechanicalTurk(2));
		p.add(new MechanicalTurk(3));
		p.add(new MechanicalTurk(4));
		p.add(2);
		p.add(3);
		p.add(2);
		p.add(3);
		p.add(2);
		if (p.getVote(0) == p.getVote(1) || p.getVote(0) != p.getVote(2) || p.getVote(0) == p.getVote(3) || p.getVote(0) != p.getVote(4) || 
				!p.isDefeated(1) ||
				!p.votedTogether(0, 2) ||
				p.getVotesAgainst(0) != 2 || p.getVotesAgainst(1) != 3 ||
				p.getVotesTogether(0) != 3 || p.getVotesTogether(1) != 2)
			System.err.println("Something wrong in the votes 2");
		
		
		System.out.println("Silence is good");
	}	

}
