import gmis
import kmeans
import higherOrderRatio
import common
import copy
import os
import sys


def test_higherOrderRatio():
    g = {}
    g[1] = [2, 3, 4]
    g[2] = [1, 3, 5]
    g[3] = [1, 2, 4]
    g[4] = [1, 3, 5]
    g[5] = [2, 4, 6]
    g[6] = [5, 7, 12]
    g[7] = [6, 8, 10]
    g[8] = [7, 9, 11]
    g[9] = [8, 10, 11]
    g[10] = [7, 9, 11]
    g[11] = [8, 9, 10]
    g[12] = [6, 13, 14, 15]
    g[13] = [12, 14, 16]
    g[14] = [12, 13, 15, 16]
    g[15] = [12, 14, 16]
    g[16] = [13, 14, 15]
    
    
    w = {}
    w[1] = 14
    w[2] = 8
    w[3] = 15
    w[4] = 8
    w[5] = 2
    w[6] = 4
    w[7] = 2
    w[8] = 8
    w[9] = 15
    w[10] = 8
    w[11] = 14
    w[12] = 2
    w[13] = 8
    w[14] = 15
    w[15] = 8
    w[16] = 14
    
    
    try:
        common.library.ensure_bidirectionality(g)
    except common.library.GraphIsDirected, gexcept:
        print "Node", gexcept.get_node(), "is directed"
        sys.exit(1)
    
    expectedoutput = [49, 50, 50, 50, 50, 50, 50]
    output = []
    for i in xrange(7):
        gcopy = copy.deepcopy(g)
        k = higherOrderRatio.higher_order_ratio_alg(gcopy, i + 1, w)
        value = 0
        for node in k:
            value += w[node]
        output.append(value)
        #print k, value
    
    for i in xrange(len(expectedoutput)):
        if expectedoutput[i] != output[i]:
            sys.stderr.write("Error in case " + str(i) + "\n")
    sys.stderr.write("Warning: results don't match the paper...\n")



#input and then the expected result
#1 - good
#2 - good naive
#3 - good colluding
#4 - naive good
#5 - colluding good
#6 - naive colluding
#7 - naive colluding naive
#0 - good
#
#output:
#1 - good
#2 - naive
#3 - colluder
#4 - naive
#5 - colluder
#6 - mixed
#7 - mixed
#0 - good
def test_mis(function):
    name = "lixo.txt"
    file = open(name, "w")
    #the names of the nodes mean nothing
    file.write(" \
1 2 3 1 1 1 1\n\
3 4 2 0 X X 1\n\
5 6 7 0 X X 1\n\
4 5 7 1 1 0 1\n\
7 0 6 X 1 0 1\n\
votes against of node 1 (honest) --> 2 3\n\
votes against of node 2 (dishonest) --> 1 3 4 6\n\
votes against of node 3 (dishonest) --> 1 2 4 6\n\
votes against of node 4 (dishonest) --> 2 3 5\n\
votes against of node 5 (honest) --> 4\n\
votes against of node 6 (honest) --> 2 3\n\
votes against of node 7 (honest) -->\n\
votes against of node 0 (honest) -->\n\
")
    file.close()
    out = function(name)
    #print "gmis result = ", out
    #note: all the indexest of the graph above are different from gmis, as here we start at 0,
    #but gmis starts at 1, which means that we have a shift for all nodes
    expectedresult = [0, 0, 1, 1, 1, 0, 0, 0]
    for i in xrange(len(expectedresult)):
        if out[i] != expectedresult[i]:
            sys.stderr.write("Problem in index " + str(i + 1) + " of test_mis()\n");
    #print out
    os.remove(name)

def test_gmis():
    try:
        test_mis(gmis.gmis)
    except common.library.GraphIsDirected, g:
        print "Error in node:", g.get_node()

    
def test_higherOrderRatio2():
    try:
        test_mis(higherOrderRatio.higher_order_ratio)
    except common.library.GraphIsDirected, g:
        print "Error in node:", g.get_node()


def test_kmeans():
    name = "input-test-kmeans.txt"
    out = kmeans.kmeans(name)
    #print "gmis result = ", out
    #note: all the indexest of the graph above are different from gmis, as here we start at 0,
    #but gmis starts at 1, which means that we have a shift for all nodes
    expectedresult1 = [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
    expectedresult2 = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
    failed = False
    for i in xrange(len(expectedresult1)):
        if out[i] != expectedresult1[i]:
            failed = True
    if failed:
        for i in xrange(len(expectedresult2)):
            if out[i] != expectedresult2[i]:
                failed = True
    if failed:
        sys.stderr.write("Possible problem in test_kmeans(). Might only be a different random output typical of k-means: " + str(out) + "\n");


test_higherOrderRatio()
test_gmis()
test_higherOrderRatio2()
test_kmeans()
print "This is the final message. If there are no errors, tests were OK"
