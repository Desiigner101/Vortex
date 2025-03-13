package com.vortex.game.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/** Launches the desktop (LWJGL3) application. */
public class VortexApplication {
    // Adjusted resolution to fit most screens while keeping navigation icons visible
    private static final int WINDOW_WIDTH = 1600;   // Reduced Width
    private static final int WINDOW_HEIGHT = 900;   // Reduced Height
    private static final int TARGET_FPS = 60;       // Target frame rate for stability

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // macOS & Windows startup helper
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new com.vortex.game.VortexMain(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Vortex");

        // Ensure windowed mode with visible upper navigation icons
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        config.setResizable(false);   // Allow resizing if needed
        config.setDecorated(true);   // Keeps close (X), minimize, and maximize buttons visible

        // Prevent fullscreen from overriding the navigation bar
        config.setMaximized(false);  // Ensure it's not full screen
        config.setFullscreenMode(null); // Explicitly disable fullscreen mode

        // Optimize FPS settings for smoother gameplay
        config.useVsync(true);
        config.setForegroundFPS(Math.max(TARGET_FPS, Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate)); // Match display refresh rate

        // Apply window icons
        setWindowIcons(config);

        return config;
    }

    /** Sets multiple resolution icons for the application window. */
    private static void setWindowIcons(Lwjgl3ApplicationConfiguration config) {
        config.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
    }
}
