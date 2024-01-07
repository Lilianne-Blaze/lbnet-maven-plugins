package lbnet.maven.plugins.mrjar.to_sort;

import lbnet.maven.plugins.shared.utils.PluginConsts_old;
import lbnet.maven.plugins.shared.utils.MavenPluginUtils;
import lbnet.maven.plugins.shared.utils.Xpp3DomUtils;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class JcpPluginUtils {

    public static Plugin getInitJcpPlugin(MavenProject mavenProject) {
        Plugin p = MavenPluginUtils.getInitPluginByKey(mavenProject, PluginConsts_old.JCP_PLUGIN_KEY, "7.1.0");

        Xpp3Dom conf = MavenPluginUtils.getInitConfiguration(p);
        Xpp3DomUtils.setTextChild(conf, "replaceSources", "false");
        Xpp3DomUtils.setTextChild(conf, "allowWhitespaces", "true");
        Xpp3DomUtils.setTextChild(conf, "dontOverwriteSameContent", "true");
        Xpp3DomUtils.setTextChild(conf, "keepComments", "REMOVE_JCP_ONLY");
        Xpp3DomUtils.setTextChild(conf, "unknownVarAsFalse", "true");

        return p;
    }

    public static PluginExecution addMrjExecution(Plugin jcpPlugin, int javaVersion, boolean testSources) {
        PluginExecution pe = new PluginExecution();
        jcpPlugin.addExecution(pe);

        String testInsert = testSources ? "test-" : "";

        String id = "preprocess-" + testInsert + "sources-java" + javaVersion;
        pe.setId(id);
        pe.setPhase("generate-" + testInsert + "sources");
        pe.getGoals().add("preprocess");

        Xpp3Dom exeConf = MavenPluginUtils.getInitConfiguration(pe);
        Xpp3Dom sharedConf = MavenPluginUtils.getInitConfiguration(jcpPlugin);
        Xpp3DomUtils.copyNoOverwrite(sharedConf, exeConf);

        String targetStr = "${project.build.directory}/generated-" + testInsert + "sources/preprocessed-java"
                + javaVersion;
        Xpp3DomUtils.setTextChild(exeConf, !testSources ? "target" : "targetTest", targetStr);
        Xpp3DomUtils.setTextChild(exeConf, "replaceSources", "false");
        Xpp3DomUtils.setTextChild(exeConf, "useTestSources", "" + testSources);

        Xpp3Dom confVars = Xpp3DomUtils.getInitChild(exeConf, "vars");

        Xpp3DomUtils.addTextChild(confVars, "target.java.version", "" + javaVersion);
        Xpp3DomUtils.addTextChild(confVars, "JAVA_VERSION", "" + javaVersion);

        return pe;
    }

}
