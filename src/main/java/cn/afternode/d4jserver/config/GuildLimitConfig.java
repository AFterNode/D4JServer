package cn.afternode.d4jserver.config;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GuildLimitConfig {
    private boolean enabled = true;
    private List<Long> allowed = new ArrayList<>();
}
