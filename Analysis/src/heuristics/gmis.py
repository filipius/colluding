#Taken from the following source:
#
#A GRASP for computing approximate solutions to the
#the maximum independent set problem.
#
#See reference: M.G.C. Resende, T.A. Feo, S.H. Smith, "FORTRAN
#subroutines for approximate solution of the maximum independent
#set problem using GRASP," AT&T Labs Research, Murray Hill, NJ, 1997.

import common
import subprocess
import os
#import sys

#path = "../../../gmis"
#path = "/Volumes/MobileUsers/filipius/filipius/research/artigos/2008/DISC2009/code/gmis"
path = "."


def gmis(name = None):
    if name == None:
        graph = common.library.read_graph()
    else:
        f = open(name)
        graph = common.library.read_graph(f)
        f.close()
    common.library.ensure_bidirectionality(graph)
    nbrnodes = len(graph)
    nbredges = common.library.get_edge_count(graph)
    #tmpname = "tmp" + str(os.getpid())
    
    exec1 = path + "/gmis"
    exec2 = path + "/filtergmisoutput.awk"
    
    #sys.stderr.write("////////////////////////////////// "+ os.getcwd() + "\n")

    if not(os.path.exists(exec1)):
        raise Exception("Application missing in gmis(): " + exec1 + "\n")
    if not(os.path.exists(exec2)):
        raise Exception("Application missing in gmis(): " + exec2 + "\n")
    
    proc1 = subprocess.Popen(exec1, stdin = subprocess.PIPE, stdout = subprocess.PIPE)
    tmpfile = proc1.stdin
    tmpfile.write(bytes(str(nbrnodes) + " " + str(nbredges) + "\n"))#, "UTF-8"))
    #print(str(nbrnodes) + " " + str(nbredges))
    allnodes = list(graph.keys())
    allnodes.sort()
    ### Note: we assume that nodes start at 0, but the program requires nodes to start at 1, so we add 1 below
    for n in allnodes:
        edges = graph[n]
        edgeslist = list(edges)
        edgeslist.sort()
        for vertex in edgeslist:
            if vertex > n:
                tmpfile.write(bytes(str(n + 1) + " " + str(vertex + 1) + "\n"))#, "UTF-8"))
                #print(str(n + 1) + " " + str(vertex + 1))

    out1 = proc1.communicate()[0] #get only the standard output
    #print out1

    proc2 = subprocess.Popen("awk -f " + exec2, stdin = subprocess.PIPE, stdout = subprocess.PIPE, shell = True)
    (out2, _) = proc2.communicate(out1)
    
    dirtylist = out2.split()
    
    honestnodes = []
    for i in dirtylist:
        honestnodes.append(int(i) - 1)
    
    result = common.library.convert_classification(honestnodes, nbrnodes)

    return result
