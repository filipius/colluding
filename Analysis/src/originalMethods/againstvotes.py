from common import library
import copy
import random


def filter_top_litigants(g, threshold, delete):
    avg = library.get_avg_votes_against(g)
    todel = []
    keys = g.keys()
    #to avoid any kind of bias:
    random.shuffle(keys)
    for i in keys:
        #sys.stderr.write(str(i) + " ")
        if len(g[i]) > threshold * avg:
            if delete:
                #sys.stderr.write(str(i) + " ")
                library.delete_node(g, i)
            todel.append(i)
    #sys.stderr.writ("deleted:" + todel)
    return todel

#sort the litigants and remove all that have
#more against votes than average
#remove and recalculate on the fly
def filter_top_litigants_2(g):
    #avg = library.get_total_votes_against(g)
    library.ensure_bidirectionality(g)
    keys = g.keys()
    descending = []
    totalvotes = 0
    for i in keys:
        totalvotes += len(g[i])
        descending.append((i, len(g[i])))
    #sorts from the higher to the lower degree
    descending.sort(key=lambda x: x[1], reverse=True)
    
    malicious = set()
    avg = 1.0 * totalvotes / len(keys)
    for i in range(len(descending)):
        node = descending[i][0]
        votes = len(g[node])
        if votes > avg: #else don't stop because values can change 
            #remove the node
            malicious.add(node)
            totalvotes -= (2 * votes)  # 2 * - because it is one vote in each direction
            library.delete_node(g, node)
            #sys.stderr.write("Warning changed len(keys) to len(g.keys())\n")
            avg = 1.0 * totalvotes / len(g.keys())
            #avg = 1.0 * totalvotes / len(keys)

    return malicious


#a and b need not be integer numbers
def helper(a, b):
    if a < b:
        return -1
    else:
        if a > b:
            return 1
        else:
            return 0


# difference from filter_top_litigants_2: this method takes the
# votes against per participations instead of simple votes against 
def filter_top_litigants_3(g, participations):
    #avg = library.get_total_votes_against(g)
    library.ensure_bidirectionality(g)
    keys = g.keys()
    descending = []
    totalvotes = 0
    for i in keys:
        if participations[i] > 0:
            totalvotes += (1.0 * len(g[i]) / participations[i])
            descending.append((i, 1.0 * len(g[i]) / participations[i]))
    #sorts from the higher to the lower degree
    descending.sort(key=lambda x: x[1], reverse=True)
    
#    sys.stderr.write("descending. len = " + str(len(descending)))
#    for n in descending:
#        sys.stderr.write(str(n[0]) + " ")
    
    activenodes = len(descending)
    malicious = set()
    for i in range(len(descending)):
        node = descending[i][0]
        votes = 1.0 * len(g[node]) / participations[node]
        avg = 1.0 * totalvotes / activenodes
        if votes - avg > 1e-6: #else don't stop because values can change
            #remove the node
            #sys.stderr.write("g[" + str(node) + "] =" + str(g[node]) + " votes =" + str(votes) + " avg = " + str(avg) + "\n")
            malicious.add(node)
            subvotes = library.delete_node(g, node, participations)
            totalvotes -= subvotes
            activenodes -= 1

    return malicious    


## difference from filter_top_litigants_2: this method takes the
## votes against per participations instead of simple votes against 
#def filter_top_litigants_3(g, participations = None):
#    #avg = library.get_total_votes_against(g)
#    library.ensure_bidirectionality(g)
#    keys = g.keys()
#    descending = []
#    totalvotes = 0
#    for i in keys:
#        p = 1
#        if participations != None and participations[i] > 0:
#            p = participations[i]
#        totalvotes += (1.0 * len(g[i]) / p)
#        descending.append((i, 1.0 * len(g[i]) / p))
#    #sorts from the higher to the lower degree
#    descending.sort(lambda x, y: helper(x[1], y[1]), reverse=True)
#    
##    sys.stderr.write("descending. len = " + str(len(descending)))
##    for n in descending:
##        sys.stderr.write(str(n[0]) + " ")
#    
#    activenodes = len(descending)
#    malicious = set()
#    for i in xrange(len(descending)):
#        node = descending[i][0]
#        votes = 1.0 * len(g[node]) / participations[node]
#        avg = 1.0 * totalvotes / activenodes
#        if votes - avg > 1e-6: #else don't stop because values can change
#            #remove the node
#            #sys.stderr.write("g[" + str(node) + "] =" + str(g[node]) + " votes =" + str(votes) + " avg = " + str(avg) + "\n")
#            malicious.add(node)
#            subvotes = library.delete_node(g, node, participations)
#            totalvotes -= subvotes
#            activenodes -= 1
#
#    return malicious    




#converts from a set of dishonest nodes to the standard format
#where each node gets a number
def convert_classification(graph, dishonest):
    result = []
    listkeys=sorted(graph.keys())
    #sys.stderr.write("\nClassif\n")
    for n in listkeys:
        # print i
        #sys.stderr.write(str(i) + " ")
        #n = keys[i]
        if n in dishonest:
            outcome = 1
        else:
            outcome = 0
        result.append(outcome)

    return result



def against_votes_classify(arguments, delnodes, participations = None):
    threshold = library.get_threshold(arguments)
    
    graph = library.read_graph()
    #print graph
    #size = len(graph)
    
    #avgvotesagainst = library.get_avg_votes_against(graph)
    
    #sys.stderr.write("Average votes against:" + str(avgvotesagainst) + "\n")
    #sys.stderr.write("throshold = " + str(1.0 * threshold * avgvotesagainst))
    
    #outside = set()
    #inside = set()
    original_graph = copy.deepcopy(graph)
    if delnodes == False:
        outside = set(filter_top_litigants(graph, threshold, False))
    else:
        if participations == None:
            outside = filter_top_litigants_2(graph)
        else:
            outside = filter_top_litigants_3(graph, participations)
    
    #inside = set(graph.keys).remove(outside)
#    for n in graph:
#        if len(graph[n]) > 1.0 * threshold * avgvotesagainst:
#            outside.add(n)
#        else:
#            inside.add(n)
        
    #sys.stderr.write("Threshold = " + str(threshold) + " Naaaodes eliminated\n")
#    sys.stderr.write("output nodes")
#    for node in outside:
#        sys.stderr.write(str(node) + " ")
    #deletedlist = []
    
    #sys.stderr.write("threshold = " + str(threshold) + "\n" + "arguments = " +"\n")
            
    #for n in outside:
    #    pro
    #    for edge in graph[n]:
    #        if edge in inside:
    
    #gives worse results:
    #library.switch_wrong_cases(graph, inside, outside)
    
    return convert_classification(original_graph, outside)
    

def against_votes_classify1(arguments):
    return against_votes_classify(arguments, False)


def against_votes_classify2(arguments):
    return against_votes_classify(arguments, True)


def against_votes_classify3(arguments):
    scores = library.read_scores()
    participations = {}
    for i in scores:  #keep the third element, i.e., the number of participations
        participations[i] = scores[i][2]
    #print "participations = ", participations
    return against_votes_classify(arguments, True, participations)
