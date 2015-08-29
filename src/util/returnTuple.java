package util;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/15/15.
 */
public final class returnTuple {
    public final String first;
    public final String second;
    public ArrayList<String> third;

    public returnTuple(String first, String second, ArrayList<String> third) {

        this.first = first;
        this.second = second;
        this.third = third;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public ArrayList<String> getThird() {
        return third;
    }
}
