package com.sakura.floorislava;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import org.bukkit.entity.Player;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.block.BlockFace;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

//import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class FloorIsLava extends JavaPlugin implements CommandExecutor, Listener {
    private BukkitTask task;
    private boolean pause;
	
	public void onEnable() {
		System.out.println("FloorIsLava plugin is ready.");
		this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
		
	}

	@EventHandler
	public void onPlayerMoveEvent (final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		if (this.task != null) {
			
			
			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.COBBLESTONE 
					|| player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR 
					|| player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WATER 
					|| player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LAVA) {
				return;
			}
			else {
				player.setHealth(0);
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(final PlayerRespawnEvent respawn) {
		final Player player = respawn.getPlayer();
		if (this.task != null) {
			player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 8));
		}
	}
	
	
	private void start() {
		this.pause = false;
		this.task = new BukkitRunnable() {
			public void run() {		
				if (FloorIsLava.this.pause) {
					return;
				}	
			}
		}.runTaskTimer((Plugin) this, 0L, 20L);
	}
	
	
	private void stop () {
		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}
		this.pause = false;
	}
	
	
	public boolean onCommand (final CommandSender sender, final Command command, final String label, final String[] args) {
		if (command.getName().equalsIgnoreCase("floorislava")) {
			if (args.length == 1) {
				if(args[0].equalsIgnoreCase("start")) {
					if (this.task != null) {
                        sender.sendMessage(ChatColor.GREEN + "The floor is already lava");
                        return false;
                    }
					for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
						onlinePlayer.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.COBBLESTONE);
						onlinePlayer.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 32));
					}
					this.start();
					sender.sendMessage(ChatColor.WHITE + "Changing the floor to lava");
					return true;
				}
				else {
					if(args[0].equalsIgnoreCase("pause")) {
						this.pause = !this.pause;
						sender.sendMessage(ChatColor.WHITE + "FloorIsLava is " + (this.pause ? "paused." : "restarted."));
						return true;
					}
					if (args[0].equalsIgnoreCase("stop")) {
						this.stop();
						sender.sendMessage(ChatColor.WHITE + "The Floor is no longer lava.");
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}

}
