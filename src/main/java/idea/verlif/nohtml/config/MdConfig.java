package idea.verlif.nohtml.config;

import java.io.*;
import java.util.Properties;

/**
 * @author Verlif
 */
public class MdConfig {

    private String profilePath;

    private String footerPath;

    /**
     * 标题
     */
    private String title;

    /**
     * MD文件列表最大数量
     */
    private int size;

    /**
     * MD文件列表中，每个概览的允许长度
     */
    private int length;

    public MdConfig() {
    }

    public void loadConfig() throws IOException {
        File file = new File("config.properties");
        Properties properties = new Properties();
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("title=INDEX\n");
                writer.write("profilePath=config/profile.md\n");
                writer.write("footerPath=config/footer.md\n");
                writer.write("size=5\n");
                writer.write("length=25\n");
                writer.flush();
            }
        }
        properties.load(new FileInputStream(file));
        title = properties.getProperty("title", "INDEX");
        profilePath = properties.getProperty("profilePath", "config/profile.md");
        footerPath = properties.getProperty("footerPath", "config/footer.md");
        size = Integer.parseInt(properties.getProperty("size", "5"));
        length = Integer.parseInt(properties.getProperty("length", "25"));
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getProfile() throws IOException {
        return getOrCreateFile(new File(profilePath));
    }

    public String getFooter() throws IOException {
        return getOrCreateFile(new File(footerPath));
    }

    private String getOrCreateFile(File file) throws IOException {
        if (!file.exists()) {
            if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("");
                    writer.flush();
                }
            } else {
                throw new IOException("Can not create the directory: " + file.getParent());
            }
        }
        try (FileReader reader = new FileReader(file)) {
            char[] b = new char[1024];
            StringBuilder sb = new StringBuilder();
            int length;
            while ((length = reader.read(b)) > 0) {
                sb.append(b, 0, length);
            }
            return sb.toString();
        }
    }
}
