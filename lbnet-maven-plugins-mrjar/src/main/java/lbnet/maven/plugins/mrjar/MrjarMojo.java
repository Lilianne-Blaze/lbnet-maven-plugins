package lbnet.maven.plugins.mrjar;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "mrjar", defaultPhase = LifecyclePhase.INITIALIZE, requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
@Slf4j
public class MrjarMojo extends AbstractMojo {

    /**
     * When to run things like writing effective pom and properties, "before" for initialize phase, "after" for package
     * phase, "both" for both. "after" gives a more complete data but doesn't get run if an error occurs earlier, so
     * using "both" gives either less or more complete dump depending on whether an error occurred or not.
     */
    @Parameter
    private String dumpWhen;

    @Parameter
    private String javaVersions;

    @Parameter
    private String javaTestVersions;

    @Override
    public void execute() throws MojoExecutionException {
    }

}
