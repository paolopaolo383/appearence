package appearence.appearence;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.checkerframework.checker.units.qual.C;


import java.io.*;
import java.util.*;

import static org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;

public final class Appearence extends JavaPlugin implements Listener, CommandExecutor {
    public HashMap<String, Material> Mitems = new HashMap<String,Material>();
    public HashMap<String, Short> items = new HashMap<String,Short>();
    public HashMap<String, Short> itemsd = new HashMap<String,Short>();
    public int product = 4;
    public int first = 38;//변경권
    public int second = 42;//무기
    public Material appe = Material.PAPER;
    public String appearencenpcname = "[외형변경]사연";
    public HashMap<UUID, Inventory> inv = new HashMap<UUID, Inventory>();
    ConsoleCommandSender consol = Bukkit.getConsoleSender();

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);

        reloadconfig();
        for(Player player : getServer().getOnlinePlayers())
        {
            inv.put(player.getUniqueId(),Bukkit.createInventory(null, 54, "combine"));
            initializeItems(player.getUniqueId());
        }
    }
    public void reloadconfig()
    {
        items.clear();
        Mitems.clear();
        itemsd.clear();
        File pluginfile = new File("plugins","appearence.jar");
        consol.sendMessage(pluginfile.getAbsolutePath().split("appearence.jar")[0]+"Appearence");
        String folderpath = pluginfile.getAbsolutePath().split("appearence.jar")[0]+"Appearence";
        File Folder = new File(folderpath);
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
                System.out.println("[Appearence]폴더가 생성되었습니다.");
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }
        String Filepath = folderpath+"\\weapon.yml";
        File cffile = new File(Filepath);
        if (!cffile.exists()) {	// 파일이 존재하지 않으면 생성
            try {
                if (cffile.createNewFile())
                    System.out.println("[weapon.yml]파일 생성 성공");
                else
                    System.out.println("[weapon.yml]파일 생성 실패");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File("plugins/Appearence", "weapon.yml");
        FileConfiguration cnf = YamlConfiguration.loadConfiguration(file);
        consol.sendMessage(ChatColor.YELLOW+"---------------무기 로드---------------");
        if(cnf.contains("weapon")){
            //consol.sendMessage("File exist");
        }else{
            //consol.sendMessage("File doesnt exist");
            String[] list = {"(보이는 이름)/DIAMOND_SWORD/100/(아이템고유번호 외형 모델링에 쓸 예정)", "(보이는 이름)/DIAMOND_SWORD/100/(아이템고유번호 외형 모델링에 쓸 예정)"};
            cnf.set("weapon", list);
            try {
                cnf.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //consol.sendMessage("Created");
        }

        List<String> weapons = (List<String>) cnf.getList("weapon");
        if(!weapons.isEmpty())
        {
            for(String weapon: weapons)
            {
                try
                {
                    String itemname = weapon.split("/")[0];
                    Material itemmaterial = Material.valueOf(weapon.split("/")[1]);
                    Short itemdurability = Short.valueOf(weapon.split("/")[2]);
                    Short itemuniqued = Short.valueOf(weapon.split("/")[2]);
                    items.put(itemname,itemdurability);
                    Mitems.put(itemname,itemmaterial);
                    itemsd.put(itemname,itemuniqued);
                    consol.sendMessage(ChatColor.BLUE+itemname+"이 로드됨");
                }
                catch (Exception e)
                {
                    consol.sendMessage(ChatColor.RED+weapon+"이 로드되지 않음");
                }

            }
        }
        else
        {
            consol.sendMessage(ChatColor.YELLOW+"로드할 무기가 없음");
        }
        consol.sendMessage(ChatColor.YELLOW+"---------------로드 완료---------------");

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        Player player = (Player) sender;
        if(!sender.isOp())
        {
            return true;
        }
        if((player.getName().equalsIgnoreCase("Geune")||player.getName().equalsIgnoreCase("LastPieceOfLife"))&&command.getName().equalsIgnoreCase("외형변경권"))
        {
            ItemStack item = new ItemStack(appe,1);
            ItemMeta itemm = item.getItemMeta();
            String name =player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            String nameme;
            if(name.contains(" +"))
            {
                nameme =ChatColor.stripColor(name).split(" \\+")[0];
            }
            else
            {
                nameme = name;
            }
            itemm.setDisplayName(ChatColor.getLastColors(name)+"외형변경권<"+nameme+">");
            item.setItemMeta(itemm);
            player.getInventory().addItem(item);
        }
        if(command.getName().equalsIgnoreCase("외형"))
        {
            openInventory(player);
        }
        if(command.getName().equalsIgnoreCase("무기로드"))
        {
            reloadconfig();
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

        inv.get(uuid).setItem(53,createUIitem(Material.IRON_AXE, " ", (short)2));
    }

    protected ItemStack createUIitem(final Material material, final String name,final short damage, final String... lore) {
        final ItemStack item = new ItemStack(material, 1,damage);
        final ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
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
        String name = ChatColor.stripColor(a.getItemMeta().getDisplayName()).split("<")[1].split(">")[0];
        ItemStack item = b.clone();
        item.setDurability(getitemdurability(name));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(HIDE_ATTRIBUTES);


        item.setItemMeta(itemMeta);
        return item;
    }
    public boolean swapitem(Inventory in)
    {

        if(in.getItem(first).getType().equals(appe)&&!in.getItem(second).getType().equals(Material.IRON_AXE))
        {
            String name;
            try
            {
                name =ChatColor.stripColor(in.getItem(first).getItemMeta().getDisplayName().split("<")[1].split(">")[0]);
                if(!items.containsKey(name)||!Mitems.containsKey(name))
                {
                    in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)64));
                    in.setItem(53,createUIitem(Material.IRON_AXE, " ", (short)2));
                    return false;
                }
            }
            catch (Exception e)
            {
                in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)64));
                in.setItem(53,createUIitem(Material.IRON_AXE, " ", (short)2));
                return false;
            }

            try
            {
                if(getitemmaterial(name)==in.getItem(second).getType())
                {
                    in.setItem(product,combine(in.getItem(first),in.getItem(second)));
                    in.setItem(53,createUIitem(Material.IRON_AXE, " ", (short)3));
                    return true;
                }

            }
            catch (Exception e)
            {
                in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)64));
                in.setItem(53,createUIitem(Material.IRON_AXE, " ", (short)2));
                return false;
            }
        }
        in.setItem(product,createUIitem(Material.IRON_AXE, " ", (short)64));
        in.setItem(53,createUIitem(Material.IRON_AXE, " ", (short)2));
        return false;
    }
    public Short getitemdurability(String name)
    {
        return items.get(name);
    }
    public Material getitemmaterial(String name)
    {
        return Mitems.get(name);
    }
    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent e)
    {

        Player p = (Player) e.getPlayer();

        if(!e.getInventory().equals(inv.get(e.getPlayer().getUniqueId()))) return;

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
    public void onInventoryItemMove(InventoryDragEvent e)
    {

        if(e.getInventory().equals(inv.get(e.getWhoClicked().getUniqueId())))
        {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        try {
            if(!e.getClickedInventory().getItem(53).getType().equals(Material.IRON_AXE)||!((e.getClickedInventory().getItem(53).getDurability()==(short)2)||(e.getClickedInventory().getItem(53).getDurability()==(short)3))) return;
            if (!e.getClickedInventory().equals(inv.get(e.getWhoClicked().getUniqueId()))) return;
        }
        catch (Exception ee)
        {
            return;
        }

        final ItemStack clickedItem = e.getCurrentItem().clone();
        final ItemStack cursor = e.getCursor().clone();



        ItemStack green = new ItemStack(Material.IRON_BLOCK,1);
        ItemStack red = new ItemStack(Material.BARRIER,1);

        final Player p = (Player) e.getWhoClicked();
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
                    ItemStack itemmmm = e.getCursor().clone();
                    itemmmm.setAmount(1);
                    e.getClickedInventory().setItem(first,itemmmm);
                    ItemStack itemmmmm = e.getCursor().clone();
                    if(e.getCursor().getAmount()>1)
                    {
                        itemmmmm.setAmount(e.getCursor().getAmount()-1);
                    }
                    else
                    {
                        itemmmmm = new ItemStack(Material.AIR);
                    }
                    e.setCursor(itemmmmm);


                    swapitem(e.getClickedInventory());
                    p.getWorld().playSound(p.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,2,0.5f);

                }
            }
            else
            {

                if(e.getCursor().getType().equals(Material.AIR))
                {//아이템이 커서로 오고 인벤토리에 공백 아이템이 오게하고 스왑 아이템
                    e.setCancelled(true);
                    e.setCursor(clickedItem);
                    e.getClickedInventory().setItem(first,createUIitem(Material.IRON_AXE, " ", (short)64));
                    e.getClickedInventory().setItem(53,createUIitem(Material.IRON_AXE, " ", (short)2));
                    swapitem(e.getClickedInventory());
                }
                else//아이템 같을때 다를때
                {
                    e.setCancelled(true);
                    if(clickedItem.getType().equals(cursor.getType()))
                    {

                    }
                    else
                    {
                        if(cursor.getAmount()>1)
                        {
                            ItemStack tp = cursor.clone();
                            tp.setAmount(cursor.getAmount()-1);
                            e.getWhoClicked().getInventory().addItem(tp);
                            ItemStack tmp = cursor.clone();
                            tmp.setAmount(1);
                            e.getClickedInventory().setItem(first,tmp);
                            e.setCursor(clickedItem);
                        }
                        else
                        {
                            ItemStack temp = cursor.clone();
                            e.getClickedInventory().setItem(first,temp);
                            e.setCursor(clickedItem);
                        }

                    }



                    swapitem(e.getClickedInventory());
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
                    ItemStack tmp = cursor.clone();
                    tmp.setAmount(1);
                    e.getClickedInventory().setItem(second,tmp);
                    ItemStack temp = cursor.clone();
                    if(cursor.getAmount()>1)
                    {
                        temp.setAmount(cursor.getAmount()-1);
                    }
                    else
                    {
                        temp = new ItemStack(Material.AIR);
                    }
                    e.setCursor(temp);
                    swapitem(e.getClickedInventory());
                    p.getWorld().playSound(p.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,2,0.5f);

                }
            }
            else
            {
                if(e.getCursor().getType().equals(Material.AIR))
                {
                    e.setCancelled(true);
                    e.setCursor(clickedItem.clone());
                    e.getClickedInventory().setItem(second,createUIitem(Material.IRON_AXE, " ", (short)64));
                    swapitem(e.getInventory());
                }
                else
                { //커서 인벤
                    e.setCancelled(true);
                    if(cursor.getType().equals(clickedItem.getType()))//같아
                    {

                    }
                    else//달라
                    {
                        if(cursor.getAmount()>1)
                        {
                            ItemStack tp = cursor.clone();
                            tp.setAmount(cursor.getAmount()-1);
                            e.getWhoClicked().getInventory().addItem(tp);
                            ItemStack tmp = cursor.clone();
                            tmp.setAmount(1);
                            e.getClickedInventory().setItem(second,tmp);
                            e.setCursor(clickedItem);
                        }
                        else
                        {
                            ItemStack temp = cursor.clone();
                            e.getClickedInventory().setItem(second,temp);
                            e.setCursor(clickedItem);
                        }

                    }


                    swapitem(e.getInventory());
                    p.getWorld().playSound(p.getLocation(),Sound.ITEM_BOTTLE_FILL_DRAGONBREATH,2,0.5f);
                }
            }
        }
        else if(e.getRawSlot()==product&&!e.getCurrentItem().getType().equals(Material.IRON_AXE))
        {

            p.getInventory().addItem(e.getInventory().getItem(product));

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
