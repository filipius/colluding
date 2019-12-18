% Clustering Toolbox
% 
% Basic algorithms:
% 
%  agglom        : Basic Agglomerative Clustering
%  kmeans        : k-means clustering
%  mixtureEM     : cluster by estimating a mixture of Gaussians
%  mixtureSelect : estimate a mixture with unknown K using BIC
%  EM            : Expectation-Maximization
%
% Demos:
% 
%  irispca     : show first two principal components of iris data
%  agglomdemo  : demonstrate agglomerative clustering
%  kmeansdemo  : demonstrate k-means clustering
%  loadiris    : loads the cluster IRIS benchmark data
%  EMintro     : an introduction to EM as lower bound maximization
%  EMdemo      : demonstrate EM clustering
%  selectdemo  : demonstrate mixtureSelect
%  clustertest : test clusterstats with really simple distribution
%  
% Cluster quality:
%  
%  bscatter     : between-cluster scatter matrix
%  clusterstats : computes the statistics for each cluster
%  critsse      : computes Sum-of-Squared-Error Criterion for a given clustering
%  misclass     : calculates percent of misclassified samples in clusters
%  scatter      : scatter matrix for samples x
%  wscatter     : within-cluster scatter matrix
% 
% Auxiliary Code:
%  
%  assign        : assign each sample in t to nearest cluster center, i.e. VQ
%  cachedSqrDist : calculate a nt*nx matrix containing weighted squared error
%  cluster       : return the matrix of samples in cluster j according to c
%  dist1         : calculate a nt*nx vector containing distances between all points
%  dmean         : distance between means of two clusters
%  majority      : returns (weighted) majority vote
%  majority1     : returns weighted majority vote for a *row vector*
%  manhattan     : calculate a 1*n vector D containing manhattan distances from z
%  misclass1     : calculates % misclassified samples in a cluster
%  move          : move sample x(s) from its current cluster c(s) to cluster j
%  nearest       : return the vector zj in z that is nearest to xi
%  printClusters : print out j-component of the data in each cluster
%  projectpca    : project data matrix on first nr eigenvectors (using svds)
%  showclusters  : show clusters using colors
%  showmixture   : show mixture graphically
%  showpca2      : project data matrix on 2 first eigenvectors and show them
%  showpca3      : project data matrix on 3 first eigenvectors and show them
%  sqrDist       : calculate a nt*nx matrix containing weighted squared error
