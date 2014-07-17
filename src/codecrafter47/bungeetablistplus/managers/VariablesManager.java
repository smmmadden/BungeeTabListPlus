package codecrafter47.bungeetablistplus.managers;

import codecrafter47.bungeetablistplus.BungeeTabListPlus;
import codecrafter47.bungeetablistplus.api.PlayerVariable;
import codecrafter47.bungeetablistplus.api.ServerVariable;
import codecrafter47.bungeetablistplus.api.Variable;
import codecrafter47.bungeetablistplus.variables.BalanceVariable;
import codecrafter47.bungeetablistplus.variables.ColorVariable;
import codecrafter47.bungeetablistplus.variables.CurrentServerPlayerCountVariable;
import codecrafter47.bungeetablistplus.variables.DisplayPrefix;
import codecrafter47.bungeetablistplus.variables.GroupVariable;
import codecrafter47.bungeetablistplus.variables.PermPrefix;
import codecrafter47.bungeetablistplus.variables.PermSuffix;
import codecrafter47.bungeetablistplus.variables.PingVariable;
import codecrafter47.bungeetablistplus.variables.PlayerCountVariable;
import codecrafter47.bungeetablistplus.variables.PlayerNameVariable;
import codecrafter47.bungeetablistplus.variables.PlayerRawNameVariable;
import codecrafter47.bungeetablistplus.variables.ServerNameVariable;
import codecrafter47.bungeetablistplus.variables.ServerPlayerCountVariable;
import codecrafter47.bungeetablistplus.variables.TimeVariable;
import codecrafter47.bungeetablistplus.variables.UUIDVariable;
import codecrafter47.bungeetablistplus.variables.WorldVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class VariablesManager {

    private final Map<String, Variable> variables = new HashMap<>();
    private final Map<String, PlayerVariable> playerVariables = new HashMap<>();
    private final Map<String, ServerVariable> serverVariables = new HashMap<>();

    public VariablesManager() {
        super();
        addVariable("server_player_count",
                new CurrentServerPlayerCountVariable());
        addVariable("player_count", new PlayerCountVariable());
        addVariable("gcount", new PlayerCountVariable());
        addVariable("players", new ServerPlayerCountVariable());
        addVariable("name", new PlayerNameVariable());
        addVariable("player", new PlayerNameVariable());
        addVariable("rawname", new PlayerRawNameVariable());
        addVariable("server", new ServerNameVariable());
        addVariable("permprefix", new PermPrefix());
        addVariable("prefix", new PermPrefix());
        addVariable("permsuffix", new PermSuffix());
        addVariable("suffix", new PermSuffix());
        addVariable("displayprefix", new DisplayPrefix());
        addVariable("ping", new PingVariable());
        addVariable("time", new TimeVariable("HH:mm:ss"));
        addVariable("date", new TimeVariable("dd.MM.yyyy"));
        addVariable("second", new TimeVariable("ss"));
        addVariable("seconds", new TimeVariable("ss"));
        addVariable("sec", new TimeVariable("ss"));
        addVariable("minute", new TimeVariable("mm"));
        addVariable("minutes", new TimeVariable("mm"));
        addVariable("min", new TimeVariable("mm"));
        addVariable("hour", new TimeVariable("HH"));
        addVariable("hours", new TimeVariable("HH"));
        addVariable("day", new TimeVariable("dd"));
        addVariable("days", new TimeVariable("dd"));
        addVariable("month", new TimeVariable("MM"));
        addVariable("months", new TimeVariable("MM"));
        addVariable("year", new TimeVariable("yyyy"));
        addVariable("years", new TimeVariable("yyyy"));
        addVariable("group", new GroupVariable());
        addVariable("uuid", new UUIDVariable());
        addVariable("UUID", new UUIDVariable());
        addVariable("world", new WorldVariable());
        addVariable("balance", new BalanceVariable());
        addVariable("color", new ColorVariable());
    }

    public void addVariable(String name, Variable var) {
        variables.put(name, var);
    }

    public void addVariable(String name, PlayerVariable var) {
        playerVariables.put(name, var);
    }

    public void addVariable(String name, ServerVariable var) {
        serverVariables.put(name, var);
    }

    public String replaceVariables(String s) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("\\{[^}]+\\}");
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            String var = s.substring(matcher.start(), matcher.end());
            var = var.replaceAll("[\\{\\}]", "");
            String arg = null;
            if (var.contains(":")) {
                arg = var.substring(var.indexOf(":"), var.length());
            }

            Variable variable = this.variables.get(var);
            String replacement = "?";
            if (variable != null) {
                replacement = variable.getReplacement(arg);
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String replacePlayerVariables(String s, ProxiedPlayer player) {
        if (player.getServer() != null) {
            s = replaceServerVariables(s, player.getServer().getInfo());
        }

        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("\\{[^}]+\\}");
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            String var = s.substring(matcher.start(), matcher.end());
            var = var.replaceAll("[\\{\\}]", "");
            String arg = null;
            if (var.contains(":")) {
                arg = var.substring(var.indexOf(":"), var.length());
            }

            PlayerVariable variable = this.playerVariables.get(var);
            String replacement = "?";
            if (variable != null) {
                replacement = variable.getReplacement(arg, player);
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public String replaceServerVariables(String s, ServerInfo server) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("\\{[^}]+\\}");
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            String var = s.substring(matcher.start(), matcher.end());
            var = var.replaceAll("[\\{\\}]", "");
            String arg = null;
            if (var.contains(":")) {
                arg = var.substring(var.indexOf(":"), var.length());
            }

            ServerVariable variable = this.serverVariables.get(var);
            String replacement = "?";
            if (variable != null) {
                replacement = variable.getReplacement(arg, server);
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
