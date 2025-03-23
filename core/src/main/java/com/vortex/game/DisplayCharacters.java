package com.vortex.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
//stinky
public class DisplayCharacters implements Screen {
    private SpriteBatch batch;

    private Texture umbraShadowStrike;
    private Texture umbraDistractingIllusions;
    private Texture umbraVeilOfShadows;
    private int selectedSkill = -1; // -1 means no skill is selected

    private Texture nova_EnergyPunch;
    private Texture nova_EnergyBlaster;
    private Texture nova_Multi;

    private Texture Jina_SledgeStrike;
    private Texture Jina_PrecisionShot;
    private Texture Jina_VanguardsResolve;

    private Texture[] centerCharacterImages;
    private Texture[] leftSideIcons;
    private Texture selectedCharacter;
    private int currentCharacterIndex;

    private BitmapFont font;
    private GameTransitions game;
    private Color[] glowColors;
    private int hoveredIndex = -1;

    private ShapeRenderer shapeRenderer;
    private String[] characterNames = {"Umbra", "Nova", "Jina"};
    private String[][] characterInfo = {};
    private Texture displayImageUmbra;
    private Texture displayImageNova;
    private Texture displayImageJina;
    private int steps; // Higher = smoother gradient
    private float stepHeight;

    public DisplayCharacters(GameTransitions game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        // Character Icons
        leftSideIcons = new Texture[]{
            new Texture("Pictures/Umbra/CharacterView/UmbraIcon.png"),
            new Texture("Pictures/Nova/CharacterView/NovaIcon.png"),
            new Texture("Pictures/Jina/CharacterView/JinaIcon.png")
        };

        // Center Character Images
        centerCharacterImages = new Texture[]{
            new Texture("Pictures/Umbra/CharacterView/Umbra_CharView.png"),
            new Texture("Pictures/Nova/CharacterView/Nova_CharView.png"),
            new Texture("Pictures/Jina/CharacterView/Jina_CharView.png")
        };

        currentCharacterIndex = 0;
        selectedCharacter = centerCharacterImages[currentCharacterIndex];

        // Umbra's Skills
        umbraShadowStrike = new Texture("Pictures/Umbra/CharacterView/Umbra_ShadowStrike.png");
        umbraDistractingIllusions = new Texture("Pictures/Umbra/CharacterView/Umbra_DistractingIllusions.png");
        umbraVeilOfShadows = new Texture("Pictures/Umbra/CharacterView/Umbra_VeilOfShadows.png");

        // Nova's Skills
        nova_EnergyPunch = new Texture("Pictures/Nova/CharacterView/Nova_EnergyPunch.png");
        nova_EnergyBlaster = new Texture("Pictures/Nova/CharacterView/Nova_EnergyBlaster.png");
        nova_Multi = new Texture("Pictures/Nova/CharacterView/Nova_MultidimensionalBlast.png");

        // Jina's Skills
        Jina_SledgeStrike = new Texture("Pictures/Jina/CharacterView/Jina_SledgeStrike.png");
        Jina_PrecisionShot = new Texture("Pictures/Jina/CharacterView/Jina_PrecisionShot.png");
        Jina_VanguardsResolve = new Texture("Pictures/Jina/CharacterView/Jina_VanguardsResolve.png");

        glowColors = new Color[]{
            new Color(0.8f, 0.6f, 1f, 1f),  // Purple (Umbra)
            new Color(0.2f, 0.4f, 1f, 1f),  // Blue (Nova)
            new Color(1f, 0.9f, 0.3f, 1f)   // Orange/Yellow (Jina)
        };


        // Display Background
        displayImageUmbra = new Texture("Pictures/Umbra/CharacterView/Umbra_CharViewBackground.png");
        displayImageNova = new Texture("Pictures/Nova/CharacterView/Nova_CharViewBackground.png");
        displayImageJina = new Texture("Pictures/Jina/CharacterView/Jina_CharViewBackground.png");

        // **INPUT PROCESSING**
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                int index = getHoveredCharacterIndex(screenX, screenY);
                if (index != -1) {
                    currentCharacterIndex = index;
                    selectedCharacter = centerCharacterImages[currentCharacterIndex];
                    selectedSkill = -1;
                }

                int screenHeight = Gdx.graphics.getHeight();
                screenY = screenHeight - screenY;
                float imageWidth = 66;
                float imageHeight = 66;
                float margin = 435;
                float spacing = 20;
                float x1 = Gdx.graphics.getWidth() - imageWidth - margin;
                float y = Gdx.graphics.getHeight() - imageHeight - 390;
                float x2 = x1 + imageWidth + spacing;
                float x3 = x2 + imageWidth + spacing;

                if (currentCharacterIndex == 0) { // Umbra Skills
                    if (screenX >= x1 && screenX <= x1 + imageWidth && screenY >= y && screenY <= y + imageHeight) {
                        selectedSkill = 0;
                    } else if (screenX >= x2 && screenX <= x2 + imageWidth && screenY >= y && screenY <= y + imageHeight) {
                        selectedSkill = 1;
                    } else if (screenX >= x3 && screenX <= x3 + imageWidth && screenY >= y && screenY <= y + imageHeight) {
                        selectedSkill = 2;
                    }
                }
                else if (currentCharacterIndex == 1) { // 🔹 Nova Skills
                    // Adjust margin and Y position ONLY for Nova
                    float adjustedMargin = 380;  // Change margin for Nova
                    float adjustedY = Gdx.graphics.getHeight() - imageHeight - 350; // Move up slightly
                    float yBuffer = 40; // Small buffer for better click accuracy

                    float x1_Nova = Gdx.graphics.getWidth() - imageWidth - adjustedMargin;
                    float x2_Nova = x1_Nova + imageWidth + spacing;
                    float x3_Nova = x2_Nova + imageWidth + spacing;

                    if (screenX >= x1_Nova && screenX <= x1_Nova + imageWidth &&
                        screenY >= (adjustedY - yBuffer) && screenY <= (adjustedY + imageHeight + yBuffer)) {
                        selectedSkill = 0;
                    } else if (screenX >= x2_Nova && screenX <= x2_Nova + imageWidth &&
                        screenY >= (adjustedY - yBuffer) && screenY <= (adjustedY + imageHeight + yBuffer)) {
                        selectedSkill = 1;
                    } else if (screenX >= x3_Nova && screenX <= x3_Nova + imageWidth &&
                        screenY >= (adjustedY - yBuffer) && screenY <= (adjustedY + imageHeight + yBuffer)) {
                        selectedSkill = 2;
                    }
                }

                else if (currentCharacterIndex == 2) { // Jina's Skills
                    if (screenX >= x1 && screenX <= x1 + imageWidth && screenY >= y && screenY <= y + imageHeight) {
                        selectedSkill = 0;
                    } else if (screenX >= x2 && screenX <= x2 + imageWidth && screenY >= y && screenY <= y + imageHeight) {
                        selectedSkill = 1;
                    } else if (screenX >= x3 && screenX <= x3 + imageWidth && screenY >= y && screenY <= y + imageHeight) {
                        selectedSkill = 2;
                    }
                }



                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                hoveredIndex = getHoveredCharacterIndex(screenX, screenY);
                return false;
            }
        });
    }

    private int getHoveredCharacterIndex(int x, int y) {
        int screenHeight = Gdx.graphics.getHeight();
        y = screenHeight - y;
        for (int i = 0; i < leftSideIcons.length; i++) {
            int iconY = screenHeight - 400 - (i * 120);
            if (x >= 60 && x <= 160 && y >= iconY && y <= iconY + 75) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw outer glowing rectangle
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(glowColors[currentCharacterIndex]);
        shapeRenderer.rect(50, 50, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 100);
        shapeRenderer.end();
        batch.begin();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float borderX = 51;
        float borderY = 51;
        float borderWidth = screenWidth - 102;
        float borderHeight = screenHeight - 102;


        if (currentCharacterIndex == 0) {

            // Start drawing filled shapes
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            steps = 300;
            stepHeight = borderHeight / (float) steps;

            for (int i = 0; i < steps; i++) {
                float t = i / (float) (steps - 1); // Normalized progress (0 to 1)

                Color blendedColor;
                if (t < 0.25f) {
                    // **Dark Blue-Black → Deep Violet (Bottom area)**
                    float t1 = t / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.02f, 0.2f, t1), // Slight Red tint
                        MathUtils.lerp(0.02f, 0.0f, t1), // No Green
                        MathUtils.lerp(0.05f, 0.35f, t1), // Deep Blue mix
                        1f
                    );
                } else if (t < 0.5f) {
                    // **Deep Violet → Vivid Magenta (Middle blend)**
                    float t2 = (t - 0.25f) / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.2f, 0.7f, t2), // More Red for Magenta
                        MathUtils.lerp(0.0f, 0.05f, t2), // Slight Green tint
                        MathUtils.lerp(0.35f, 0.7f, t2), // Purple highlight
                        1f
                    );
                } else if (t < 0.75f) {
                    // **Vivid Magenta → Neon Blue-Purple (Top area)**
                    float t3 = (t - 0.5f) / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.7f, 0.6f, t3), // Slightly reduce Red
                        MathUtils.lerp(0.05f, 0.2f, t3), // Green glow increases
                        MathUtils.lerp(0.7f, 1.0f, t3), // Bright Electric Blue mix
                        1f
                    );
                } else {
                    // **Neon Blue-Purple → Cosmic Black (Top edges)**
                    float t4 = (t - 0.75f) / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.6f, 0.1f, t4), // Fade out Red
                        MathUtils.lerp(0.2f, 0.0f, t4), // Fade out Green
                        MathUtils.lerp(1.0f, 0.2f, t4), // Fade out Blue into Black
                        1f
                    );
                }

                // **Ensure it stays inside the square!**
                float startX = borderX;
                float startY = borderY + i * stepHeight;
                float width = borderWidth;
                float height = stepHeight;

                shapeRenderer.setColor(blendedColor);
                shapeRenderer.rect(startX, startY, width, height);
            }

            // End drawing
            shapeRenderer.end();


            float imageWidth = 66;
            float imageHeight = 66;
            float margin = 435;
            float spacing = 20;
            float glowSize = 1; // Glow effect size
            float x1 = screenWidth - imageWidth - margin;
            float y = screenHeight - imageHeight - 390;
            float x2 = x1 + imageWidth + spacing;
            float x3 = x2 + imageWidth + spacing;
            // Detect hover
            int mouseX = Gdx.input.getX();
            int mouseY = screenHeight - Gdx.input.getY();
            boolean isHover1 = (mouseX >= x1 && mouseX <= x1 + imageWidth && mouseY >= y && mouseY <= y +
                imageHeight);
            boolean isHover2 = (mouseX >= x2 && mouseX <= x2 + imageWidth && mouseY >= y && mouseY <= y +
                imageHeight);
            boolean isHover3 = (mouseX >= x3 && mouseX <= x3 + imageWidth && mouseY >= y && mouseY <= y +
                imageHeight);
            // Adjust size if hovered
            float scale = 1.2f;
            float newWidth = imageWidth * scale;
            float newHeight = imageHeight * scale;
            // Set border color based on character index
            Color borderColor = Color.WHITE; // Default (won't be used)
            if (currentCharacterIndex == 0) {
                borderColor = Color.MAGENTA;
            }
            else if (currentCharacterIndex == 1) {
                borderColor = Color.BLUE;
            }
            else if (currentCharacterIndex == 2) {
                borderColor = Color.YELLOW;
            }
            // Draw border instead of glow
            drawGlow(batch, x1, y, imageWidth, imageHeight, borderColor);
            batch.draw(umbraShadowStrike, isHover1 ? x1 - (newWidth - imageWidth) / 2 : x1, y, isHover1 ? newWidth :
                imageWidth, isHover1 ? newHeight : imageHeight);
            drawGlow(batch, x2, y, imageWidth, imageHeight, borderColor);
            batch.draw(umbraDistractingIllusions, isHover2 ? x2 - (newWidth - imageWidth) / 2 : x2, y, isHover2 ?
                newWidth : imageWidth, isHover2 ? newHeight : imageHeight);
            drawGlow(batch, x3, y, imageWidth, imageHeight, borderColor);
            batch.draw(umbraVeilOfShadows, isHover3 ? x3 - (newWidth - imageWidth) / 2 : x3, y, isHover3 ?
                newWidth : imageWidth, isHover3 ? newHeight : imageHeight);
            font.getData().setScale(5.5f);
            float imageX = screenWidth - 250;
            float imageY = screenHeight - 300;
            float textXx = imageX - 250;
            float textYy = imageY + 100;
            font.draw(batch, "UMBRA", textXx, textYy);
            font.getData().setScale(1.5f);
            float descriptionY = textYy - 50;
            font.draw(batch, "A skilled acquaintance and", textXx, descriptionY - 30);
            font.draw(batch, "is knowledgeable in traversing", textXx, descriptionY - 60);
            font.draw(batch, "the multiverse.", textXx, descriptionY - 90);

            // Load the image (Make sure displayImage is initialized in create())
            float displayImageX = 90;  // Adjust X position
            float marginBelow = 20;
            float displayImageY = screenHeight - 245 - marginBelow;  // Adjust Y position
            float displayImageWidth = 300;  // Adjust width
            float displayImageHeight = 150;  // Adjust height

            batch.draw(displayImageUmbra, displayImageX, displayImageY, displayImageWidth, displayImageHeight);

            // Draw the text on top of the image
            font.getData().setScale(1.5f);  // Adjust text size
            font.setColor(Color.WHITE);  // Set text color

            // Calculate text position (centered on the image)
            float textX = displayImageX + (displayImageWidth / 2) - 125;  // Adjust for centering
            float textY = displayImageY + (displayImageHeight / 2) - 30; // Adjust for centering

            font.draw(batch, "NYXARION", textX, textY);
        }
       if (currentCharacterIndex == 1) {  // 🔹 Nova Skills
            // Start drawing filled shapes for Nova
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            steps = 300;
            stepHeight = borderHeight / (float) steps;

            for (int i = 0; i < steps; i++) {
                float t = i / (float) (steps - 1); // Normalized progress (0 to 1)

                Color blendedColor;
                if (t < 0.25f) {
                    // **Black → Deep Blue (Bottom area)**
                    float t1 = t / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.0f, 0.15f, t1), // **Slight Red tint**
                        MathUtils.lerp(0.0f, 0.0f, t1),  // No Green
                        MathUtils.lerp(0.0f, 0.3f, t1),  // **Deep Blue mix**
                        1f
                    );
                } else if (t < 0.5f) {
                    // **Deep Blue → Aqua (Middle blend)**
                    float t2 = (t - 0.25f) / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.15f, 0.3f, t2), // **More Blue for Aqua**
                        MathUtils.lerp(0.0f, 0.5f, t2),  // Slight Green tint
                        MathUtils.lerp(0.3f, 0.7f, t2),  // Lighter Blue mix
                        1f
                    );
                } else if (t < 0.75f) {
                    // **Aqua → Neon Cyan (Top area)**
                    float t3 = (t - 0.5f) / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.3f, 0.0f, t3),  // **More vibrant Blue fading out**
                        MathUtils.lerp(0.5f, 0.8f, t3),  // **Stronger Green glow**
                        MathUtils.lerp(0.7f, 1.0f, t3),  // **Electric Cyan mix**
                        1f
                    );
                } else {
                    // **Neon Cyan → Abyssal Black (Top edges)**
                    float t4 = (t - 0.75f) / 0.25f;
                    blendedColor = new Color(
                        MathUtils.lerp(0.0f, 0.1f, t4),  // Fade out Blue
                        MathUtils.lerp(0.8f, 0.0f, t4),  // Fade out Green
                        MathUtils.lerp(1.0f, 0.0f, t4),  // Fade out Cyan
                        1f
                    );
                }

                // Ensure it stays inside the square
                float startX = borderX;
                float startY = borderY + i * stepHeight;
                float width = borderWidth;
                float height = stepHeight;

                shapeRenderer.setColor(blendedColor);
                shapeRenderer.rect(startX, startY, width, height);
            }

            // End drawing
            shapeRenderer.end();

            // Skill Icons for Nova (same positions, just different character)
            float imageWidth = 66;
            float imageHeight = 66;
            float margin = 380;
            float spacing = 20;
            float glowSize = 1; // Glow effect size
            float x1 = screenWidth - imageWidth - margin;
            float y = screenHeight - imageHeight - 390;
            float x2 = x1 + imageWidth + spacing;
            float x3 = x2 + imageWidth + spacing;

            // Detect hover
            int mouseX = Gdx.input.getX();
            int mouseY = screenHeight - Gdx.input.getY();
            boolean isHover1 = (mouseX >= x1 && mouseX <= x1 + imageWidth && mouseY >= y && mouseY <= y + imageHeight);
            boolean isHover2 = (mouseX >= x2 && mouseX <= x2 + imageWidth && mouseY >= y && mouseY <= y + imageHeight);
            boolean isHover3 = (mouseX >= x3 && mouseX <= x3 + imageWidth && mouseY >= y && mouseY <= y + imageHeight);

            // Adjust size if hovered
            float scale = 1.2f;
            float newWidth = imageWidth * scale;
            float newHeight = imageHeight * scale;

            // Set border color based on character index (Nova specific color)
            Color borderColor = Color.WHITE; // Default (won't be used)
            if (currentCharacterIndex == 1) {
                borderColor = Color.CYAN;  // Nova's border color
            }

            // Draw border instead of glow
            drawGlow(batch, x1, y, imageWidth, imageHeight, borderColor);
            batch.draw(nova_EnergyPunch, isHover1 ? x1 - (newWidth - imageWidth) / 2 : x1, y, isHover1 ? newWidth : imageWidth, isHover1 ? newHeight : imageHeight);
            drawGlow(batch, x2, y, imageWidth, imageHeight, borderColor);
            batch.draw(nova_EnergyBlaster, isHover2 ? x2 - (newWidth - imageWidth) / 2 : x2, y, isHover2 ? newWidth : imageWidth, isHover2 ? newHeight : imageHeight);
            drawGlow(batch, x3, y, imageWidth, imageHeight, borderColor);
            batch.draw(nova_Multi, isHover3 ? x3 - (newWidth - imageWidth) / 2 : x3, y, isHover3 ? newWidth : imageWidth, isHover3 ? newHeight : imageHeight);

            // Nova's Name and Description
            font.getData().setScale(5.5f);
            float imageX = screenWidth - 250;
            float imageY = screenHeight - 300;
            float textXx = imageX - 200;
            float textYy = imageY + 100;
            font.draw(batch, "NOVA", textXx, textYy);
            font.getData().setScale(1.5f);
            float descriptionY = textYy - 50;
            font.draw(batch, "A logical thinker with stubborn", textXx, descriptionY - 30);
            font.draw(batch, "tendencies struggles with unclear", textXx, descriptionY - 60);
            font.draw(batch, "motives of allies.", textXx, descriptionY - 90);

            // Load Nova's image (Make sure displayImage is initialized in create())
            float displayImageX = 90;  // Adjust X position
            float marginBelow = 20;
            float displayImageY = screenHeight - 245 - marginBelow;  // Adjust Y position
            float displayImageWidth = 300;  // Adjust width
            float displayImageHeight = 150;  // Adjust height

            batch.draw(displayImageNova, displayImageX, displayImageY, displayImageWidth, displayImageHeight);

            // Draw the text on top of the image
            font.getData().setScale(1.5f);  // Adjust text size
            font.setColor(Color.WHITE);  // Set text color

            // Calculate text position (centered on the image)
            float textX = displayImageX + (displayImageWidth / 2) - 125;  // Adjust for centering
            float textY = displayImageY + (displayImageHeight / 2) - 30; // Adjust for centering

            font.draw(batch, "EARTH", textX, textY);
        }

       if(currentCharacterIndex == 2){
           // Start drawing filled shapes
           shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
           steps = 300;
           stepHeight = borderHeight / (float) steps;

           for (int i = 0; i < steps; i++) {
               float t = i / (float) (steps - 1); // Normalized progress (0 to 1)

               Color blendedColor;
               if (t < 0.25f) {
                   // **Electric Gold → Fiery Red (Bottom)**
                   float t1 = t / 0.25f;
                   blendedColor = new Color(
                       MathUtils.lerp(0.95f, 1.0f, t1), // Strong Gold to Red
                       MathUtils.lerp(0.75f, 0.2f, t1), // Green fades to fiery red
                       MathUtils.lerp(0.0f, 0.0f, t1),  // No Blue
                       1f
                   );
               } else if (t < 0.5f) {
                   // **Fiery Red → Deep Crimson (Middle)**
                   float t2 = (t - 0.25f) / 0.25f;
                   blendedColor = new Color(
                       MathUtils.lerp(1.0f, 0.8f, t2), // Strong Red to Crimson
                       MathUtils.lerp(0.2f, 0.1f, t2), // Dimming Green for deeper red
                       MathUtils.lerp(0.0f, 0.1f, t2),  // Slight Blue for richness
                       1f
                   );
               } else if (t < 0.75f) {
                   // **Deep Crimson → Dark Purple (Upper Section)**
                   float t3 = (t - 0.5f) / 0.25f;
                   blendedColor = new Color(
                       MathUtils.lerp(0.8f, 0.5f, t3),  // Red softens to purple tone
                       MathUtils.lerp(0.1f, 0.05f, t3), // Minimal green remains
                       MathUtils.lerp(0.1f, 0.3f, t3),  // Blue rises for purple effect
                       1f
                   );
               } else {
                   // **Dark Purple → Abyssal Black (Top Edge)**
                   float t4 = (t - 0.75f) / 0.25f;
                   blendedColor = new Color(
                       MathUtils.lerp(0.5f, 0.0f, t4),  // Red fades to black
                       MathUtils.lerp(0.05f, 0.0f, t4), // Green fades completely
                       MathUtils.lerp(0.3f, 0.0f, t4),  // Blue disappears into darkness
                       1f
                   );
               }

               // **Ensure it stays inside the square!**
               float startX = borderX;
               float startY = borderY + i * stepHeight;
               float width = borderWidth;
               float height = stepHeight;

               shapeRenderer.setColor(blendedColor);
               shapeRenderer.rect(startX, startY, width, height);
           }

            // End drawing
           shapeRenderer.end();


           float imageWidth = 66;
           float imageHeight = 66;
           float margin = 435;
           float spacing = 20;
           float glowSize = 1; // Glow effect size
           float x1 = screenWidth - imageWidth - margin;
           float y = screenHeight - imageHeight - 390;
           float x2 = x1 + imageWidth + spacing;
           float x3 = x2 + imageWidth + spacing;
           // Detect hover
           int mouseX = Gdx.input.getX();
           int mouseY = screenHeight - Gdx.input.getY();
           boolean isHover1 = (mouseX >= x1 && mouseX <= x1 + imageWidth && mouseY >= y && mouseY <= y +
               imageHeight);
           boolean isHover2 = (mouseX >= x2 && mouseX <= x2 + imageWidth && mouseY >= y && mouseY <= y +
               imageHeight);
           boolean isHover3 = (mouseX >= x3 && mouseX <= x3 + imageWidth && mouseY >= y && mouseY <= y +
               imageHeight);
           // Adjust size if hovered
           float scale = 1.2f;
           float newWidth = imageWidth * scale;
           float newHeight = imageHeight * scale;
           // Set border color based on character index
           Color borderColor = Color.WHITE; // Default (won't be used)
           if (currentCharacterIndex == 0) {
               borderColor = Color.PURPLE;
           } else if (currentCharacterIndex == 1) {
               borderColor = Color.BLUE;
           } else if (currentCharacterIndex == 2) {
               borderColor = Color.YELLOW;
           }
           // Draw border instead of glow
           drawGlow(batch, x1, y, imageWidth, imageHeight, borderColor);
           batch.draw(Jina_SledgeStrike, isHover1 ? x1 - (newWidth - imageWidth) / 2 : x1, y, isHover1 ? newWidth :
               imageWidth, isHover1 ? newHeight : imageHeight);
           drawGlow(batch, x2, y, imageWidth, imageHeight, borderColor);
           batch.draw(Jina_PrecisionShot, isHover2 ? x2 - (newWidth - imageWidth) / 2 : x2, y, isHover2 ?
               newWidth : imageWidth, isHover2 ? newHeight : imageHeight);
           drawGlow(batch, x3, y, imageWidth, imageHeight, borderColor);
           batch.draw(Jina_VanguardsResolve, isHover3 ? x3 - (newWidth - imageWidth) / 2 : x3, y, isHover3 ?
               newWidth : imageWidth, isHover3 ? newHeight : imageHeight);
           font.getData().setScale(5.5f);
           float imageX = screenWidth - 250;
           float imageY = screenHeight - 300;
           float textXx = imageX - 250;
           float textYy = imageY + 100;
           font.draw(batch, "JINA", textXx, textYy);
           font.getData().setScale(1.5f);
           float descriptionY = textYy - 50;
           font.draw(batch, "A logical thinker with stubborn", textXx, descriptionY - 30);
           font.draw(batch, "tendencies, struggles with unclear", textXx, descriptionY - 60);
           font.draw(batch, "motives of allies.", textXx, descriptionY - 90);

           // Load the image (Make sure displayImage is initialized in create())
           float displayImageX = 90;  // Adjust X position
           float marginBelow = 20;
           float displayImageY = screenHeight - 245 - marginBelow;  // Adjust Y position
           float displayImageWidth = 300;  // Adjust width
           float displayImageHeight = 150;  // Adjust height

           batch.draw(displayImageJina, displayImageX, displayImageY, displayImageWidth, displayImageHeight);

           // Draw the text on top of the image
           font.getData().setScale(1.5f);  // Adjust text size
           font.setColor(Color.WHITE);  // Set text color

           // Calculate text position (centered on the image)
           float textX = displayImageX + (displayImageWidth / 2) - 125;  // Adjust for centering
           float textY = displayImageY + (displayImageHeight / 2) - 30; // Adjust for centering

           font.draw(batch, "VORTEXIA", textX, textY);
       }
        handleBackButton();


        if (currentCharacterIndex == 0 && selectedSkill != -1) {
            float textX = screenWidth - 500;
            float textY = screenHeight - 490;
            String[][] skillDescriptions = {
                {
                    "Basic Attack: Shadow Strike",
                    "A stealthy attack from the shadows.",
                    "Attack Power: 90",
                    "Skill Points (SP): +1",
                    "Deals basic attack damage."
                },
                {
                    "Skill Attack: Distracting Illusion",
                    "Creates a decoy that draws",
                    "enemy attention.",
                    "Magical Attack: 250",
                    "Skill Points (SP): -3",
                    "Deals heavy skill attack damage."
                },
                {
                    "Ultimate Attack: Veil of Shadows",
                    "Unleashes a flurry of shadow attacks,",
                    "damaging enemies.",
                    "Ultimate Attack: 350",
                    "Cooldown (CD): 4 turns",
                    "Deals ultimate attack damage."
                }
            };

            for (String line : skillDescriptions[selectedSkill]) {
                font.draw(batch, line, textX, textY);
                textY -= 30;
            }
     } else if(currentCharacterIndex == 1 && selectedSkill != -1){
            float textX = screenWidth - 450;
            float textY = screenHeight - 490;
            String[][] skillDescriptions = {
                {
                    "Basic Attack: Energy Punch",
                    "A rapid punch focusing Nova's energy.",
                    "Attack Power: 60",
                    "Skill Points (SP): +1",
                    "Deals basic attack damage."
                },
                {
                    "Skill Attack: Energy Blaster",
                    "A powerful skill, dealing damage.",
                    "Physical Attack: 150",
                    "Skill Points (SP): -1",
                    "Deals skill attack damage."
                },
                {
                    "Ultimate Attack: Multidimensional Blast",
                    "Deals AoE damage, stuns enemies,",
                    "grants an extra turn.",
                    "Ultimate Attack: 250",
                    "Cooldown (CD): 3 turns."
                },
                {
                    "Character Info",
                    "Name: Kaia \"Nova\" Novere",
                    "Background: A logical thinker with stubborn tendencies.",
                    "Struggles with unclear motives of allies."
                }
            };

            for (String line : skillDescriptions[selectedSkill]) {
                font.draw(batch, line, textX, textY);
                textY -= 30;
            }
        }  else if(currentCharacterIndex == 2 && selectedSkill != -1){
            float textX = screenWidth - 500;
            float textY = screenHeight - 500;
            String[][] skillDescriptions = {
                {
                    "Basic Attack: Sledge Strike",
                    "A powerful melee strike with",
                    "reinforced gun.",
                    "Attack Power: 50",
                    "Skill Points (SP): +1",
                    "Deals basic attack damage."
                },
                {
                    "Skill Attack: Precision Shot",
                    "A calculated shot that pierces,",
                    "enemy defenses.",
                    "Physical Attack: 120",
                    "Skill Points (SP): -1",
                    "Deals skill attack damage."
                },
                {
                    "Ultimate Attack: Vanguard's Resolve",
                    "Unleashes a critical shot,",
                    "boosting Jina's accuracy",
                    "and granting an extra turn.",
                    "Ultimate Attack: 200",
                    "Cooldown (CD): 3 turns."
                },
                {
                    "Character Info",
                    "Name: Jina Melody",
                    "Background: A battle-hardened mercenary, known for her",
                    "calculated strategies and unwavering focus."
                }
            };

            for (String line : skillDescriptions[selectedSkill]) {
                font.draw(batch, line, textX, textY);
                textY -= 30;
            }
        }


        for (int i = 0; i < leftSideIcons.length; i++) {
            float drawX = 90;
            float drawY = screenHeight - 400 - (i * 100);
            float width = 75;
            float height = 75;

            boolean isHovered = (i == hoveredIndex);
            boolean isSelected = (i == currentCharacterIndex);

            float scale = isHovered ? 1.2f : 1.0f;
            float newWidth = width * scale;
            float newHeight = height * scale;

            float adjustedX = drawX - (newWidth - width) / 2;
            float adjustedY = drawY - (newHeight - height) / 2;

            // Apply circle glow when hovered or selected
            if (isHovered || isSelected) {
                drawCircleGlow(batch, adjustedX, adjustedY, newWidth, newHeight, glowColors[i]);
            }

            // Set grayscale if NOT hovered or selected
            if (!isHovered && !isSelected) {
                batch.setColor(0.3f, 0.3f, 0.3f, 1f); // Grayscale effect
            } else {
                batch.setColor(1f, 1f, 1f, 1f); // Full color
            }

            // Draw the left-side character icon
            batch.draw(leftSideIcons[i], adjustedX, adjustedY, newWidth, newHeight);

            // Reset color after drawing
            batch.setColor(Color.WHITE);
        }

        int centerImageWidth = 700;
            int centerImageHeight = 1050;
            int centerX = screenWidth / 2 - centerImageWidth / 2 - 50;
            int centerY = -150;
            batch.draw(selectedCharacter, centerX, centerY, centerImageWidth, centerImageHeight);///////////
            batch.end();
 }
            // Glow effect function (unchanged)
            private void drawGlow(SpriteBatch batch, float x, float y, float width, float height, Color color) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(color.r, color.g, color.b, 0.1f);
                shapeRenderer.rect(x - 5, y - 5 + 10, width + 3, height + 4);
                shapeRenderer.end();
                batch.begin();
            }

            private void drawCircleGlow(SpriteBatch batch, float x, float y, float width, float height, Color color) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(color.r, color.g, color.b, 0.3f);

                float radius = (width / 2) + 1; // Extra glow effect
                shapeRenderer.circle(x + width / 2, y + height / 2, radius);

                shapeRenderer.end();
                batch.begin();
            }

            @Override
            public void resize(int width, int height) {}
            @Override
            public void pause() {}
            @Override
            public void resume() {}
            @Override
            public void hide() {}
            @Override
            public void dispose() {
                batch.dispose();
                font.dispose();
                shapeRenderer.dispose();
                for (Texture texture : leftSideIcons) {
                    texture.dispose();
                }
                displayImageUmbra.dispose();
                displayImageNova.dispose();
            }

            private void handleBackButton() {
                int screenWidth = Gdx.graphics.getWidth();
                int screenHeight = Gdx.graphics.getHeight();

                // Define the back button bounds
                float backButtonX = 100; // X position of the back button
                float backButtonY = screenHeight - 815; // Y position of the back button
                float backButtonWidth = 100; // Width of the back button
                float backButtonHeight = 50; // Height of the back button

                // Check if the mouse is hovering over the back button
                int mouseX = Gdx.input.getX();
                int mouseY = screenHeight - Gdx.input.getY(); // Flip Y coordinate
                boolean isHovered = (mouseX >= backButtonX && mouseX <= backButtonX + backButtonWidth &&
                    mouseY >= backButtonY && mouseY <= backButtonY + backButtonHeight);

                // Save the original font color
                Color originalColor = new Color(font.getColor());

                // Get the hover color based on the current character index
                Color hoverColor = glowColors[currentCharacterIndex]; // Use the glow color of the current character

                // Draw the back button text
                font.getData().setScale(2); // Set font size
                font.setColor(isHovered ? hoverColor : Color.WHITE); // Change color on hover
                font.draw(batch, "Back", backButtonX, backButtonY + backButtonHeight);

                // Restore the original font color
                font.setColor(originalColor);

                // Handle click on the back button
                if (Gdx.input.justTouched() && isHovered) {
                    // Transition back to the previous screen
                    game.startGameMenu(); // Replace `startGameMenu` with the actual method to go back
                }
            }

}
