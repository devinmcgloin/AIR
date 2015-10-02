#CSV Format

Standard Matrix formatting has tables in the first row, with items in subsequent rows. Everything is always ',' separated.

- We want to be able to create nodes from each row.
- So we need to match up column headers with key nodes.
- Each row has a title which is at the 0th index. This should be created as a node.
- Each column corresponds to a key, and that is added. Have to create the node for the val first though.
    - For numbers this is easy, but will be more complex for more complicated data types.

#History Format
- Need to have a timestamp, the action, and the arguments that were used for it.
- Should be the highest level commands.
    - For repl sessions this could be the inputs given, ditto for nulp.
- Not to keen on this setup

#Dictionary Format
- This is mostly a blaze thing.
