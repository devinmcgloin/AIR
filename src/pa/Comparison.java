package pa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazej on 8/15/2015.
 */
public final class Comparison {

//    public static void getDiff(NBN a, NBN b){
//        //Might be able to pass in "fidelity" level for OF?
//
//    }
//
//    public static void compareBy(metric?, NBN a, NBN b){
//        //idk what metric means...
//        //int ordering for things
//    }
//
//    public static void stableSort(metric? , ArrayList<NBN> nodes){
//        //idk what metric means...
//        //sort by a second qualifier if two are equal, without disturbing the order of the first
//    }
//
//    public static void compare(NBN a, NBN b){
//        //proximity. should be very simple.
//        //it should be simple because it could be used by the AI to try and decide if a new node that's being talked about
//            //might belong to another set. (Would allow it to ask if x is y and then inherit more keys it could ask about.
//    }

    public static void getDistribution(ArrayList<NBN> set, String key){

        LDBN ldbn = PA.getLDATA(key);
        if(ldbn.equals(null)){
            System.out.println("Comparison: You shit outta luck");
            return;
        }

        double mean;
        int count = 0;
        double total = 0;
        double sd = 0;
        double q1, q2, q3; //q2 is median, q1 is middle of first half of data set.

        ArrayList<Double> values = new ArrayList<Double>();
        ArrayList<String> units = LDATA.getUnits(ldbn);

        String value = "";
        //Check to see if they have the value, get the total
        for(NBN node: set){
            value = Noun.simpleSearch(node, key);
            if(value.startsWith("^")){
                System.out.println("Comparison: Yo i didn't even have that key or value, homes");
                continue;
            }
            if(value.contains("^")){
                System.out.println("Comparison: Yo I ain't gonna look into OF nodes                           ...bitch");
                continue;
            }

            value = value.split(" ")[0];
            if(LDATA.isNumeric(value)){
                double tmp = Double.parseDouble(value);
                values.add(tmp);
                total += tmp;
            }
            count++;
        }
        //Get the mean
        mean = total/count;

        double diffSumSq = 0;

        for(double val : values){
            diffSumSq += (val - mean)*(val-mean);
        }

        //Standard deviation
        sd = diffSumSq/count;

        if(values.size()%2 != 0){
            int middle = (count+1)/2;
            q1 = getMedian( values.subList(0, middle ) );
            q2 = values.get( middle );
            q3 = getMedian( values.subList(middle , values.size()) );
        }else{
            int mid = count/2;
            int mid2 = mid+1;
            double m1, m2;
            m1 = values.get(mid);
            m2 = values.get(mid2);


            q1 = getMedian( values.subList( 0, mid ));
            q2 = (m1 +m2)/2;
            q3 = getMedian( values.subList( mid, count));


        }




    }

    private static double getMedian(List<Double> values){


        if(values.size()%2 != 0){
            int middle = (values.size()+1)/2;
            return values.get(middle);
        }else{
            double mid1 = values.get( values.size()/2 );
            double mid2 = values.get( values.size()/2 + 1);
            return (mid1+mid2)/2;
        }



    }



}
