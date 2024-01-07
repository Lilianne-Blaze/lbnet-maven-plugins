package lbnet.maven.plugins.shared.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceUtils {

    public static final int DEF_BUF_SIZE = 16 * 1024;

    public static void writeResource(String resourcePath, File targetFile) throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            try (OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile, false), DEF_BUF_SIZE)) {
                byte buf[] = new byte[DEF_BUF_SIZE];
                int curLen = 0;
                while ((curLen = is.read(buf)) != -1) {
                    os.write(buf, 0, curLen);
                    os.flush();
                }
            }
        }
    }

}
