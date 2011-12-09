package com.herocraftonline.dev.heroes.skill.skills;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.api.SkillResult;
import com.herocraftonline.dev.heroes.effects.common.SafeFallEffect;
import com.herocraftonline.dev.heroes.hero.Hero;
import com.herocraftonline.dev.heroes.skill.ActiveSkill;
import com.herocraftonline.dev.heroes.skill.SkillType;
import com.herocraftonline.dev.heroes.util.Setting;
import org.bukkit.util.config.ConfigurationNode;

// The Jump velocity and the safefall duration need to be tested. 
//I cant seem to figure out how to bind this either. could be my server derping.

public class SkillNomadJump extends ActiveSkill {

    public SkillNomadJump(Heroes plugin) {
        super(plugin, "NomadJump");
        setDescription("The Traveler uses his powerful legs to launch himself into the air");
        setUsage("/skill njump");
        setArgumentRange(0, 0);
        setIdentifiers("skill njump");
        setTypes(SkillType.MOVEMENT, SkillType.PHYSICAL);
    }
    
    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = super.getDefaultConfig();
        node.setProperty(Setting.DURATION.node(), 2000);
        node.setProperty("jump-force-multiplier", 10);
        return node;
    }

    @Override
    public SkillResult use(Hero hero, String[] args) {
        Player player = hero.getPlayer();
        float jumpMult = (float) getSetting(hero, "jump-force-multiplier", 10, false);
        float pitch = player.getEyeLocation().getPitch();
        int jumpForwards = 1;
        if (pitch > 45) {
            jumpForwards = 1;
        }
        if (pitch > 0) {
            pitch = -pitch;
        }
        float multiplier = ((90f + pitch) / 50f) * jumpMult;
        Vector v = player.getVelocity().setY(1).add(player.getLocation().getDirection().setY(0).normalize().multiply(multiplier * jumpForwards));
        player.setVelocity(v);
        player.setFallDistance(-8f);
        int duration = (int) getSetting(hero, Setting.DURATION.node(), 2000, false);
        hero.addEffect(new SafeFallEffect(this, duration));
        broadcastExecuteText(hero);
        
        return SkillResult.NORMAL;
    }
}