# current format: {card name/number}_of_{card suit}.png
# desired format: {card number}_{first letter of suit}.png

import subprocess as sp

currentvals = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
desiredvals = ["ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"]

currentsuits = ["c", "d", "h", "s"]
desiredsuits = ["CLUBS", "DIAMONDS", "HEARTS", "SPADES"]

for n in currentvals:
    for c in currentsuits:
        sp.run(
            f"mv {n}_{c}.png {desiredvals[currentvals.index(n)]}_{desiredsuits[currentsuits.index(c)]}.png")
