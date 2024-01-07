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
public class GitCommitIdPluginProcessor extends PluginProcessorBase {

    public GitCommitIdPluginProcessor() {
        super(PluginInfos.GIT_COMMIT_ID);
    }

    @Override
    public void configure(MavenSession session, PluginProcessorContext context) {
        MavenProject project = session.getCurrentProject();

        // String mode = Xpp3DomUtils.getTextChildOpt(mainConfig, "configGitCommitIdPlugin").orElse("auto");
        // String presetsStr = Xpp3DomUtils.getTextChildOpt(mainConfig,
        // "gitCommitIdPluginPresets").orElse("preset2");

        // Plugin plugin = getPluginOpt(session).orElse(null);

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
        if (context.getPresetsStr().contains("preset2")) {
            Xpp3DomUtils.setTextChild(conf, "generateGitPropertiesFile", "true");
            Xpp3DomUtils.setTextChild(conf, "generateGitPropertiesFilename",
                    "${project.build.outputDirectory}/META-INF/git.properties");
            Xpp3DomUtils.setTextChild(conf, "commitIdGenerationMode", "full");

            Xpp3Dom iopConf = Xpp3DomUtils.getInitChild(conf, "includeOnlyProperties");
            for (String propName : GitSettings.SELECTED_PROPS) {
                Xpp3DomUtils.addTextChild(iopConf, "includeOnlyProperty", propName);
            }

        }
    }

}
