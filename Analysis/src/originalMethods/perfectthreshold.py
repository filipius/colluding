from common import library
#import sys

#this gives the ideal separation between good and bad nodes, in terms of votes 
#the problem with this approach is that it relies on omniscient knowledge

def perfect_threshold_classify(filename, test = False):
    classification = library.perfect_classification(filename)
    
    if test:
        file = open(filename)
        graph = library.read_graph(file)
        file.close()
    else:
        graph = library.read_graph()
    #size = len(graph)
    
    #avgagainst = library.get_avg_votes_against(graph)
    
    good = {}
    bad = {}
    
    for n in graph:
        if n in classification:
            nbragainst = len(graph[n])
            #good node?
            if classification[n] == 1:
                dict = good
            else:
                dict = bad
            if not(nbragainst in dict):
                dict[nbragainst] = 0
            dict[nbragainst] += 1
            
    #allnbrs of votes against in the good and the bad group
    allnbrs = list(set(good.keys()).union(bad.keys()))
    allnbrs.sort()
    
    #now do the splitting
    #by default all nodes are bad (threshald <= 0)
    nbrgoods = 0
    nbrbads = len(good) + len(bad)
    distgood = {}
    distbad = {}
    minerrors = -1
    for i in allnbrs:
        if i in good:
            nbrgoods += good[i]
        if i in bad:
            nbrbads -= bad[i]
        errors = len(good) - nbrgoods + len(bad) - nbrbads
        if minerrors == -1 or errors < minerrors:
            minerrors = errors
            bestthreshold = i
        distgood[i] = nbrgoods
        distbad[i] = nbrbads
      
    result = []
    keys = graph.keys()
    keys.sort()
    for i in xrange(len(keys)):
        # print i
        if len(graph[i]) <= bestthreshold:
            outcome = 0
        else:
            outcome = 1
        result.append(outcome)
    
    if test:
        return distgood, distbad, bestthreshold, result
    else:
        return result
        
    


