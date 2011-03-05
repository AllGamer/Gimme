package com.dustinessington.gimme;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author aetaric
 */
public class gimmePlayerListener extends PlayerListener {
    private final gimme plugin;

    public gimmePlayerListener(gimme instance) {
        plugin = instance;
    }

    //Insert Player related code here
}

