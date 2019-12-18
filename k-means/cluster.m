matrix = load('matrix.txt');
[centroid, pointsInCluster, assignment]=myKmeans(matrix, 2);
printf("finished\n");
printf("%i ", assignment);
