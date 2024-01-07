package lbnet.maven.plugins.smallpom.utils;

import lbnet.maven.plugins.shared.utils.ParamUtils;
import java.util.Map;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.PluginConsts_old;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Slf4j
public class ShadePluginUtils {

    public static void configShadePlugin(MavenSession mavenSession, Map<String, Object> params) {
        MavenProject project = mavenSession.getCurrentProject();
        String configShadePlugin = ParamUtils.getParamStrOpt(params, "configShadePlugin").orElse("false");
        log.info("configShadePlugin = {}", configShadePlugin);

        if (configShadePlugin.equals("false")) {
            return;
        }
        // proceed on true
        // proceed on auto, add some checks lates

        Plugin shadePlugin = MavenPluginUtils.getInitPluginByKey(project, PluginConsts_old.SHADE_PLUGIN_KEY,
                PluginConsts_old.SHADE_PLUGIN_VERSION);

        // PluginExecution defPe = MavenPluginUtils.getExecutionByIdOpt(shadePlugin, "default-shade").orElse(null);
        // if (defPe == null) {
        // return;
        // }
        PluginExecution defPe = MavenPluginUtils.getExecutionByIdOpt(shadePlugin, "default-shade").orElse(null);
        if (defPe == null) {
            defPe = MavenPluginUtils.getNewExecution(shadePlugin, "default-shade");
            defPe.setPhase("package");
            defPe.addGoal("shade");
        }

        Xpp3Dom conf = MavenPluginUtils.getInitConfiguration(defPe);
        Xpp3Dom confTransformers = Xpp3DomUtils.getInitChild(conf, "transformers");

        Xpp3Dom confTransformer = null;
        String transformerClass = "org.apache.maven.plugins.shade.resource.ManifestResourceTransformer";
        for (Xpp3Dom child : confTransformers.getChildren()) {
            if (child.getName().equals("transformer")) {
                if (child.getAttribute("implementation").equals(transformerClass)) {
                    confTransformer = child;
                }
            }
        }
        if (confTransformer == null) {
            confTransformer = Xpp3DomUtils.getInitChild(confTransformers, "transformer");
            confTransformer.setAttribute("implementation", transformerClass);
        }

        Xpp3Dom confManifestEntries = Xpp3DomUtils.getInitChild(confTransformer, "manifestEntries");
        Xpp3DomUtils.setTextChild(confManifestEntries, "Multi-Release", "true");
        Xpp3DomUtils.setTextChild(confManifestEntries, "Main-Class", "${exec.mainClass}");
    }

}
