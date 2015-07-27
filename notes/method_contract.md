#LDATA
- evalidate(LDBN, Val) - Bool
- evaluate(NBN, expression) - Bool
- convert(Val, unitTo) - converted Val
#Set Logic
- filter(Arraylist<NBN> nodes, ArrayList<expression> expressions)
-
#Nouns
- add()
- rm()
- update()
#Reader
- parse(String)
#Inheritance
- xISy(NBN-A, NBN-B)
#Matrix Gen
- genMatrix(ArrayList<String> headers, ArrayList<NBN> nodes)
#Comparison
- getDiff(NBN, NBN) - Difference between two nodes
- compareBy(metric, NBN-A, NBN-B) - int ordering
- equals(NBN, NBN) - Bool
- compare(NBN, NBN) - Proximity Metric
#PA
- hashSearch()
- put(NBN)
- get()
