package lbnet.maven.plugins.shared.utils.plugins;

import java.util.Map;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.PluginConsts_old;
import lbnet.maven.plugins.shared.utils.ParamUtils;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class DumpPluginsUtils {

    public static void registerDumpPlugins(MavenSession session, Map<String, Object> params) {
        MavenProject project = session.getCurrentProject();
        String dumpWhen = ParamUtils.getParamStrOpt(params, "dumpWhen").orElse(null);
        boolean addBefore = false, addAfter = false;
        if ("before".equals(dumpWhen)) {
            addBefore = true;
        } else if ("after".equals(dumpWhen)) {
            addAfter = true;
        } else if ("both".equals(dumpWhen)) {
            addBefore = addAfter = true;
        } else {
            return;
        }

        Plugin helpPlugin = MavenPluginUtils.getInitPluginByKey(project, PluginConsts_old.HELP_PLUGIN_KEY, "3.4.0");

        if (addBefore) {
            addExecutions(helpPlugin, "initialize");
        }

        if (addAfter) {
            addExecutions(helpPlugin, "package");
        }

    }

    protected static void addExecutions(Plugin plugin, String phase) {
        addExecution(plugin, "effective-pom", phase, "effective-pom.xml.log");
        addExecution(plugin, "effective-settings", phase, "effective-settings.xml.log");
        addExecution(plugin, "system", phase, "sysprops-and-envvars.log");
    }

    protected static void addExecution(Plugin plugin, String goal, String phase, String relOutput) {
        PluginExecution pe = MavenPluginUtils.getNewExecution(plugin, "log-" + goal + "-" + phase);
        pe.addGoal(goal);
        pe.setPhase(phase);
        Xpp3Dom conf = MavenPluginUtils.getInitConfiguration(pe);
        Xpp3DomUtils.addTextChild(conf, "output", "${project.build.directory}/" + relOutput);
    }

}
