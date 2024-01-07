package lbnet.maven.plugins.smallpom;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "smallpom", defaultPhase = LifecyclePhase.INITIALIZE, requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
@Slf4j
public class SmallpomMojo extends AbstractMojo {

    public static final String DEF_DUMP_WHEN = "both";

    public static final String DEF_CONFIG_GIT_COMMIT_ID_PLUGIN = "auto";

    public static final String DEF_GIT_COMMIT_ID_PLUGIN_PRESETS = "preset2";

    public static final String DEF_CONFIG_REVELC_CODE_FORMATTER_PLUGIN = "auto";

    public static final String DEF_REVELC_CODE_FORMATTER_PLUGIN_PRESETS = "preset2, leLf";

    public static final String DEF_CONFIG_SHADE_PLUGIN = "auto";

    public static final String DEF_SHADE_PLUGIN_PRESETS = "preset2, cleanMetaInf";

    /**
     * When to run things like writing effective pom and properties, "before" for initialize phase, "after" for package
     * phase, "both" for both. "after" gives a more complete data but doesn't get run if an error occurs earlier, so
     * using "both" gives either less or more complete dump depending on whether an error occurred or not.
     */
    @Parameter(defaultValue = DEF_DUMP_WHEN)
    private String dumpWhen;

    @Parameter(defaultValue = DEF_CONFIG_GIT_COMMIT_ID_PLUGIN)
    private String configGitCommitIdPlugin;

    @Parameter(defaultValue = DEF_GIT_COMMIT_ID_PLUGIN_PRESETS)
    private String gitCommitIdPluginPresets;

    @Parameter(defaultValue = DEF_CONFIG_REVELC_CODE_FORMATTER_PLUGIN)
    private String configRevelcCodeFormatterPlugin;

    @Parameter(defaultValue = DEF_REVELC_CODE_FORMATTER_PLUGIN_PRESETS)
    private String revelcCodeFormatterPluginPresets;

    @Parameter(defaultValue = DEF_CONFIG_SHADE_PLUGIN)
    private String configShadePlugin;

    @Parameter(defaultValue = DEF_SHADE_PLUGIN_PRESETS)
    private String shadePluginPresets;

    // @Parameter
    // private String formatterPreset;
    @Parameter(defaultValue = "auto")
    private String configJarPlugin;

    @Parameter(defaultValue = "auto")
    private String configSourcePlugin;

    @Override
    public void execute() throws MojoExecutionException {
    }

}
