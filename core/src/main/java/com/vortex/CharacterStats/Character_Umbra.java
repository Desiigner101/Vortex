package com.vortex.CharacterStats;

public class Character_Umbra implements Character_BattleStats{
    private int HP = 800;
    private int basicAttackDamage = 80;
    private int skillDamage = 230;
    private int skillCost = 3;
    private int ultimateDamage = 300;
    private int ultCooldown = 9; // turns
    private String basicAtkImage = "Pictures/Umbra/CharacterView/Umbra_ShadowStrike.png";
    private String skillImage = "Pictures/Umbra/CharacterView/Umbra_DistractingIllusions.png";
    private String ultImage = "Pictures/Umbra/CharacterView/Umbra_VeilOfShadows.png";

    private String idleAnimation = "Pictures/Umbra/BattleView/umbra_idle_battle_sheet.png";
    private String basicAtkAnimation = "Pictures/Umbra/BattleView/umbra_basicAtk_battle.png";

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

    @Override
    public String getIdleAnimation(){
        return idleAnimation;
    }

    @Override
    public String getBasicAtkAnimation(){
        return basicAtkAnimation;
    }
}
