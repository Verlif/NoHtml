package idea.verlif.nohtml.cmd;

import idea.verlif.nohtml.cmd.handler.ConfigModifyCmd;
import idea.verlif.nohtml.cmd.handler.ExitCmd;
import idea.verlif.nohtml.cmd.handler.RecoveryCmd;
import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.parser.cmdline.CmdlineParser;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/10 9:36
 */
public class MdKeyParser extends CmdlineParser {

    private final MdConfig config;

    public MdKeyParser(MdConfig config) {
        super("--");
        this.config = config;
    }

    public void initParser() {
        addMdKeyHandler(new RecoveryCmd(config));
        addMdKeyHandler(new ExitCmd());
        addMdKeyHandler(new ConfigModifyCmd(config));
    }

    public void addMdKeyHandler(MdKeyHandler handler) {
        addHandler(handler.key(), handler);
    }
}
