package idea.verlif.nohtml.md;

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

    private final List<MdTag> children;

    private int mdFileCount;

    public MdTag(String tag) {
        this.tag = tag;
        this.children = new ArrayList<>();
    }

    public void addChild(String tag) {
        children.add(new MdTag(tag));
    }

    public void addChild(MdTag tag) {
        children.add(tag);
    }

    public String getTag() {
        return tag;
    }

    public List<MdTag> getChildren() {
        return children;
    }

    public int getMdFileCount() {
        return mdFileCount;
    }

    public void setMdFileCount(int mdFileCount) {
        this.mdFileCount = mdFileCount;
    }

    public void mdFileCountAdd() {
        this.mdFileCount ++;
    }

    @Override
    public String toString() {
        return "MdTag{" +
                "tag='" + tag + '\'' +
                ", children=" + children +
                '}';
    }
}
