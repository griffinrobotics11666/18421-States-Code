package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.List;

public class Log {
    public static List<String> log = new ArrayList<>(0);

    public static String add(String s){
        log.add(s);
        return s;
    }

    public static Object clear(){
        log.clear();
        return null;
    }
}
