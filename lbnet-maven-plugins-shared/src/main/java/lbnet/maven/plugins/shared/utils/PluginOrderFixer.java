package lbnet.maven.plugins.shared.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

/**
 * Check for known ordering issues and tries to fix them.
 */
@Slf4j
public class PluginOrderFixer {

    public static void fixPluginOrder(MavenProject project) {
        final String SHADE_PLUGIN_KEY = PluginInfos.SHADE.getKey();
        final String LAUNCH4J_PLUGIN_KEY = PluginInfos.LAUNCH4J.getKey();
        final String LAUNCH4J_WRP_PLUGIN_KEY = "lbnet.maven.plugins:lbnet-maven-plugins-launch4j-wrp";

        // Shade must run before Launch4j
        tryFixPluginOrder(project, PluginConsts_old.LAUNCH4J_WRP_PLUGIN_KEY, SHADE_PLUGIN_KEY);
        tryFixPluginOrder(project, PluginConsts_old.LAUNCH4J_PLUGIN_KEY, SHADE_PLUGIN_KEY);
    }

    protected static void tryFixPluginOrder(MavenProject project, String firstKey, String secondKey) {
        Plugin first = MavenPluginUtils.getPluginByKeyOpt(project, firstKey).orElse(null);
        Plugin second = MavenPluginUtils.getPluginByKeyOpt(project, secondKey).orElse(null);
        if (MavenPluginUtils.isBefore(project, first, second)) {
            log.warn("Plugin {} is before {}, attempting to correct...", first.getId(), second.getId());
            MavenPluginUtils.moveAfter(project, first, second);
        }
    }

}
