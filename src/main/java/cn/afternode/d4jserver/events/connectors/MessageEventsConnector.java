package cn.afternode.d4jserver.events.connectors;

import cn.afternode.d4jserver.api.D4JServerAPI;
import cn.afternode.d4jserver.api.event.events.message.MessageInteractEvent;
import cn.afternode.d4jserver.api.event.events.message.MessageReceiveEvent;
import cn.afternode.d4jserver.api.event.events.message.MultiMessageDeleteEvent;
import cn.afternode.d4jserver.events.SubConnector;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.message.MessageBulkDeleteEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.MessageEditSpec;
import discord4j.discordjson.json.ChannelData;
import discord4j.discordjson.possible.Possible;
import discord4j.rest.entity.RestChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MessageEventsConnector extends SubConnector {
    private void onMessageBulkDelete(@NotNull MessageBulkDeleteEvent event) {
        try {
            Guild g = event.getGuild().block();

            if (D4JServerAPI.getGuildManager().isGuildAllowed(g)) {
                D4JServerAPI.getEventManager().call(new MultiMessageDeleteEvent(event, g));
            }
        } catch (Throwable t) {
            handleError(event, t);
        }
    }

    private void onMessageCreate(@NotNull MessageCreateEvent event) {
        try {
            Guild guild = event.getGuild().block();
            if (D4JServerAPI.getGuildManager().isGuildAllowed(guild)
                    && event.getMember().isPresent()) {

                RestChannel rest = event.getMessage().getRestChannel();
                ChannelData channelData = rest.getData().block();
                String channelName;
                if (channelData != null) channelName = channelData.name().get(); else channelName = "null";

                if (guild != null) {
                    MessageReceiveEvent callback = (MessageReceiveEvent) D4JServerAPI.getEventManager().call(new MessageReceiveEvent(event, guild));
                    if (callback.isCancelled()) {
                        event.getMessage().delete(callback.getDeleteReason()).block();
                    }

                    Member member = event.getMember().get();
                    String name;
                    if (member.getGlobalName().isPresent()) name = member.getGlobalName().get(); else name = member.getDisplayName();
                    logger.info("[Message][ " + guild.getName() + " / " + channelName + " ][ " + name + " ] " + event.getMessage().getContent());
                }
            }
        } catch (Throwable t) {
            handleError(event, t);
        }
    }

    private void onMessageDelete(@NotNull MessageDeleteEvent e) {
        try {
            Guild guild = e.getGuild().block();
            if (D4JServerAPI.getGuildManager().isGuildAllowed(guild)) {
                D4JServerAPI.getEventManager().call(new cn.afternode.d4jserver.api.event.events.message.MessageDeleteEvent(e, guild));
            }
        } catch (Throwable t) {
            handleError(e, t);
        }
    }

    private void onMessageInteraction(@NotNull MessageInteractionEvent event) {
        try {
            Guild g = event.getResolvedMessage().getGuild().block();

            if (D4JServerAPI.getGuildManager().isGuildAllowed(g)) {
                D4JServerAPI.getEventManager().call(new MessageInteractEvent(event, g));
            }
        } catch (Throwable t) {
            handleError(event, t);
        }
    }

    private void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        try {
            Guild g = event.getGuild().block();

            if (D4JServerAPI.getGuildManager().isGuildAllowed(g)) {
                cn.afternode.d4jserver.api.event.events.message.MessageUpdateEvent ec = (cn.afternode.d4jserver.api.event.events.message.MessageUpdateEvent) D4JServerAPI.getEventManager().call(new cn.afternode.d4jserver.api.event.events.message.MessageUpdateEvent(event, g));
                if (ec.isCancelled()) {
                    ec.getMessage().edit(MessageEditSpec.builder().content(Possible.of(Optional.of(ec.getOld().getContent()))).build()).block();
                }
            }
        } catch (Throwable t) {
            handleError(event, t);
        }
    }

    @Override
    public void connect(GatewayDiscordClient g) {
        g.on(MessageBulkDeleteEvent.class).subscribe(this::onMessageBulkDelete);
        g.on(MessageCreateEvent.class).subscribe(this::onMessageCreate);
        g.on(MessageDeleteEvent.class).subscribe(this::onMessageDelete);
        g.on(MessageInteractionEvent.class).subscribe(this::onMessageInteraction);
        g.on(MessageUpdateEvent.class).subscribe(this::onMessageUpdate);
    }
}
