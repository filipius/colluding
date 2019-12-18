#Uses the k-means algorithm

import common.library
import subprocess
import os
import sys

#path = "../../../gmis"
#path = "/Volumes/MobileUsers/filipius/filipius/research/artigos/2008/DISC2009/code/gmis"
#path = "/Volumes/MobileUsers/filipius/filipius/research/artigos/2008/DISC2009/code/k-means" 
path = "../k-means"


def kmeans(name = None):
    if name == None:
        f = sys.stdin
    else:
        f = open(name)
        
    
    exec1 = path + "/kmeans-classify.sh"
    
    #sys.stderr.write("////////////////////////////////// "+ os.getcwd() + "\n")

    if not(os.path.exists(exec1)):
        raise Exception("Application missing in kmeans(): " + exec1 + "\n")
    
    proc1 = subprocess.Popen(exec1, stdin = f, stdout = subprocess.PIPE)
    #proc1.stdin = sys.stdin
    #tmpfile = proc1.stdin
    #tmpfile.write(str(nbrnodes) + " " + str(nbredges) + "\n")
    #print(str(nbrnodes) + " " + str(nbredges))

    out1 = proc1.communicate()[0] #get only the standard output
    #print out1
    result = common.library.convert_for_kmeans(out1)

    if name != None:
        f.close()

    return result

#res = kmeans()
#print res
