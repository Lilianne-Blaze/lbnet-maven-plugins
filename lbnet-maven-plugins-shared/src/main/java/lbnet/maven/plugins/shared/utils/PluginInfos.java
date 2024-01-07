package lbnet.maven.plugins.shared.utils;

public class PluginInfos {

    // 2024-01-08
    public static final PluginInfo GIT_COMMIT_ID = new PluginInfo("io.github.git-commit-id",
            "git-commit-id-maven-plugin", "7.0.0", "default-get-the-git-infos", "revision", "initialize");

    // 2024-01-08, note that 2.16_0 is the last version that supports Java 8
    public static final PluginInfo REVELC_CODE_FORMATTER_JAVA8 = new PluginInfo("net.revelc.code.formatter",
            "formatter-maven-plugin", "2.16.0", "default-format", "format", "process-sources");

    public static final PluginInfo REVELC_CODE_FORMATTER = REVELC_CODE_FORMATTER_JAVA8;

    public static final PluginInfo SHADE = new PluginInfo("org.apache.maven.plugins", "maven-shade-plugin", "3.5.1",
            "default-shade", "shade", "package");

    // 2023-12-13
    public static final PluginInfo SUREFIRE = new PluginInfo("org.apache.maven.plugins", "maven-surefire-plugin",
            "3.2.2");

    // 2024-01-08
    public static PluginInfo LAUNCH4J = new PluginInfo("com.akathist.maven.plugins.launch4j", "launch4j-maven-plugin",
            "2.4.1");

}
