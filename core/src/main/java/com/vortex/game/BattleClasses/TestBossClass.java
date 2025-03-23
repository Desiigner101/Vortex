package com.vortex.game.BattleClasses;

public class TestBossClass implements EnemyClass{
    private int HP = 2000;
    private int Atk = 100;
    @Override
    public int getHP() {
        return HP;
    }

    @Override
    public int getMaxHP() {
        return 2000;
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
        return "Test Boss";
    }

    @Override
    public String getIdleAnimation() {
        return "Enemies/TestBoss.png";
    }
}
