package util;

/**
 * Created by devinmcgloin on 8/15/15.
 */
public final class returnTuple {
    public final Object first;
    public final Object second;
    public Object third;

    public returnTuple(Object first, Object second, Object third) {

        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Object getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }

    public Object getThird() {
        return third;
    }
}
