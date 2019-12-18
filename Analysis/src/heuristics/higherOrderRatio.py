#based on a paper from 1988:
#Heuristic Solutions for the General Maximum Independent Set Problem with Applications
#to Expert System Design

import sys
import common.library


iterations_limit = 70
margin_improving_iterations = 5

#note: this changes the graph
def higher_order_ratio_alg(graph, nbriterations, weight = None):
    if weight == None:
        weight = {}
        for node in graph:
            weight[node] = 1   #all nodes are worth the same
    
    K = set()
    
    while len(graph) > 0:
        #step 1
        for _ in range(nbriterations):
            nbrnodes = len(graph)
            #print "iteration", iteration#, "weight = ", weight, nbrnodes
            allweights = 0
            for node in graph:
                allweights += weight[node]
            c = 1.0 * allweights / nbrnodes
        
            newweight = {}
            for node in graph:
                x = weight[node]
                y = 0
                for neighbor in graph[node]:
                    y += weight[neighbor]
                newweight[node] = 1.0 * x / (c + y)
                
            weight = newweight
            
        #step2
        max = None
        for node in graph:
            if max == None or weight[node] > max:
                max = weight[node]
                posmax = node
        K.add(posmax)
        common.library.remove_node_and_neighbors_from_graph(posmax, graph)
#        try:
#            if (posmax == 643):
#                sys.stderr.write(str(posmax) + " " + str(max) + str(graph) + "\n")
#        except KeyError, k:
#            sys.stderr.write(str(k) + " " + str(posmax) + " " + str(max) + str(graph) + "\n")
#            sys.exit(1)
    
    return K


def higher_order_ratio(name = None):
    if name == None:
        graph = common.library.read_graph()
    else:
        f = open(name)
        graph = common.library.read_graph(f)
        f.close()
    common.library.ensure_bidirectionality(graph)
    nbrnodes = len(graph)
    prevresult = -1
    previteration = 0
    #try to improve and concede a few iterations for that
    for i in range(iterations_limit):
        mis = higher_order_ratio_alg(graph, i + 1)
        if len(mis) <= prevresult:
            if previteration + margin_improving_iterations < i:
                break
        else:
            prevresult = len(mis)
            previteration = i     
            bestmis = mis    
    mislist = list(bestmis)
    mislist.sort()
    #print mislist
    result = common.library.convert_classification(mislist, nbrnodes)
    return result
