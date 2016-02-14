# AIR
Algorithms for Intelligent Reasoning  
**This project is defunct**

## Overview
AIR operated under the assumption that content is key to intelligence. The central idea of the system was to represent content in a universal manner, let that content form a web of relationships, and then parse that web for information useful to NLP, and other tasks.

## Motivations

The point of computing is to learn something about the world. We tell the machine some information, and build up the content it knows about. We teach it what a clock is, what a calendar event is, or what a bicycle is. We create these objects and apply manipulations to them and store them away.

It seems then, that it very well could be better if the machine already knew what these things were. If the machines knew what it meant to add the sales from this month together, to know what it means to search for a flight, or to find a used bike for sale.

We think computers should know these things, and we want to allow users to work with computers as if the machine had the same intuitions and assumptions.

In short, we want contextual computing.

Contextual Computing means you can convey instructions in natural language, that you don’t worry about the computer being a computer, and that the technology fades into the background.

## Theory
These are some of the general ideas behind how AIR operated.

### NLP and Recursive SVO
We theorized that all sentences can be broken into discrete parts that either follow Subject - Verb structure, or Subject - Verb - Object Structure. We call a SV or SVO structure to be a molecule.

A sentence can be reduced from "The small orange cat jumped over the child's toys." to in its most simple for "cat jumped toys", such data would look like this:

```
cat{
  size: small
  color: orange
  numOf: 1
}

jumped{
  prep: over
}

toys{
  possession: child{
    numOf: 1
  }
}
```

We call this recursive SVO as each item in the sentence can hold infinitely many other SVO structures.

### Output Graph
The output graph is a dynamic programming environment. The graph is build for each starting dataset and explored using common graph search algorithms (A* or DFS). We had in mind that the graph would be goal oriented, and would explore until a suitable outcome is found.

The graph has to be generated for each time due to certain edges being invalidated for the initial dataset.

### Set Operations
We figure you can get a lot of logic by having a comprehensive set system, that does basic membership tests common set operations (union, symmetric difference, difference, intersection). As well as statistical operations on those sets. With such information you can infer answers to membership tests, if the answer is not know, as well as make inferences regarding the relationship between members of two different sets. Most queries into AIR were going to be treated as set operations.

## Usage
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

----
If you think some of these ideas are interesting, get in touch! We'd love to hear from you. 
