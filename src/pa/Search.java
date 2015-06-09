package pa;

import r.TreeNode;

/**
 * Created by devinmcgloin on 6/6/15.
 * Controls TreeNodeHash and is present in GeneralTree.
 *
 * CONSIDER the Following:
 *
 * ------- BASIC SEARCH ----------
 * SEARCH FORM: "tree`leaves`green"
 * First word is the primary term.
 * Other words are secondary, with the 2nd having more weight than the third.
 *
 * Steps:
 * locate first node. in this case "tree"
 * Find distance of the 2nd term from "tree", remove nodes that are further than say 3 steps away.
 * Do the same for the third search and so on.
 * Always keep nodes that do not meet the all search terms, but push them to the end of the list.
 *
 * ------- FUZZY SEARCH ----------
 * What happens when Basic search cant locate a node?
 *
 * ------- SET SEARCH ----------
 * There will be situations in which a special search is needed, this is mostly for set logic.
 *
 * First:
 * What are sets? And what kinds of things do we want from them?
 * Sets are
 *
 * ------- KEY VAL SEARCH ----------
 * This type of search should only return key value pairs.
 * EG "How tall is the Empire State Building" etc etc ad infinitum.
 *
 * Steps:
 * Goto Empire State Building node, hash search for tall or synonyms.
 * Pull out key value pair related to query.
 */
public class Search {

}
