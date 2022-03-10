package idea.verlif.nohtml.cmd;

import idea.verlif.parser.cmdline.CmdHandler;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/10 9:36
 */
public interface MdKeyHandler extends CmdHandler {

    /**
     * 指令关键词
     */
    String key();
}
