package com.vortex.game.BattleClasses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

interface EnemyClass {
     public int getHP();
     public int getMaxHP();
     public void setHP(int HP);
     public int getAtk();
     public String getName();
     public String getIdleAnimation();
     public boolean isBoss();
     public TextureRegion getCurrentFrame();
     public void update(float delta);
     public void dispose();
}
