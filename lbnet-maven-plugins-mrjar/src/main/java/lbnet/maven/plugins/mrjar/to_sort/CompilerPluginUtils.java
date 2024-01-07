package lbnet.maven.plugins.mrjar.to_sort;

import lbnet.maven.plugins.shared.utils.MavenDebug;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Slf4j
public class CompilerPluginUtils {

    public static PluginExecution addMrjExecution(Plugin compilerPlugin, PluginExecution peTemplate, int javaVersion,
            boolean testSources) {
        PluginExecution pe = peTemplate.clone();

        compilerPlugin.addExecution(pe);

        boolean isPre9 = javaVersion < 9;
        String testInsert = testSources ? "test-" : "";
        pe.setId((testSources ? "testCompile" : "compile") + "-java" + javaVersion);

        Xpp3Dom conf = MavenPluginUtils.getInitConfiguration(pe);
        Xpp3Dom confSrcRoots = Xpp3DomUtils.getInitChild(conf, "compileSourceRoots");
        String src = "${project.build.directory}/generated-" + testInsert + "sources/preprocessed-java" + javaVersion;
        Xpp3DomUtils.addTextChild(confSrcRoots, "src", src);

        Xpp3DomUtils.addTextChild(conf, "release", "" + javaVersion);
        Xpp3DomUtils.addTextChild(conf, "target", "" + javaVersion);
        Xpp3DomUtils.addTextChild(conf, "source", "" + javaVersion);
        if (!testSources) {
            Xpp3DomUtils.addTextChild(conf, "multiReleaseOutput", "" + !isPre9);
            // Xpp3Utils.addTextChild(conf, "enablePreview", "true" + javaVersion);
        } else {
            Xpp3DomUtils.addTextChild(conf, "multiReleaseOutput", "false");
            Xpp3DomUtils.addTextChild(conf, "outputDirectory",
                    "${project.build.directory}/test-classes-java" + javaVersion);
        }

        return pe;
    }
}
