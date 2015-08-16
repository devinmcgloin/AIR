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
  measurement
  count
  status_location
  geo_location
  time
  change_time
Once removed:
  Height
    measurement
  Width
    measurement
  Length
    measurement
  Temperature
    measurement
  Weight / Technically Mass.
    measurement
Twice Removed:
  Materials
    color
    Mass
    chemical composition
  Color
    red
    green
    blue
    alpha
  Geometric Shapes
    sides
    area
    perimeter
  Chemicals
    AU
    Mass
    Chemical Structure


* The once removed values are composed of the core values.
* The twice removed values are composed of either the core values, or the once removed values.
* Count is a type and class in and of itself, while ordered is more of a template.
* The only reason we really have height, length, width, temperature and weight separated is because the require units and conversions. (Perhaps the best way to deal with units is to have a simple conversion class, that isolates the whole problem. It isn't like conversions are going to change. Although, it should be able to explain the conversions and add new ones easily.) What about converting currency? Thats never static. Dont want to deal with a hardwired formula.


------------- Methods ------------------
These are ignoring location and time and really most of the more complex LDATA types. Perhaps it would be a good trial for full Method linking to implement a LDATA version that takes care of conversions and basic calculations like area or perimeter.

Bool - validate (LDBN, Value) -- says if the given value is acceptable for the given LDBN.
Bool - evaluate (expression, NBN) -- says if the NBN fulfills the given expression.
String - conversion(value, unitFrom, unitTo) -- Returns a string of the new value with unit.
Int - compareBy (Metric, NBN, NBN) -- Compares the given nodes using the LDATA comparison logic for the given metric. ?Not sure how this would work for not ordered types.
Bool - equals (NBN, NBN) -- Compares the two nodes and returns true if they are equal within a given proximity.
Double - compare (NBN, NBN) -- Compares the two nodes and returns a proximity value. Which is a metric of how alike two nodes are.
String - getDiff(NBN, NBN) -- returns the difference between two NBN nodes.

--------------- DB Scheme Mock Up ---------------------
measurement
  ^storage
    double
  ^comparison
    default unit
    ordered
  ^conversion
  ^value_ranges
    [ 0 - inf ]
  ^significant_figures
    2
count
  ^storage
    int
  ^comparison
    ordered
  ^value_ranges
    [0 - inf ]
length
  ^is
    measurement
  ^conversions
    ft->meters
      * 0.305
  ^significant_figures
    4
color
  ^composed
    red
      count = ^value_ranges [ 0 - 255 ]
    green
      count = ^value_ranges [ 0 - 255 ]
    blue
      count = ^value_ranges [ 0 - 255 ]
    alpha
      count = ^value_ranges [ 0 - 255 ]
Geometric Shapes
  ^composed
    sides // needs to include the number of sides, but also their lengths.
    perimeter //If no reference is given then the value has to be a LDBN?
    area
formula
  ^values required
  ^steps // how to make these into formulas? Or should they just link to LDATA method formulas?
