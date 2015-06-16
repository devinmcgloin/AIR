Should we support all the basic SQL commands? although we ignore the table scheme.

Useful information on SQL Schemes. https://technet.microsoft.com/en-us/library/bb264565(v=sql.90).aspx

SELECT <fields> -FROM <set of base nodes> -WHERE <condition that has to be true> -ORDER BY <sorting logic>
INSERT -TO <BN added too> -VALUES <I like record syntax here.>
UPDATE -TO <BN updated too> -VALUES <I like record syntax here.>
DELETE -FROM <BN taken from> -VALUES < key val pairs to delete>
CREATE -Not needed.

---------------- R Specific ----------------
Membership - basically set logic
Matrix building - Used for items in the same set, can be modified with standard sql commands and saved in R format. (If this can be achieved it also allows for seamless import into R structure, and also makes the system somewhat backwards compatable)
Wildcards in search
Search
Validation -Used to match conditions for search or removing nodes.
Inheritance - What to copy over?
Set Logic - Most operations will be mapped over sets of similar nodes.

How to deal with mutiples additions /edits / etc to the same file?


-------- R Standards --------
Fidelity plays a part in storing and representing content.
We also have to have a system for storing relative information, specifically dates.


-------- R Information --------
What kinds of things would we support storing?
How do deal with/provide complex data types? EG 3D files, music etc.(If at all)
