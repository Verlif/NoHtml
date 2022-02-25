package idea.verlif.nohtml.sort;

import idea.verlif.nohtml.md.MdFile;

import java.util.Comparator;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/2/25 16:56
 */
public class CreateTimeSort implements Comparator<MdFile> {
    @Override
    public int compare(MdFile o1, MdFile o2) {
        int t1 = (int) (o1.getCreateTime().getTime() / 1000);
        int t2 = (int) (o2.getCreateTime().getTime() / 1000);
        return t1 == t2 ? 0 : (t2 - t1);
    }
}
