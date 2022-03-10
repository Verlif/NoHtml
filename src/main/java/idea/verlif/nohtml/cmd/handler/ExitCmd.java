package idea.verlif.nohtml.cmd.handler;

import idea.verlif.nohtml.cmd.MdKeyHandler;
import idea.verlif.nohtml.util.PrintUtil;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/10 10:02
 */
public class ExitCmd implements MdKeyHandler {
    @Override
    public String key() {
        return "exit";
    }

    @Override
    public void handle(String s) {
        PrintUtil.printLoading("Stopping...");
        System.exit(0);
    }
}
