function[assignment]= recursiveKmeans(data, nbCluster)
% usage
% function[centroid, pointsInCluster, assignment]=
% recursiveKmeans(data, nbCluster)
%
% Output:
% centroid: matrix in each row are the Coordinates of a centroid
% pointsInCluster: row vector with the nbDatapoints belonging to
% the centroid
% assignment: row Vector with clusterAssignment of the dataRows
%
% Input:
% data in rows
% nbCluster : nb of centroids to determine
%
% original by Christian Herta ( www.christianherta.de )
% modified by Filipe Araujo
%

%printf("points in the cluster: %i\n", nbCluster)
%fflush(stdout)
[centroid, pointsInCluster, theassignment] = myKmeans(data, nbCluster);
%printf("finished myKmeans")
%fflush(stdout)
if (nbCluster == 2)
	assignment = theassignment
else
	nbrpoints = round(sqrt(nbCluster));
	if (nbrpoints <= 2)
		nbrpoints = 2;
	end
	%printf("before recursive");
	%fflush(stdout);
	assignment2 = recursiveKmeans(centroid, nbrpoints);
	%printf("before recursive");
	%fflush(stdout);
	finalassignment = [];
	%printf("length of data = %i\n", length(data));
	%fflush(stdout);
	for i = 1 : length(data)
		%%for example: point 33 belongs to cluster 6 in 20 clusters
		%%next, we cluster the centroids of the 20 clusters
		%%as a result cluster 6 may now be cluster 3
		%%this means that point 33 will belong to cluster 3
		firstnumber = theassignment(i,1);
		secondnumber = assignment2(firstnumber,1);
		finalassignment(i,1) = secondnumber;
	end
	assignment = finalassignment;
end
