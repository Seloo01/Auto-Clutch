package mainpackage.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {
    private boolean isActivated = false;

    @Override
    public void onEnable() {
        System.out.println("Eklenti Aktif!");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("plugin").setExecutor(this);
    }

    @Override
    public void onDisable() {
        System.out.println("Eklenti Aktif Değil!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                sender.sendMessage(ChatColor.RED + "Gerekli yetkiye sahip değilsiniz");
                player.sendTitle("", ChatColor.RED + "" + ChatColor.BOLD + "!");
                return false;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Baslatmak için /plugin baslat yazabilirsiniz");
                sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Kapatmak için /plugin kapat yazabilirsiniz");
                player.sendTitle("", ChatColor.YELLOW + "" + ChatColor.BOLD + "!");
                return false;
            }
            if (args[0].equals("baslat")) {
                if (this.isActivated) {
                    sender.sendMessage(ChatColor.RED + "Plugin zaten aktif?");
                    player.sendTitle("", ChatColor.RED + "" + ChatColor.BOLD + "?");
                    return false;
                }
                this.isActivated = true;
                sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Eklenti başlatıldı!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendTitle(ChatColor.GREEN + "Blume Plugins", ChatColor.BOLD + "İyi eğlenceler diler.!");
                    }
                }.runTaskLater(this, 30);
                return true;
            }
            if (args[0].equals("kapat")) {
                if (!this.isActivated) {
                    sender.sendMessage(ChatColor.GRAY + "Plugin zaten devre dışı");
                    player.sendTitle("", ChatColor.RED + "" + ChatColor.BOLD + "?");
                    return false;
                }
                this.isActivated = false;
                sender.sendMessage(ChatColor.YELLOW + "Eklenti durduruldu!");
                player.sendTitle("", ChatColor.YELLOW + "" + ChatColor.BOLD + "!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendTitle(ChatColor.GREEN + "Blume Plugins", ChatColor.BOLD + "İyi eğlenceler diler.!");
                    }
                }.runTaskLater(this, 30);
                return true;
            }
            return false;
        }
        return true;
    }
    public boolean isHandEmpty(Player p) {
        if(p.getInventory().getItemInMainHand()==null) {
            return true;
        }
        else {
            if(p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.GREEN + "Hoşgeldin" + player.getName());
    }

    @EventHandler
    public void onSneak(PlayerInteractEvent e) {
        if (this.isActivated) {
            Player player = e.getPlayer();
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                    if (isHandEmpty(player)) {
                        Block block = player.getLocation().getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                        block.setType(Material.WATER);
                        player.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, block.getLocation(), 200);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                block.setType(Material.AIR);
                                player.getWorld().spawnParticle(Particle.WATER_BUBBLE, block.getLocation(), 20);
                            }
                        }.runTaskLater(this, 5);
                    }
                }
            }
        }
    }
}
