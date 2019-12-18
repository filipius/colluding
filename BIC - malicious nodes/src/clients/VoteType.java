package clients;


/*
 * XXX: There are scripts that depend on the values of this class
 * as it is the case of the method perfect_classification() in library.py
 */
public class VoteType {
	public static int INVALID = -1;
	//public static int NAIVE_RANDOM = 0;
	public static int COLLUDER_BAD = 0;
	public static int OK = 1;
	//public static int LENGTH = 3;
	
	public static String name(int vote) {
		if (vote == -1)
			return "INVALID";
		else
			return "" + vote;
		/*
		String names [] = {"INVALID", "X", "0", "1"};
		if (pos + 1 > LENGTH)
			return "" + (pos - 1);
		return names[pos + 1];
		*/
	}
};  
