package idea.verlif.nohtml.util;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/10 9:46
 */
public class PrintUtil {

    public static void println(String message) {
        System.out.println(message);
    }

    public static void println(Type type, String message) {
        println(type.prefix + " " + message);
    }

    public static void printLoading(String message) {
        println(Type.LOADING, message);
    }

    public static void printOk(String message) {
        println(Type.OK, message);
    }

    public static void printFailed(String message) {
        println(Type.FAILED, message);
    }

    public static void printError(String message) {
        println(Type.ERROR, message);
    }

    public enum Type {
        /**
         * 加载中
         */
        LOADING("[~]"),

        /**
         * 完成或成功
         */
        OK("[K]"),

        /**
         * 失败
         */
        FAILED("[F]"),

        /**
         * 出错
         */
        ERROR("[E]");

        private final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
