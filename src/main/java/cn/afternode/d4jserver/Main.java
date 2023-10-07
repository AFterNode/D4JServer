package cn.afternode.d4jserver;

import cn.afternode.d4jserver.impl.D4JServerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger("Launcher");

        try {
            D4JServerImpl impl = new D4JServerImpl();
            impl.launch(args);
        } catch (Throwable t) {
            logger.fatal("Unable to launch/lock", t);
            System.exit(-1);
        }
    }
}
