package lbnet.maven.plugins.smallpom.pluginprocessors;

import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.PluginInfos;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Slf4j
public class RevelcCodeFormatterPluginProcessor extends PluginProcessorBase {

    public RevelcCodeFormatterPluginProcessor() {
        super(PluginInfos.REVELC_CODE_FORMATTER);
    }

    @Override
    public void configure(MavenSession session, PluginProcessorContext context) {
        MavenProject project = session.getCurrentProject();

        if (!shouldConfig(session, context)) {
            return;
        }

        Plugin plugin = getInitPlugin(session);
        PluginExecution defExec = MavenPluginUtils.getInitExecution(plugin, getPluginInfo().getDefaultExecution());
        MavenPluginUtils.addGoalIfAbsent(defExec, getPluginInfo().getDefaultGoal());
        MavenPluginUtils.setPhaseIfAbsent(defExec, getPluginInfo().getDefaultPhase());
        Xpp3Dom execConf = MavenPluginUtils.getInitConfiguration(defExec);
        Xpp3Dom pluginConf = MavenPluginUtils.getInitConfiguration(plugin);

        configExec(pluginConf, context);
        Xpp3DomUtils.copyNoOverwrite(pluginConf, execConf);
        MavenPluginUtils.moveBeforeAll(project, plugin);
    }

    protected void configExec(Xpp3Dom conf, PluginProcessorContext context) {
        if (context.hasPreset("preset2")) {

            Xpp3Dom dirsConf = Xpp3DomUtils.getInitChild(conf, "directories");
            Xpp3DomUtils.addTextChild(dirsConf, "directory", "${project.build.sourceDirectory}");
            Xpp3DomUtils.addTextChild(dirsConf, "directory", "${project.build.sourceDirectory}/../java-templates");
            Xpp3DomUtils.addTextChild(dirsConf, "directory", "${project.build.directory}/generated-sources");
            Xpp3DomUtils.addTextChild(dirsConf, "directory", "${project.build.testSourceDirectory}");
        }

        if (context.hasPreset("leAuto")) {
            Xpp3DomUtils.setTextChild(conf, "lineEnding", "AUTO");
        } else if (context.hasPreset("leKeep")) {
            Xpp3DomUtils.setTextChild(conf, "lineEnding", "KEEP");
        } else if (context.hasPreset("leLf")) {
            Xpp3DomUtils.setTextChild(conf, "lineEnding", "LF");
        }
    }

}
