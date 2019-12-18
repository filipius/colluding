package clients.MISResistant;

import java.util.HashMap;
import java.util.HashSet;

public class AttackGroups {
	private HashMap<Integer, HashSet<Integer>> groups;

	private static AttackGroups instance = null;

	protected AttackGroups() {
		this.groups = new HashMap<Integer, HashSet<Integer>>();
	}

	public static AttackGroups getInstance() {
		if(instance == null) {
			instance = new AttackGroups();
		}
		return instance;
	}

	private HashSet<Integer> getColluderSet(int offender) {
		HashSet<Integer> colluderset = this.groups.get(offender);
		if (colluderset == null) {
			colluderset = new HashSet<Integer>();
			colluderset.add(offender);
		}
		return colluderset;
	}

	public void join(int[] offendersids, int size) {
		if (size >= 2) {
			HashSet<Integer> colluderset = this.getColluderSet(offendersids[0]);
			for (int i = 1; i < size; i++) {
				HashSet<Integer> newset = this.getColluderSet(offendersids[i]);
				colluderset.addAll(newset);
			}
			for (int node : colluderset)
				this.groups.put(node, colluderset);
		}
	}


	public boolean canAttack(int[] offendersids) throws PoolSizeNotSupportedException {
		if (offendersids.length < 2)
			throw new PoolSizeNotSupportedException();
		HashSet<Integer> colluderset0 = this.getColluderSet(offendersids[0]);
		HashSet<Integer> colluderset1 = this.getColluderSet(offendersids[1]);
		for (int n : colluderset0)
			if (colluderset1.contains(n))
				return false;
		return true;
	}

	
	public void clear() {
		this.groups = new HashMap<Integer, HashSet<Integer>>();
	}

	private static void testSequentialCollusions(int run, int[][] set,
			boolean[] canattack, AttackGroups ag)
			throws PoolSizeNotSupportedException {
		for (int i = 0; i < set.length; i++) {
			boolean res = ag.canAttack(set[i]); 
			if (res)
				ag.join(set[i], set[i].length);
			if (res != canattack[i])
				System.err.println("Wrong result for run " + run + " i = " + i);
		}
	}

	public static void main(String[] args) throws PoolSizeNotSupportedException {
		AttackGroups ag = AttackGroups.getInstance();

		int set1[][] = { {0, 1}, {0, 2}, {1, 2} };
		boolean canattack1[] = { true, true, false };
		testSequentialCollusions(1, set1, canattack1, ag);
		ag.clear();
		
		int set2[][] = { {0, 1}, {2, 3}, {1, 2}, {0, 3} };
		boolean canattack2[] = { true, true, true, false };
		testSequentialCollusions(2, set2, canattack2, ag);
		ag.clear();
		
		int set3[][] = { {0, 1}, {2, 3}, {0, 2} };
		boolean canattack3[] = { true, true, true };
		testSequentialCollusions(3, set3, canattack3, ag);
		ag.clear();

		System.out.println("Testing AttackGroups. No news is good news...");
	}

}

