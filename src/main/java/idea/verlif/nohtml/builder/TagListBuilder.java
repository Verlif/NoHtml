package idea.verlif.nohtml.builder;

import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.md.MdFile;
import idea.verlif.nohtml.md.MdTag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签列表页构建
 *
 * @author Verlif
 * @version 1.0
 * @date 2022/2/23 9:03
 */
public class TagListBuilder extends Builder {

    public static final String SPLIT = "-";

    /**
     * 标签列表
     */
    private final List<MdTag> tags;

    /**
     * 标签构建器列表
     */
    private final List<TagBuilder> tagBuilders;

    private final MdConfig config;

    public TagListBuilder(MdConfig config) {
        this.tags = new ArrayList<>();
        this.tagBuilders = new ArrayList<>();

        this.config = config;
    }

    public void addMdTag(MdTag tag) {
        tags.add(tag);
    }

    public void addMdTags(List<MdTag> tags) {
        this.tags.addAll(tags);
    }

    @Override
    public String build() {
        return "# 标签\n\n" +
                "[" + config.getTitle() + "](" + config.getIndexName() + ")\n\n" +
                listMdTags("", "", tags);
    }

    private String listMdTags(String prefix, String tagLink, List<MdTag> tags) {
        StringBuilder sb = new StringBuilder();
        for (MdTag tag : tags) {
            // 添加标签生成器
            tagBuilders.add(new TagBuilder(tag.getTag(), tagLink, config));
            String tagLinkNow = tagLink + SPLIT + tag.getTag();
            sb.append(prefix).append("* __[")
                    .append(tag.getTag())
                    .append("](")
                    .append(TagBuilder.buildFilename(tag.getTag()))
                    .append(")__");
            // 添加标签的额外信息
            if (tag.getMdFileCount() > 0) {
                MdFile mdFile = tag.getLastMdFile();
                sb.append("\n\n  ").append(prefix).append("↑ 共计 ")
                        .append(tag.getMdFileCount()).append(" 篇，最新的一篇为 [")
                        .append(mdFile.getTitle())
                        .append("](")
                        .append(tag.getPath()).append(MdConfig.PATH_SPLIT)
                        .append(mdFile.getFilename()).append(MdFile.SUFFIX)
                        .append(")\n\n");
            }
            // 添加子标签
            sb.append(listMdTags(prefix + "  ", tagLinkNow, tag.getChildren()));
        }
        return sb.toString();
    }

    @Override
    public void saveToFile() throws IOException {
        saveAsMdFile(new File(buildFilename()));
    }

    @Override
    public void saveAsMdFile(File file) throws IOException {
        super.saveAsMdFile(file);
        for (TagBuilder tagBuilder : tagBuilders) {
            tagBuilder.saveToFile();
        }
    }

    public static String buildFilename() {
        return MdConfig.TAGS_NAME + MdFile.SUFFIX;
    }
}
