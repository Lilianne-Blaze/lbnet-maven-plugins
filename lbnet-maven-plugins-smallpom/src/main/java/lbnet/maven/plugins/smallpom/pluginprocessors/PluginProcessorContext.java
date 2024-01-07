package lbnet.maven.plugins.smallpom.pluginprocessors;

import lbnet.maven.plugins.shared.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class PluginProcessorContext {

    private String configMode = "auto";

    private String presetsStr = "preset2";

    public boolean hasPreset(String presetName) {
        if (presetsStr == null) {
            return false;
        }

        return StringUtils.tokenizeToListCW(presetsStr).contains(presetName);
    }

}
