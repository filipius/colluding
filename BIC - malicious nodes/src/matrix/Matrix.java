package matrix;

import java.util.ArrayList;
import java.util.Collections;

//square matrix
public class Matrix {
	MatrixElement [][] m;

	public Matrix(int size) {
		m = new MatrixElement[size][size];
	}

	public void setElement(int row, int col, MatrixElement val) {
		m[row][col] = val;
	}

	public MatrixElement getElement(int row, int col) {
		return m[row][col];
	}

	public void addGood(int id, int otherid) {
		m[id][otherid].addGood();
		
	}

	public void addBad(int id, int otherid) {
		m[id][otherid].addBad();
	}

	
	/*
	 * XXX: this can be improved...
	 */
	public int[] getVote(int[] clients) throws InternalBugException {
		ArrayList<Integer> setone = new ArrayList<Integer>(), settwo = new ArrayList<Integer>();
		int [] results = new int[clients.length];
		ArrayList<AgainstVoteTie> againsttie = this.getSortedAgainst(clients);
		for (int i = 0; i < againsttie.size(); i++) {
			AgainstVoteTie tie = againsttie.get(i);
			int node1 = tie.getNode1();
			int node2 = tie.getNode2();
			ArrayList<Integer> set = setone, otherset = settwo;
			if (settwo.contains(node1) || setone.contains(node2)) {
				set = settwo;
				otherset = setone;
			}
			if (!set.contains(node1) && !otherset.contains(node1))
				set.add(node1);
			if (!otherset.contains(node2) && !set.contains(node2))
				otherset.add(node2);
		}
		int pos = 0;
		for (int i : clients)
			if (setone.contains(i))
				results[pos++] = 0;
			else
				if (settwo.contains(i))
					results[pos++] = 1;
				else
					throw new InternalBugException("Wrong situation in getVote()");
		return results;
	}

	private ArrayList<AgainstVoteTie> getSortedAgainst(int[] clients) {
		ArrayList<AgainstVoteTie> order  = new ArrayList<AgainstVoteTie>(); 
		for (int i = 0; i < clients.length; i++)
			for (int j = 0; j < clients.length; j++)
				order.add(new AgainstVoteTie(i, j, m[i][j].getGood(), m[i][j].getBad()));
		Collections.sort(order);
		return order;
	}



}
