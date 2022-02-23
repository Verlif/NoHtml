package idea.verlif.nohtml.builder;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/2/23 10:10
 */
public abstract class Builder {

    /**
     * 构建结果
     *
     * @return 构建完成后生产的文本
     */
    public abstract String build() throws IOException;

    /**
     * 按照内置的构建规则构建文件
     */
    public abstract void saveToFile() throws IOException;

    /**
     * 按照给予的文件生成文件
     *
     * @param file 目标文件
     * @throws IOException 文件无法写入时抛出异常
     */
    public void saveAsMdFile(File file) throws IOException {
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            if (!dir.mkdirs()) {
                throw new FileNotFoundException("Can not create directory: " + file.getPath());
            }
        }
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))) {
            writer.write(build());
            writer.flush();
        }
    }
}
