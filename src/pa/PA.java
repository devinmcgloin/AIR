//package pa;
//
//import r.TreeNode;
//import r.R;
//import r.TreeNodeBase;
//
//import java.util.ArrayList;
//
///**
// * Created by Blazej on 6/3/2015.
// */
//
////External Methods of PA
//public class PA {
//
//
//    /**
//     * HashSearch (Get set -> set logic???)
//     *
//     * user defined matrices --> export set of BN in a csv file?
//     * header logic --> failed add: create header, in all set logic, make sure to
//     *      reanalyze headers into their true base nodes and re-rank them.
//     * merging nodes --> tricky, idk how that function would look like
//     */
//
//    R R;
//    public PA(){
//        R = new R();
//    }
//
//    //---------------------------------R WRAPPERS---------------------------------//
//    public void rename(String nodeName, String rAddress){
//        R.rename(nodeName, rAddress);
//    }
//
//    public void del(String nodeName, String rAddress){
//        R.del(nodeName, rAddress);
//
//    }
//    public void add(String nodeName, String rAddress){
//        R.add(nodeName, rAddress);
//
//    }
//    public void addParent(String nodeName, String rAddress){
//        R.addParent(nodeName, rAddress);
//
//    }
//
//    public TreeNode get(String rAddress) {
//        return R.get(rAddress);
//    }
//
//    public void save(){
//        R.save();
//    }
//
//    // ----------------------------- END R WRAPPERS ----------------------------------//
//
//}
