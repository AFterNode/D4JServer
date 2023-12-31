package cn.afternode.d4jserver.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface D4JServerPlugin {
    String name();

    String[] authors();

    String description() default "";

    String version();

    String[] dependencies() default {};
}
