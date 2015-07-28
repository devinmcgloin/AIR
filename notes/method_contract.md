#LDATA
- validate(LDBN, Value) - Bool
- evaluate(NBN, expression) - Bool
- convert(Value, unitTo) - converted Val
###Expressions
- Expressions are 4-tuples in which the type is first, operator second, number third and unit forth.
###Values
- Values are 2-tuples in which the value is first, and unit second.
#Set Logic
- filter(Arraylist<NBN> nodes, ArrayList<expression> expressions)
- getSetMembers(NBN)
#Noun
- add()
- rm()
- update()
- get()
#Reader
- parse(String)
#Inheritance
- xISy(NBN-A, NBN-B)
#Matrix Gen
- genMatrix(ArrayList<String> headers, ArrayList<NBN> nodes)
#Comparison
- getDiff(NBN, NBN) - Difference between two nodes
- compareBy(metric, NBN-A, NBN-B) - int ordering
- equals(NBN, NBN) - Bool [Not sure if needed]
- compare(NBN, NBN) - Proximity Metric [Not sure if we're still doing this]
- getDistribution(NBN, key)
#PA
- hashSearch()
- put(NBN)
- get()
