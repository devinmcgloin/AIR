Should we support all the basic SQL commands? although we ignore the table scheme.

Useful information on SQL Schemes. https://technet.microsoft.com/en-us/library/bb264565(v=sql.90).aspx

SELECT <fields> -FROM <set of base nodes> -WHERE <condition that has to be true> -ORDER BY <sorting logic>
INSERT -TO <BN added too> -VALUES <I like record syntax here.>
UPDATE -TO <BN updated too> -VALUES <I like record syntax here.>
DELETE -FROM <BN taken from> -VALUES < key val pairs to delete>
CREATE -To this set of base nodes add this attribute. Matches based on the BN title.

---------------- R Specific ----------------
Membership - basically set logic
Matrix building - Used for items in the same set, can be modified with standard sql commands and saved in R format. (If this can be achieved it also allows for seamless import into R structure, and also makes the system somewhat backwards compatable)
Wildcards in search
Search
Validation -Used to match conditions for search or removing nodes.
Inheritance - What to copy over?
Set Logic - Most operations will be mapped over sets of similar nodes.
Insert - Its like this but without that. etc

How to deal with mutiples additions /edits / etc to the same file?


-------- R Standards --------
Fidelity plays a part in storing and representing content.
We also have to have a system for storing relative information, specifically dates.


-------- R Information --------
What kinds of things would we support storing?
How do deal with/provide complex data types? EG 3D files, music etc.(If at all)


---------Spit Balling ---------
What if the database pulls in content from your accounts say email, texts, news etc and just pushes those things to you? It always keeps itself updated and is aware of the content that matters to you most. Means everything is already universal and can be rendered in a universal manner on the client side. Meaning that is super minimal and doesn't care what kinds of information you give it really. Information is all rendered in the same manner.

How to integrate into existing systems?


--------- LDATA ----------------
Returns true or false. Needs to know what you're asking of it and what node its from.
Returns True if it's valid, false if not.
