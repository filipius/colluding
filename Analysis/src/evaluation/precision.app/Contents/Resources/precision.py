import common.library
import sys


#groups comes in as the group length. This function returns the start position plus the length
#e.g., [10, 80, 100] returns [[0, 10], [10, 80], [90, 100]]
def computestart(groups):
    start = 0
    retval = []
    for l in groups:
        retval.append([start, l])
        start += l
    return retval

#this receives lists in the form [num1, size1], [num2, size2] and compares the sizes ignoring the first numbers
def compareclusters(x, y):
    len1 = x[1]
    len2 = y[1]
    return len1 - len2
  
##finds the number that occurs more frequently in the list from startnode to startnode + l - 1
#def findmajority(list, startnode, l, usednumbers):
#    occurrences = {}
#    for i in range(startnode, startnode + l, 1):
#        num = list[i]
#        if not(num in occurrences):
#            occurrences[num] = 0
#        occurrences[num] += 1
#    
#    max = 0
#    val = None
#    for key in occurrences:
#        if not(key in usednumbers) and occurrences[key] > max:
#            max = occurrences[key]
#            val = key
#    return val
       

#finds how many of the l numbers starting in startnode are wrong 
def finderrors(list, startnode, l, idnumber):
    errors = 0
    for i in range(startnode, startnode + l, 1):
        if list[i] != idnumber:
            errors += 1
    return errors
    
    
##returns the number of nodes that were incorrectly classified
##returns [errors, honestnbr], which means 
##number of errors and number of the honest worker
##the number considered to be honest is the most frequent in the
##largest group
#def compute_classification_errors(list, groups):
#    #number of node where the cluster starts and respective length
#    startnbr = computestart(groups)
#
#    #numbers of clusters already used
#    usednumbers = []
#
#    numclusters = len(groups)
#    clusters = []
#    for x in xrange(numclusters):
#        clusters.append([x, groups[x]])
#
#    #XXX: this algorithm might not yield the best possible results.
#    #This sorts the cluster sizes in decreasing order,
#    #assigns a number to the largest, counts the errors
#    #does the same for the second largest (except that the number must be different)
#    #etc.
#    errors = 0
#    clusters.sort(compareclusters,reverse=True)
#    print clusters
#    honestnbr = None
#    print "Clusters =", clusters
#    for i in xrange(len(clusters)):
#        cluster = clusters[i]
#        pos = cluster[0]
#        startnode = startnbr[pos][0]
#        length = startnbr[pos][1]
#        #this is the number that identifies the cluster
#        idnumber = findmajority(list, startnode, length, usednumbers)
#        if honestnbr == None:
#            honestnbr = idnumber
#        errors += finderrors(list, startnode, length, idnumber)
#        #print errors
#        usednumbers.append(idnumber)
#        
#    return [errors, honestnbr]


#format for each voting pool:
#[[node1, node2, node3, vote1, vote2, vote3, result],
# [node1, node2, node3, vote1, vote2, vote3, result]
# etc.
#]
#honest is an array that for each node number
#contains 0 or 1 if the node is resp. dishonest honest
#returns [errors, actualerrors]
#discard - the number of voting pools that are considered as wrong
#actualerrors - the 
def counterrors(votingpools, honest):
    nbrnodes = (len(votingpools[votingpools.keys()[0]]) - 1) / 2
    totalpools = 0
    discard = 0
    initiallywrong = 0
    falsepositives = []
    falsenegatives = []
    for key in votingpools.keys():
        pool = votingpools[key]
        #print "key = ", key, " pool = ", pool
        #the final vote
        finalvote = pool[-1]
        #print pool
        #these pools are discarded by the server as there is no consensus in the voting
        if finalvote != "INVALID":
            totalpools += 1
            #we start by not accepting the result
            accept = False
            votes = {}
            i = 0
            for i in xrange(nbrnodes):
                if honest[pool[i]]:
                    vote = pool[nbrnodes + i]
                    if vote in votes:
                        votes[vote] += 1
                    else:
                        votes[vote] = 1

            #this finds the most frequent honest vote
            #in fact, it could happen that several nodes we considered as honest could 
            #be, in fact, dishonest and vote against each other
            max = 0
            for vote in votes:
                if votes[vote] > max:
                    max = votes[vote]
                    #res = vote
                
            #did we find a honest node?
            if max > 0:
                #we accept the final vote of the voting pool if it was among the most common vote among supposedly honest nodes
                #XXX: if there are voting pools with more than 3 nodes this may cause a problem
                if nbrnodes > 3:
                    raise Exception("method counterrors() may not be well done for more than 3 nodes")
                if finalvote in votes and votes[finalvote] == max:
                    accept = True
            #sys.stderr.write("Pool[" + str(key) + "]: accept = " + str(accept) + " final vote = " + str(finalvote) + "\n")
            if (not(accept)):
                #print "************************************** discard"
                discard += 1
                if finalvote == 1:
                    falsenegatives.append(key)
            else:
                if finalvote != 1:
                    #our algorithm accepted the vote, but it wasn't valid...
                    falsepositives.append(key)
                #print "++++++++++++++++++++++++++++++++++++++++++++++ false positive"
                    #print "++++++++++++++++++++++++++++++++++++++++++++++ false negative"
            if finalvote != 1:
                initiallywrong += 1
            
    return [totalpools, initiallywrong, discard, falsepositives, falsenegatives]

            
def testcounterrors():
    testlists = {}
    testlists[1] = ['d','h','d', 0,1,0, 0]  #result -> [1, 0]
    testlists[2] = ['d','h','d', 0,1,0, 1]  #result -> [0, 0]
    testlists[3] = ['h','h','d', 0,1,0, 1]  #result -> [0, 0]
    testlists[4] = ['h','h','h', 0,0,1, 1]  #result -> [1, 1]
    testlists[5] = ['h','h','h', 0,0,1, 0]  #result -> [0, 1]
    
    expectedresults = []
    expectedresults.append([1,0])
    expectedresults.append([0,0])
    expectedresults.append([0,0])
    expectedresults.append([1,1])
    expectedresults.append([0,1])
    
    observedresults = []
    for k in testlists.keys():
        test = testlists[k]
        print test
        #original = copy.copy(testlists)
        #nbrnodes = 3
        honestarray = [False, False, False]
        for i in xrange(3):
            if test[i] == 'h':
                honestarray[i] = True
            test[i] = i
        observedresults.append(counterrors([test], honestarray))
    
    for i in xrange(len(observedresults)):
        if observedresults[i] != expectedresults[i]:
            print "error in testcounterrors()"
        
    


def findhonestnodes(honestnbr, nodeclassification):
    honestarray = []
    for nodeclass in nodeclassification:
        if nodeclass == honestnbr:
            honestarray.append(True)
        else:
            honestarray.append(False)
    return honestarray


def evaluate(configfile, inputfile, votingpoolsfile):
    #filename = sys.argv[0]
    params = {}
    paramfile = open(configfile)
    for line in paramfile:
        if line.strip()[0] != '#':
            vector = line.split("=");
            paramname = vector[0].strip()
            paramvalue = vector[1].strip()
            params[paramname] = paramvalue
    paramfile.close()
        
    NUM_POOLS = int(params["NUM_POOLS"]);
    NUM_CLIENTS = int(params["NUM_CLIENTS"])
    P1 = int(params["P1"]);
    P2 = int(params["P2"]);
    P3 = int(params["P3"]);
    P4 = int(params["P4"]);
    P5 = int(params["P5"]);
    P6 = int(params["P6"]);
    P7 = int(params["P7"]);
    NUM_NAIVE_MALICIOUS = (int) ((P1 * NUM_CLIENTS) / 100.0)
    NUM_COLLUDING_MALICIOUS = (int) ((P2 * NUM_CLIENTS) / 100.0)
    NUM_MIXED_MALICIOUS = (int) ((P3 * NUM_CLIENTS) / 100.0)
    NUM_UNRELIABL_COLLUDING_MALICIOUS = (int) ((P4 * NUM_CLIENTS) / 100.0)
    NUM_GAUSSIAN_NAIVE_MALICIOUS = (int) ((P5 * NUM_CLIENTS) / 100.0)
    NUM_SINGLE_ATTACK_COLLUDERS = (int) ((P6 * NUM_CLIENTS) / 100.0)
    NUM_MISRESISTANT_COLLUDERS = (int) ((P7 * NUM_CLIENTS) / 100.0)
    NUM_GOOD = NUM_CLIENTS - (NUM_NAIVE_MALICIOUS + NUM_COLLUDING_MALICIOUS + NUM_MIXED_MALICIOUS + \
                              NUM_UNRELIABL_COLLUDING_MALICIOUS + NUM_GAUSSIAN_NAIVE_MALICIOUS + \
                              NUM_SINGLE_ATTACK_COLLUDERS + NUM_MISRESISTANT_COLLUDERS)
        
    if inputfile != sys.stdin:
        f = open(inputfile)
    else:
        f = sys.stdin
    line = f.readline()
    #contains the group of each cluster
    nodeclassification = common.library.intifylist(line.split())
    #listlen = len(nodeclassification)

    groups = [NUM_NAIVE_MALICIOUS, NUM_COLLUDING_MALICIOUS, NUM_MIXED_MALICIOUS, NUM_UNRELIABL_COLLUDING_MALICIOUS, NUM_SINGLE_ATTACK_COLLUDERS, NUM_MISRESISTANT_COLLUDERS, NUM_GOOD]

    #honestnbr is the classification number of node considered honest
    #[errors, honestnbr] = compute_classification_errors(nodeclassification, groups)
    print "pools =", NUM_POOLS, "Groups =", groups
    #print errors, "classification errors in", listlen, "nodes"
    
    #print nodeclassification
    
    
    honestnbr = 0   #honestnbr = findmajority(nodeclassification)
    
    honestarray = findhonestnodes(honestnbr, nodeclassification)
    #print "honest array =", honestarray
    votingpools = common.library.filter(votingpoolsfile)
    #print "voting pools = ", votingpools
    [totalpools, initiallywrong, discarded, falsepositives, falsenegatives]  = counterrors(votingpools, honestarray)
    print totalpools, " valid pools.", initiallywrong, " miscalculated.", discarded, "results discarded.", len(falsepositives), "false positives.", len(falsenegatives), "false negatives."
    print "false positives:", falsepositives
    print "false negatives:", falsenegatives 



#def test1():
#    list = [1,1,1,1, 2,2,2,2, 3,3,3,3]
#    groups = [4,4,4]
#    errors = compute_classification_errors(list, groups)
#    if errors[0] != 0:
#        print "error in test1"
#
#def test2():
#    list = [1,1,1,1, 2,2,2,2, 3,3,3,3]
#    groups = [5,4,3]
#    errors = compute_classification_errors(list, groups)
#    if errors[0] != 2:
#        print "error in test2"
#
#def test3():
#    list = [1,1,1,1, 2,2,2,2, 3,3,3,3]
#    groups = [5,4,2,1]
#    errors = compute_classification_errors(list, groups)
#    if errors[0] != 3:
#        print "error in test3"
#
#def test4():
#    list = [1,2,3,4,5,6,7,8,9,10,11,12,13]
#    groups = [5,4,2,2]
#    errors = compute_classification_errors(list, groups)
#    if errors[0] != 9:
#        print "error in test4"
#
#def test5():
#    list = [1,2,3,4,5,6,7,8,9,10,11,12,13]
#    groups = [5,4,0,2,2]
#    errors = compute_classification_errors(list, groups)
#    if errors[0] != 9:
#        print "error in test5"





#dothetests()

print sys.argv
if len(sys.argv) != 4:
    print "Wrong arguments. Correct format:"
    print "precision.py config voting_pools classification"
    sys.exit(1)

#classificationfilename = sys.stdin
configfilename = sys.argv[1]   # "../../ConfigFiles/config5E4.txt"
votingpoolsfile = sys.argv[2]  #"../../ConfigFiles/votingpoolsfile.txt"
classificationfilename = sys.argv[3]   #"../../ConfigFiles/input1.txt"
evaluate(configfilename, classificationfilename, votingpoolsfile)


