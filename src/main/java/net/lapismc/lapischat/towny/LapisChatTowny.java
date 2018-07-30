package net.lapismc.lapischat.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.lapismc.lapischat.api.ChannelAPI;
import net.lapismc.lapischat.events.LapisChatEvent;
import net.lapismc.lapischat.towny.channels.TownChannel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public final class LapisChatTowny extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ChannelAPI channelAPI = new ChannelAPI(this);
        String name = getConfig().getString("Channel.Name");
        String shortName = getConfig().getString("Channel.ShortName");
        String prefix = getConfig().getString("Channel.Prefix");
        String permission = getConfig().getString("Channel.Permission");
        String format = getConfig().getString("Channel.Format");
        channelAPI.addChannel(new TownChannel(name, shortName, prefix, new Permission(permission, PermissionDefault.FALSE), format));
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onLapisChat(LapisChatEvent e) {
        String value = "";
        try {
            Resident resident = TownyUniverse.getDataSource().getResident(e.getSender().getPlayer().getName());
            value = getConfig().getString("Format");
            String role = resident.isKing() ? "King" : resident.isMayor() ? "Mayor" : "Resident";
            String townName = resident.getTown().getName();
            value = value.replace("{ROLE}", role);
            value = value.replace("{TOWN_NAME}", townName);
        } catch (NotRegisteredException ignored) {
        }
        e.applyFormat("{TOWNY}", value);
    }
}
