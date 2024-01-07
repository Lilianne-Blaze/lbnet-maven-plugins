package lbnet.maven.plugins.smallpom.pluginprocessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.PluginInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Slf4j
public abstract class PluginProcessorBase {

    @Getter
    private PluginInfo pluginInfo;

    public PluginProcessorBase(PluginInfo defaultsInfo) {
        this.pluginInfo = defaultsInfo;
    }

    public abstract void configure(MavenSession session, PluginProcessorContext context);

    public Optional<Plugin> getPluginOpt(MavenSession session) {
        return MavenPluginUtils.getPluginByKeyOpt(session.getCurrentProject(), pluginInfo.getKey());
    }

    public boolean shouldConfig(MavenSession session, PluginProcessorContext context) {
        Plugin plugin = getPluginOpt(session).orElse(null);
        String mode = context.getConfigMode();

        switch (mode) {
        case "true":
            return true;
        case "false":
            return false;
        case "auto":
            return plugin == null;
        default:
            throw new IllegalArgumentException("mode allowed values: true/false/auto");
        }
    }

    public Plugin getInitPlugin(MavenSession session) {
        return MavenPluginUtils.getInitPluginByKey(session.getCurrentProject(), getPluginInfo().getKey(),
                getPluginInfo().getRecommendedVersion());
    }

    public List<String> parsePresets(String presetsStr) {
        List<String> presetsList = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(presetsStr, ", \t\n\r\f");
        while (st.hasMoreTokens()) {
            presetsList.add(st.nextToken());
        }
        return presetsList;
    }

    public String getShortClassName() {
        return getClass().getSimpleName();
    }

}
