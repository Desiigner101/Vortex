package com.vortex.game.BattleClasses;

 interface EnemyClass {
     public int getHP();
     public int getMaxHP();
     public void setHP(int HP);
     public int getAtk();
     public String getName();
     public String getIdleAnimation();
}
