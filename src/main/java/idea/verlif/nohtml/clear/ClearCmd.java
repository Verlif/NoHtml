package idea.verlif.nohtml.clear;

import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.parser.cmdline.CmdHandler;

import java.io.File;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/9 10:25
 */
public class ClearCmd implements CmdHandler {

    @Override
    public void handle(String s) {
        System.out.println("[~] Clear...");
        // 删除标签页面
        deleteFile(new File(MdConfig.TAGS_NAME));
    }

    /**
     * 删除文件或文件夹
     *
     * @param file 文件对象
     */
    private void deleteFile(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            file.delete();
        } else {
            for (File f : files) {
                deleteFile(f);
            }
        }
    }
}
