package idea.verlif.nohtml;

import idea.verlif.nohtml.back.BackFileHolder;
import idea.verlif.nohtml.builder.IndexBuilder;
import idea.verlif.nohtml.builder.TagListBuilder;
import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.md.MdFile;
import idea.verlif.nohtml.md.MdFinder;
import idea.verlif.nohtml.record.RecordBuilder;
import idea.verlif.nohtml.sort.UpdateTimeSort;
import idea.verlif.parser.cmdline.CmdHandler;
import idea.verlif.parser.cmdline.CmdlineParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Verlif
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Loading config...");
        MdConfig config = new MdConfig();
        config.loadConfig();
        // 初始化档案构建器
        System.out.println("Init record...");
        RecordBuilder recordBuilder = new RecordBuilder(new File(MdConfig.DOCS_NAME), config);
        CmdlineParser parser = new CmdlineParser("--");
        parser.addHandler("recovery", s -> {
            System.out.println("Recovering record from " + s + "...");
            try {
                recordBuilder.recovery(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        parser.exec(args);
        System.out.println("Finding markdown files...");
        MdFinder finder = new MdFinder(MdConfig.DOCS_NAME);
        List<MdFile> files = finder.getAllMdFiles();
        System.out.println("Found " + files.size() + " markdown files.");
        files.sort(new UpdateTimeSort());
        IndexBuilder builder = new IndexBuilder(MdConfig.DOCS_NAME, config);
        for (MdFile file : files) {
            builder.addNewest(file);
            builder.addHashes(file);
        }
        System.out.println("Building " + config.getIndexName() + "...");
        builder.saveToFile();
        TagListBuilder tagListBuilder = new TagListBuilder(config);
        tagListBuilder.addMdTags(finder.getAllTags());
        System.out.println("Building " + TagListBuilder.buildFilename() + "...");
        tagListBuilder.saveToFile();
        if (config.isEnableBackup()) {
            BackFileHolder backFileHolder = new BackFileHolder(config);
            if (backFileHolder.backup()) {
                System.out.println("Backup success!");
            } else {
                System.out.println("Backup failed!");
            }
        }
        System.out.println("Building record...");
        recordBuilder.build();
        System.out.println("Finished!");
    }
}
