package pa;

import r.TreeNode;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Blazej on 6/17/2015.
 */
public class Query {

    PA pa;
    Inheritance inherit;
    Scanner console = new Scanner(System.in);
    String input = "";

    public Query(){

        long start = System.currentTimeMillis();

        pa = new PA();

        long end = System.currentTimeMillis();

        System.out.println("Loading took: " + (end-start)/1000 + "."+ (end-start)%1000);

        inherit = new Inheritance(pa);
        System.out.println("Welcome to the PA Query System. We'll think of a better name for it shortly!");
        System.out.print(getHelp());

        //NEVER MIND, we;ll make it only work for nouns.
//        System.out.println("Please pick from the following databases: \n");
//        PABN r = pa.get("R/");
//        List<String> children = r.getOrigin().getChildrenString();
//        for(String child: children){
//            System.out.println(child);
//        }
//        input = console.nextLine().trim();
//        if(r.getOrigin().contains(input)){
//            System.out.println("i hope you choose nouns cause this only works in nouns really.");
//            rDB = pa.get("R/"+input);
//            loop();
//        }

        loop();

    }

    public void loop(){

        while(true){
            System.out.print(">>> ");

            input = console.nextLine().toLowerCase();

            if(input.equals("q")){
                pa.save();
                break;
            }else if(input.startsWith("ddev")) {
                pa.devintest();
            }else if(input.equals("bdev")){
                pa.blazetest();
            }

            parse(input);


        }
        System.out.println("Exiting Query.");

    }

    private void parse(String input){

        input = input.trim();
        String words[] = input.split(" ");

        if (words[0].equals("") || words[0].equals(" "))
            return;
        else if(words[0].equals("save"))
            pa.save();
        else if (words[0].equals("help") || words[0].equals("?")) {
            System.out.print(getHelp());
        } else if (words[0].equals("add") && words.length > 1) {
            input = input.replaceAll("add", "");
            input = input.trim();
            input = input.toLowerCase();
            pa.addBaseNode("noun", input);
        } else if (input.startsWith("+")) {
            input = input.replace('+', ' ');
            input = input.trim();
            input = input.toLowerCase();
            pa.addBaseNode("noun", input);

        } else if (words[0].equals("del") && words.length > 1) {
            input = input.replaceAll("del", "");
            input = input.trim();
            pa.del("noun", input, "R/noun/");
        } else if (input.startsWith("-")) {
            input = input.replace('-', ' ');
            input = input.trim();
            pa.del("noun", input, "R/noun/");
        } else if(input.startsWith("genset")){
            input = input.replace("genset", "");
            input = input.trim();
            pa.getset(input);
        } else if (words.length >2){
            //Could be loads of fun stuff.
            //x is y?   (boolean)



            if( input.contains(" is ") && input.endsWith("?")) {
                input = input.replace("?","");
                input.trim();
                String x = input.split(" is ")[0].trim();
                String y = input.split(" is ")[1].trim();

                //Does x exist?
                NBN xNode = pa.getNoun(x);
                if(xNode != null ){
                    //Get x
                    System.out.println(xNode.isFilter(y));
                }
                else {
                    System.out.println(x +" does not exist in R Nouns.");
                }



            }
            //x is y    (inheritence)
            else if( input.contains(" is ") && !input.endsWith("?")) {

                input.trim();
                String x = input.split(" is ")[0].trim();
                String y = input.split(" is ")[1].trim();

                //Do x and y exist?
                if( pa.isNounBase(x) && pa.isNounBase(y)){
                    //Inherit y ^has traits to x
                    inherit.xISy(x, y);
                    System.out.println(x +" ^is "+ y +" now saved.");
                }
                else {
                    System.out.println("Names supplied do not exist in R Nouns.");
                }

            }
            //x has y?
            else if(input.contains(" has ") && input.endsWith("?") ){
                input = input.replace("?","");
                input.trim();
                String x = input.split(" has ")[0].trim();
                String y = input.split(" has ")[1].trim();

                //Does x exist?
                NBN xNode = pa.getNoun(x);
                if(xNode != null ){
                    //Get x
                    System.out.println(xNode.hasFilter(y));
                }
                else {
                    System.out.println(x +" does not exist in R Nouns.");
                }
            }
            //x has y  (add has)
            else if(input.contains(" has ") && !input.endsWith("?") ){
                input.trim();
                String x = input.split(" has ")[0].trim();
                String y = input.split(" has ")[1].trim();

                //Does x exist?
                NBN xNode = pa.getNoun(x);
                if(xNode != null ){
                    //Get x
                    pa.add("noun", y, xNode.getOrigin().getAddress()+"^has/");
                }
                else {
                    System.out.println(x +" does not exist in R Nouns.");
                }
            }

        }

    }

    private String getHelp() {
        return String.format(
                "This system considers the BNs of RNouns and offers easy interaction with them\n" +
                        "\nCommands:\n" +
                        "(ADD    | +  ): Adds a BN to RN\n" +
                        "(DEL    | -  ): What do you think\n" +
                        "(   x is y   ): Triggers inheritance y -> x \n" +
                        "(   x is y?  ): True if x ^is y \n" +
                        "(   x has y  ): Adds y to ^has of x\n" +
                        "(  x has y?  ): lol\n" +
                        "(   genset   ): generates sets. ex: city`post office,population`length <= 100 ft\n" +
                        "( PRINT | ls ): There is no print.\n" +
                        "( RENAME| mv ): PATH - newName\n" +
                        "(     q      ): Quits the query mode\n" +
                        "( Help  | ?  ): For help.\n\n");
    }
}
