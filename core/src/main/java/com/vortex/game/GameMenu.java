package com.vortex.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vortex.SpriteSheetAnimator.SpriteSheetAnimator;


public class GameMenu implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private String[] menuOptions = {"New Game", "Load Game", "View Characters", "Settings", "Exit"};
    private Vector2[] optionPositions;
    private int selectedIndex = -1;
    private final GameTransitions game;
    private Texture background, highlightTexture;
    private float screenWidth, screenHeight;
    private float textScale = 2f;
    private float glowAlpha = 0.5f;
    private boolean glowIncreasing = true;

    public GameMenu(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = generateFont("fonts/PressStart-Regular.ttf", 15);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        background = new Texture(Gdx.files.internal("Backgrounds/LabMenu_temp.png"));
        highlightTexture = new Texture(Gdx.files.internal("UI/highlight.png"));
        optionPositions = new Vector2[menuOptions.length];
        float startX = screenWidth * 0.15f;
        float startY = screenHeight * 0.7f;

        for (int i = 0; i < menuOptions.length; i++) {
            optionPositions[i] = new Vector2(startX, startY - (i * 80));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(background, 0, 0, screenWidth, screenHeight);
        updateGlowEffect(delta);
        updateMouseSelection();

        for (int i = 0; i < menuOptions.length; i++) {
            float textWidth = getTextWidth(menuOptions[i], textScale);
            float textHeight = font.getLineHeight() * textScale;
            boolean isSelected = (i == selectedIndex);

            if (isSelected) {
                batch.setColor(1, 1, 0.5f, 0.7f);
                batch.draw(highlightTexture, optionPositions[i].x - 20, optionPositions[i].y - (textHeight * 0.85f), textWidth + 40, textHeight + 20);
            }

            font.setColor(isSelected ? new Color(0, 1, 1, glowAlpha) : Color.LIGHT_GRAY);
            font.draw(batch, menuOptions[i], optionPositions[i].x, optionPositions[i].y);
        }
        batch.end();
        handleInput();
    }

    private void updateGlowEffect(float delta) {
        glowAlpha += (glowIncreasing ? delta : -delta);
        if (glowAlpha >= 1 || glowAlpha <= 0.5f) glowIncreasing = !glowIncreasing;
    }

    private float getTextWidth(String text, float scale) {
        font.getData().setScale(scale);
        GlyphLayout layout = new GlyphLayout(font, text);
        return layout.width;
    }

    private void updateMouseSelection() {
        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY();
        selectedIndex = -1;

        for (int i = 0; i < menuOptions.length; i++) {
            float textWidth = getTextWidth(menuOptions[i], textScale);
            float textHeight = font.getLineHeight() * textScale;
            if (mouseX >= optionPositions[i].x && mouseX <= optionPositions[i].x + textWidth &&
                mouseY >= optionPositions[i].y - textHeight && mouseY <= optionPositions[i].y) {
                selectedIndex = i;
                break;
            }
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
        }
    }

    private void executeAction(int index) {
        switch (index) {
            case 0: game.newGame(); break;
            case 2: game.displayCharacters(); break;
            case 4: Gdx.app.exit(); break;
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        background.dispose();
        highlightTexture.dispose();
    }

    private BitmapFont generateFont(String fontPath, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2f;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
