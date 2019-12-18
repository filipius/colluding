# colluding
This is the source code used to produce the following article:

Filipe Araujo, Jorge Farinha, Patricio Domingues, Gheorghe Cosmin Silaghi, Derrick Kondo,

A maximum independent set approach for collusion detection in voting pools,

Journal of Parallel and Distributed Computing,

Volume 71, Issue 10,

2011,

Pages 1356-1366,

ISSN 0743-7315,

https://doi.org/10.1016/j.jpdc.2011.06.004.

(http://www.sciencedirect.com/science/article/pii/S0743731511001316)

Abstract: From agreement problems to replicated software execution, we frequently find scenarios with voting pools. Unfortunately, Byzantine adversaries can join and collude to distort the results of an election. We address the problem of detecting these colluders, in scenarios where they repeatedly participate in voting decisions. We investigate different malicious strategies, such as naïve or colluding attacks, with fixed identifiers or in whitewashing attacks. Using a graph-theoretic approach, we frame collusion detection as a problem of identifying maximum independent sets. We then propose several new graph-based methods and show, via analysis and simulations, their effectiveness and practical applicability for collusion detection.

Keywords: Collusion detection; Maximum independent set; Volunteer computing


# To run the program
The file AnalysisConfiguration.xml defines the configuration for the simulation. For example, to run configuration #10, from the runs group of elements, one does as follows:

cd Analysis

./stats-compare-algorithms.sh AnalysisConfiguration.xml 10

cd results

mv *.txt results-Extremecolluding/

./bargraphs.sh ../AnalysisConfiguration.xml 10

cd ../gnuplot/

./bargraphs.sh ../AnalysisConfiguration.xml 10

cd ../results/results-Extremecolluding/   ---> where the results are

One may optionally repeat this for other tests.
