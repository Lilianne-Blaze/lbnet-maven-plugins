package lbnet.maven.plugins.smallpom.utils;

import lbnet.maven.plugins.shared.utils.ParamUtils;
import java.util.Map;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.PluginConsts_old;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;

@Slf4j
public class SourcePluginUtils {

    public static void configSourcePlugin(MavenSession mavenSession, Map<String, Object> params) {
        MavenProject project = mavenSession.getCurrentProject();
        String configSourcePlugin = ParamUtils.getParamStrOpt(params, "configSourcePlugin").orElse("false");
        log.info("configSourcePlugin = {}", configSourcePlugin);

        if (configSourcePlugin.equals("false")) {
            return;
        }
        // proceed on true
        // proceed on auto, add some checks lates

        Plugin sourcePlugin = MavenPluginUtils.getInitPluginByKey(project, PluginConsts_old.SOURCE_PLUGIN_KEY,
                PluginConsts_old.SOURCE_PLUGIN_VERSION);

        PluginExecution defPe = MavenPluginUtils.getExecutionByIdOpt(sourcePlugin, "default-source").orElse(null);
        if (defPe == null) {
            defPe = MavenPluginUtils.getNewExecution(sourcePlugin, "default-source");
            // defPe.setPhase("package");
            defPe.addGoal("jar-no-fork");
        }

    }

}
