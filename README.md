# AIR
Algorithms for Intelligent Reasoning

#Usage
```
usage: AIR
 -c,--csv-reader <FILE_PATH>    Read files in from CSV.
 -d,--dict-reader <FILE_PATH>   Read in standard dictionary files
 -db,--db-path <FILE_PATH>      DB folder to read files from
 -h,--hist-reader <FILE_PATH>   Read in history to DB.
 -n,--nulp                      Launch air in NULP mode.
 -r,--repl                      Launch air in REPL mode.
 ```
 
 You must specify the `-db` path, which should be a folder that contains the database titled `noun`.
 If no additional args are present then the program will terminate. The other commands do as they say, and are pretty straightforward. 
 The db filename can be changed in `funct.Const` class.