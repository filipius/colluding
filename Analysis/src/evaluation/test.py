'''
Created on Nov 20, 2009

@author: filipius
'''
import precision


def test1():
    list = [1,1,1,1, 2,2,2,2, 3,3,3,3]
    honestnbr = precision.findmajority(list)
    if honestnbr != 1:
        print "error in test1"

def test2():
    list = [1,1,1,2, 2,2,2,2, 3,3,3,3]
    honestnbr = precision.findmajority(list)
    if honestnbr != 2:
        print "error in test2"

def test3():
    list = [1,1,1,1, 2,2,2,3, 3,3,3,3]
    honestnbr = precision.findmajority(list)
    if honestnbr != 3:
        print "error in test3"

def test4():
    list = [1,2,3,4,5,6,7,8,9,10,11,12,13]
    honestnbr = precision.findmajority(list)
    if honestnbr != 1:
        print "error in test4"

def test5():
    list = [1,2,3,4,5,6,6,8,9,9,11,11,11]
    honestnbr = precision.findmajority(list)
    if honestnbr != 11:
        print "error in test5"


def dothetests():
    test1()
    test2()
    test3()
    test4()
    test5()
    #testcounterrors()
    

#this requires comments in the end of the precision.py file
dothetests()
print "Finished. If there are no messages it is OK"
