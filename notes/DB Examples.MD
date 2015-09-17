Things to go over: Overflow, collection, generic thing, numbers, measurements, expression, unit.

12lk3jssa2iwqe
	^name

sky
	color
		blue
	cloud

day
	^lp
		unit

unit
	^lc
		day
		hour
		foot

bridge
	road
	support
	color
	height
	width
	length
	mass

movie
	director
	producer
	character
	budget
	title
	duration
	rating
	release date

Actor is a Character, this should have been self-reconstructing.


star wars iv
	^lp
		movie
	director
	producer
	character
		star wars iv^character
	budget
	title
		star wars: episode iv a new hope
	duration
	rating
	release date

star wars iv^character
	^#of
	^collection
	^lp
		set plural
		string representable

set plural
	^#of
	^collection

string representable

number
	^lp
		ldata
	#
	^value ranges

expression
	^lp
		ldata
	operator
	value
	unit

length
	#
	unit
	^lp
		measurement
	^conversion

measurement
	#
	unit
	^lp
		number
	^conversion
