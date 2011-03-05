package aetaric.gimme;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * gimme block listener
 * @author aetaric
 */
public class gimmeBlockListener extends BlockListener {
    private final gimme plugin;

    public gimmeBlockListener(final gimme plugin) {
        this.plugin = plugin;
    }

    //put all Block related code here
}
