package nl.johannisk.finalizer.test;

import nl.johannisk.finalizer.annotation.FinalizeVars;
import nl.johannisk.finalizer.annotation.MutableVar;


/**
 * Created by johankragt on 25/11/2016.
 */
@FinalizeVars
public class Test {

    public static void main(String... args) {
        new Test();
        new Test2("test");
        new Test3();
    }

    @MutableVar String i = "test";

    public Test () {
        i = "test2";
        System.out.println(i);
        for(@MutableVar int i = 0; i < 5; i++) {
        }
    }
}

@FinalizeVars
class Test2{

    volatile int i4;

    public Test2 (String t4) {
       t4 = "dus";
       i4 = 8;
    }

    public void dus(final int i2) {
        i2 = 9;
    }
}

class Test3{
    String t1 = "";
    static String t2 = "";
    String t3 = "";

    public Test3 () {
        t1 = "test";
        t2 = "test";

        for(int i = 0; i < 5; i++) {
            t3 = "" + i;
        }
    }
}
