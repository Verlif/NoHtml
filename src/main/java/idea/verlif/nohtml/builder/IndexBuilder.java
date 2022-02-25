package idea.verlif.nohtml.builder;

import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.md.MdFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页构建
 *
 * @author Verlif
 */
public class IndexBuilder extends Builder {

    private static String indexName = MdConfig.INDEX_NAME;

    private final String base;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述段落
     */
    private final StringBuilder desc;

    /**
     * 结尾段落
     */
    private final StringBuilder footer;

    /**
     * 最近更新
     */
    private final List<MdFile> newest;

    /**
     * 未归档
     */
    private final List<MdFile> hashes;

    private final MdConfig config;

    public IndexBuilder(String base, MdConfig config) {
        this.base = base;
        this.desc = new StringBuilder();
        this.newest = new ArrayList<>();
        this.hashes = new ArrayList<>();
        this.footer = new StringBuilder();

        this.config = config;
        indexName = config.getIndexName();
    }

    public IndexBuilder title(String title) {
        this.title = title;
        return this;
    }

    public IndexBuilder descriptionLine(String line) {
        desc.append(line).append("\n\n");
        return this;
    }

    public IndexBuilder addNewest(MdFile mdFile) {
        newest.add(mdFile);
        return this;
    }

    public IndexBuilder addHashes(MdFile mdFile) {
        if (mdFile.getPath() == null || mdFile.getPath().length() == 0) {
            hashes.add(mdFile);
        }
        return this;
    }

    public IndexBuilder footerLine(String line) {
        footer.append(line);
        return this;
    }

    @Override
    public String build() throws IOException {
        StringBuilder sb = new StringBuilder();
        if (title == null) {
            this.title = config.getTitle();
        }
        // 标题
        sb.append("# ").append(title).append("\n\n");
        // 标签归档
        sb.append("[")
                .append("标签归档](")
                .append(TagListBuilder.buildFilename())
                .append(")\n\n");
        // 首页描述
        sb.append(config.getProfile()).append("\n\n");
        sb.append(desc);
        sb.append("------\n\n");
        // 未归档
        sb.append("## 未归档\n\n");
        hashes.sort((o1, o2) -> (int) ((o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime()) / 1000));
        for (int i = 0, size = Math.min(config.getSize(), hashes.size()); i < size; i++) {
            MdFile mdFile = hashes.get(i);
            sb.append(oneMdLink(mdFile)).append("\n\n");
        }
        sb.append("------\n\n");
        // 最新md文件
        sb.append("## 最新\n\n");
        newest.sort((o1, o2) -> (int) ((o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime()) / 1000));
        for (int i = 0, size = Math.min(config.getSize(), newest.size()); i < size; i++) {
            MdFile mdFile = newest.get(i);
            sb.append(oneMdLink(mdFile)).append("\n\n");
        }
        // 结尾描述
        sb.append("------\n\n").append(config.getFooter()).append("\n\n")
                .append(footer);
        return sb.toString();
    }

    private String oneMdLink(MdFile mdFile) {
        StringBuilder sb = new StringBuilder();
        sb.append("### [");
        String tag = mdFile.getTag();
        if (tag != null && tag.length() > 0) {
            sb.append(tag).
                    append("](")
                    .append(TagBuilder.buildFilename(tag))
                    .append(") ○ [");
        }
        sb.append(mdFile.getTitle())
                .append("](")
                .append(base)
                .append(MdConfig.PATH_SPLIT)
                .append(mdFile.getPath())
                .append(MdConfig.PATH_SPLIT)
                .append(mdFile.getFilename())
                .append(MdFile.SUFFIX)
                .append(")");
        String profile = mdFile.getProfile();
        if (profile != null && profile.length() > 0) {
            sb.append("\n\n> ")
                    .append(profile.length() > config.getLength() ? profile.substring(0, config.getLength()) + "..." : profile);
        }
        return sb.toString();
    }

    @Override
    public void saveToFile() throws IOException {
        saveAsMdFile(new File(buildFilename()));
    }

    public static String buildFilename() {
        return indexName;
    }
}
