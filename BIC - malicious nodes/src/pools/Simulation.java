package pools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;

import clients.Client;
import clients.ClientTypes;
import clients.ColludingMalicious;
import clients.DuplicationUnsupportedException;
import clients.GaussianNaiveMalicious;
import clients.MechanicalTurk;
import clients.MixedMalicious;
import clients.NaiveMalicious;
import clients.NormalClient;
import clients.SingleAttackColluder;
import clients.UnreliableColluding;
import clients.VoteOperationNotSupportedException;
import clients.VoteType;
import clients.MISResistant.MISResistantColluder;
import clients.MISResistant.PoolSizeNotSupportedException;
import util.MersenneRandom;



/**
 * 
 * @author Jorge Farinha jfar*AT*student.dei.uc.pt
 * 09/2008
 *
 */
public class Simulation {

	//private final int DESIRED_NUM_POOLS = 1000000;
	private  int NUM_POOLS;
	private  int NUM_CLIENTS;
	private  int P1;
	private  int P2;
	private  int P3;
	private  int P4;
	private  int P5;
	private  int P6;
	private  int P7;
	private  double NM_WRONG_RESULTS;
	private  double CM_WRONG_RESULTS;
	private  double MM_NAIVE_RATIO;
	private  double UC_KEEPS_ATTACK;
	private  double GNM_WRONG_AVG;   //average of errors
	private  double GNM_WRONG_STDEV; //standard deviation of errors
	//private  int NUM_CLIENTS_PER_POOL;
	private  int AVG_NODE_LIFE;

	private  int NUM_NAIVE_MALICIOUS;
	private  int NUM_COLLUDING_MALICIOUS;
	private  int NUM_MIXED_MALICIOUS;
	private  int NUM_UNRELIABL_COLLUDING_MALICIOUS;
	private  int NUM_GAUSSIAN_NAIVE_MALICIOUS;
	private  int NUM_SINGLE_ATTACK_COLLUDERS;
	private  int NUM_MISRESISTANT_COLLUDERS;

	private int clientid = 0;

	private static  boolean shouldIPlot = false; 
	private static  String MECHANICAL_TURK_FILE = "mturk_file"; 

	private Client[] clients;
	// should delete this variable allclients somehow, but it is tied to clients that die and comeback
	private ArrayList<Client> allclients = new ArrayList<Client>(); 
	private Pool[] pools;
	private HashMap<Integer, ArrayList<Integer>> votesagainst = new HashMap<Integer, ArrayList<Integer>>();

	public Simulation(HashMap<String,String> parameters) throws DuplicationUnsupportedException, PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		this.NUM_POOLS = new Integer(parameters.get("NUM_POOLS"));
		this.NUM_CLIENTS = new Integer(parameters.get("NUM_CLIENTS"));
		this.P1 = new Integer(parameters.get("P1"));
		this.P2 = new Integer(parameters.get("P2"));
		this.P3 = new Integer(parameters.get("P3"));
		this.P4 = new Integer(parameters.get("P4"));
		this.P5 = new Integer(parameters.get("P5"));
		this.P6 = new Integer(parameters.get("P6"));
		this.P7 = new Integer(parameters.get("P7"));
		this.NM_WRONG_RESULTS = new Double(parameters.get("NM_WRONG_RESULTS"));
		this.CM_WRONG_RESULTS = new Double(parameters.get("CM_WRONG_RESULTS"));
		this.MM_NAIVE_RATIO = new Double(parameters.get("MM_NAIVE_RATIO"));
		this.UC_KEEPS_ATTACK = new Double(parameters.get("UC_KEEPS_ATTACK"));
		this.GNM_WRONG_AVG = new Double(parameters.get("GNM_WRONG_AVG"));
		this.GNM_WRONG_STDEV = new Double(parameters.get("GNM_WRONG_STDEV"));
		int numclientsperpool = new Integer(parameters.get("NUM_CLIENTS_PER_POOL"));
		this.AVG_NODE_LIFE = new Integer(parameters.get("AVG_NODE_LIFE"));

		this.NUM_NAIVE_MALICIOUS = (int) ((P1 * NUM_CLIENTS) / 100.0);			//M1
		this.NUM_COLLUDING_MALICIOUS = (int) ((P2 * NUM_CLIENTS) / 100.0);		//M2
		this.NUM_MIXED_MALICIOUS = (int) ((P3 * NUM_CLIENTS) / 100.0);  			//M3
		this.NUM_UNRELIABL_COLLUDING_MALICIOUS = (int) ((P4 * NUM_CLIENTS) / 100.0);  			//M4
		this.NUM_GAUSSIAN_NAIVE_MALICIOUS = (int) ((P5 * NUM_CLIENTS) / 100.0);
		this.NUM_SINGLE_ATTACK_COLLUDERS = (int) ((P6 * NUM_CLIENTS) / 100.0);
		this.NUM_MISRESISTANT_COLLUDERS = (int) ((P7 * NUM_CLIENTS) / 100.0);  //M7
		
		clients = genClients();
		System.out.println(clients.length + " Clients generated");
		pools = genPools(numclientsperpool);
		System.out.println(pools.length + " Pools generated");

		/*
		pools = this.expandpools(pools);
		System.out.println(pools.length + " Pools evaluated either from source or extrapolated");
		 */

		System.out.println("three different votes = " + Pool.getAlldifferent() + " two to one = " + Pool.getTwoone() + " all together = " + Pool.getAllequal());

	}
	
	public Simulation(String filename) throws IOException, ErrorInDataException {
		this.pools = this.genPools(filename);
	}
	
	private void simulate(boolean plot) { 
		this.poolevaluation();
		System.out.println("Evaluation finished");

		//print();
		//printInterestingPools();
		if (plot)
			plot();
	}

	private void printPools() {
		for(int i=0;i<pools.length;i++){
			//if (!pools[i].allAgree()){
			System.out.println(pools[i]);
			//}
		}

	}

	private void plot() {

		//gráfico de colunas com a pontuação de cada cliente

		double values[][] = new double[1][NUM_CLIENTS];
		for(int i=0;i<NUM_CLIENTS;i++)
			values[0][i]=clients[i].getScore();

		plotTable(values, "clients", "votes", "pontuação de cada cliente");


		//gráfico de colunas com o número de clientes por intervalo de pontuação
		/*
		int numSlots=20;
		double groups[][] =new double[1][numSlots];
		for(int i=0;i<clients.length;i++){
			double sc=clients[i].getScore();
			int slot=(int)(sc*(numSlots-1));
			groups[0][slot]+=1;
		}

		plotTable(groups, "intervalo", "# de clientes","clientes agrupados por pontuação");	
		 */

		//número de derrotas em pools por cliente
		double[][] cl=new double[1][NUM_CLIENTS];
		for (int i = 0; i < NUM_CLIENTS; i++) {
			cl[0][i]=clients[i].getDefeats();
		}	
		plotTable(cl, "clientes", "# de derrotas", "# de derrotas por cliente");
	}

	private void plotTable(double[][] table, String xTitle, String yTitle, String windowTitle){
		CategoryDataset dataset=DatasetUtilities.createCategoryDataset("category","",table);
		JFreeChart chart =ChartFactory.createBarChart("", xTitle, yTitle, dataset, PlotOrientation.VERTICAL, false, true, false);

		ChartPanel cp=new ChartPanel(chart);

		ApplicationFrame af=new ApplicationFrame(windowTitle);
		af.setContentPane(cp);
		af.pack();
		af.setVisible(true);
	}

	private void print() {
		//int typesofclients = Client.getTypesofClients();
		//double defeats[] = new double[typesofclients], 
		//against[] = new double[typesofclients], 
		//num[] = new double[typesofclients];
		//String names[] = new String[typesofclients];

		Map<ClientTypes, int[]> hmscores = new HashMap<>();
		for (Client client : this.allclients) {
			//int type = ClientTypes.getNumber(client.getType());
			int [] scores = hmscores.get(client.getType());
			if (scores == null) {
				scores = new int[]{0, 0, 0};
				hmscores.put(client.getType(), scores);
			}
			scores[0] += client.getDefeats();
			scores[1] += client.getScore();
			scores[2] += 1;

			/*
			defeats[type] += client.getDefeats();
			against[type] += client.getScore();
			num[type]++;
			if (names[type] == null)
				names[type] = client.typeString();
				*/
		}

		for (ClientTypes ct : hmscores.keySet()) {
			int [] scores = hmscores.get(ct);
			System.out.println(scores[2] + " " + ct.name() + ": Defeats = " + (1.0 * scores[0] / scores[2]) + ". Against = " + (1.0 * scores[1] / scores[2]));
		}
			
		/*
		for (int i = 0; i < typesofclients; i++)
			if (names[i] != null)
				System.out.println(num[i] + " " + names[i] + ": Defeats = " + (1.0 * defeats[i] / num[i]) + ". Against = " + (1.0 * against[i] / num[i]));
		*/

		for (Client client : this.allclients)
			System.out.print(client.getDefeats() + "," + client.getScore() + "," + client.getWeightedScore() + "," + client.getScore() / client.getWeightedScore()  + ";");
		System.out.println();

		for (Client client : this.allclients) {
			System.out.print("votes against of node " + client.getId() + " (" + client.typeString() + ") -->");
			ArrayList<Integer> al = this.votesagainst.get(client.getId());
			if (al != null)
				for (int j : al)
					System.out.print(" " + j);
			System.out.println();
		}
	}

	/*
	private void print() {
		HashMap<Class<?>, Integer> defeats = new HashMap<Class<?>, Integer>();
		HashMap<Class<?>, Integer> against = new HashMap<Class<?>, Integer>();
		HashMap<Class<?>, Integer> number = new HashMap<Class<?>, Integer>();
		//int defeats[] = new int[4], against[] = new int[4], num[] = new int[4];

		for (int i = 0; i < clients.length; i++) {
			Class<?> theclass = clients[i].getClass();
			Integer thesedefeats = defeats.get(theclass);
			if (thesedefeats == null) {
				defeats.put(theclass, 0);
				thesedefeats = defeats.get(theclass);
			}

			thesedefeats.
			defeats[clients[i].getType()] += clients[i].getDefeats();
			against[clients[i].getType()] += clients[i].getScore();
			num[clients[i].getType()]++;
		}

		for (int i = 0; i < Client.names.length; i++)
			System.out.println(num[i] + " " + Client.names[i] + ": Defeats = " + (1.0 * defeats[i] / num[i]) + ". Against = " + (1.0 * against[i] / num[i]));

		for (int i = 0; i < NUM_CLIENTS; i++) {
			System.out.print(clients[i].getDefeats() + "," + clients[i].getScore() + ";");
			//if (i == 99 || i == 199) System.out.println();
		}
		System.out.println();

	}
	 */

	private boolean interestingpool(Pool p) {
		return !p.allAgree() || p.getVote(0) != VoteType.OK;		
	}


	/**
	 * This method infers pool results from the actual methods
	 * @param pools2 
	 */
	/*
	private Pool[] expandpools(Pool[] pools1) {
		for (Pool pool : pools1) {
			for (int k = 0; k < NUM_CLIENTS_PER_POOL; k++) {
				int id = pool.getClients()[k].getId();

				clients[id].setParticipationInGroups(pool.getVotesTogether(k));
			}
		}

		// now invent the pools...
		Pool pools2[] = this.genPools(NUM_POOLS, DESIRED_NUM_POOLS, true);
		Pool[] poolfinal = new Pool[pools1.length + pools2.length];
		System.arraycopy(pools1, 0, poolfinal, 0, pools1.length);
		System.arraycopy(pools2, 0, poolfinal, pools1.length, pools2.length);
		return poolfinal;
	}
	 */

	private void poolevaluation() {
		int interestingPools=0;
		/*
		int DIdidntattack = 0;
		int againstIdidntattack = 0;
		 */

		interestingPools=0;
		// para todos os pools
		for (int j = 0; j < pools.length; j++) {
			// escolhe os que têm vários resultados diferentes
			// ou onde fizeram todos batota
			if (this.interestingpool(pools[j])) {
				interestingPools++;
				//System.out.println("Interesting pool: " + pools[j]);
				//int result = pools[j].getFinalResult();

				// para cada cliente da pool, calcula a sua pontuação
				for (int k = 0; k < pools[j].getPoolSize(); k++) {
					Client client = pools[j].getClient(k); 
					//int id = .getId();
					//	System.out.print((pools[j].getResults()[k]?"T":"F")+"->");

					client.addScore(pools[j].getVotesAgainst(k));


					/*
					if (clients[id].getType() == Client.NAIVE_MALICIOUS && pools[j].getVote(k) != VoteType.NAIVE_RANDOM) {
						againstIdidntattack += pools[j].getVotesAgainst(k);
						if (pools[j].isDefeated(k))
							DIdidntattack++;
					}
					 */

					// se o seu resultado não é = ao resultado final da pool
					if (pools[j].isDefeated(k))
						client.incDefeats();

					//collect the nodes that voted against
					ArrayList<Integer> al = this.votesagainst.get(client.getId());
					if (al == null)
						al = new ArrayList<Integer>();
					for (int m = 0; m < pools[j].getPoolSize(); m++)
						if (m != k && !pools[j].votedTogether(k, m))
							al.add(pools[j].getClient(m).getId());
					this.votesagainst.put(client.getId(), al);


					//debug
					//for(int l=0;l<clients.length;l++)
					//	System.out.print(clients[l].getScore()+" ");
					//System.out.println();
					//debug end

				}
			}
		}

		for (Pool pool : pools) {
			if (this.interestingpool(pool)) {
				for (int k = 0; k < pool.getPoolSize(); k++) {
					Client client = pool.getClient(k);
					// each client that votes against gives between 0 and 1 points
					client.addWeightedScore(pool.getWeightedVotesAgainst(k));
				}
			}
		}

		/*
		Matrix m = new Matrix(NUM_CLIENTS);
		for (Pool pool : pools) {
			if (this.interestingpool(pool)) {
				for (int k = 0; k < NUM_CLIENTS_PER_POOL; k++) {
					int id = pool.getClients()[k].getId();
					for (int l = 0; l < NUM_CLIENTS_PER_POOL; l++) {
						if (l != k) {
							int otherid = pool.getClients()[l].getId();
							if (pool.getVote(k) == pool.getVote(l))
								m.addGood(id, otherid);
							else
								m.addBad(id, otherid);
						}
					}
				}
			}
		}
		 */

		//System.out.println("I didn't attack against = " + againstIdidntattack + " defeats = " + DIdidntattack);
		System.out.println("Interesting Pools="+interestingPools+" ("+(interestingPools*100/pools.length)+"%)");
		this.printPools();

		//normaliza o o score dos clientes
		this.normalize(this.clients);
	}


	/*
	private void normalize() {
		Client[] c = this.clients;

		double maxScore=0, maxdefeats = 0;
		for(int i=0;i<c.length;i++){
			maxScore=Math.max(maxScore, c[i].getScore());
			maxdefeats = Math.max(maxdefeats, c[i].getDefeats());
		}
		for(int i=0;i<c.length;i++){
			c[i].setScore(100*c[i].getScore()/maxScore);
			c[i].setDefeats(100*c[i].getDefeats()/maxdefeats);
		}

	}
	 */

	/*
	private void normalize(Client[] clients) {
		double sumagainst = 0;
		for (Client c : clients) {
			sumagainst += c.getScore();
		}
		for (Client c : clients) {
			int part = c.getParticipations();
			if (part > 0) {
				c.setScore(c.getScore()/part);
				c.setDefeats(c.getDefeats()/part);
			}
			if (sumagainst > 0)
				c.setWeightedScore(c.getWeightedScore() / sumagainst);
		}

	}
	 */

	private void normalize(Client[] clients) {
		//double maxweight = 0;
		for (Client c : this.allclients) {
			System.out.println("Client " + c.getId() + " score = " + c.getScore() + " defeats = " + c.getDefeats() + " participations = " + c.getParticipations());
			int part = c.getParticipations();
			//double votesagainst = c.getScore();
			if (part > 0) {
				c.setScore(c.getScore()/part);
				c.setDefeats(c.getDefeats()/part);
			}

			/*
			if (votesagainst > 0) {
				c.setWeightedScore(c.getWeightedScore() / votesagainst);
				if (c.getWeightedScore() > maxweight)
					maxweight = c.getWeightedScore();
			}
			 */
		}

		// now let's normalize the weighted scores
		//for (Client c : clients)

	}


	private Client[] genClients() {
		int lastNaive=NUM_NAIVE_MALICIOUS;
		int lastColluding=lastNaive+NUM_COLLUDING_MALICIOUS;
		int lastMixed=lastColluding+NUM_MIXED_MALICIOUS;
		int lastUnreliableColluding=lastMixed+NUM_UNRELIABL_COLLUDING_MALICIOUS;
		int lastGaussianNaive=lastUnreliableColluding+NUM_GAUSSIAN_NAIVE_MALICIOUS;
		int lastSingleAttackColluder=lastGaussianNaive+NUM_SINGLE_ATTACK_COLLUDERS;
		int lastMISResistantColluder=lastSingleAttackColluder+NUM_MISRESISTANT_COLLUDERS;
		Client[] clients = new Client[NUM_CLIENTS];
		Client newclient;

		//System.out.println("single: " + lastSingleAttackColluder + "  MISresistant: " + lastMISResistantColluder);
		for (int i = 0; i < NUM_CLIENTS; i++) {
			//determina o tipo de cliente a gerar
			//gera o cliente
			if (i < lastNaive)
				newclient = new NaiveMalicious(this.clientid++, NM_WRONG_RESULTS);
			else if (i < lastColluding)
				newclient = new ColludingMalicious(this.clientid++, CM_WRONG_RESULTS);
			else if (i<lastMixed)
				newclient = new MixedMalicious(this.clientid++, MM_NAIVE_RATIO, NM_WRONG_RESULTS, CM_WRONG_RESULTS);
			else if (i<lastUnreliableColluding)
				newclient = new UnreliableColluding(this.clientid++, CM_WRONG_RESULTS, UC_KEEPS_ATTACK);
			else if (i<lastGaussianNaive)
				newclient = new GaussianNaiveMalicious(this.clientid++, GNM_WRONG_AVG, GNM_WRONG_STDEV);
			else if (i < lastSingleAttackColluder)
				newclient = new SingleAttackColluder(this.clientid++, CM_WRONG_RESULTS);
			else if (i < lastMISResistantColluder)
				newclient = new MISResistantColluder(this.clientid++);
			else
				newclient = new NormalClient(this.clientid++);
			if (this.AVG_NODE_LIFE != 0)
				newclient.setAvg_life(this.AVG_NODE_LIFE);
			clients[i] = newclient;
			this.allclients.add(clients[i]);
		}
		return clients;
	}

	private Pool[] genPools(int start_num_pools, int end_num_pools, int numclientsperpool, boolean infered) throws DuplicationUnsupportedException, PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		Pool[] pools = new Pool[end_num_pools - start_num_pools];
		int clientspos[] = new int[numclientsperpool];

		for (int i = start_num_pools; i < end_num_pools; i++) {
			int pos = i - start_num_pools;
			pools[pos] = new Pool(i); //,numclientsperpool);
			for (int j = 0; j < numclientsperpool; ) {
				clientspos[j] = (int) (MersenneRandom.nextDouble() * NUM_CLIENTS);
				j = pools[pos].add(clients[clientspos[j]]);
			}
			if (!infered)
				pools[pos].vote();
			else
				pools[pos].infervote();
			for (int j = 0; j < numclientsperpool; j++) {
				Client c = pools[pos].getClient(j);
				if (c.isout()) {
					int thepos = clientspos[j];
					clients[thepos] = c.duplicate(this.clientid++);
					this.allclients.add(clients[thepos]);
				}

			}
		}
		//System.out.println("2");
		return pools;
	}

	private Pool[] genPools(int numclientsperpool) throws DuplicationUnsupportedException, PoolSizeNotSupportedException, VoteOperationNotSupportedException {
		return this.genPools(0, NUM_POOLS, numclientsperpool, false);
	}

	private Pool[] genPools(String filename) throws IOException, ErrorInDataException {
		boolean firstline = true;
		InputStream fis;
		BufferedReader br;
		String line;
		
		fis = new FileInputStream(filename);
		br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		
		Map<String, Client> clientsbyname = new HashMap<>();
		Map<String, Pool> poolsbyname = new HashMap<>();
		Map<String, Integer> votesbyname = new HashMap<>();
		List<Pool> lpools = new ArrayList<>();
		int poolid = 0, clientid = 0, votenbr = VoteType.OK + 1;
		while ((line = br.readLine()) != null) {
			if (firstline)
				firstline = false;
			else {
				String [] data = line.replaceAll("\n", "").split(",");
				String voter = data[0];
				String poolname = data[1];
				String correctvote = data[2];
				Integer correctvotenbr = votesbyname.get(poolname + correctvote);
				if (correctvotenbr != null && correctvotenbr != VoteType.OK) {
					br.close();
					throw new ErrorInDataException("Line " + line);
				}
				votesbyname.put(poolname + correctvote, VoteType.OK);
				String vote = data[3];
				Pool pl = poolsbyname.get(poolname);
				if (pl == null) {
					pl = new Pool(poolid++);
					lpools.add(pl);
					poolsbyname.put(poolname, pl);
				}
				Client cl = clientsbyname.get(voter);
				if (cl == null) {
					cl = new MechanicalTurk(clientid++);
					clientsbyname.put(voter, cl);
					allclients.add(cl);
				}
				cl.incParticipations();
				Integer votevalue = votesbyname.get(poolname + vote);
				if (votevalue == null) {
					votevalue = new Integer(votenbr++);
					votesbyname.put(poolname + vote, votevalue);
				}
				if (pl.getPoolSize() < pl.add(cl))
					pl.add(votevalue);
			}
		}
		for (Pool p : lpools)
			p.decide_vote();
		br.close();
		return (Pool[]) lpools.toArray(new Pool[0]);
	}
	
	
	public static HashMap<String, String> readParams(String filename) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) {
			line.trim();
			if (!line.startsWith("#")) {
				String [] vector = line.split("=");
				String paramname = vector[0].trim();
				String paramvalue= vector[1].trim();
				params.put(paramname, paramvalue);
			}
		}
		br.close();
		return params;
	}


	public static void main(String[] args) throws IOException, DuplicationUnsupportedException, PoolSizeNotSupportedException, VoteOperationNotSupportedException, ErrorInDataException {
		String filename = null;
		Simulation sim;

		filename = System.getProperty(MECHANICAL_TURK_FILE);
		if (filename != null && args.length > 0 || filename == null && args.length != 1) {
			System.err.println("Wrong Parameters. Correct format should include filename with parameters.");
			System.err.println("or filename -D " + MECHANICAL_TURK_FILE + "=filename with no parameters.");
			System.exit(1);
		}
		if (filename == null) {
			filename = args[0];
			sim = new Simulation(Simulation.readParams(filename));
		}
		else
			sim = new Simulation(filename);
		sim.simulate(shouldIPlot);
		sim.print();
		//sim.printInterestingPools();
	}
}