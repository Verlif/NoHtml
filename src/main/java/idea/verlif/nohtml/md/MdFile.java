package idea.verlif.nohtml.md;

import idea.verlif.nohtml.config.MdConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Locale;

/**
 * @author Verlif
 */
public class MdFile {

    public static final String SUFFIX = ".md";

    private final String filename;

    private String title;

    private Date createTime;

    private Date updateTime;

    private String profile;

    private String path;

    public MdFile(String path, File file) throws IOException {
        String name = file.getName();
        if (!name.toLowerCase(Locale.ROOT).endsWith(SUFFIX)) {
            throw new IOException("This is not a markdown file: " + name);
        }
        this.path = path;
        this.filename = name.substring(0, name.length() - 3).replaceAll(" ", "&#32;");;
        readProfile(file);
        setTime(file);
    }

    public String getFilename() {
        return filename;
    }

    public String getTitle() {
        return title == null ? filename : title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getProfile() {
        return profile;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        path = path.replaceAll(" ", "&#32;");
        if (path.startsWith("/") || path.startsWith("\\")) {
            this.path = path.replaceAll("\\\\", "/").substring(1);
        } else {
            this.path = path;
        }
    }

    public String getTag() {
        int i = path.lastIndexOf(MdConfig.PATH_SPLIT);
        if (i == -1) {
            return path;
        } else {
            return path.substring(i + 1);
        }
    }

    private void readProfile(File file) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
        ) {
            StringBuilder sb = new StringBuilder();
            String li;
            while ((li = reader.readLine()) != null) {
                sb.append(li).append("\n");
            }
            String[] lines = sb.toString().split("\n");
            StringBuilder prof = new StringBuilder();
            boolean start = false;
            for (String line : lines) {
                String read = line.trim();
                if (!start && line.startsWith("# ")) {
                    this.title = line.substring(2).trim();
                } else if (read.length() > 0) {
                    start = true;
                }
                if (start) {
                    if (read.length() > 0) {
                        prof.append(read);
                    }
                    if (read.length() == 0 || (line.lastIndexOf(read.charAt(read.length() - 1)) + 2) < line.length()) {
                        break;
                    }
                }
            }
            this.profile = prof.toString();
        }
    }

    private void setTime(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = view.readAttributes();
        createTime = new Date(attr.creationTime().toMillis());
        updateTime = new Date(attr.lastModifiedTime().toMillis());
    }

    public static boolean isMkFile(File file) {
        return file.getName().toLowerCase(Locale.ROOT).endsWith(SUFFIX);
    }

    @Override
    public String toString() {
        return "MdFile{" +
                "filename='" + filename + '\'' +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", profile='" + profile + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
