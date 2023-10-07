package cn.afternode.d4jserver.testplugin;

import cn.afternode.d4jserver.api.plugin.D4JServerPlugin;
import cn.afternode.d4jserver.api.plugin.Plugin;

@D4JServerPlugin(name="TestPlugin", authors = {"H3xadecimal"}, version="1.0.0")
public class TestPlugin extends Plugin {
    @Override
    public void onEnable() {
        getLogger().info("Hello World");
    }
}
