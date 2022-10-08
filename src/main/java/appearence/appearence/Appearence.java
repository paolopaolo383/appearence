package appearence.appearence;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;

public final class Appearence extends JavaPlugin implements Listener, CommandExecutor {

    public HashMap<UUID, Inventory> inv = new HashMap<UUID, Inventory>();
    ConsoleCommandSender consol = Bukkit.getConsoleSender();
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable()
    {

    }
    @EventHandler
    public void onPlayerClickEvent(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand()!=null)
        {
            if(e.getAction().equals(Action.RIGHT_CLICK_AIR))
            {
                if(player.getInventory().getItemInMainHand().getType()== Material.ANVIL)
                {
                    if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("외형변경권"))
                    {
                        openInventory(player);
                    }
                }
            }
            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            {
                if(player.getInventory().getItemInMainHand().getType()== Material.ANVIL)
                {
                    if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("외형변경권"))
                    {
                        e.setCancelled(true);
                        openInventory(player);
                    }
                }
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(command.getName().equalsIgnoreCase("외형"))
        {
            //consol.sendMessage(ChatColor.AQUA+"[Hypixel] 월드 생성중");
            Player player = (Player) sender;
            player.getInventory().addItem(createGuiItem(Material.ANVIL,"외형변경권"));


        }
        return true;
    }
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e)
    {
        inv.put(e.getPlayer().getUniqueId(),Bukkit.createInventory(null, 9, "combine"));
        initializeItems(e.getPlayer().getUniqueId());
    }


    public void initializeItems(UUID uuid) {
        inv.get(uuid).clear();
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short) 50,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"input1"));///
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"input2"));///
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"output"));///
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)50,"menu"));


    }


    protected ItemStack createUIitem(final Material material, final String name,final short damage, final String... lore) {
        final ItemStack item = new ItemStack(material, 1,damage);
        final ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(HIDE_ATTRIBUTES);
        // Set the name of the item
        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }
    public ItemStack combine(final ItemStack a,final ItemStack b) {
        return b;
    }
    public void openInventory(final HumanEntity ent) {
        initializeItems(ent.getUniqueId());
        ent.openInventory(inv.get(ent.getUniqueId()));

    }
    public void swapitem(Inventory in)
    {
        if(!in.getItem(2).getType().equals(Material.IRON_AXE)&&!in.getItem(4).getType().equals(Material.IRON_AXE))
        {
            in.setItem(6,combine(in.getItem(2),in.getItem(4)));
        }
        else
        {
            in.setItem(6,createUIitem(Material.IRON_AXE, " ", (short)50,"output"));
        }
    }
    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent e)
    {
        Player p = (Player) e.getPlayer();
        if(!inv.get(p.getUniqueId()).getItem(8).getType().equals(Material.IRON_AXE))
            return;
        if(!inv.get(p.getUniqueId()).getItem(2).getType().equals(Material.IRON_AXE))
        {
            p.getInventory().addItem(inv.get(p.getUniqueId()).getItem(2));

        }
        if(!inv.get(p.getUniqueId()).getItem(4).getType().equals(Material.IRON_AXE))
        {
            p.getInventory().addItem(inv.get(p.getUniqueId()).getItem(4));
        }
        initializeItems(p.getUniqueId());
    }
    @EventHandler
    public void onInventoryItemMove(InventoryMoveItemEvent e)
    {
        if(e.getDestination().getItem(8).getType().equals(Material.IRON_AXE))
        {
            e.setCancelled(true);
        }
        if(e.getInitiator().getItem(8).getType().equals(Material.IRON_AXE))
        {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        try {
            if (!e.getClickedInventory().equals(inv.get(e.getWhoClicked().getUniqueId()))) return;
        }
        catch (Exception ee)
        {
            return;
        }

        final ItemStack clickedItem = e.getCurrentItem();



        final Player p = (Player) e.getWhoClicked();
        ItemMeta itemmeta  =  e.getCurrentItem().getItemMeta();
        if(e.getRawSlot()==2)
        {

            if(e.getCurrentItem().getType().equals(Material.IRON_AXE))//공백
            {
                if(e.getCursor().getType().equals(Material.AIR))
                {
                    e.setCancelled(true);
                }
                else
                {
                    e.setCancelled(true);
                    e.getInventory().setItem(2,e.getCursor());
                    e.setCursor(new ItemStack(Material.AIR));
                    swapitem(e.getClickedInventory());
                }
            }
            else
            {
                if(e.getCursor().getType().equals(Material.AIR))
                {//아이템이 커서로 오고 인벤토리에 공백 아이템이 오게하고 스왑 아이템
                    e.setCancelled(true);
                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(2,createUIitem(Material.IRON_AXE, " ", (short)50,"input1"));
                    swapitem(e.getClickedInventory());
                }
                else
                {
                    e.setCancelled(true);
                    ItemStack temp = e.getCursor();

                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(2,temp);
                    swapitem(e.getClickedInventory());
                }
            }




            //e.getInventory().setItem(6,combine(e.getInventory().getItem(2),e.getInventory().getItem(4)));

        }
        else if(e.getRawSlot()==4)
        {

            if(e.getCurrentItem().getType().equals(Material.IRON_AXE))//공백
            {
                if(e.getCursor().getType().equals(Material.AIR))
                {
                    e.setCancelled(true);
                }
                else
                {
                    e.setCancelled(true);
                    e.getInventory().setItem(4,e.getCursor());
                    e.setCursor(new ItemStack(Material.AIR));
                    swapitem(e.getClickedInventory());
                }
            }
            else
            {
                if(e.getCursor().getType().equals(Material.AIR))
                {
                    e.setCancelled(true);
                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(4,createUIitem(Material.IRON_AXE, " ", (short)50,"input2"));
                    swapitem(e.getClickedInventory());
                }
                else
                {
                    e.setCancelled(true);
                    ItemStack temp = e.getCursor();

                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(4,temp);
                    swapitem(e.getClickedInventory());
                }
            }

        }
        else if(e.getRawSlot()==6&&!e.getCurrentItem().getType().equals(Material.IRON_AXE))
        {
            initializeItems(p.getUniqueId());
            p.getInventory().addItem(e.getClickedInventory().getItem(6));

            p.getInventory().removeItem(createGuiItem(Material.ANVIL,"외형변경권"));
            //외형변경권 없애기
            p.closeInventory();
        }
        else
        {
            e.setCancelled(true);
        }
        //2 4 6

    }

}












































































//https://monsnode.com/search.php?search=%EC%9E%90%EC%9C%84