package clients;

public enum ClientTypes {
	NORMAL_CLIENT, NAIVE_MALICIOUS, COLLUDING_MALICIOUS, MIXED_MALICIOUS, UNRELIABLE_COLLUDER, GAUSSIAN_NAIVE_MALICIOUS, SINGLE_ATTACK_COLLUDER, MISRESISTANT_COLLUDER, MECHANICAL_TURK;
	
	private static String [] names = {"NORMAL", "NAIVE", "COLLUDING", "MIXED", "UNR_COLLUDING", "GaussianNaiveClient", "SINGLECOLLUDER", "RESISTANT", "MT"};
	private static String [] suffix = {"R", "N", "C", "M", "U", "N", "C", "R", "T"};

	public static String[] getNames() {
		return names;
	}
	
	public static String getName(ClientTypes mytype) {
		return names[mytype.ordinal()];
	}


	public static String getSuffix(ClientTypes mytype) {
		return suffix[mytype.ordinal()];
	}

	public static int getNumber(ClientTypes ct) {
		return ct.ordinal();
	}
}
