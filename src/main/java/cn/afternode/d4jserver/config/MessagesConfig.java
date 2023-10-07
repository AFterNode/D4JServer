package cn.afternode.d4jserver.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class MessagesConfig {
    @SerializedName("cmd-invalid-command")
    private String cmdInvalidCommand = "Invalid command, please type /help";
}
