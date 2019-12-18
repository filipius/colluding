package clients;

public class DebugClients {

	public static void main(String[] args) {
		int id = 0;
		Client[] clientarray = {
				new NormalClient(id++),
				new NaiveMalicious(id++),
				new ColludingMalicious(id++),
				new MixedMalicious(id++, 0.2, 0.2, 0.2),
				new UnreliableColluding(id++, 0.2, 0.2),
				new GaussianNaiveMalicious(id++,  0.8,  0.2),
				new SingleAttackColluder(id++, 0.2),
				new MechanicalTurk(id++)
		};
		
		int [] typesexpected = {0, 1, 2, 3, 4, 5, 6, 7};
		int numtypes = typesexpected.length;
		String [] typenamesexpected = {"NORMAL", "NAIVE", "COLLUDING", "MIXED", "UNR_COLLUDING", "GaussianNaiveClient", "SINGLECOLLUDER", "MechanicalTurk"};
		String [] namesexpected = {"Client: 0 R", "Client: 1 N", "Client: 2 C", "Client: 3 M", "Client: 4 U", "Client: 5 N", "Client: 6 C", "Client: 7 T"};
		
		for (int i = 0; i < clientarray.length; i++) {
			if (ClientTypes.getNumber(clientarray[i].getType()) != typesexpected[i] || !clientarray[i].typeString().equals(typenamesexpected[i]) || !clientarray[i].toString().equals(namesexpected[i]))
				System.err.println("Error in client" + i);
			if (Client.getTypesofClients() != numtypes)
				System.err.println("Error 2 in client" + i);
		}
		
		System.out.println("Silence is good. Red lines are errors.");
	}

}
