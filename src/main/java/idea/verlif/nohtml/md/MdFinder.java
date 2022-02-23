package idea.verlif.nohtml.md;

import idea.verlif.nohtml.config.MdConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Verlif
 */
public class MdFinder {

    /**
     * 档案根目录
     */
    private final File base;

    /**
     * 档案起始路径（可以是相对根目录的路径，也可以是根路径的绝对路径）
     */
    private final String path;

    public MdFinder(String base) {
        this(new File(base));
    }

    public MdFinder(String path, String base) {
        this(path, new File(base));
    }

    public MdFinder(File base) {
        this("", base);
    }

    public MdFinder(String path, File base) {
        this.path = path;
        this.base = base;
    }

    /**
     * 获取当前路径下的所有Markdown文件
     *
     * @return Markdown文件列表
     * @throws IOException 文件无法读取时抛出异常
     */
    public List<MdFile> getAllMdFiles() throws IOException {
        List<MdFile> list = new ArrayList<>();
        if (base.exists()) {
            if (base.isDirectory()) {
                insertFromDir(list, base, path);
            } else {
                insertFromFile(list, base, path);
            }
        }
        return list;
    }

    private void insertFromDir(List<MdFile> list, File dir, String path) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    insertFromFile(list, file, path);
                } else {
                    insertFromDir(list, file, path + MdConfig.PATH_SPLIT + getLastDirname(file.getPath()));
                }
            }
        }
    }

    private void insertFromFile(List<MdFile> list, File file, String path) throws IOException {
        if (MdFile.isMkFile(file)) {
            MdFile mdFile = new MdFile(path, file);
            mdFile.setPath(path);
            list.add(mdFile);
        }
    }

    public String getTag() {
        if (base.isFile()) {
            return getLastDirname(base.getParent());
        } else {
            return getLastDirname(base.getPath());
        }
    }

    public List<MdFinder> getInnerFinders() {
        List<MdFinder> list = new ArrayList<>();
        if (base.exists()) {
            if (base.isDirectory()) {
                File[] files = base.listFiles(File::isDirectory);
                if (files != null) {
                    for (File file : files) {
                        list.add(new MdFinder(file));
                    }
                }
            }
        }
        return list;
    }

    public List<MdTag> getAllTags() {
        List<MdTag> list = new ArrayList<>();
        if (base.isFile()) {
            return new ArrayList<>();
        } else {
            File[] files = base.listFiles();
            if (files != null) {
                for (File file : files) {
                    MdTag child = getTagFromFile(file);
                    if (child != null) {
                        list.add(child);
                    }
                }
            }
        }
        return list;
    }

    private MdTag getTagFromFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                MdTag tag = new MdTag(getLastDirname(file.getPath()));
                for (File f : files) {
                    MdTag child = getTagFromFile(f);
                    if (child != null) {
                        tag.addChild(child);
                    }
                }
                return tag;
            }
        }
        return null;
    }

    private String getLastDirname(String path) {
        int i = path.replaceAll("\\\\", MdConfig.PATH_SPLIT).lastIndexOf(MdConfig.PATH_SPLIT);
        if (i == -1) {
            return path;
        } else {
            return path.substring(i + 1);
        }
    }
}
