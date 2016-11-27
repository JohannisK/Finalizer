package nl.johannisk.finalizer.test;

import nl.johannisk.finalizer.annotation.FinalizeVars;
import nl.johannisk.finalizer.annotation.MutableVar;


/**
 * Created by johankragt on 25/11/2016.
 */
@FinalizeVars
public class Test {
    String t1 = "";
    static String t2 = "";
    @MutableVar String t3 = "";


    public static void main(String... args) {
        new Test();
        new Test2();
        new Test3();
    }

    public Test () {
        t3 = "test";

        for(@MutableVar int i = 0; i < 5; i++) {
            t3 = "" + i;
        }
    }
}

@FinalizeVars
class Test2{
    String t1 = "";
    static String t2 = "";
    @MutableVar String t3 = "";

    public Test2 () {
        t3 = "test";

        for(@MutableVar int i = 0; i < 5; i++) {
            t3 = "" + i;
        }
    }
}

class Test3{
    String t1 = "";
    static String t2 = "";
    String t3 = "";

    public Test3 () {
        t1 = "test";

        for(int i = 0; i < 5; i++) {
            t3 = "" + i;
        }
    }
}
