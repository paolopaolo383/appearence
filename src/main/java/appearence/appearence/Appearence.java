package appearence.appearence;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
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
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();


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
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent e) {

                Player p = e.getPlayer();
                PacketContainer packet = e.getPacket();
                int id = packet.getIntegers().read(0);

                if (.tutorialnpc.getId() == id) {
                    if (packet.getEntityUseActions().read(0) != EnumWrappers.EntityUseAction.ATTACK) {

                        p.sendMessage("pog");

                    }
                }


            }
        });
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent e) {

                Player p = e.getPlayer();
                PacketContainer packet = e.getPacket();
                int id = packet.getIntegers().read(0);

                if (.tutorialnpc.getId() == id) {
                    if (packet.getEntityUseActions().read(0) != EnumWrappers.EntityUseAction.ATTACK) {

                        p.sendMessage("pog");

                    }
                }


            }
        });
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
            ItemStack itemStack = createUIitem(Material.DIAMOND_PICKAXE,"외형으로 쓰일 무기(?)", (short)50,"t","h","i","s","is lore");
            ItemStack item = createGuiItem(Material.DIAMOND_PICKAXE,"기능으로 쓰일 무기","무기임","암튼 무기임");
            player.getInventory().addItem(item);
            player.getInventory().addItem(itemStack);
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
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"input1"));///
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"input2"));///
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"output"));///
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"menu"));
        inv.get(uuid).addItem(createUIitem(Material.IRON_AXE, " ", (short)3,"menu"));


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
        ItemStack item = new ItemStack(a.getType(), 1);
        item.setDurability(a.getDurability());


        item.setItemMeta(b.getItemMeta());
        return item;
    }
    public void openInventory(final HumanEntity ent) {
        initializeItems(ent.getUniqueId());
        ent.openInventory(inv.get(ent.getUniqueId()));

    }
    public void swapitem(Inventory in)
    {
        if(!in.getItem(2).getType().equals(Material.IRON_AXE)&&!in.getItem(4).getType().equals(Material.IRON_AXE))
        {
            if(in.getItem(2).getType().equals(in.getItem(4).getType()))
            {
                if(materials.contains(in.getItem(2).getType()))
                {
                    in.setItem(6,combine(in.getItem(2),in.getItem(4)));
                    return;
                }
            }
        }
        in.setItem(6,createUIitem(Material.IRON_AXE, " ", (short)3,"output"));

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
                    e.getInventory().setItem(2,createUIitem(Material.IRON_AXE, " ", (short)3,"input1"));
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
                    e.getInventory().setItem(4,createUIitem(Material.IRON_AXE, " ", (short)3,"input2"));
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

            p.getInventory().addItem(e.getClickedInventory().getItem(6));

            p.getInventory().removeItem(createGuiItem(Material.ANVIL,"외형변경권"));
            //외형변경권 없애기
            initializeItems(p.getUniqueId());
            p.closeInventory();

        }
        else
        {
            e.setCancelled(true);
        }
        //2 4 6

    }


}













































