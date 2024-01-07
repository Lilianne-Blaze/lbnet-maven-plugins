package lbnet.maven.plugins.shared.utils;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.plugin.lifecycle.Phase;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Slf4j
public class MavenPluginUtils {

    /**
     * Get an existing plugin.
     */
    public static Optional<Plugin> getPluginByKeyOpt(MavenProject project, String key) {
        log.debug("getPluginByKeyOpt(project,'{}') called...", key);
        try {
            for (Plugin p : project.getBuild().getPlugins()) {
                if (p.getKey().equals(key)) {
                    log.debug("Found, returning {}", p);
                    return Optional.of(p);
                }
            }
        } catch (Exception e) {
        }
        log.debug("Not found.");
        return Optional.empty();
    }

    public static Plugin getInitPlugin(MavenProject project, PluginDefaultsInfo pluginInfo) {
        return getInitPluginByKey(project, pluginInfo.getKey(), pluginInfo.getRecommendedVersion());
    }

    /**
     * Get an existing plugin, or create and return a new empty plugin.
     */
    public static Plugin getInitPluginByKey(MavenProject project, String key, String defVer) {
        log.info("getInitPluginByKey(project,'{}','{}') called...", key, defVer);
        try {
            for (Plugin p : project.getBuild().getPlugins()) {
                // log.info("Checking {}" + p);
                if (p.getKey().equals(key)) {
                    log.info("Found, returning {}", p);
                    return p;
                }
            }

            Plugin p = new Plugin();
            String[] keyParts = key.split(":");
            assert keyParts.length == 2;

            p.setGroupId(keyParts[0]);
            p.setArtifactId(keyParts[1]);
            if (defVer != null) {
                p.setVersion(defVer);
            }

            project.getBuild().addPlugin(p);
            log.info("Created, returning {}", p);
            return p;
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

    /**
     * Get an existing execution.
     */
    public static Optional<PluginExecution> getExecutionByIdOpt(Plugin plugin, String executionId) {
        try {
            for (PluginExecution pe : plugin.getExecutions()) {
                if (pe.getId().equals(executionId)) {
                    return Optional.of(pe);
                }
            }
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    /**
     * Create and return a new execution, deleting the old execution with the same id if it exists.
     */
    public static PluginExecution getNewExecution(Plugin plugin, String executionId) {
        PluginExecution peRemove = null;
        for (PluginExecution pe : plugin.getExecutions()) {
            if (pe.getId().equals(executionId)) {
                peRemove = pe;
            }
        }
        if (peRemove != null) {
            plugin.removeExecution(peRemove);
        }

        PluginExecution pe = new PluginExecution();
        pe.setId(executionId);
        plugin.addExecution(pe);
        return pe;
    }

    public static PluginExecution getInitExecution(Plugin plugin, String executionId) {
        PluginExecution pe = null;
        for (PluginExecution peCandidate : plugin.getExecutions()) {
            if (peCandidate.getId().equals(executionId)) {
                pe = peCandidate;
            }
        }
        if (pe == null) {
            pe = new PluginExecution();
            pe.setId(executionId);
            plugin.addExecution(pe);
        }
        return pe;
    }

    public static Optional<Xpp3Dom> getConfigurationOpt(Plugin plugin) {
        try {
            return Optional.of((Xpp3Dom) plugin.getConfiguration());
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    public static Xpp3Dom getInitConfiguration(Plugin plugin) {
        Xpp3Dom d = (Xpp3Dom) plugin.getConfiguration();
        if (d == null) {
            d = new Xpp3Dom("configuration");
            plugin.setConfiguration(d);
        }
        return d;
    }

    public static Xpp3Dom getInitConfiguration(PluginExecution pe) {
        Xpp3Dom d = (Xpp3Dom) pe.getConfiguration();
        if (d == null) {
            d = new Xpp3Dom("configuration");
            pe.setConfiguration(d);
        }
        return d;
    }

    public static void addGoalIfAbsent(PluginExecution pe, String goal) {
        try {
            for (String goalName : pe.getGoals()) {
                if (goalName.equals(goal)) {
                    return;
                }
            }
        } catch (Exception e) {
        }
        pe.addGoal(goal);
    }

    public static void setPhaseIfAbsent(PluginExecution pe, String phase) {
        try {
            if (pe.getPhase() == null || pe.getPhase().equals("")) {
                pe.setPhase(phase);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Checks if the first plugin is declared/configured before the second one.
     */
    public static boolean isBefore(MavenProject project, Plugin firstPlugin, Plugin secondPlugin) {
        try {
            Build build = project.getBuild();
            List<Plugin> plugins = build.getPlugins();
            if (plugins.contains(firstPlugin) && plugins.contains(secondPlugin)) {
                return plugins.indexOf(firstPlugin) < plugins.indexOf(secondPlugin);
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Attempts to move the first plugin just after the second one.
     */
    public static boolean moveAfter(MavenProject project, Plugin firstPlugin, Plugin secondPlugin) {
        try {
            Build build = project.getBuild();
            List<Plugin> plugins = build.getPlugins();
            if (plugins.contains(firstPlugin) && plugins.contains(secondPlugin)) {
                int secondIndex = plugins.indexOf(secondPlugin);
                plugins.remove(firstPlugin);
                plugins.add(secondIndex, firstPlugin);
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean moveBeforeAll(MavenProject project, Plugin plugin) {
        try {
            Build build = project.getBuild();
            List<Plugin> plugins = build.getPlugins();
            plugins.remove(plugin);
            plugins.add(0, plugin);
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean moveAfterAll(MavenProject project, Plugin plugin) {
        try {
            Build build = project.getBuild();
            List<Plugin> plugins = build.getPlugins();
            plugins.remove(plugin);
            plugins.add(plugin);
        } catch (Exception e) {
        }
        return false;
    }

}
