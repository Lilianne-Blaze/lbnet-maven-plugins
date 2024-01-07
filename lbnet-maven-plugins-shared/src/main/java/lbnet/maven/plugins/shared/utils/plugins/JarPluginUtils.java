package lbnet.maven.plugins.shared.utils.plugins;

import lbnet.maven.plugins.shared.utils.ParamUtils;
import java.util.Map;
import lbnet.maven.plugins.shared.utils.PluginConsts_old;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Slf4j
public class JarPluginUtils {

    public static void configJarPlugin(MavenSession mavenSession) {
        configJarPlugin(mavenSession, ParamUtils.singleParam("configJarPlugin", "true"));
    }

    public static void configJarPlugin(MavenSession mavenSession, Map<String, Object> params) {
        MavenProject project = mavenSession.getCurrentProject();
        String configJarPlugin = ParamUtils.getParamStrOpt(params, "configJarPlugin").orElse("false");

        Plugin jarPlugin = null;
        if (configJarPlugin.equals("true")) {
            jarPlugin = MavenPluginUtils.getInitPluginByKey(project, PluginConsts_old.JAR_PLUGIN_KEY,
                    PluginConsts_old.JAR_PLUGIN_VERSION);
        } else if (configJarPlugin.equals("auto")) {
            jarPlugin = MavenPluginUtils.getPluginByKeyOpt(project, PluginConsts_old.JAR_PLUGIN_KEY).orElse(null);
        }

        if (jarPlugin == null) {
            return;
        }

        log.info("configJarPlugin = {}", configJarPlugin);

        PluginExecution defPe = MavenPluginUtils.getExecutionByIdOpt(jarPlugin, "default-jar").orElse(null);
        if (defPe == null) {
            // can it even happen?
            // return;
            defPe = MavenPluginUtils.getInitExecution(jarPlugin, "default-jar");
            defPe.setPhase("package");
            defPe.addGoal("jar");
        }

        Xpp3Dom conf = MavenPluginUtils.getInitConfiguration(defPe);
        Xpp3Dom confArchive = Xpp3DomUtils.getInitChild(conf, "archive");
        Xpp3Dom confManifestEntries = Xpp3DomUtils.getInitChild(confArchive, "manifestEntries");
        Xpp3DomUtils.setTextChild(confManifestEntries, "Multi-Release", "true");
        Xpp3DomUtils.setTextChild(confManifestEntries, "Main-Class", "${mainClass}");
    }

}
