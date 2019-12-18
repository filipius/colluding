#import random
import re
import sys
from copy import copy

#XXX: this could and should be converted to a class
class GraphIsDirected(Exception):
    def __init__(self, node):
        self.node = node
    
    def get_node(self):
        return self.node

def get_threshold(arglist):
    if len(arglist) < 2:
        #    sys.stderr.write("Wrong arguments. Correct format:")
        #    sys.stderr.write("bfs threshold")
        #    sys.exit(1)
        #how sensitive the method is to the number of votes against
        threshold = 0.15
    else:
        threshold = float(arglist[1])
    return threshold

def is_bidirectional(graph):
    for node in graph:
        neighbors = graph[node]
        for n in neighbors:
            if not(node in graph[n]):
                return False, node
    return True, None

def ensure_bidirectionality(graph):
    result, node = is_bidirectional(graph)
    if not(result):
        raise GraphIsDirected(node)
    
def remove_node_from_graph(node, graph):
    neighbors = graph[node]
    for n in neighbors:
        graph[n].remove(node)
    del graph[node]
    
    
def remove_node_and_neighbors_from_graph(node, graph):
    neighbors = copy(graph[node])
    #print neighbors
    for n in neighbors:
        #print n
        remove_node_from_graph(n, graph)
    del graph[node]


def read_scores():
    scores = {}
    start = False
    try:
        j = 0
        while True:
            line = raw_input()
            if re.match("Client", line):
                if not(start):
                    start = True
                vector = line.split()
                against = vector[4]
                defeats = vector[7]
                participations = vector[10]
                scores[j] = [int(float(against)), int(float(defeats)), int(participations)]
                #print scores[j]
                j += 1
            else:
                if start:
                    break
    except EOFError:
        pass
    return scores



def read_graph(file = sys.stdin):
    graph = {}
    #smallest = -1
    while True:
        line = file.readline()
        if line == "":
            break
        match = re.match("votes against", line)
        if match != None:
            list = line.split()
            neighborlist = set()
            for neighbor in list[7:]:
                neighborlist.add(int(neighbor))
            id = int(list[4])
            graph[id] = neighborlist
            #if smallest == -1 or id < smallest:
            #    smallest = id
    return graph

#note: this assumes that graph is bidirectional
def get_edge_count(g):
    count = 0
    for n in g:
        edges = g[n]
        for vertex in edges:
            if vertex > n:
                count += 1
    return count


def delete_node(g, node, participation = None):
    if participation != None:
        #start by removing the against votes caused by the node we are removing
        subvotes = 1.0 * len(g[node]) / participation[node]
    for peer in g[node]:
        if participation != None:
            #remove each one of the peers
            subvotes += 1.0 / participation[peer]
        g[peer].remove(node)
    del g[node]
    if participation != None:
        return subvotes
    


def get_avg_votes_against(g):
    total = 0
    for i in g:
        total += len(g[i])
    avg = 1.0 * total / len(g)
    return avg


def switch_wrong_cases(g, good, bad):
    for n in g:
        if n in good:
            mine = good
            other = bad
        else:
            mine = bad
            other = good
        pro = 0
        con = 0
        for peer in g[n]:
            if peer in mine:
                con += 1
            else:
                pro += 1
        if pro < con:
            mine.remove(n)
            other.append(n)
        #print n, "con = ", con, "pro =", pro



#reads the voting pool file and puts it to a list
def filter(file):
    allpools = {}
    f = open(file)
    j = 0
    for line in f:
        votingpool = line.split()
        finalpool = []
        for i in votingpool:
            try:
                finalpool.append(int(i))
            except ValueError:
                finalpool.append(i)
        allpools[j] = finalpool
        j += 1
    f.close()
    return allpools



#def perfect_classification(file):
#    f = open(file)
#    classification = {}
#    for line in f:
#        elements = line.split()
#        for i in [0, 1, 2]:
#            classification[int(elements[2 * i + 1])] = elements[2 * i]
#    f.close()
#    return classification

# 0 - honest
# 1 - naive malicious
# 2 - colluding
# 3 - mixed
def perfect_classification(file):
    #change table
    #e.g., table[1,2]=2 means that if node had been honest and made a malicious vote
    #it becomes a malicious node (1,X) --> X
    #0 - colluding malicious
    #1 - good
    #2 - naive malicious
    #3 - mixed malicious
    #absent values mean no change
    table = {}
    table[1, 2] = 2
    table[1, 0] = 0
    table[2, 0] = 3
    table[0, 2] = 3
    
    pools = filter(file)
    
    classification = {}
    
    for pool in pools.values():
        #print pool
        for position in [0, 1, 2]:
            node = pool[position]
            vote = pool[position + 3]
            if vote == 'X':
                vote = 2
            if node in classification:
                prevclass = classification[node]
                if (prevclass, vote) in table:
                    classification[node] = table[prevclass, vote]
            else:
                classification[node] = vote

    return classification


#converts ['1', '2', ...] to [1, 2, ...]
def intifylist(list):
    result = []
    for i in list:
        result.append(int(i))
    return result


#receives a list of sorted honest nodes
#returns a list with all nodes classified as 0 - honest or 1 - dishonest
def convert_classification(listhonetsnodes, nbrnodes):
    result = []
    pos = 0
    for i in xrange(nbrnodes):
        if pos < len(listhonetsnodes) and listhonetsnodes[pos] == i:
            #since it is in the Maximal Independent Set, it is honest
            result.append(0)
            pos += 1
        else:
            result.append(1) 
    return result


#finds the number that occurs more frequently in the list from startnode to startnode + l - 1
def findmajority(list):
    occurrences = {}
    for num in list:
        if not(num in occurrences):
            occurrences[num] = 0
        occurrences[num] += 1
    
    max = 0
    val = None
    for key in occurrences:
        if occurrences[key] > max:
            max = occurrences[key]
            val = key
    return val


def convert_for_kmeans(output):
    mylist = output.split()
    result = []
    for v in mylist:
        result.append(int(v))
    honestnbr = findmajority(result)
    
    correctedres = []
    for v in result:
        if v == honestnbr:
            correctedres.append(0)
        else:
            correctedres.append(1)
    
    return correctedres
