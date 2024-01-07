package lbnet.maven.plugins.shared.utils;

public class PluginDefaultsInfos {

    public static PluginDefaultsInfo GIT_COMMIT_ID;

    public static PluginDefaultsInfo REVELC_CODE_FORMATTER;

    public static PluginDefaultsInfo SHADE;

    static {
        staticInit();
    }

    private static void staticInit() {
        GIT_COMMIT_ID = new PluginDefaultsInfo("io.github.git-commit-id", "git-commit-id-maven-plugin", "7.0.0",
                "default-get-the-git-infos", "revision", "initialize");

        REVELC_CODE_FORMATTER = new PluginDefaultsInfo("net.revelc.code.formatter", "formatter-maven-plugin", "2.16.0",
                "default-format", "format", "process-sources");

        SHADE = new PluginDefaultsInfo("org.apache.maven.plugins", "maven-shade-plugin", "3.5.1", "default-shade",
                "shade", "package");

    }

}
