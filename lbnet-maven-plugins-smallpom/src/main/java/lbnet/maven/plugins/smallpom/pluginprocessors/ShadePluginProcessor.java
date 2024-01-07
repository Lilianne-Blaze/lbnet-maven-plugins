package lbnet.maven.plugins.smallpom.pluginprocessors;

import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.PluginInfos;
import lbnet.maven.plugins.shared.utils.PluginOrderFixer;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Slf4j
public class ShadePluginProcessor extends PluginProcessorBase {

    public ShadePluginProcessor() {
        super(PluginInfos.SHADE);
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

        doConfig(pluginConf, context);
        Xpp3DomUtils.copyNoOverwrite(pluginConf, execConf);

        PluginOrderFixer.fixPluginOrder(project);
    }

    protected void doConfig(Xpp3Dom conf, PluginProcessorContext context) {
        if (context.hasPreset("preset2")) {
            Xpp3Dom confTransformers = Xpp3DomUtils.getInitChild(conf, "transformers");

            Xpp3Dom confTransformer1 = Xpp3DomUtils.getInitChild(confTransformers, "transformer");
            confTransformer1.setAttribute("implementation",
                    "org.apache.maven.plugins.shade.resource.ManifestResourceTransformer");
            Xpp3Dom confManEnts = Xpp3DomUtils.getInitChild(confTransformer1, "manifestEntries");
            Xpp3DomUtils.addTextChild(confManEnts, "Main-Class", "${exec.mainClass}");
            Xpp3DomUtils.addTextChild(confManEnts, "Git-Build-Time", "${git.build.time}");
            Xpp3DomUtils.addTextChild(confManEnts, "Git-Build-Version", "${git.build.version}");
            Xpp3DomUtils.addTextChild(confManEnts, "Git-Commit-Time", "${git.commit.time}");
            Xpp3DomUtils.addTextChild(confManEnts, "Git-Commit-Id-Full", "${git.commit.id.full}");
            Xpp3DomUtils.addTextChild(confManEnts, "Git-Commit-Id-Abbrev", "${git.commit.id.abbrev}");
            Xpp3DomUtils.addTextChild(confManEnts, "Git-Commit-Message-Short", "${git.commit.message.short}");

            if (context.hasPreset("cleanMetaInf")) {
                Xpp3Dom confFilters = Xpp3DomUtils.getInitChild(conf, "filters");
                Xpp3Dom confFilter1 = Xpp3DomUtils.getInitChild(confFilters, "filter");
                Xpp3DomUtils.addTextChild(confFilter1, "artifact", "*:*");
                Xpp3Dom confExcludes = Xpp3DomUtils.getInitChild(confFilter1, "excludes");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/*.SF");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/*.DSA");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/*.RSA");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/maven/**");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/AL*.*");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/LGPL*.*");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/GPL*.*");
                Xpp3DomUtils.addTextChild(confExcludes, "excludes", "META-INF/LICENSE*.*");
            }

        }
    }

}
