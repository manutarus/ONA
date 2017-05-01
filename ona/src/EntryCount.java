import java.util.HashMap;

/**
 * Created by manu on 30/04/17.
 */

public class EntryCount<T> {
    private final HashMap<T, Integer> map = new HashMap<T, Integer>();

    public int get(T key) {
        final Integer n = map.get(key);
        return n == null ? 0 : n;
    }

    public void count(T key) {
        map.put(key, get(key) + 1);
    }
}
