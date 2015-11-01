package parse;

/**
 * @author devinmcgloin
 * @version 10/31/15.
 */
public interface Predicate<E>{
    boolean apply(E o);
}
