import common.library
import perfectthreshold
import againstvotes
import os
import sys

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
def test_perfect_classification():
    name = "lixo.txt"
    file = open(name, "w")
    #the names of the nodes mean nothing
    file.write("\
1 2 3 1 1 1 1\n\
3 4 2 0 X X 1\n\
5 6 7 0 X X 1\n\
4 5 7 1 1 0 1\n\
7 0 6 X 1 0 1\n")
    file.close()
    out = common.library.perfect_classification(name)
    expectedresult = [1, 1, 2, 0, 2, 0, 3, 3]
    for i in xrange(len(expectedresult)):
        if out[i] != expectedresult[i]:
            sys.stderr.write("Problem in index " + str(i + 1) + " of test_perfect_classification()\n");
    os.remove(name)
    



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
def test_perfect_threshold_classify():
    name = "lixo.txt"
    file = open(name, "w")
    #the names of the nodes mean nothing
    file.write(" \
1 2 3 1 1 1 1\n\
3 4 2 0 X X 1\n\
5 6 7 0 X X 1\n\
4 5 7 1 1 0 1\n\
7 0 6 X 1 0 1\n\
votes against of node 1 --> 2 3\n\
votes against of node 2 --> 1 3 4 6\n\
votes against of node 3 --> 1 2 4 6\n\
votes against of node 4 --> 2 3 5\n\
votes against of node 5 --> 4\n\
votes against of node 6 --> 2 3\n\
votes against of node 7 -->\n\
votes against of node 0 -->\n\
")
    file.close()
    _, _, _, out = perfectthreshold.perfect_threshold_classify(name, True)
    #print out
    expectedresult = [0, 1, 1, 1, 1, 1, 1, 0]
    for i in xrange(len(expectedresult)):
        if out[i] != expectedresult[i]:
            sys.stderr.write(str(i))
            sys.stderr.write(str(out[i]))
            sys.stderr.write(str(expectedresult[i]))
            sys.stderr.write("Problem in index " + str(i + 1) + " of test_perfect_classification()\n");
    #print out
    os.remove(name)


def test_top_litigants_2():
    g = {}
    g[0] = [1, 2, 3, 4, 5]
    g[1] = [0, 2]
    g[2] = [0, 1, 3, 6]
    g[3] = [0, 2, 6]
    g[4] = [0, 6]
    g[5] = [0, 6]
    g[6] = [2, 3, 4, 5]
    colluders = againstvotes.filter_top_litigants_2(g)
    if len(colluders) != 3 or not(0 in colluders) or not(2 in colluders) or not(6 in colluders):
        sys.stderr.write("test_top_litigants_2 failed") 


def preparepg():
    p = {}
    p[0] = 10
    p[1] = 20
    p[2] = 20
    p[3] = 20
    p[4] = 20
    p[5] = 20
    p[6] = 10
    
    g = {}
    g[0] = [1, 2, 3, 4, 5]
    g[1] = [0, 2]
    g[2] = [0, 1, 3, 6]
    g[3] = [0, 2, 6]
    g[4] = [0, 6]
    g[5] = [0, 6]
    g[6] = [2, 3, 4, 5]
    return p, g

def test_remove_node():
    p, g = preparepg()
    sub = common.library.delete_node(g, 0, p)
    if abs(sub - 0.75) > 1e-6:
        sys.stderr.write("Big mistake in test_remove_node(). Wrong value was " + str(sub) + "\n")
    p, g = preparepg()
    sub = common.library.delete_node(g, 2, p)
    if abs(sub - 0.5) > 1e-6:
        sys.stderr.write("Big mistake in test_remove_node(). Wrong value was " + str(sub) + "\n")
    
def test_top_litigants_3():
    p, g = preparepg()
    colluders = againstvotes.filter_top_litigants_3(g, p)
    print colluders
    if len(colluders) != 3 or not(0 in colluders) or not(2 in colluders) or not(6 in colluders):
        sys.stderr.write("test_top_litigants_3 failed") 


test_perfect_classification()
test_perfect_threshold_classify()
test_top_litigants_2()
test_remove_node()
test_top_litigants_3()
