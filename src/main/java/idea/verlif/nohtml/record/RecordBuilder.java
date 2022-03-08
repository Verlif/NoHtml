package idea.verlif.nohtml.record;

import idea.verlif.nohtml.config.MdConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/8 9:11
 */
public class RecordBuilder {

    public static final String RECORD_NAME = "record.properties";

    public static final String RECORD_DIR = "records";

    public static final String SPLIT = ".";

    private final File target;
    private final MdConfig config;

    public RecordBuilder(File target, MdConfig config) {
        this.target = target;
        this.config = config;
    }

    private void handleFiles(File[] files) {
        int over = files.length - config.getRecordMax() + 1;
        if (over > 0) {
            List<File> list = Arrays.asList(files);
            list.sort((o1, o2) -> (int) ((o1.lastModified() - o2.lastModified()) / 1000));
            for (int i = 0; i < over; i++) {
                list.get(i).delete();
            }
        }
    }

    public void build() throws IOException {
        // 新建record文件夹
        File dir = new File(RECORD_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new FileNotFoundException("Can not create directory " + dir.getPath() + " for records!");
        }
        // 处理记录最大值
        File[] files = dir.listFiles();
        if (files != null) {
            handleFiles(files);
        }
        // 遍历获取文件record信息
        Map<String, FileRecord> recordMap = new HashMap<>();
        createRecords("", target, recordMap);

        // 对record信息进行保存
        SimpleDateFormat sdf = new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss-");
        File file = new File(dir, target.getName() + sdf.format(new Date()) + RECORD_NAME);
        // 检测文件是否已存在
        if (file.exists()) {
            if (!file.delete()) {
                throw new FileNotFoundException("Can not cover file " + file.getParent());
            }
        }
        // 转存properties信息
        Properties properties = new Properties();
        for (String key : recordMap.keySet()) {
            FileRecord record = recordMap.get(key);
            properties.put("u" + SPLIT + key, String.valueOf(record.getUpdateTime()));
            properties.put("c" + SPLIT + key, String.valueOf(record.getCreateTime()));
        }
        // 保存properties文件
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            properties.store(writer, "Using \" --recovery filename \" to recovery docs with record.");
        }
    }

    public void recovery(String filename) throws IOException {
        File dir = new File(RECORD_DIR);
        if (!dir.exists()) {
            throw new FileNotFoundException("Not found record directory!");
        }
        File file = new File(dir, filename);
        if (!dir.exists()) {
            throw new FileNotFoundException("No such record file: " + filename);
        }
        recovery(target.getName(), target, file);
    }

    public static void recovery(String prefix, File target, File recordFile) throws IOException {
        // 加载properties文件
        Properties properties = new Properties();
        try (Reader reader = new InputStreamReader(new FileInputStream(recordFile), StandardCharsets.UTF_8)) {
            properties.load(reader);
        }

        // 对properties文件进行遍历，并构建Map表
        Map<String, FileRecord> recordMap = new HashMap<>();
        for (Object o : properties.keySet()) {
            String key = o.toString();
            String[] ss = key.split("\\" + SPLIT, 2);
            String name = ss[1].replaceAll("\\" + SPLIT, MdConfig.PATH_SPLIT);
            FileRecord record = recordMap.computeIfAbsent(name, s -> new FileRecord());
            switch (ss[0]) {
                case "u":
                    record.setUpdateTime(Long.parseLong(properties.getProperty(key)));
                    break;
                case "c":
                    record.setCreateTime(Long.parseLong(properties.getProperty(key)));
                    break;
                default:
            }
            recordMap.put(name, record);
        }

        // 对目标文件进行恢复
        recoveryFile(prefix, target, recordMap);
    }

    private static void recoveryFile(String prefix, File file, Map<String, FileRecord> map) throws IOException {
        String key = MdConfig.DOCS_NAME + file.getPath().substring(prefix.length()).replaceAll("\\\\", MdConfig.PATH_SPLIT).replaceAll("\\" + SPLIT, MdConfig.PATH_SPLIT);
        FileRecord record = map.get(key);
        if (record != null) {
            Path path = Paths.get(file.getPath());
            Files.setAttribute(path, "basic:creationTime", FileTime.fromMillis(record.getCreateTime()));
            Files.setLastModifiedTime(path, FileTime.fromMillis(record.getUpdateTime()));
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    recoveryFile(prefix, f, map);
                }
            }
        }
    }

    private void createRecords(String base, File file, Map<String, FileRecord> map) throws IOException {
        if (base.length() > 0 && base.charAt(0) == '.') {
            base = base.substring(1);
        }
        FileRecord record = new FileRecord(file);
        map.put(base.length() == 0 ? file.getName() : (base + SPLIT + file.getName()), record);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    createRecords(base + SPLIT + file.getName(), f, map);
                }
            }
        }
    }
}
