package cn.afternode.d4jserver.api;

import discord4j.core.object.entity.Guild;

public interface GuildManager {
    long[] getAllowedGuilds();
    boolean isGuildAllowed(Guild guild);
    boolean isGuildAllowed(long id);
    boolean isUnlimited();
}
