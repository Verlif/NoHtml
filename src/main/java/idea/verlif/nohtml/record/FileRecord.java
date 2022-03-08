package idea.verlif.nohtml.record;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/8 9:07
 */
public class FileRecord {

    private final File file;
    private long createTime;
    private long updateTime;

    public FileRecord(File file) throws IOException {
        this.file = file;
        readFile(file);
    }

    public FileRecord() {
        this.file =  null;
    }

    private void readFile(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = view.readAttributes();
        createTime = attr.creationTime().toMillis();
        updateTime = attr.lastModifiedTime().toMillis();
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return file == null ? null : file.getName();
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        return "FileRecord{" +
                "file=" + file.getName() +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
