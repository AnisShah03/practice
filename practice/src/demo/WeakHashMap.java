package demo;

import java.util.Map;

public class WeakHashMap {

    public static void main(String[] args) {
        Map<String, Integer> map = new java.util.WeakHashMap<>();
        String key1 = new String("first");
        Integer val1 = 1;

        String key2 = new String("second");
        Integer val2 = 2;

        map.put(key1, val1);
        map.put(key2, val2);

        System.out.println("map = " + map);

        key1 = null;

        System.gc();

        System.out.println("map = " + map);


    }

}
