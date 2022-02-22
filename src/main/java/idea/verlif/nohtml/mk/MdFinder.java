package idea.verlif.nohtml.mk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Verlif
 */
public class MdFinder {

    private final File base;
    private final String path;

    public MdFinder(String base) {
        this(new File(base));
    }

    public MdFinder(String base, String path) {
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
                    insertFromDir(list, file, path + File.separator + getLastDirname(file.getPath()));
                }
            }
        }
    }

    private void insertFromFile(List<MdFile> list, File file, String path) throws IOException {
        if (MdFile.isMkFile(file)) {
            list.add(new MdFile(path, file));
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

    private String getLastDirname(String path) {
        int i = path.lastIndexOf(File.separator);
        if (i == -1) {
            return path;
        } else {
            return path.substring(i + 1);
        }
    }
}
