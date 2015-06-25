Conversion Data
Acceptable value ranges
Assess truth of values sent to it. EG here is an RGB, is it red.

Need unit of measurement scheme
[ 98 - 100 lbs )
199 cm

Date Scheme:
5-15-1995
*-*-1995
[ 1995 - 1998 ]

---------------- LDATA ---------------

CORE:
  ordered
  count
  status_location
  geo_location
  time
Once removed:
  Height
    ordered
  Width
    ordered
  Length
    ordered
  Temperature
    ordered
  Weight / Technically Mass.
    ordered
Twice Removed:
  Materials
  Color
  Geometric Shapes
  Chemicals

The once removed values are composed of the core values.
The twice removed values are composed of either the core values, or the once removed values.
Count is a type and class in and of itself, while ordered is more of a template.


------------- Methods ------------------
Bool - validate (LDBN, Value) -- says if the given value is acceptable for the given base node
Bool - evaluate (expression, NBN) -- says if the NBN fulfills the given expression.
String - conversion(value, unitFrom, unitTo) -- Returns a string of the new value with unit.
Int - compareBy (Metric, NBN, NBN) -- Compares the given nodes using the LDATA comparison logic for the given metric. ?Not sure how this would work for not ordered types.
Bool - equals (NBN, NBN) -- Compares the two nodes and returns true if they are equal within a given proximity.
Double - compare (NBN, NBN) -- Compares the two nodes and returns a proximity value. Which is a metric of how alike two nodes are.
String - getDiff(NBN, NBN) -- returns the difference between two NBN nodes.

--------------- DB Scheme Mock Up ---------------------
ordered
  ^comparison
  ^conversion
  ^value_ranges
count
  ^comparison
  ^value_ranges
