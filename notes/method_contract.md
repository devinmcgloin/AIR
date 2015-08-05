#General Notes
- Predicate functions ones that return Bools when applicable should allow for thresholds to be passed into the functions.
- All predicate functions have an uppercase P at the end of their function name.

#LDATA
- validateP(LDBN, Value) - Bool
- evaluateP(NBN, expression) - Bool
- convert(Value, unitTo) - converted Val
- getType(Unit) - returns the type of that specific unit
- getUnits(Type) - returns the units of the specified type or the default unit
- LDATAP(Type) - Bool, returns true if the type passed in matches an LDATA Type

###Expressions

- Expressions are 4-tuples in which the type is first, operator second, number third and unit forth.

###Values

- Values are 3-tuples in which the type is first, value second and unit third.

#Set Logic

- filter(Arraylist<NBN> nodes, ArrayList<expression> expressions), this takes an arraylist of nodes as returned from a hashsearch and filters them based on the given expressions.
- getSetMembers(NBN), returns the nodes that are members of the NBN set. REQUIRES: access to PA to return the NBNs that the passed in node is a member of.
- getSets(NBN), returns the sets by name, that the NBN is a member of. In turn those set names can be used to generate the set of objects that are members of that set using getSetMembers. REQUIRES: Access to PA in order to pull out nodes that are part of the set the NBN represents.
- xISyP(NBN-A, NBN-B) - Bool true if A is a member of the B set
- xINHERITy(NBN-A, NBN-B) - New NBN in which A has inherited from B.

###Set Logic Methods
Set logic methods can be called with two or more NBN (In which case the function will operate on the set members of that NBN), or they can be called with sets (In which case the functions will operate on the sets themselves.)
- intersection(2 or more NBN)
- difference(2 or more NBN)
- union(2 or more NBN)
- supersetP(2 or more NBN)
- subsetP(2 or more NBN)
- memberP()
- notmemberP()

#Noun

- add(NBN, Key, Val)
- rm(NBN, Key, val)
- rm(NBN, Key)
- update(NBN, Key, OldVal, NewVal)
- getTitle(NBN) - returns the internal R title of the node, which has to be unique. (ferrari1)
- getName(NBN) - returns the most common name of the node (Ferrari)
- getKeys(NBN)
- get(NBN, Key)
- search(NBN, key) - looks thru the given node, and the overflown nodes for the specified key. REQUIRES: pa, in order to search thru overflown nodes and access their contents. Could structure it so that there is a PA function that sends takes a NBN, and returns that NBN, plus all the NBN's that are overflown from it.

#Reader

- parse(String) -parse is responsible for taking inputs, from the terminal and returning the pattern name that needs to be called (these must be user specified, and accurate), and the arguments for that pattern. This class also has various helper functions.
- expressionParse(String Expression) - returns a arraylist of expressions.

#Matrix Gen

- genMatrix(ArrayList<String> headers, ArrayList<NBN> nodes) - creates a matrix with the given nodes as rows, and values specified by the headers arraylist, which in this case are basically keys.

#Comparison

- getDiff(fidelity, NBN, NBN) - Difference between two nodes
- compareBy(metric, NBN-A, NBN-B) - int ordering
- stableSort(metric, ArrayList<NBN>) - sort that preserves order for objects with equal values.
- sort(metric, ArrayList<NBN>) - sort that does not preserve the order
- compare(NBN, NBN) - Proximity Metric [Not sure if we're still doing this]
- getDistribution(ArrayList<NBN>, key) - min, Q1, Mean, Q3, Max for quantitative info, spread for qualitative info.

#PA

- hashSearch()
- put(NBN)
- get()
- search() - used to search thru the whole DB via name attributes, possibly the same as hash search.