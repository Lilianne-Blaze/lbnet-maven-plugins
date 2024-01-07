package lbnet.maven.plugins.shared.utils;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

@Slf4j
public class MavenDebug {

    public static void listPlugins(MavenProject project, Consumer<String> out) {
        project.getBuild().getPlugins().forEach((t) -> {
            out.accept("plugin: " + t.getId());
        });
    }

    public static void listExecutions(Plugin plugin, Consumer<String> out) {
        plugin.getExecutions().forEach((t) -> {
            String s = String.format("execution: id=%s phase=%s", t.getId(), t.getPhase());
            out.accept(s);
        });
    }

}
