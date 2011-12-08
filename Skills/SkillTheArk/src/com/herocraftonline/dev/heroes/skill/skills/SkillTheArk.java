package com.herocraftonline.dev.heroes.skill.skills;

import org.bukkit.entity.Player;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.api.SkillResult;
import com.herocraftonline.dev.heroes.hero.Hero;
import com.herocraftonline.dev.heroes.skill.ActiveSkill;
import com.herocraftonline.dev.heroes.skill.SkillType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.CreatureType;

public class SkillTheArk extends ActiveSkill {
    public SkillTheArk(Heroes plugin) {
        super(plugin, "TheArk");
        setDescription("Creates a wall of chickens");
        setUsage("/skill theark");
        setArgumentRange(0, 0);
        setIdentifiers(new String[]{"skill theark"});
        
        setTypes(SkillType.SUMMON, SkillType.DARK, SkillType.SILENCABLE);

    }

    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = super.getDefaultConfig();
        return node;
    }

    @Override
    public SkillResult use(Hero hero, String[] args) {
        Player player = hero.getPlayer();
        broadcastExecuteText(hero);
        Block wTargetBlock = player.getTargetBlock(null, 20).getFace(
                        BlockFace.UP);
      
         double rand = Math.random();
        
        int count = 1;
        int chicknum = 5;
        if(count <= chicknum){
            player.getWorld().spawnCreature(wTargetBlock.getLocation(),
                        CreatureType.CHICKEN);
            count++;
        }
        
    
        broadcast(player.getLocation(), "" + count + "x Multiplier!");
        return SkillResult.NORMAL;
    }
    }
    

