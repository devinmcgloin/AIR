need to create type method web for method linking.

you should know what type of thing you have, and what type of thing you're going to. methods have to match those parameters. do this via hash for the ones that we end the right way and for ones that start the right way. that ought to narrow the options alot, then you can filter on semantic tags. hash would have to contain all of the paths. not just the ending method and starting method. bummer here is that you have to walk the whole web. is there any way to get around that?

perhaps we generate this daily in a separate update utility, and load that in. have it also make function compositions of the options. so you just call one function. how to title?

-------------- What types of methods are there? ---------------------
* methods to calculate values. area of circle. air resistance etc.
    this is more of a tom decomposition problem.
* methods to read in data and send it to the right place.
    eg parsers, and whatnot
* external methods that the AI can do.
    eg send email to John.
    * generating output for the user
        taking raw data and generating a card or piece of content
* making transformations to existing data
    read in this document, split on new lines, and remove empty lines. then use this file for quotes of the day..
* methods the verify accuracy of information // perhaps this is a PA question.
    is blue a color? or is this a quotation?
* methods to extend initial capabilities. // ideally composition of the above by a programmer.
    eg a quote of the day feature that wasn't normally included.


---------------Thoughts about the Method graph------------------------
Method graph works well for functions that take one variable and return another. Not so well when it comes to methods that have to take multiple variables and returns mutiple variables.

New rule, methods can only return one type. They can still take multiple types, this is mostly to combine values into something more usefull. if you have the length of a side, but area is more usefull to the problem you want to solve. This can go on infinitely, but seems to be the primary use case.

What would be more clever is to pull out methods that have the proper starting structure. EG: They require the types of data you have or less. We can probably only map the 3 functions away from the current function being selected, so how do we choose the right one, or do we explore all possibilities?

Methods dont have too many possibilities, as they take a finite ammount of types and return a specific type. And by that I dont mean they return an int, or a char. They have to be very specific LDATA ideas, which are in a sense our primitives, while the Nouns that are combined of LDATA are what we're really after.



---------------- How do methods and verbs interact -------------------
each verb generally has a set of methods associated to it, and by word I mean more specifically a word concept or bucket. Depending on which noun is triggering it that verb would behave differently.


REMEMBER
* User doesnt give some vague thing they want to do, they will probably have to spell it out a little bit more, until we can use those ideas as standard programs.
    EG = Someone gives a sudo code program to remove duplicate files, when some one else asks to remove duplicate files, we would have to verify that their program works then just run the other users program. If we section off specific initial blocks the security overhead isnt huge. and once one solution is found we're good.
    Most problems are not unique, if we can solve them abstractly then we dont have to do it over and over again.

* Methods should be extensible in their use, perhaps a strict mode and a normal mode?
    If I ask to remove duplicates in a given folder it should be able to do that, but we should have some notion of to what extent. Does it compare the actual content? Or does it just look at timestamps and file names? the strict mode should do everything, whereas the normal mode should make a better balance of efficiency.
