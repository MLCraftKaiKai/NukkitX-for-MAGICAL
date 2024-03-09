package cn.nukkit.command.defaults;

import cn.nukkit.Nukkit;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.TextFormat;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on 2015/11/12 by xtypr.
 * Package cn.nukkit.command.defaults in project Nukkit .
 */
public class VersionCommand extends VanillaCommand {

    public VersionCommand(String name) {
        super(name,
                "%nukkit.command.version.description",
                "%nukkit.command.version.usage",
                new String[]{"ver", "about"}
        );
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("pluginName", true, CommandParamType.STRING)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0 || !sender.hasPermission("nukkit.command.version.plugins")) {
            final String branch = Nukkit.getBranch();

            sender.sendMessage("正在运行「NukkitX-MAGICAL」 - 1.0.0 - 开源链接：https://github.com/MLCraftKaiKai/NukkitX-for-MAGICAL");


                CompletableFuture.runAsync(() -> {
                    try {
                        URLConnection request = new URL(Nukkit.BRANCH).openConnection();
                        request.connect();
                        InputStreamReader content = new InputStreamReader((InputStream) request.getContent());
                        String latest = "git-" + JsonParser.parseReader(content).getAsJsonObject().get("sha").getAsString().substring(0, 7);
                        content.close();
                    } catch (Exception ignore) {
                    }
                });
            }
            return true;
        }
}
