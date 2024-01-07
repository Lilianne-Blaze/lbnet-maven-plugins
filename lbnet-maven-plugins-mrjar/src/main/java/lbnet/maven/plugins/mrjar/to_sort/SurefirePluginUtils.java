package lbnet.maven.plugins.mrjar.to_sort;

import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class SurefirePluginUtils {

    public static PluginExecution addMrjExecution(Plugin surefirePlugin, PluginExecution peTemplate, int javaVersion,
            boolean pre9) {
        PluginExecution pe = peTemplate.clone();
        surefirePlugin.addExecution(pe);

        pe.setId("test-java" + javaVersion);
        MavenPluginUtils.addGoalIfAbsent(pe, "test");
        pe.setPhase("test");

        Xpp3Dom conf = MavenPluginUtils.getInitConfiguration(pe);

        String clsDir = "${project.build.directory}/classes";
        if (!pre9) {
            clsDir = clsDir + "/META-INF/versions/" + javaVersion;
        }
        String tstClsDir = "${project.build.directory}/test-classes-java" + javaVersion;
        Xpp3DomUtils.setTextChild(conf, "classesDirectory", clsDir);
        Xpp3DomUtils.setTextChild(conf, "testClassesDirectory", tstClsDir);
        Xpp3DomUtils.setTextChild(conf, "reportNameSuffix", "java" + javaVersion);

        return pe;
    }
}
