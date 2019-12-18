# Table of Contents
## The article
## Run the program and get results
## Preparing a Docker container

# The article
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

# Run the program and get results
The file AnalysisConfiguration.xml defines the configuration for the simulation. For example, to run configuration #10, from the runs group of elements, one does as follows:

1. `cd /colluding/Analysis`
2. `./stats-compare-algorithms.sh AnalysisConfiguration.xml 10`
3. `cd results`
4. `mkdir results-Extremecolluding`
5. `mv *.txt results-Extremecolluding/`
6. `cd ../gnuplot/`
7. `./bargraphs.sh ../AnalysisConfiguration.xml 10`
8. `cd ../results/results-Extremecolluding/`
9. `ls`

And the results should be visible in the current directory. One may optionally repeat this for other tests besides "Extremecolluding".

However, this is likely to fail, because there are copious dependencies required for these scripts to run. Please see the following section.

# Docker container
To manage the execution environment, I prepared a Docker Container where all the dependencies are ready and in place for the previous commands to succeed. You may create the docker container and make it run by putting the Dockerfile in an empty directory and then running the following commands from there:

1. `docker build -t colluding .`
2. `docker create -t -i colluding bash`
3. `docker start -a -i <container_id>`