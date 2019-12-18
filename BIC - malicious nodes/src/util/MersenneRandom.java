package util;

import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;

public class MersenneRandom {
	private static MersenneTwister twister = new MersenneTwister((new java.util.Random()).nextInt());
	private static Normal normaltwister = new Normal(0, 1, twister);
	
	public static double nextDouble() {
		return twister.nextDouble();
	}

	public static double nextGaussianDouble() {
		return normaltwister.nextDouble();
	}
}
