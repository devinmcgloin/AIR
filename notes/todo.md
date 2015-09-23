TODO:

- Think about types of sets, keys and ldata values
- Questions & Accel Learning:

    1. Populating NBN
    2. Identify ^LP that share certain Keys to narrow sets it could belong to

        2.5) Keep in mind ^LP should account for OF keys as well.
             Need to account some Keys are more valuable than others depending on the NBN
    3. Use getProbability(<set>, key, value) to narrow down on value

- Probability of a key being in a set of NBNs. (Related to 2.5). (Simple T/F probability).
    EX: Prob of car having a back seat...prob sports car does not have a back seat...

- Figure out if OF as a Value in a Key becomes ^LC of Key or ^LC of ^LP's OF... Mustang+eng is Car+engine or engine...

- Population of OF node/creation of an OF node in context. (Needs higher level logic, perhaps REPL)

- Create NBN should add the default ^carrot headers.

- Add carrot ^notKeys. Store keys that the NBN explicitly does not have. The only two functions effected
    are xINHERITy (which now has to check to make sure it doesn't add to x something it explicitly
    shouldn't have... and Noun.rm(key, to make sure to add the key being removed to the ^notKeys).

- Can validate values written to a node, by checking in Noun.add(NBN, Key, Val), if the value is a node member of the key set.
    EX: add(John, born, Washington DC) washington DC must be a member of the places set.


Useful:

- Values: LData, # of Key, Set Answer, Overflown Node (not Ans to set), Invisible node from parent

- Once a node filters to a very specific adhoc set using Key and Value distributions, it is easier to
    predict values for the remaining Keys. (Rear wheel drive, American made, Year 2008, 8 Cyl) makes
    it much easier to guess top speed.
    
- Sorting adhoc sets by a specific value would be sweet. 