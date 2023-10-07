package cn.afternode.d4jserver.impl;

import cn.afternode.d4jserver.api.GuildManager;
import discord4j.core.object.entity.Guild;
import org.apache.commons.lang3.ArrayUtils;

public class GuildManagerImpl implements GuildManager {

    @Override
    public long[] getAllowedGuilds() {
        return ArrayUtils.toPrimitive(D4JServerImpl.getInstance().getConfig().getGuildLimitConfig().getAllowed().toArray(new Long[0]));
    }

    @Override
    public boolean isGuildAllowed(Guild guild) {
        return isGuildAllowed(guild.getId().asLong());
    }

    @Override
    public boolean isGuildAllowed(long id) {
        if (isUnlimited()) return true;

        for (long i: getAllowedGuilds()) {
            if (id == i) return true;
        }
        return false;
    }

    @Override
    public boolean isUnlimited() {
        return !D4JServerImpl.getInstance().getConfig().getGuildLimitConfig().isEnabled();
    }
}
