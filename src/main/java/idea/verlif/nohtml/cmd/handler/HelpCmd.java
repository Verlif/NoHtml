package idea.verlif.nohtml.cmd.handler;

import idea.verlif.nohtml.cmd.MdKeyHandler;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/5/20 11:09
 */
public class HelpCmd implements MdKeyHandler {

    @Override
    public String key() {
        return "help";
    }

    @Override
    public void handle(String s) {
        System.out.println(
                "--help      List all cmd parameters.\n" +
                "--config    Change configuration on this time. Like \"--config backupMax=2;recordMax=2\".\n" +
                "--exit      Shutdown this process.\n" +
                "--recovery  Use the newest recovery file to change information of documents. Or using \"--recovery docs-2022-03-08-11-46-21-record.properties\" to load special recovery file."
        );
    }
}
