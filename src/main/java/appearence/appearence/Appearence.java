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


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;

public final class Appearence extends JavaPlugin implements Listener, CommandExecutor {

    public HashMap<String, String> items = new HashMap<String,String>();
    public int product = 4;
    public int first = 20;
    public int second = 24;
    public String appearencenpcname = "[외형변경]사연";
        //public List<String> materiallist =null;
    public HashMap<UUID, Inventory> inv = new HashMap<UUID, Inventory>();
    ConsoleCommandSender consol = Bukkit.getConsoleSender();

    public void rii() throws IOException {//recieve item information by google spreed sheet
        items = null;
        BufferedReader reader = new BufferedReader(
                new FileReader("c:\\\\items.txt")
        );
        String str;
        Material a = Material.DIAMOND_AXE;
        Material b=Material.DIAMOND_AXE;
        while ((str = reader.readLine()) != null)
        {
            try
            {
                a=Material.valueOf(str);
                b=a;
            }
            catch(Exception e)
            {
                a=null;
            }
            if(a==null)
            {
                String[] str2 = str.split("/");
                items.put("a","b");//str.split("/")[0],b.toString()+"/"+str2[1]
                consol.sendMessage(str.split("/")[0]);

            }
            else
            {
                consol.sendMessage(str);
            }


        }

    }
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);

        File file = new File("c:\\\\items.txt");

        if (!file.exists()) {
            try {

                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            rii();
        } catch (IOException e) {

        }
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
            itemm.setDisplayName("외형변경권<"+player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()+">");
            item.setItemMeta(itemm);

            player.getInventory().addItem(item);
        }
        if(command.getName().equalsIgnoreCase("외형"))
        {
            openInventory(player);
        }
        if(player.getName().equalsIgnoreCase("Geune")&&command.getName().equalsIgnoreCase("아이템받기"))
        {
            try {
                rii();
            } catch (IOException e) {

            }
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
        for(int i = 0;i<54;i++)
            inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)64));

        inv.get(uuid).setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
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
        String name = a.getItemMeta().getDisplayName().split("<")[1].split(">")[0];
        item.setDurability(Short.valueOf(getiteminformation(name).split("/")[1]));
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
            String name = in.getItem(first).getItemMeta().getDisplayName().split("<")[1].split(">")[0];
            Material ap;
            try
            {
                ap = Material.valueOf(getiteminformation(name).split("/")[0]);
            }
            catch (Exception e)
            {
                in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)64));
                in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
                return false;
            }
            if(ap==in.getItem(second).getType())
            {
                try
                {
                    in.setItem(product,combine(in.getItem(first),in.getItem(second)));
                    in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)3));
                    return true;
                }
                catch (Exception e)
                {
                    in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)64));
                    in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
                    return false;
                }

            }
        }
        in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)64));
        in.setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
        return false;
    }
    public String getiteminformation(String name)
    {
        return items.get(name);
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
                    e.getInventory().setItem(first,createUIitem(Material.IRON_AXE, " ", (short)64));
                    e.getInventory().setItem(product+9,createUIitem(Material.IRON_AXE, " ", (short)2));
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
                    e.getInventory().setItem(second,createUIitem(Material.IRON_AXE, " ", (short)64));
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
