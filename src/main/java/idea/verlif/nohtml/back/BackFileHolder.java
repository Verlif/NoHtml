package idea.verlif.nohtml.back;

import idea.verlif.nohtml.builder.TagListBuilder;
import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.record.FileRecord;
import idea.verlif.nohtml.record.RecordBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Verlif
 */
public class BackFileHolder {

    public static final String BACK_DIR = "backs";

    public static final String BACK_NAME = "back.zip";

    private final MdConfig mdConfig;

    public BackFileHolder(MdConfig config) {
        this.mdConfig = config;
    }

    /**
     * 开始备份文件
     *
     * @return 是否备份成功
     */
    public boolean backup() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
        File backDir = new File(BACK_DIR);
        if (!backDir.exists() && !backDir.mkdirs()) {
            return false;
        }
        File[] files = backDir.listFiles(File::isFile);
        if (files != null) {
            handleFiles(files);
        }
        File backFile = new File(backDir, sdf.format(new Date()) + BACK_NAME);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(backFile))) {
            zos.setLevel(7);
            zos.setComment("backup");
            zos.setMethod(ZipEntry.DEFLATED);
            // 备份源Markdown文件
            zip(zos, new File(MdConfig.DOCS_NAME), MdConfig.DOCS_NAME);
            // 备份配置文件组
            zip(zos, new File(MdConfig.CONFIG_NAME), MdConfig.CONFIG_NAME);
            // 备份标签页
            zip(zos, new File(MdConfig.TAGS_NAME), MdConfig.TAGS_NAME);
            // 备份配置文件
            zip(zos, new File(MdConfig.CONFIG_FILENAME), MdConfig.CONFIG_FILENAME);
            // 备份标签列表
            zip(zos, new File(TagListBuilder.buildFilename()), TagListBuilder.buildFilename());
            // 备份主页列表
            for (String indexName : mdConfig.getIndexNames()) {
                zip(zos, new File(indexName), indexName);
            }
            // 备份记录
            zip(zos, new File(RecordBuilder.RECORD_DIR), RecordBuilder.RECORD_DIR);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (backFile.exists()) {
            backFile.delete();
        }
        return false;
    }

    private void handleFiles(File[] files) {
        int over = files.length - mdConfig.getBackupMax() + 1;
        if (over > 0) {
            List<File> list = Arrays.asList(files);
            list.sort((o1, o2) -> (int) ((o1.lastModified() - o2.lastModified()) / 1000));
            for (int i = 0; i < over; i++) {
                list.get(i).delete();
            }
        }
    }

    private void zip(ZipOutputStream zos, File file, String path) throws IOException {
        // 当文件是文件夹时
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            // 当文件夹下没有文件时，直接创建文件夹
            if (files == null || files.length == 0) {
                zos.putNextEntry(buildEntry(path + MdConfig.PATH_SPLIT));
            } else {
                // 对其下的文件进行遍历压缩
                for (File f : files) {
                    zip(zos, f, path + MdConfig.PATH_SPLIT + f.getName());
                }
            }
        } else {
            if (path.length() > 0) {
                zos.putNextEntry(buildEntry(path));
            }
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                int length;
                byte[] bytes = new byte[1024];
                while ((length = bis.read(bytes)) != -1) {
                    zos.write(bytes, 0, length);
                }
            }
        }
    }

    private ZipEntry buildEntry(String p) throws IOException {
        Path path = Paths.get(p);
        BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = view.readAttributes();

        ZipEntry entry = new ZipEntry(p);
        entry.setCreationTime(attr.creationTime());
        entry.setLastModifiedTime(attr.lastModifiedTime());
        entry.setLastAccessTime(attr.lastAccessTime());
        return entry;
    }
}
