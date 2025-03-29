package com.vortex.game.BattleClasses;

public class TestBossClass implements EnemyClass{
    private int HP = 100;
    private int maxHP = 100;
    private int Atk = 500;
    private String name = "TestBoss";
    @Override
    public int getHP() {
        return HP;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    @Override
    public void setHP(int HP) {
        this.HP = HP;
    }

    @Override
    public int getAtk() {
        return Atk;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIdleAnimation() {
        return "Enemies/TestBoss.png";
    }
}
