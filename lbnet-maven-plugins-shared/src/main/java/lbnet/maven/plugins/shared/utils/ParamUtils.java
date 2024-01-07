package lbnet.maven.plugins.shared.utils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ParamUtils {

    public static Map<String, Object> singleParam(String name, Object value) {
        return Collections.singletonMap(name, value);
    }

    public static Optional<String> getParamStrOpt(Map<String, Object> params, String name) {
        try {
            return Optional.of((String) params.get(name));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> getParamBooleanOpt(Map<String, Object> params, String name) {
        try {
            Object obj = params.get(name);
            if (obj instanceof Boolean) {
                return Optional.of((Boolean) obj);
            } else if (obj instanceof Number) {
                Number n = (Number) obj;
                return Optional.of(n.longValue() != 0);
            } else if (obj instanceof String) {
                String str = (String) obj;
                return Optional.of(Boolean.parseBoolean(str));
            }
        } catch (Exception e) {
        }
        return Optional.empty();
    }

}
