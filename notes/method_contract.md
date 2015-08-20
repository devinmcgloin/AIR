#General Notes
- Predicate functions ones that return Bools when applicable should allow for thresholds to be passed into the functions.
- All predicate functions have an uppercase P at the end of their function name.

#LDATA
Default return value is true, as to not filter out nodes on accout of improper expressions.
- validateP(LDBN, Value) - Bool
- validateP(NBN, expression) - Bool
- convert(Value, unitTo) - converted Val
- getType(Unit) - returns the type of that specific unit
- getUnits(Type) - returns the units of the specified type or the default unit
- LDATAP(Type) - Bool, returns true if the type passed in matches an LDATA Type

###Expressions

- Expressions are 4-tuples in which the type is first, operator second, number third and unit forth.

#Set Logic

- filter(Arraylist<NBN> nodes, ArrayList<expression> expressions), this takes an arraylist of nodes as returned from a hashsearch and filters them based on the given expressions.
- genSet()
- hasFilter(NBN, String[] attributes)
- isFilter(NBN, String[] attributes)
- LDATAFilter(NBN, ArrayList<expressions> expressions)
- getSetMembers(NBN), returns the nodes that are members of the NBN set. REQUIRES: access to PA to return the NBNs that the passed in node is a member of.
- getSets(NBN), returns the sets by name, that the NBN is a member of. In turn those set names can be used to generate the set of objects that are members of that set using getSetMembers. REQUIRES: Access to PA in order to pull out nodes that are part of the set the NBN represents.
- xISyP(NBN-A, NBN-B) - Bool true if A is a member of the B set
- xINHERITy(NBN-A, NBN-B) - New NBN in which A has inherited from B. Needs to be changed to allow for overflow reorganization.
- xLikeY(NBN-A, NBN-B) - Inheritance without the set, you get the keys but no values and are not logical children.

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

- add(NBN, Key, Val) - EFFECTED BY OVERFLOW, CHECK WITH LDATA TO CONFIRM ITS A VALID VALUE.
- rm(NBN, Key, val) - EFFECTED BY OVERFLOW
- rm(NBN, Key)- EFFECTED BY OVERFLOW
- update(NBN, Key, OldVal, NewVal)
- getTitle(NBN) - returns the internal R title of the node, which has to be unique. (ferrari1)
- getName(NBN) - returns the most common name of the node (Ferrari), right now is identical to get title.
- getKeys(NBN)
- get(NBN, Key)
- genSearch(key) - uses contextual search and other search methods.
- overflowSearch(NBN, key - Checks checks simple search first for the given node, then checks the overflown nodes. Returns all values that match
- simpleSearch(NBN, key) - searching only the immediate NBN passed in
- bestGuess(NBN, key) - uses stats to guess how good something is
- contextSearch(NBN, key, filters) - searches the whole database for nodes that have this key and match these filters.

###Record
(add, )


#Reader
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
