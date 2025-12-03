package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generic {


    /*

        | Type                | Can read as                   | Can add (write)               |
        | ------------------- | ----------------------------- | ----------------------------- |
        | `List<T>`           | `T`                           | `T`                           |
        | `List<? extends T>` | `T`                           | **No** (only `null`)          |
        | `List<? super T>`   | `Object` (or known supertype) | **Yes:** `T` and its subtypes |
        | `List<?>`           | `Object`                      | **No** (only `null`)          |

    */

    public static void main(String[] args) {
        List<Integer> ints;
        ints = Arrays.asList(1, 2, 3);
        ints.set(0, 5);
        System.out.println(ints);

        ints = List.of(1, 2, 3);
//        ints.set(0,5);
        System.out.println(ints);

        /// //////////////////////////////


//       Rule: Extends ka matlab sirf read karo, write mat karo (except null).

        List<Integer> intList = List.of(1, 2, 3);
        List<? extends Number> numLists = intList;

        // READ: allowed
        Number n = numLists.get(0); // OK

        // WRITE: not allowed
//        numList.add(5); // ❌ Compile error
        numLists.add(null); // ✅ allowed


//        💡 Rule: Super ka matlab sirf write karo, read karoge to Object milega.

        List<Number> numList = new ArrayList<>();
        List<? super Integer> superList = numList;

        // WRITE: allowed
        superList.add(10); // ✅ Integer add allowed

        // READ: only Object
        Object obj = superList.get(0); // ✅
        Integer i = (Integer) superList.get(0); // cast lagana padega


        /*
        Easy yaad rakhne ka trick — PECS
        Producer → Extends
                (Jo list se value produce ho rahi hai → extends use karo)

        Consumer → Super
                (Jo list me value consume/insert ho rahi hai → super use karo)

        */


        List<Number> dest = new ArrayList<>();
        List<Integer> src = List.of(1, 2, 3);

        copy(dest, src);
        System.out.println(dest); // [1, 2, 3]

    }


    // src = produce values, so extends
    // dest = consume values, so super
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T t : src) {
            dest.add(t);
        }
    }



}
