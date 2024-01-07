package lbnet.maven.plugins.smallpom;

import java.util.Optional;
import lbnet.maven.plugins.shared.utils.ParamUtils;
import javax.inject.Named;
import javax.inject.Singleton;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.PluginConsts_old;
import lbnet.maven.plugins.shared.utils.PluginOrderFixer;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import lbnet.maven.plugins.shared.utils.plugins.DumpPluginsUtils;
import lbnet.maven.plugins.shared.utils.plugins.JarPluginUtils;
import lbnet.maven.plugins.smallpom.pluginprocessors.GitCommitIdPluginProcessor;
import lbnet.maven.plugins.smallpom.pluginprocessors.PluginProcessorBase;
import lbnet.maven.plugins.smallpom.pluginprocessors.PluginProcessorContext;
import lbnet.maven.plugins.smallpom.pluginprocessors.RevelcCodeFormatterPluginProcessor;
import lbnet.maven.plugins.smallpom.pluginprocessors.ShadePluginProcessor;
import lbnet.maven.plugins.smallpom.utils.SourcePluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

// #ifndef GIT_BUILD_TIME
// #local GIT_BUILD_TIME = "unknown"
// #endif

@Named("smallpom")
@Singleton
@Slf4j
public class SmallpomExtension extends AbstractMavenLifecycleParticipant {

    protected String logTag = "[smallpom]";

    protected String configJarPlugin;

    @Override
    public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
        log.info(logTag + " afterProjectsRead called...");
        log.info(logTag + " Plugin Git commit time/id: /*$GIT_COMMIT_TIME$*/, /*$GIT_COMMIT_ID_FULL$*/");
        log.info(logTag + " Plugin build time/version: /*$GIT_BUILD_TIME$*/, /*$GIT_BUILD_VERSION$*/");

        MavenProject project = session.getCurrentProject();
        Plugin plugin = MavenPluginUtils.getPluginByKeyOpt(project, PluginConsts_old.SMALLPOM_PLUGIN_KEY).get();

        Xpp3Dom mainConf = MavenPluginUtils.getConfigurationOpt(plugin).orElse(new Xpp3Dom("empty"));

        // ===== GIT =====
        PluginProcessorBase gitPp = new GitCommitIdPluginProcessor();
        String gitPpConfigMode = Xpp3DomUtils.getTextChildOpt(mainConf, "configGitCommitIdPlugin")
                .orElse(SmallpomMojo.DEF_CONFIG_GIT_COMMIT_ID_PLUGIN);
        String gitPpPresets = Xpp3DomUtils.getTextChildOpt(mainConf, "gitCommitIdPluginPresets")
                .orElse(SmallpomMojo.DEF_GIT_COMMIT_ID_PLUGIN_PRESETS);
        gitPp.configure(session,
                new PluginProcessorContext().withConfigMode(gitPpConfigMode).withPresetsStr(gitPpPresets));

        // ===== REVELC FORMATTER =====
        PluginProcessorBase rcfPp = new RevelcCodeFormatterPluginProcessor();
        String rcfPpConfigMode = Xpp3DomUtils.getTextChildOpt(mainConf, "configRevelcCodeFormatterPlugin")
                .orElse(SmallpomMojo.DEF_CONFIG_REVELC_CODE_FORMATTER_PLUGIN);
        String rcfPpPresets = Xpp3DomUtils.getTextChildOpt(mainConf, "revelcCodeFormatterPluginPresets")
                .orElse(SmallpomMojo.DEF_REVELC_CODE_FORMATTER_PLUGIN_PRESETS);
        rcfPp.configure(session,
                new PluginProcessorContext().withConfigMode(rcfPpConfigMode).withPresetsStr(rcfPpPresets));

        // ===== SHADE =====
        PluginProcessorBase shdPp = new ShadePluginProcessor();
        String shdPpConfigMode = Xpp3DomUtils.getTextChildOpt(mainConf, "configShadePlugin")
                .orElse(SmallpomMojo.DEF_CONFIG_SHADE_PLUGIN);
        String shdPpPresets = Xpp3DomUtils.getTextChildOpt(mainConf, "shadePluginPresets")
                .orElse(SmallpomMojo.DEF_SHADE_PLUGIN_PRESETS);
        shdPp.configure(session,
                new PluginProcessorContext().withConfigMode(shdPpConfigMode).withPresetsStr(shdPpPresets));

        // =====
        String attrDumpWhen = Xpp3DomUtils.getTextChildOpt(mainConf, "dumpWhen").orElse("both");
        DumpPluginsUtils.registerDumpPlugins(session, ParamUtils.singleParam("dumpWhen", attrDumpWhen));

        // =====
        String configSourcePlugin = Xpp3DomUtils.getTextChildOpt(mainConf, "configSourcePlugin").orElse("false");
        SourcePluginUtils.configSourcePlugin(session, ParamUtils.singleParam("configSourcePlugin", configSourcePlugin));

        // =====
        // add and configure Jar plugin
        configJarPlugin = Xpp3DomUtils.getTextChildOpt(mainConf, "configJarPlugin").orElse("auto");
        JarPluginUtils.configJarPlugin(session, ParamUtils.singleParam("configJarPlugin", configJarPlugin));

        // =====
        // String configShadePlugin = Xpp3DomUtils.getTextChildOpt(mainConf,
        // "configShadePlugin").orElse("false");
        // ShadePluginUtils.configShadePlugin(session,
        // ParamUtils.singleParam("configShadePlugin", configShadePlugin));
        PluginOrderFixer.fixPluginOrder(project);
    }

}
