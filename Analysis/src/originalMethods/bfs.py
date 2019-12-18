import collections
import copy
from common import library
import random
#import sys



def mybfs_process(graph, tags, v):
    thismin = -1
    #print "v =", v, "graph[v] =", graph[v]
    for n in graph[v]:
        if n in tags and (thismin == -1 or tags[n] < thismin):
            thismin = tags[n]
    if thismin == -1:
        thismin = 0
    tags[v] = thismin + 1
    return tags

#changed from dfs by using popleft() instead of pop
def bfs(graph, root, visited = None, bfs_process  = lambda x: None):
    to_visit = collections.deque()
    if visited is None: visited = set()
 
    tags = {}
    to_visit.append(root) # Start with root
    while len(to_visit) != 0:
        v = to_visit.popleft()
        if v not in visited:
            visited.add(v)
            tags = bfs_process(graph, tags, v)
            #print "v = ", v, "graph[v] =", graph[v]
            to_visit.extend(graph[v])
    return tags
            
            

            




def pick_bfs_root1(graph):
    keys = graph.keys()
    root = keys[int(random.uniform(0, len(keys)))]
    return root

#pick the node with the highest number of enemies as the root
def pick_bfs_root2(graph):
    maxlen = -1
    for node in graph:
        if maxlen < 0 or len(graph[node]) > maxlen:
            maxlen = len(graph[node])
            maxpos = node
    return maxpos

def bfs_classify(arguments):
        
    #threshold_of_good = 0.5
    
    
    graph = library.read_graph()
    
    original_graph = copy.deepcopy(graph)
    
    #threshold = library.get_threshold(arguments)
    #deletedlist = library.filter_top_litigants(graph, threshold, True)
    
    #sys.stderr.write("Throshold = " + str(threshold) + " Nodes eliminated\n")
    #for node in deletedlist:
    #    sys.stderr.write(str(node) + " ")
    
    
    good = []
    bad = []
    done = False
    while not(done):
        #keys = graph.keys()
        root = pick_bfs_root1(graph)
        #print "root =", root 
        tags = bfs(graph, root, None, mybfs_process)
        #print tags
        
        #odd = []
        #even = []
        #for i in tags:
        #    if tags[i] % 2 == 1:
        #        odd.append(i)
        #    else:
        #        even.append(i)
        #        
        #odd.sort()
        #even.sort()
        #
        #print "Odd"
        #for i in odd:
        #    print i
        #    
        #print "Even"
        #for i in even:
        #    print i
        
        values = {}
        for i in tags.keys():
            if tags[i] in values:
                values[tags[i]].append(i)
            else:
                values[tags[i]] = [i]
    
        odd = []
        even = []
        for i in values.keys():
            #print i, ":", values[i]
            if i % 2 == 0:
                odd.extend(values[i])
            else:
                even.extend(values[i])
                
        #odd.sort()
        #even.sort()
    
        if len(even) > len(odd):
            good.extend(even)
            bad.extend(odd)
        else:
            good.extend(odd)
            bad.extend(even)
        #print "len(good) =", len(good)
        #bad.extend(deletedlist)
        #deletedlist = []
        
        #get ready for another round
        keys = graph.keys()
        for n in keys:
            if n in good or n in bad:
                library.delete_node(graph, n)
        if len(graph) == 0:
            done = True
    
    #switch_wrong_cases(graph_without_naives, good, bad)
    
    good.sort()
    bad.sort()
       
    #print "Good nodes:", good
    #print "Bad nodes:", bad
    #print "Naive nodes:", deletedlist
    
    output = []
    keys = original_graph.keys()
    keys.sort()
    for i in xrange(len(keys)):
        # print i
        n = keys[i]
        #if n in deletedlist:
        #    outcome = 0
        #else:
        if n in bad:
            outcome = 1
        else:
            outcome = 0
        output.append(outcome)
    return output
    

#check if there are votes against inside some level of the tree
#against = {}
#for i in values.keys():
#    for node in values[i]:
#        against[node] = 0
#        for neighbor in graph[node]:
#            if neighbor in values[i]:
#                against[node] += 1
#            
#for node in against:
#    print node, " -X-> ", against[node]
    
    
    