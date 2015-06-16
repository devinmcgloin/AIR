------------R Nouns -----------------

Node everflow.

Once a node has a key value pair with more than one value.
The it overflows.
Overflow name is the name of the node it came from + the overflow term.

Need unit of measurement scheme
[98-100 lbs]
199 cm

Date Scheme:
5-15-1995
*-*-1995
[1995-1998]


Base Nodes get LDATA flag for logical data. Anything that can be sent back to LDATA gets flag

Height
  is
    LDATA

Weight
  is
    LDATA

BN
  ^has - has
  ^is - is
  ^adj - Adj
  ^logicalchild - Children (is Children)
  ^acts - Verbs it acts with
  ^acted-upon - Verbs it has done to it


Set search = "Set of cities in the US with a population > 100,000"

Types of R filters
isFilter -> isCity
hasField -> Can people have birthdates?
hasValue -> Loc = US || Pop > 100,000 (Sent to LDATA)
hasAdj -> Give me all the tall buildings in NYC.
canVerb -> Give me the people here who can drive.
hasVerb -> Give me a set of the cars that have been stolen.

Has to be able to take Adj too.
EG give me a set of all the large cities in the US.


------------ R Adj -------------------


Stores relative data, eg tall or short and remebers that its related to height.
Stores in RN devin is tall. If asked how tall will know that tall is greater than average and pull out the average of human and say taller than that.




------------- NULP --------------------

Markov or RNN predictions based on word buckets not symbols.

NULP needs to create word clusters. Need to establish how to do that.

NULP asks PAR about things with filters.
EG - I want a orange room.
It would ask about orange as related to room.


-------------LDATA ---------------------

LDATA should be in charge of conversions, and also acceptable values.
Min max ranges.
