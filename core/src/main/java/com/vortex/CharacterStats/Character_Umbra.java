package com.vortex.CharacterStats;

public class Character_Umbra implements Character_BattleStats{
    private int HP = 800;
    private int basicAttackDamage = 80;
    private int skillDamage = 230;
    private int skillCost = 3;
    private int ultimateDamage = 300;
    private int ultCooldown = 9; // turns
    private String basicAtkImage = "Umbra_ShadowStrike.png";
    private String skillImage = "Umbra_DistractingIllusions.png";
    private String ultImage = "Umbra_VeilOfShadows.png";
    @Override
    public int getHP(){
        return HP;
    }
    @Override
    public int getBasicAttackDamage(){
        return basicAttackDamage;
    }
    @Override
    public int getSkillDamage(){
        return skillDamage;
    }
    @Override
    public int getSkillCost(){
        return skillCost;
    }
    @Override
    public int getUltimateDamage(){
        return ultimateDamage;
    }
    @Override
    public int getUltCooldown(){
        return ultCooldown;
    }
    @Override
    public String getBasicAtkImage(){
        return basicAtkImage;
    }
    @Override
    public String getSkillImage(){
        return skillImage;
    }
    @Override
    public String getUltImage(){
        return ultImage;
    }
}
