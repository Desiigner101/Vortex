package com.vortex.CharacterStats;

public interface Character_BattleStats {
    public int getHP();
    public int getMaxHP();
    public void setHP(int HP);

    public int getBasicAttackDamage();
    public int getSkillDamage();
    public int getSkillCost();
    public int getUltimateDamage();
    public int getUltCooldown();
    public String getBasicAtkImage();
    public String getSkillImage();
    public String getUltImage();
    public String getIdleAnimation();
    public String getBasicAtkAnimation();
}
