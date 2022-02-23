package idea.verlif.nohtml.builder;

import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.md.MdFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/2/23 10:19
 */
public class TagBuilder extends Builder {

    /**
     * 基础路径
     */
    private final String path;

    /**
     * 标签名称
     */
    private final String tag;

    /**
     * 标签链
     */
    private final String tagLink;

    /**
     * 标签包含的MD文件列表
     */
    private final List<MdFile> mdFiles;

    /**
     * MD文件配置
     */
    private final MdConfig config;

    public TagBuilder(String tag, String tagLink, MdConfig config) {
        this.path = ".." + MdConfig.PATH_SPLIT + MdConfig.DOCS_NAME + tagLink.replaceAll(TagListBuilder.SPLIT, MdConfig.PATH_SPLIT) + MdConfig.PATH_SPLIT + tag;
        this.tag = tag;
        this.tagLink = tagLink;
        this.mdFiles = new ArrayList<>();

        this.config = config;
    }

    public void addMdFile(MdFile mdFile) {
        mdFiles.add(mdFile);
    }

    @Override
    public String build() throws IOException {
        StringBuilder sb = new StringBuilder();
        // 标题
        sb.append("# ").append(tag).append("\n\n");
        // 导航
        String[] tags = tagLink.split(TagListBuilder.SPLIT);
        for (String tag : tags) {
            if (tag.length() == 0) {
                sb.append("[").append(config.getTitle()).append("](../").append(IndexBuilder.buildFilename()).append(")");
            } else {
                sb.append(" / [").append(tag).append("](").append(tag).append(MdFile.SUFFIX).append(")");
            }
        }
        sb.append(" / ").append(tag).append("\n\n");
        // 文件列表
        File dir = new File(path.substring(1));
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(MdFile::isMkFile);
            if (files != null) {
                List<MdFile> mdFiles = new ArrayList<>();
                for (File file : files) {
                    mdFiles.add(new MdFile(path, file));
                }
                mdFiles.sort((o1, o2) -> (int) (o2.getCreateTime().getTime() - o1.getCreateTime().getTime()));
                int year = -1;
                int moth = -1;
                Calendar calendar = Calendar.getInstance();
                for (MdFile mdFile : mdFiles) {
                    // 划分时间点
                    calendar.setTime(mdFile.getCreateTime());
                    if (calendar.get(Calendar.YEAR) != year) {
                        year = calendar.get(Calendar.YEAR);
                        sb.append("## __").append(year).append("年__\n\n");
                        moth = calendar.get(Calendar.MONTH);
                        sb.append("### __").append(moth + 1).append("月__\n\n");
                    } else if (calendar.get(Calendar.MONTH) != moth) {
                        moth = calendar.get(Calendar.MONTH);
                        sb.append("### __").append(moth + 1).append("月__\n\n");
                    }
                    // 描述文件
                    sb.append(oneMdLink(mdFile)).append("\n\n");
                }
            }
        }
        return sb.toString();
    }

    private String oneMdLink(MdFile mdFile) {
        StringBuilder sb = new StringBuilder();
        sb.append("### [")
                .append(mdFile.getTitle())
                .append("](")
                .append(mdFile.getPath())
                .append(MdConfig.PATH_SPLIT)
                .append(mdFile.getFilename())
                .append(MdFile.SUFFIX)
                .append(")");
        String profile = mdFile.getProfile();
        if (profile != null && profile.length() > 0) {
            sb.append("\n> ")
                    .append(profile.length() > config.getLength() ? profile.substring(0, config.getLength()) + "..." : profile);
        }
        return sb.toString();
    }

    @Override
    public void saveToFile() throws IOException {
        saveAsMdFile(new File(buildFilename(tag)));
    }

    public static String buildFilename(String tag) {
        return MdConfig.TAGS_NAME + MdConfig.PATH_SPLIT + tag + MdFile.SUFFIX;
    }
}
