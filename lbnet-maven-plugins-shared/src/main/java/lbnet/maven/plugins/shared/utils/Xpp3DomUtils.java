package lbnet.maven.plugins.shared.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class Xpp3DomUtils {

    public static List<Xpp3Dom> getChildren(Xpp3Dom dom) {
        return new ArrayList<>(Arrays.asList(dom.getChildren()));
    }

    public static void setTextChild(Xpp3Dom dom, String name, String text) {
        getInitChild(dom, name).setValue(text);
    }

    public static Xpp3Dom getInitChild(Xpp3Dom dom, String name) {
        Xpp3Dom child;
        child = dom.getChild(name);
        if (child == null) {
            child = new Xpp3Dom(name);
            dom.addChild(child);
        }
        return child;
    }

    public static Xpp3Dom addTextChild(Xpp3Dom dom, String name, String text) {
        Xpp3Dom child = new Xpp3Dom(name);
        child.setValue(text);
        dom.addChild(child);
        return child;
    }

    public static Optional<String> getTextChildOpt(Xpp3Dom dom, String name) {
        try {
            Xpp3Dom child = dom.getChild(name);
            return Optional.of(child.getValue());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static void copyNoOverwrite(Xpp3Dom source, Xpp3Dom target) {
        // org.apache.maven.shared.utils.xml.Xpp3Dom.mergeXpp3Dom(source, target);
        Xpp3Dom.mergeXpp3Dom(target, source);
    }

}
