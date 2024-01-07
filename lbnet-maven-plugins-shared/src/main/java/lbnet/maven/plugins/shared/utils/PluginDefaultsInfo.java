package lbnet.maven.plugins.shared.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class PluginDefaultsInfo {

    @NonNull
    private String groupId;

    @NonNull
    private String artifactId;

    @NonNull
    private String recommendedVersion;

    @With
    private String defaultExecution;

    @With
    private String defaultGoal;

    @With
    private String defaultPhase;

    public String getKey() {
        return groupId + ":" + artifactId;
    }
}
