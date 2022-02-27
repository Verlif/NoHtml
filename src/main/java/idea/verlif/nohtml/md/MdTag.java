package idea.verlif.nohtml.md;

import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.sort.CreateTimeSort;

import java.util.ArrayList;
import java.util.List;

/**
 * MD文件标签
 *
 * @author Verlif
 * @version 1.0
 * @date 2022/2/23 10:40
 */
public class MdTag {

    private final String tag;

    private final String path;

    private final List<MdTag> children;

    private final List<MdFile> files;

    public MdTag(String path) {
        this.tag = MdFinder.getLastDirname(path);
        this.path = path.replaceAll("\\\\", MdConfig.PATH_SPLIT);
        this.children = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public void addChild(String path) {
        children.add(new MdTag(path));
    }

    public void addChild(MdTag tag) {
        children.add(tag);
    }

    public void addMdFile(MdFile mdFile) {
        this.files.add(mdFile);
    }

    public String getPath() {
        return path;
    }

    public String getTag() {
        return tag;
    }

    public List<MdTag> getChildren() {
        return children;
    }

    public MdFile getLastMdFile() {
        if (files.size() == 0) {
            return null;
        }
        files.sort(new CreateTimeSort());
        return files.get(0);
    }

    public int getMdFileCount() {
        return files.size();
    }

    @Override
    public String toString() {
        return "MdTag{" +
                "tag='" + tag + '\'' +
                ", children=" + children +
                '}';
    }
}
