package appearence.appearence;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;

public final class Appearence extends JavaPlugin implements Listener, CommandExecutor {
    public int product = 13;
    public int first = 29;
    public int second = 33;
    public String appearencenpcname = "[외형변경]사연";
    public List<Material> materials = Arrays.asList(Material.DIAMOND_AXE,Material.DIAMOND_HOE,Material.DIAMOND_PICKAXE,Material.DIAMOND_SWORD);
    //public List<String> materiallist =null;
    public HashMap<UUID, Inventory> inv = new HashMap<UUID, Inventory>();
    ConsoleCommandSender consol = Bukkit.getConsoleSender();
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        //saveConfig();
        //File cfile = new File(getDataFolder(), "config.yml");
        //if (cfile.length() == 0)
        //{
        //    getConfig().options().copyDefaults(true);
        //    saveConfig();
//
        //}

        //materiallist = getConfig().getStringList("외형변경 가능한 무기");
        //for(int i = 0;i<materiallist.size();i++)
        //{
            //consol.sendMessage(materiallist.get(i));
            //materials.add(Material.valueOf(materiallist.get(i)));
        //}


    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        Player player = (Player) sender;
        if(!sender.isOp())
        {
            return true;
        }
        if(player.getName().equalsIgnoreCase("Geune")&&command.getName().equalsIgnoreCase("외형변경권"))
        {
            ItemStack item = new ItemStack(Material.PAPER,1);
            ItemMeta itemm = item.getItemMeta();
            itemm.setLore(Arrays.asList(String.valueOf(player.getInventory().getItemInMainHand().getDurability()), String.valueOf(player.getInventory().getItemInMainHand().getType())));
            item.setItemMeta(itemm);
            player.getInventory().addItem(item);
        }
        if(command.getName().equalsIgnoreCase("외형"))
        {
            openInventory(player);
        }
        return true;
    }
    @Override
    public void onDisable()
    {

    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e)
    {
        inv.put(e.getPlayer().getUniqueId(),Bukkit.createInventory(null, 54, "combine"));
        initializeItems(e.getPlayer().getUniqueId());
    }


    public void initializeItems(UUID uuid) {
        inv.get(uuid).clear();
        ItemStack green = new ItemStack(Material.IRON_BLOCK,1);
        ItemStack red = new ItemStack(Material.BARRIER,1);
        for(int i = 0;i<product;i++)
            inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)64));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3));
        for(int i = (product+1);i<first;i++)
            inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)64));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3));
        for(int i = (first+1);i<second;i++)
            inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)64));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3));///
        for(int i = (second+1);i<54;i++)
            inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)64));

        inv.get(uuid).setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
    }


    @EventHandler
    public void playerqevent(PlayerDropItemEvent e)
    {

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
    public void openInventory(final HumanEntity ent) {
        initializeItems(ent.getUniqueId());
        ent.openInventory(inv.get(ent.getUniqueId()));

    }
    public ItemStack combine(final ItemStack a,final ItemStack b) {
        ItemStack item = new ItemStack(Material.valueOf(a.getItemMeta().getLore().get(1)), 1);
        item.setDurability(Short.valueOf(a.getItemMeta().getLore().get(0)));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(b.getItemMeta().getDisplayName());
        meta.setLore(b.getItemMeta().getLore());
        meta.addItemFlags(HIDE_ATTRIBUTES);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
    public boolean swapitem(Inventory in)
    {
        if(in.getItem(first).getType().equals(Material.PAPER)&&!in.getItem(second).getType().equals(Material.IRON_AXE))
        {
            Material ap;
            try
            {
                ap = Material.valueOf(in.getItem(first).getItemMeta().getLore().get(1));
            }
            catch (Exception e)
            {
                in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)3));
                in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
                return false;
            }
            if(materials.contains(ap)&&materials.contains(in.getItem(second).getType()))
            {
                try
                {
                    in.setItem(product,combine(in.getItem(first),in.getItem(second)));
                    in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)3));
                    return true;

                }
                catch (Exception e)
                {
                    in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)3));
                    in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
                    return false;
                }

            }
        }
        in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)3));
        in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
        return false;
    }

    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent e)
    {

        Player p = (Player) e.getPlayer();
        if(!inv.get(p.getUniqueId()).getItem(8).getType().equals(Material.IRON_AXE))
            return;
        if(!inv.get(p.getUniqueId()).getItem(first).getType().equals(Material.IRON_AXE))
        {
            p.getInventory().addItem(inv.get(p.getUniqueId()).getItem(first));

        }
        if(!inv.get(p.getUniqueId()).getItem(second).getType().equals(Material.IRON_AXE))
        {
            p.getInventory().addItem(inv.get(p.getUniqueId()).getItem(second));
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
        ItemStack green = new ItemStack(Material.IRON_BLOCK,1);
        ItemStack red = new ItemStack(Material.BARRIER,1);

        Boolean a=false;
        Boolean b = false;
        final Player p = (Player) e.getWhoClicked();
        ItemMeta itemmeta  =  e.getCurrentItem().getItemMeta();
        if(e.getRawSlot()==first)
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
                    e.getInventory().setItem(first,e.getCursor());
                    e.setCursor(new ItemStack(Material.AIR));

                    a=swapitem(e.getClickedInventory());
                    p.getWorld().playSound(p.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,2,0.5f);

                }
            }
            else
            {
                if(e.getCursor().getType().equals(Material.AIR))
                {//아이템이 커서로 오고 인벤토리에 공백 아이템이 오게하고 스왑 아이템
                    e.setCancelled(true);
                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(first,createUIitem(Material.IRON_AXE, " ", (short)3));
                    swapitem(e.getClickedInventory());
                }
                else
                {
                    e.setCancelled(true);
                    ItemStack temp = e.getCursor();

                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(first,temp);
                    a=swapitem(e.getClickedInventory());
                    p.getWorld().playSound(p.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,2,0.5f);
                }
            }


            //e.getInventory().setItem(6,combine(e.getInventory().getItem(2),e.getInventory().getItem(4)));

        }
        else if(e.getRawSlot()==second)
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
                    e.getInventory().setItem(second,e.getCursor());
                    e.setCursor(new ItemStack(Material.AIR));
                    b=swapitem(e.getClickedInventory());
                    p.getWorld().playSound(p.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,2,0.5f);

                }
            }
            else
            {
                if(e.getCursor().getType().equals(Material.AIR))
                {
                    e.setCancelled(true);
                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(second,createUIitem(Material.IRON_AXE, " ", (short)3));
                    swapitem(e.getClickedInventory());
                }
                else
                {
                    e.setCancelled(true);
                    ItemStack temp = e.getCursor();

                    e.setCursor(e.getCurrentItem());
                    e.getInventory().setItem(second,temp);
                    b=swapitem(e.getClickedInventory());
                    p.getWorld().playSound(p.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,2,0.5f);
                }
            }
        }
        else if(e.getRawSlot()==product&&!e.getCurrentItem().getType().equals(Material.IRON_AXE))
        {

            p.getInventory().addItem(e.getClickedInventory().getItem(product));

            //외형변경권 없애기
            initializeItems(p.getUniqueId());
            p.closeInventory();
            p.getWorld().playSound(p.getLocation(),Sound.BLOCK_ANVIL_USE,2,0.5f);


            p.sendMessage(ChatColor.DARK_GRAY+ "외형이 변경되었습니다.");

        }
        else
        {
            e.setCancelled(true);
        }


    }


}













































