package lbnet.maven.plugins.mrjar.to_sort;

import lbnet.maven.plugins.shared.utils.plugins.DumpPluginsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.inject.Named;
import javax.inject.Singleton;
import lbnet.maven.plugins.shared.utils.MavenDebug;
import lbnet.maven.plugins.shared.utils.PluginConsts_old;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.ParamUtils;
import lbnet.maven.plugins.shared.utils.StringUtils;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import lbnet.maven.plugins.shared.utils.plugins.JarPluginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Named("mrjar")
@Singleton
@Slf4j
public class MrjarExtension extends AbstractMavenLifecycleParticipant {

    protected String logTag = "[mrjar]";

    protected String javaVersionsStr;

    protected String javaTestVersionsStr;

    protected List<Integer> javaVersions = new ArrayList<>();

    protected List<Integer> javaTestVersions = new ArrayList<>();

    protected String configJarPlugin;

    @Override
    public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
        log.info(logTag + " afterProjectsRead called...");
        log.info(logTag + " Plugin Git commit time/id: /*$GIT_COMMIT_TIME$*/, /*$GIT_COMMIT_ID_FULL$*/");
        log.info(logTag + " Plugin build time/version: /*$GIT_BUILD_TIME$*/, /*$GIT_BUILD_VERSION$*/");

        MavenProject mavenProject = session.getCurrentProject();
        Plugin plugin = MavenPluginUtils.getPluginByKeyOpt(mavenProject, PluginConsts_old.MRJAR_PLUGIN_KEY).get();
        Xpp3Dom pluginConf = MavenPluginUtils.getConfigurationOpt(plugin).orElse(new Xpp3Dom("empty"));

        // =====
        // dump effective POM etc
        String mrjarDumpWhen = Xpp3DomUtils.getTextChildOpt(pluginConf, "dumpWhen").orElse("both");
        DumpPluginsUtils.registerDumpPlugins(session, ParamUtils.singleParam("dumpWhen", mrjarDumpWhen));

        // =====
        // get, parse and check target Java versions
        javaVersionsStr = Xpp3DomUtils.getTextChildOpt(pluginConf, "javaVersions").orElse(null);
        javaTestVersionsStr = Xpp3DomUtils.getTextChildOpt(pluginConf, "javaTestVersions").orElse(javaVersionsStr);
        log.info("attr javaVersion=\"{}\"", javaVersionsStr);
        log.info("attr javaTestVersion=\"{}\"", javaTestVersionsStr);

        javaVersions = StringUtils.tokenizeToListOfIntsCW(javaVersionsStr);
        javaTestVersions = StringUtils.tokenizeToListOfIntsCW(javaTestVersionsStr);

        // =====
        // add and configure JCP preprocessor plugin
        Plugin jcpPlugin = JcpPluginUtils.getInitJcpPlugin(mavenProject);
        processJcpPlugin(jcpPlugin);

        // =====
        // add and configure Compiler plugin
        Plugin compilerPlugin = MavenPluginUtils.getInitPluginByKey(mavenProject, PluginConsts_old.COMPILER_PLUGIN_KEY,
                PluginConsts_old.COMPILER_PLUGIN_VERSION);
        processCompilerPlugin(compilerPlugin);

        // =====
        // add and configure Surefire plugin
        Plugin surefirePlugin = MavenPluginUtils.getInitPluginByKey(mavenProject, PluginConsts_old.SUREFIRE_PLUGIN_KEY,
                PluginConsts_old.SUREFIRE_PLUGIN_VERSION);
        processSurefirePlugin(surefirePlugin);

        // =====
        // add and configure Jar plugin
        configJarPlugin = Xpp3DomUtils.getTextChildOpt(pluginConf, "configJarPlugin").orElse("true");
        JarPluginUtils.configJarPlugin(session, ParamUtils.singleParam("configJarPlugin", configJarPlugin));

    }

    protected void processJcpPlugin(Plugin plugin) {
        for (Integer javaVersion : javaVersions) {
            JcpPluginUtils.addMrjExecution(plugin, javaVersion, false);
            JcpPluginUtils.addMrjExecution(plugin, javaVersion, true);
        }
    }

    protected void processCompilerPlugin(Plugin plugin) {
        PluginExecution peCompile = MavenPluginUtils.getInitExecution(plugin, "default-compile");
        PluginExecution peTestCompile = MavenPluginUtils.getInitExecution(plugin, "default-testCompile");

        plugin.removeExecution(peCompile);
        plugin.removeExecution(peTestCompile);

        for (int javaVersion : javaVersions) {
            boolean isPre9 = javaVersion < 9;
            boolean isNewest = javaVersion == javaVersions.get(javaVersions.size() - 1);

            CompilerPluginUtils.addMrjExecution(plugin, peCompile, javaVersion, false);
            CompilerPluginUtils.addMrjExecution(plugin, peTestCompile, javaVersion, true);
        }

    }

    protected void processSurefirePlugin(Plugin plugin) {
        PluginExecution peTest = MavenPluginUtils.getInitExecution(plugin, "default-test");
        plugin.removeExecution(peTest);

        for (int javaTestVersion : javaTestVersions) {
            boolean isPre9 = javaTestVersion < 9;
            boolean isNewest = javaTestVersion == javaTestVersions.get(javaTestVersions.size() - 1);

            SurefirePluginUtils.addMrjExecution(plugin, peTest, javaTestVersion, isPre9);
        }

    }

}
