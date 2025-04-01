package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.vortex.SFX.PlayAudio;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameMenu implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private String[] menuOptions = {"New Game", "View Characters", "Settings", "Exit"};
    private Vector2[] optionPositions;
    private int selectedIndex = -1;
    private final GameTransitions game;
    private float screenWidth, screenHeight;
    private float textScale = 2f;
    private float glowAlpha = 0.5f;
    private boolean glowIncreasing = true;
    private int lastHoveredIndex = -1;
    private MainMenuAnimator mainMenuAnimator;
    private PlayAudio sfx;

    public GameMenu(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = generateFont("fonts/Poppins-ExtraBold.ttf", 42);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        optionPositions = new Vector2[menuOptions.length];
        float startX = screenWidth * 0.15f;
        float startY = screenHeight * 0.5f;

        for (int i = 0; i < menuOptions.length; i++) {
            optionPositions[i] = new Vector2(startX, startY - (i * 80));
        }

        mainMenuAnimator = new MainMenuAnimator();
        sfx = new PlayAudio();
        sfx = game.getAudioManager();
        sfx.setMusicVolume(game.getMusicVolume());
        sfx.setSoundVolume(game.getSoundVolume());

        if (!sfx.isMusicPlaying()) {
            sfx.playMusic("MainMenuMusic.wav");
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
       mainMenuAnimator.update(delta);

        batch.begin();
      mainMenuAnimator.render();
        batch.end();

        batch.begin();
        updateGlowEffect(delta);
        updateMouseSelection();

        for (int i = 0; i < menuOptions.length; i++) {
            boolean isSelected = (i == selectedIndex);
            font.setColor(isSelected ? new Color(1, 1, 0, glowAlpha) : Color.LIGHT_GRAY);
            font.draw(batch, menuOptions[i], optionPositions[i].x, optionPositions[i].y);
        }
        batch.end();

        handleInput();
    }

    private void updateGlowEffect(float delta) {
        glowAlpha += (glowIncreasing ? delta : -delta);
        if (glowAlpha >= 1 || glowAlpha <= 0.5f) glowIncreasing = !glowIncreasing;
    }

    private void updateMouseSelection() {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY();
        int previousIndex = selectedIndex;
        selectedIndex = -1;

        GlyphLayout layout = new GlyphLayout();

        for (int i = 0; i < menuOptions.length; i++) {
            layout.setText(font, menuOptions[i]);
            float textWidth = layout.width;
            float textHeight = font.getCapHeight();

            if (mouseX >= optionPositions[i].x && mouseX <= optionPositions[i].x + textWidth &&
                mouseY >= optionPositions[i].y - textHeight && mouseY <= optionPositions[i].y) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1 && selectedIndex != lastHoveredIndex) {
            sfx.playSoundEffect("hover_button.wav", 0);
            lastHoveredIndex = selectedIndex;
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
        }

        if ((Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && selectedIndex != -1) {
            executeAction(selectedIndex);
            sfx.playSoundEffect("whenTextIsClicked.wav", 0);
        }
    }

    private void executeAction(int index) {
        switch (index) {
            case 0: game.newGame(); break;
            case 1: game.displayCharacters(); break;
            case 2: game.showControls(); break;
            case 3: Gdx.app.exit(); break;
        }
    }

    private BitmapFont generateFont(String fontPath, int fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

   @Override
    public void resize(int width, int height) {
      mainMenuAnimator.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        if (sfx != null) {
            sfx.stopMusic();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        mainMenuAnimator.dispose();
        if (sfx != null) {
            sfx.stopMusic();
        }
    }
}
