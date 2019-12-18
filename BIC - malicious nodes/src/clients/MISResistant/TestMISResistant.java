package clients.MISResistant;

import pools.Pool;
import clients.Client;
import clients.NormalClient;
import clients.VoteOperationNotSupportedException;

/*
class Test {
	private int size, bad;
	private int pools[][];
	private int votes[][];
	
	public Test(int size, int bad, int[][] pools, int[][] votes) {
		super();
		this.size = size;
		this.bad = bad;
		this.pools = pools;
		this.votes = votes;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getBad() {
		return bad;
	}
	public void setBad(int bad) {
		this.bad = bad;
	}
	public int[][] getPools() {
		return pools;
	}
	public void setPools(int[][] pools) {
		this.pools = pools;
	}
	public int[][] getVotes() {
		return votes;
	}
	public void setVotes(int[][] votes) {
		this.votes = votes;
	}
	
}
*/

public class TestMISResistant {

	private static int pools1[][] = { {0, 1, 2}, {0, 1, 4}, {0, 1, 5}, {0, 2, 5}, {0, 2, 6}, {1, 2, 6}, {6, 2, 3}, {2, 7, 4}, {3, 4, 8}};
	private static int votes1[][] = { {0, 0, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}, {1, 1, 1}, {1, 0, 0}, {1, 1, 1}, {1, 1, 1}}; 

	private static int pools2[][] = { {0, 4, 5}, {0, 1, 5} };
	private static int votes2[][] = { {1, 1, 1}, {0, 0, 1} };

	private static int pools3[][] = { {0, 1, 4}, {2, 3, 5}, {0, 2, 6} };
	private static int votes3[][] = { {0, 0, 1}, {0, 0, 1}, {0, 0, 1} };

	private static int pools4[][] = { {0, 1, 4}, {2, 3, 5}, {0, 2, 6}, {1, 3, 7} };
	private static int votes4[][] = { {0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {1, 1, 1} };

	/**
	 * @param args
	 * @throws PoolSizeNotSupportedException 
	 * @throws VoteOperationNotSupportedException 
	 */
	public static void main(String[] args) throws PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		testpools(1, pools1, votes1, 4, 10);
		testpools(2, pools2, votes2, 4, 10);
		testpools(3, pools3, votes3, 4, 10);
		testpools(4, pools4, votes4, 4, 10);
		System.out.println("Test finished. No news is good news.");
	}

	private static void testpools(int run, int inpools[][], int expectedvotes[][], int bad, int size) throws PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		Client [] client = new Client[size ];
		
		for (int i = 0; i < bad ; i++)
			client[i] = new MISResistantColluder(i);
	
		for (int i = bad; i < size; i++)
			client[i] = new NormalClient(i);
		
		for (int i= 0; i < inpools.length; i++) {
			Pool p = new Pool(i); //, inpools[i].length);
			for (int j = 0; j < inpools[i].length; j++) {
				p.add(client[inpools[i][j]]);
			}
			p.vote();
			
			for (int j = 0; j < expectedvotes[i].length; j++)
				if (expectedvotes[i][j] != p.getVote(j))
					System.err.println("run = " + run + " wrong vote in pool " + i + " client " + j);
		}
		
	}

}
