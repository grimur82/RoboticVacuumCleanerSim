package util;

public class Debugger {

    private static boolean status = false;

    public static void log(Object o) {
        if (status)
            System.out.println(o.toString());
    }

    public static boolean getMode() {
        return status;
    }

}