package lbnet.maven.plugins.shared.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {

    public static final String DELIM_COMA_WHITESPACE = ", \t\n\r\f";

    public static StringTokenizer tokenizerCW(String s) {
        return new StringTokenizer(s, DELIM_COMA_WHITESPACE);
    }

    public static List<String> tokenizeToListCW(String s) {
        ArrayList<String> list = new ArrayList<>();
        StringTokenizer st = tokenizerCW(s);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }

    public static List<Integer> tokenizeToListOfIntsCW(String s) {
        ArrayList<Integer> list = new ArrayList<>();
        StringTokenizer st = tokenizerCW(s);
        while (st.hasMoreTokens()) {
            list.add(Integer.valueOf(st.nextToken()));
        }
        return list;
    }

}
