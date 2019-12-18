#import re
import common.library


def defeats_classify():
    scores = common.library.read_scores()

    result = []
    for n in xrange(len(scores)):
        s = scores[n]
        if s[1] == 0: #if the node has no defeats it is malicious colluder
            outcome = 1
        else:
            outcome = 0
        result.append(outcome)
    return result
