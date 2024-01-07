package lbnet.maven.plugins.smallpom.pluginprocessors;

import java.util.ArrayList;
import java.util.List;

public class GitSettings {

    public static final List<String> SELECTED_PROPS = initSelectedProps();

    public static final List<String> SELECTED_PROPS_LCASE_DOT = SELECTED_PROPS;

    private static List<String> initSelectedProps() {
        List<String> list = new ArrayList<>();

        // note this list can change, but it will likely only be additions

        list.add("git.branch");

        list.add("git.commit.id.full");
        list.add("git.commit.id.abbrev");
        list.add("git.commit.time");
        list.add("git.commit.message.short");

        list.add("git.build.time");
        list.add("git.build.version");

        list.add("git.closest.tag.name");
        list.add("git.tags");

        return list;
    }

    public static String toUcaseUnderscore(String str) {
        return str.toUpperCase().replace(".", "_");
    }

}
