package idea.verlif.nohtml.cmd.handler;

import idea.verlif.nohtml.cmd.MdKeyHandler;
import idea.verlif.nohtml.config.MdConfig;
import idea.verlif.nohtml.util.PrintUtil;
import idea.verlif.parser.ParamParser;
import idea.verlif.parser.ParamParserService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/10 10:09
 */
public class ConfigModifyCmd implements MdKeyHandler {

    private static final String SPLIT = ";";

    private final MdConfig config;

    public ConfigModifyCmd(MdConfig config) {
        this.config = config;
    }

    @Override
    public String key() {
        return "config";
    }

    /**
     * 解析临时配置参数
     * @param s 参数字符串；允许的格式为"configName=configValue"，有多个配置需要改动时可以用";"分割。
     *          例如"backupMax=6;recordMax=4"。
     */
    @Override
    public void handle(String s) {
        if (s == null || s.length() == 0) {
            PrintUtil.printFailed("No config param had been modified.");
            return;
        }
        // 构造配置属性表
        Field[] fields = MdConfig.class.getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap<>(fields.length);
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }

        // 解析参数
        ParamParserService pps = new ParamParserService();
        String[] ss = s.split(SPLIT);
        for (String arg : ss) {
            String[] kv = arg.split("=");
            if (kv.length > 0) {
                Field field = fieldMap.get(kv[0]);
                if (field == null) {
                    PrintUtil.printFailed("Unknown config param " + kv[0]);
                } else {
                    field.setAccessible(true);
                    try {
                        ParamParser<?> pp = pps.getParser(field.getType());
                        field.set(config, pp.parser(kv[1]));
                        PrintUtil.printOk("Changed config param " + kv[0] + " with value " + kv[1]);
                    } catch (Exception e) {
                        PrintUtil.printError("Cannot modify config param " + kv[0]);
                    }
                }
            }
        }
    }
}
