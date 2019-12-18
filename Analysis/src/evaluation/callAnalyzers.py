from originalMethods import bfs, perfectthreshold, defeats, againstvotes
import sys
from heuristics import higherOrderRatio, gmis, kmeans


typeanalyzer = sys.argv[1]
remainingargs = sys.argv[2:]

if typeanalyzer == "PERFECT_THRESHOLD" or typeanalyzer == "4":
    votingpoolsfile = remainingargs[0]
    outcome = perfectthreshold.perfect_threshold_classify(votingpoolsfile)
elif typeanalyzer == "AGAINSTVOTESSIMPLE" or typeanalyzer == "1":
    outcome = againstvotes.against_votes_classify1(remainingargs)
elif typeanalyzer == "BFS" or typeanalyzer == "3":
    outcome = bfs.bfs_classify(remainingargs)
elif typeanalyzer == "DEFEATS" or typeanalyzer == "2":
    outcome = defeats.defeats_classify()
elif typeanalyzer == "HIGHER_ORDER" or typeanalyzer == "5":
    outcome = higherOrderRatio.higher_order_ratio()
elif typeanalyzer == "GIMS" or typeanalyzer == "6":
    outcome = gmis.gmis()
elif typeanalyzer == "KMEANS" or typeanalyzer == "7":
    outcome = kmeans.kmeans()
elif typeanalyzer == "AGAINSTVOTES1" or typeanalyzer == "8":
    outcome = againstvotes.against_votes_classify2(remainingargs)
elif typeanalyzer == "AGAINSTVOTES2" or typeanalyzer == "9":
    outcome = againstvotes.against_votes_classify3(remainingargs)
else:
    sys.stderr.write("Wrong option in callAnalyzers\n")

for i in outcome:
    sys.stdout.write(str(i) + " ")
sys.stdout.write("\n")
