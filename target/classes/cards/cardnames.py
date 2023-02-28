# card images obtained from https://code.google.com/archive/p/vector-playing-cards/

import subprocess as sp


currentvals = ["ace", "2", "3", "4", "5", "6",
               "7", "8", "9", "10", "jack", "queen", "king"]
desiredvals = ["ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX",
               "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"]

currentsuits = ["clubs", "diamonds", "hearts", "spades"]
desiredsuits = ["CLUBS", "DIAMONDS", "HEARTS", "SPADES"]

for n in currentvals:
    for c in currentsuits:
        sp.run(
            f"mv {n}_of_{c}.png {desiredvals[currentvals.index(n)]}_{desiredsuits[currentsuits.index(c)]}.png")
