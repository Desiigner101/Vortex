package com.vortex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.HashMap;

public class LoadGame extends ScreenAdapter {
    private final GameTransitions game;
    private SpriteBatch batch;
    private Texture background;
    private Texture highlightTexture;
    private Texture emptySlotTexture;
    private BitmapFont titleFont, slotFont;
    private int selectedIndex = 0;
    private float screenWidth, screenHeight;
    private Skin skin;
    private Stage stage;
    private TextButton backButton;

    // store save data with 2 slots only
    private HashMap<Integer, SaveSlot> saveSlots = new HashMap<>();

    public LoadGame(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        background = new Texture(Gdx.files.internal("Backgrounds/LoadGame_BG.jpg"));
        highlightTexture = new Texture(Gdx.files.internal("UI/highlight.png"));
        emptySlotTexture = new Texture(Gdx.files.internal("UI/slot_bg.jpg"));

        titleFont = generateFont("Fonts/PressStart-Regular.ttf", 32);
        slotFont = generateFont("Fonts/PressStart-Regular.ttf", 18);

        loadSaveData();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        createUI();
    }

    private void createUI() {
        // back button to return to GameMenu
        backButton = new TextButton("Back", skin);
        backButton.setSize(120, 50);
        backButton.setPosition(20, 20);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new GameMenu(game)); // return to GameMenu
            }
        });
        stage.addActor(backButton);
    }

    private void loadSaveData() {
        // mo automatic ug load ang save data (just placeholders for now)
        saveSlots.put(0, new SaveSlot("Slot 1", "No Data", "", "", false));
        saveSlots.put(1, new SaveSlot("Slot 2", "No Data", "", "", false));

        System.out.println("Save data initialized. Ready to load when implemented.");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        batch.draw(background, 0, 0, screenWidth, screenHeight);

        titleFont.draw(batch, "Load Game", screenWidth * 0.4f, screenHeight * 0.9f);

        float slotX = screenWidth * 0.2f;
        float slotYStart = screenHeight * 0.75f;
        float slotHeight = 120f;

        for (int i = 0; i < 2; i++) {
            SaveSlot slot = saveSlots.get(i);
            float slotY = slotYStart - (i * (slotHeight + 10));

            if (selectedIndex == i) {
                batch.setColor(1, 1, 0.5f, 0.8f);
                batch.draw(highlightTexture, slotX - 20, slotY - slotHeight * 0.8f, 700, slotHeight);
            }

            if (slot.isAvailable) {
                slotFont.setColor(Color.WHITE);
                slotFont.draw(batch, slot.slotName, slotX, slotY);
                slotFont.draw(batch, slot.chapterName, slotX + 20, slotY - 30);
                slotFont.draw(batch, slot.saveDate + " | " + slot.playTime, slotX + 20, slotY - 60);
            } else {
                batch.draw(emptySlotTexture, slotX, slotY - slotHeight * 0.8f, 700, slotHeight);
                slotFont.draw(batch, "Empty Slot", slotX + 20, slotY - 30);
            }
        }
        batch.end();

        stage.act(delta);
        stage.draw();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % 2;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + 2) % 2;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            executeLoadAction(selectedIndex);
        }
    }

    private void executeLoadAction(int index) {
        SaveSlot selectedSlot = saveSlots.get(index);
        if (selectedSlot.isAvailable) {
            System.out.println("Loading game from: " + selectedSlot.slotName);
            // resume game with real save data later
            game.resumeGame(selectedSlot.slotName);
        } else {
            System.out.println("No save data found. Returning to Game Menu...");
            game.setScreen(new GameMenu(game));
        }
    }

    private BitmapFont generateFont(String fontPath, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        highlightTexture.dispose();
        emptySlotTexture.dispose();
        titleFont.dispose();
        slotFont.dispose();
        stage.dispose();
        skin.dispose();
    }

    // Internal SaveSlot class for now
    private static class SaveSlot {
        String slotName;
        String chapterName;
        String saveDate;
        String playTime;
        boolean isAvailable;

        public SaveSlot(String slotName, String chapterName, String saveDate, String playTime, boolean isAvailable) {
            this.slotName = slotName;
            this.chapterName = chapterName;
            this.saveDate = saveDate;
            this.playTime = playTime;
            this.isAvailable = isAvailable;
        }
    }
}
