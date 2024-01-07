# Plugin lbnet-maven-plugins-smallpom usage


Relevant pom.xml fragments
```
    <properties>

        <!-- set the main class for Shade / Launch4j -->
        <exec.mainClass>com.example.mrjar.demo1.App</exec.mainClass>

    </properties>
```

```
    <!-- Maven repos containing the plugin -->
    <!-- note you can use both or just a single one. -->
    <pluginRepositories>

        <!-- you probably want this one -->
        <pluginRepository>
            <id>repsy-lbnet-releases</id>
            <url>https://repo.repsy.io/mvn/lilianne_blaze/lbnet-releases</url>
            <releases>
                <enabled>true</enabled>
            </releases>
        </pluginRepository>

        <pluginRepository>
            <id>repsy-lbnet-snapshots</id>
            <url>https://repo.repsy.io/mvn/lilianne_blaze/lbnet-snapshots</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>

    </pluginRepositories>
```

```
            <plugin>
                <groupId>lbnet.maven.plugins</groupId>
                <artifactId>lbnet-maven-plugins-smallpom</artifactId>
                <version>0.0.55-SNAPSHOT</version>
                <extensions>true</extensions>
                <configuration>
                    <dumpWhen>both</dumpWhen>
                    <configJarPlugin>auto</configJarPlugin>
                    <configShadePlugin>auto</configShadePlugin>
                    <configSourcePlugin>auto</configSourcePlugin>
                </configuration>
            </plugin>
```

```
<extensions>true</extensions>
```
This must be set to true for the plugin to work. This is *not* optional.

```
<dumpWhen>both</dumpWhen>
```
Optional, valid values "both/before/after", default "both".
Makes the plugin dump data such as effective pom, properties, variables, etc.
"before" makes it do very early in the build process, "after" after most stages are done.
"both" does both, the rationale being it collects whatever data is available near start,
then updates it if any changed after packaging the jar. If the build fails, there's
still the earlier data available.



