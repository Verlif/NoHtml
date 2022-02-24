package idea.verlif.nohtml.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Verlif
 */
public class MdConfig {

    public static final String DOCS_NAME = "docs";

    public static final String CONFIG_NAME = "config";

    public static final String TAGS_NAME = "tags";

    public static final String PATH_SPLIT = "/";

    public static final String INDEX_NAME = "index.md";

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

    /**
     * 首页文件名
     */
    private String indexName;

    public MdConfig() {
    }

    public void loadConfig() throws IOException {
        File file = new File("config.properties");
        Properties properties = new Properties();
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))) {
                writer.write("title=INDEX\n");
                writer.write("size=5\n");
                writer.write("length=25\n");
                writer.write("indexName=" + INDEX_NAME);
                writer.flush();
            }
        }
        properties.load(new FileInputStream(file));
        title = properties.getProperty("title", "INDEX");
        size = Integer.parseInt(properties.getProperty("size", "5"));
        length = Integer.parseInt(properties.getProperty("length", "25"));
        indexName = properties.getProperty("indexName", INDEX_NAME);
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

    public String getIndexName() {
        return indexName;
    }

    public String getProfile() throws IOException {
        String profilePath = CONFIG_NAME + PATH_SPLIT + "profile.md";
        return getOrCreateFile(new File(profilePath));
    }

    public String getFooter() throws IOException {
        String footerPath = CONFIG_NAME + PATH_SPLIT + "footer.md";
        return getOrCreateFile(new File(footerPath));
    }

    private String getOrCreateFile(File file) throws IOException {
        if (!file.exists()) {
            if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))) {
                    writer.write("");
                    writer.flush();
                }
            } else {
                throw new IOException("Can not create the directory: " + file.getParent());
            }
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
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
