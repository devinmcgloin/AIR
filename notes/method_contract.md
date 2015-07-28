#General Notes
- Predicate functions ones that return Bools when applicable should allow for thresholds to be passed into the functions.
- All predicate functions have an uppercase P at the end of their function name.





#LDATA
- validateP(LDBN, Value) - Bool
- evaluateP(NBN, expression) - Bool
- convert(Value, unitTo) - converted Val
- getType(Unit) - returns the type of that specific unit
- getUnits(Type) - returns the units of the specified type or the default unit
- LDATAP(Type) - Bool, returns true if the type passed in matches and LDATA Type
###Expressions
- Expressions are 4-tuples in which the type is first, operator second, number third and unit forth.
###Values
- Values are 2-tuples in which the value is first, and unit second.
#Set Logic
- filter(Arraylist<NBN> nodes, ArrayList<expression> expressions), this takes an arraylist of nodes as returned from a hashsearch and filters them based on the given expressions.
- getSetMembers(NBN)
- xISyP(NBN-A, NBN-B) - Bool true if A is a member of the B set
- xINHERITy(NBN-A, NBN-B) - New NBN in which A has inherited from B.
- intersection(2 or more NBN)
- difference(2 or more NBN)
- union(2 or more NBN)
- supersetP(2 or more NBN)
- subsetP(2 or more NBN)
- memberP()
- notmemberP()
#Noun #Fill out with info from PA-Rewrite
- add(NBN, Key, Val)
- rm(NBN, Key, val)
- rm(NBN, Key)
- update(NBN, Key, OldVal, NewVal)
- getTitle(NBN)
- getName(NBN)
- getKeys(NBN)
- get(NBN, Key)
#Reader
- parse(String) -#EXPLAIN
#Matrix Gen
- genMatrix(ArrayList<String> headers, ArrayList<NBN> nodes)
#Comparison
- getDiff(fidelity, NBN, NBN) - Difference between two nodes
- compareBy(metric, NBN-A, NBN-B) - int ordering
- stableCompareBy(metric, NBN-A, NBN-B) - int ordering, that preserves order for objects with equal values.
- compare(NBN, NBN) - Proximity Metric [Not sure if we're still doing this]
- getDistribution(NBN, key) - min, Q1, Mean, Q3, Max for quantitative info, spread for qualitative info.
#PA
- hashSearch()
- put(NBN)
- get()
