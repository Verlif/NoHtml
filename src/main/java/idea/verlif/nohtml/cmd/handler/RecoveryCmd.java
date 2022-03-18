package idea.verlif.nohtml.cmd.handler;

import idea.verlif.nohtml.cmd.MdKeyHandler;
import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.record.RecordBuilder;
import idea.verlif.nohtml.util.PrintUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/10 9:43
 */
public class RecoveryCmd implements MdKeyHandler {

    private final MdConfig config;

    public RecoveryCmd(MdConfig config) {
        this.config = config;
    }

    @Override
    public String key() {
        return "recovery";
    }

    @Override
    public void handle(String s) {
        RecordBuilder recordBuilder = new RecordBuilder(new File(MdConfig.DOCS_NAME), config);
        try {
            if (s == null) {
                String filename = recordBuilder.recoveryFromNew();
                if (filename == null) {
                    PrintUtil.printError("Cannot find record file.");
                } else {
                    PrintUtil.printOk("Recovery info from " + filename + ".");
                }
            } else {
                recordBuilder.recovery(s);
                PrintUtil.printOk("Recovery info from " + s + ".");
            }
        } catch (IOException e) {
            PrintUtil.printError(e.getMessage());
        }
    }
}
