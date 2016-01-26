# AIR
Algorithms for Intelligent Reasoning  
**This project is now defunct.**

# Overview

# Motivations 

The point of computing is to learn something about the world. We tell the machine some information, and build up the content it knows about. We teach it what a clock is, what a calendar event is, or what a bicycle is. We create these objects and apply manipulations to them and store them away.

It seems then, that it very well could be better if the machine already knew what these things were. If the machines knew what it meant to add the sales from this month together, to know what it means to search for a flight, or to find a used bike for sale.

We think computers should know these things, and we want to allow users to work with computers as if the machine had the same intuitions and assumptions.

In short, we want contextual computing.

Contextual Computing means you can convey instructions in natural language, that you don’t worry about the computer being a computer, and that the technology fades into the background.

You get to focus on being you, and not you being your technology.

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
 
 - You must specify the `-db` path, which should be a folder that contains the database titled `noun`.
 - If no additional args are present then the program will terminate. The other commands do as they say, and are pretty straightforward. 
 - The db filename can be changed in `funct.Const` class.
