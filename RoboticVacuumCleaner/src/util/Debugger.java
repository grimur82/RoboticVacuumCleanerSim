package util;

public class Debugger {

    private static boolean mode = true;

    public static boolean getMode() {
        return mode;
    }

    public static void log(Object o) {
        if (mode)
            System.out.println(o.toString());
    }

}