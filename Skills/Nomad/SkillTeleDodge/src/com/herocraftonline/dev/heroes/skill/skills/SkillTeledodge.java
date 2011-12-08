package com.herocraftonline.dev.heroes.skill.skills;

import org.bukkit.Location;
import org.bukkit.util.config.ConfigurationNode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.hero.Hero;
import com.herocraftonline.dev.heroes.skill.PassiveSkill;
import com.herocraftonline.dev.heroes.skill.SkillType;
import com.herocraftonline.dev.heroes.util.Setting;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;
//Just a Quick note, the dodge chance is currently at 100% for testing
public class SkillTeledodge extends PassiveSkill {
    
    private static final Set<Material> noTeleMaterials;
    static {
        noTeleMaterials = new HashSet<Material>();
        noTeleMaterials.add(Material.AIR);
        noTeleMaterials.add(Material.WATER);
    }

    public SkillTeledodge(Heroes plugin) {
        super(plugin, "TDodge");
        setDescription("Passive chance to dodge an enemy attack and teleport away");
        setTypes(SkillType.COUNTER, SkillType.BUFF);
        //Setting up a damage listener
        registerEvent(Type.ENTITY_DAMAGE, new SkillHeroListener(), Priority.Normal);
        
    }

    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = super.getDefaultConfig();
        //Config Values, stated below.
        node.setProperty("chance-to-dodge", .1);    
        node.setProperty("radius", 10);
        node.setProperty("chance-per-level", 0.01);
        node.setProperty(Setting.DURATION.node(), 10000);
        return node;
    }
    
    
        //This is the class where everything actually happens.
    public class SkillHeroListener extends EntityListener {
        @Override
        public void onEntityDamage(EntityDamageEvent event) {
            if (event.isCancelled() || !(event.getEntity() instanceof Player))
                return;
            //Defining Variables
            Player player = (Player) event.getEntity();
            Hero hero = getPlugin().getHeroManager().getHero(player);
            Location location = player.getLocation();
            //This is where we find our players current loc and modify it.
            //All this stuff about radius is to randomize dodge location
            int radius = (int) getSetting(hero, "radius", 10, false);
            int xRadius = (int) (Math.random()*radius);
             if (Math.random() > .5) {
            xRadius = xRadius *-1; } 
             int x = location.getBlockX() + xRadius;
             int zRadius = (int) ((Math.sqrt(radius*radius - xRadius*xRadius)));
             if (Math.random() > .5) {
             zRadius = zRadius *-1;}
              int z = location.getBlockZ() + zRadius;
              int y = location.getBlockY();
            
            //Quick coding note here, eventually add safefall instead of +5 to height. Could still get stuck in a wall.
            if (hero.hasEffect("TDodge")) {
                double chance = getSetting(hero, "chance-to-dodge", 0.1, false) + (getSetting(hero, "chance-per-level", 0.01, false) * hero.getLevel());
                
                if (Math.random() <= chance) {
                    //Setting damage to 0
                    event.setDamage(0);
                    event.setCancelled(true);
                    //Here is where we actually tp
                    hero.getPlayer().teleport(getPlugin().getServer().getWorld(location.getWorld().getName()).getBlockAt(x, y, z).getLocation());
                    Material mat = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
                    while(noTeleMaterials.contains(mat) == false){
                        int adjusty = location.getBlockY() + 2;
                         hero.getPlayer().teleport(getPlugin().getServer().getWorld(location.getWorld().getName()).getBlockAt(x, adjusty, z).getLocation());
                    }
                    broadcast(player.getLocation(), "$1 dodged an attack!", player.getDisplayName());
                }
            }
        }
    }
}