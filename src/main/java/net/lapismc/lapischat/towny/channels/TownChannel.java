package net.lapismc.lapischat.towny.channels;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.lapismc.lapischat.LapisChat;
import net.lapismc.lapischat.framework.Channel;
import net.lapismc.lapischat.framework.ChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class TownChannel extends Channel {

    public TownChannel(String name, String shortName, String prefix, Permission perm, String format) {
        super(name, shortName, prefix, perm, format);
    }

    @Override
    protected String format(ChatPlayer from, String msg, String format) {
        return applyDefaultFormat(from, msg, format);
    }

    @Override
    public List<ChatPlayer> getRecipients(ChatPlayer p) {
        List<ChatPlayer> list = new ArrayList<>();
        try {
            Resident resident = TownyUniverse.getDataSource().getResident(p.getPlayer().getName());
            Town town = resident.getTown();
            for (Resident r : town.getResidents()) {
                //noinspection deprecation
                OfflinePlayer op = Bukkit.getOfflinePlayer(r.getName());
                if (op.isOnline()) {
                    list.add(LapisChat.getInstance().getPlayer(op.getUniqueId()));
                }
            }
        } catch (NotRegisteredException e) {
            return list;
        }
        return list;
    }
}
