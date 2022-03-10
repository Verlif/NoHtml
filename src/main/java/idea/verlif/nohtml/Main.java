package idea.verlif.nohtml;

import idea.verlif.nohtml.back.BackFileHolder;
import idea.verlif.nohtml.builder.IndexBuilder;
import idea.verlif.nohtml.builder.TagListBuilder;
import idea.verlif.nohtml.cmd.MdKeyParser;
import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.md.MdFile;
import idea.verlif.nohtml.md.MdFinder;
import idea.verlif.nohtml.record.RecordBuilder;
import idea.verlif.nohtml.sort.UpdateTimeSort;
import idea.verlif.nohtml.util.PrintUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Verlif
 */
public class Main {

    public static void main(String[] args) throws IOException {
        // 加载配置文件
        PrintUtil.printLoading("Loading config...");
        MdConfig config = new MdConfig();
        config.loadConfig();
        if (args.length > 0) {
            // 解析启动指令
            PrintUtil.printLoading("Executing cmd...");
            MdKeyParser parser = new MdKeyParser(config);
            parser.initParser();
            parser.exec(args);
        }
        // 寻找所有的Markdown文档
        PrintUtil.printLoading("Finding markdown files...");
        MdFinder finder = new MdFinder(MdConfig.DOCS_NAME);
        List<MdFile> files = finder.getAllMdFiles();
        PrintUtil.printOk("Found " + files.size() + " markdown files.");
        // Markdown文档通过更新时间排序
        files.sort(new UpdateTimeSort());
        IndexBuilder builder = new IndexBuilder(MdConfig.DOCS_NAME, config);
        for (MdFile file : files) {
            builder.addNewest(file);
            builder.addHashes(file);
        }
        // 构建主页
        PrintUtil.printLoading("Building " + config.getIndexName() + "...");
        builder.saveToFile();
        // 构建标签页
        PrintUtil.printLoading("Building " + TagListBuilder.buildFilename() + "...");
        TagListBuilder tagListBuilder = new TagListBuilder(config);
        tagListBuilder.addMdTags(finder.getAllTags());
        tagListBuilder.saveToFile();
        // 生成新的记录
        PrintUtil.printLoading("Building record...");
        RecordBuilder recordBuilder = new RecordBuilder(new File(MdConfig.DOCS_NAME), config);
        recordBuilder.build();
        // 生成新的备份文件
        PrintUtil.printLoading("Saving backup file...");
        if (config.isEnableBackup()) {
            BackFileHolder backFileHolder = new BackFileHolder(config);
            if (!backFileHolder.backup()) {
                PrintUtil.printError("Wrong Backup!");
            }
        }
        // 完成
        PrintUtil.printOk("Finished!");
    }
}
