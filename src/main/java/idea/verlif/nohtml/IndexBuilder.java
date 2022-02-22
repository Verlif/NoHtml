package idea.verlif.nohtml;

import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.mk.MdFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Verlif
 */
public class IndexBuilder {

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

    private final MdConfig config;

    public IndexBuilder() throws IOException {
        desc = new StringBuilder();
        newest = new ArrayList<>();
        footer = new StringBuilder();

        config = new MdConfig();
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

    public IndexBuilder footerLine(String line) {
        footer.append(line);
        return this;
    }

    public String build() throws IOException {
        config.loadConfig();
        StringBuilder sb = new StringBuilder();
        if (title == null) {
            this.title = config.getTitle();
        }
        // 标题
        sb.append("# ").append(title).append("\n\n");
        // 首页描述
        sb.append(config.getProfile()).append("\n\n");
        sb.append(desc);
        // 最新md文件
        sb.append("## NEWEST\n\n");
        newest.sort((o1, o2) -> (int) (o1.getUpdateTime().getTime() - o2.getUpdateTime().getTime()));
        for (int i = 0, size = Math.min(config.getSize(), newest.size()); i < size; i++) {
            MdFile mdFile = newest.get(i);
            sb.append(oneMdLink(mdFile)).append("\n\n");
        }
        // 结尾描述
        sb.append("## FINAL\n\n")
                .append(config.getFooter()).append("\n\n")
                .append(footer);
        return sb.toString();
    }

    private String oneMdLink(MdFile mdFile) {
        return "### [" +
                mdFile.getTitle() +
                "](" +
                mdFile.getPath() +
                File.separator +
                mdFile.getFilename() +
                MdFile.SUFFIX +
                ")\n> " +
                (mdFile.getProfile().length() > config.getLength() ? mdFile.getProfile().substring(0, config.getLength()) + "..." : mdFile.getProfile());
    }
}
