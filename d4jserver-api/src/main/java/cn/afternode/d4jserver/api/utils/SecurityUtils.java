package cn.afternode.d4jserver.api.utils;

import cn.afternode.d4jserver.api.D4JServerAPI;

public class SecurityUtils {
    public static final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static void check(Class<?> caller) throws SecurityException {
        if (!Boolean.parseBoolean(System.getProperty("d4jserver.security", "true"))) return;

        if (caller.getProtectionDomain().getCodeSource() != D4JServerAPI.class.getProtectionDomain().getCodeSource())
            throw new SecurityException("Illegal access from " + caller.getName());
    }
}
