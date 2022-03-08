package idea.verlif.nohtml.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author Verlif
 */
public class MdConfig {

    public static final String DOCS_NAME = "docs";

    public static final String CONFIG_NAME = "config";

    public static final String CONFIG_FILENAME = "config.properties";

    public static final String TAGS_NAME = "tags";

    public static final String PATH_SPLIT = "/";

    public static final String INDEX_NAME = "index.md";

    /**
     * 标题
     */
    private String title = "INDEX";

    /**
     * MD文件列表最大数量
     */
    private int size = 5;

    /**
     * MD文件列表中，每个概览的允许长度
     */
    private int length = 25;

    /**
     * 首页文件名
     */
    private String[] indexNames = new String[]{INDEX_NAME};

    /**
     * 标题分割符
     */
    private String titleSplit = "⚪";

    /**
     * 是否开启备份模式
     */
    private boolean enableBackup = true;

    /**
     * 备份文件最大数量
     */
    private int backupMax = 10;

    /**
     * 记录最大数量
     */
    private int recordMax = 5;

    public MdConfig() {
    }

    public void loadConfig() throws IOException {
        File file = new File(CONFIG_FILENAME);
        Properties properties = new Properties();
        if (!file.exists()) {
            saveConfig(file);
        }
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        loadConfig(properties);
        saveConfig(file);
    }

    private void saveConfig(File file) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))) {
            writer.write("title=" + title);
            writer.write("\nsize=" + size);
            writer.write("\nlength=" + length);
            String indexName = Arrays.toString(indexNames);
            writer.write("\nindexName=" + indexName.substring(1, indexName.length() - 1));
            writer.write("\ntitleSplit=" + titleSplit);
            writer.write("\nenableBackup=" + enableBackup);
            writer.write("\nbackupMax=" + backupMax);
            writer.write("\nrecordMax=" + recordMax);
            writer.flush();
        }
    }

    private void loadConfig(Properties properties) {
        if (properties.containsKey("title")) {
            title = properties.getProperty("title");
        }
        if (properties.containsKey("size")) {
            size = Integer.parseInt(properties.getProperty("size"));
        }
        if (properties.containsKey("length")) {
            length = Integer.parseInt(properties.getProperty("length"));
        }
        if (properties.containsKey("indexName")) {
            String[] names = properties.getProperty("indexName").split(",");
            indexNames = new String[names.length];
            for (int i = 0; i < indexNames.length; i++) {
                indexNames[i] = names[i].trim();
            }
        }
        if (properties.containsKey("titleSplit")) {
            titleSplit = properties.getProperty("titleSplit");
        }
        if (properties.containsKey("enableBackup")) {
            enableBackup = Boolean.parseBoolean(properties.getProperty("enableBackup"));
        }
        if (properties.containsKey("backupMax")) {
            backupMax = Integer.parseInt(properties.getProperty("backupMax"));
        }
        if (properties.containsKey("recordMax")) {
            recordMax = Integer.parseInt(properties.getProperty("recordMax"));
        }
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = Math.max(0, size);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = Math.max(0, length);
    }

    public String getIndexName() {
        return indexNames[0];
    }

    public String[] getIndexNames() {
        return indexNames;
    }

    public String getProfile() throws IOException {
        String profilePath = CONFIG_NAME + PATH_SPLIT + "profile.md";
        return getOrCreateFile(new File(profilePath));
    }

    public String getFooter() throws IOException {
        String footerPath = CONFIG_NAME + PATH_SPLIT + "footer.md";
        return getOrCreateFile(new File(footerPath));
    }

    public String getTitleSplit() {
        return titleSplit;
    }

    public void setEnableBackup(boolean enableBackup) {
        this.enableBackup = enableBackup;
    }

    public boolean isEnableBackup() {
        return enableBackup;
    }

    public int getBackupMax() {
        return backupMax;
    }

    public void setBackupMax(int backupMax) {
        this.backupMax = Math.max(1, backupMax);
    }

    public int getRecordMax() {
        return recordMax;
    }

    public void setRecordMax(int recordMax) {
        this.recordMax = Math.max(1, recordMax);
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
