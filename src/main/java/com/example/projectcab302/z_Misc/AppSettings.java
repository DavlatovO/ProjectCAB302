package com.example.projectcab302.z_Misc;

import javafx.scene.Scene;

import java.util.prefs.Preferences;

public class AppSettings {
    private static final AppSettings INSTANCE = new AppSettings();
    public static AppSettings getInstance() { return INSTANCE; }

    private static final String NODE = "com.example.projectcab302.settings";
    private static final String K_DARK = "ui.dark";
    private static final String K_SCALE = "ui.scale";
    private static final String K_LANG = "ui.lang";
    private static final String K_SFX  = "ui.sfx";
    private static final String K_NOTI = "ui.noti";

    private final Preferences p = Preferences.userRoot().node(NODE);

    private boolean darkMode;
    private double  uiScale;
    private String  language;
    private boolean soundEffects;
    private boolean notifications;

    private AppSettings() {
        this.darkMode      = p.getBoolean(K_DARK, false);
        this.uiScale       = p.getDouble(K_SCALE, 1.0);
        this.language      = p.get(K_LANG, "en");
        this.soundEffects  = p.getBoolean(K_SFX, true);
        this.notifications = p.getBoolean(K_NOTI, true);
    }

    // ---- getters
    public boolean isDarkMode()      { return darkMode; }
    public double  getUiScale()      { return uiScale; }
    public String  getLanguage()     { return language; }
    public boolean isSoundEffects()  { return soundEffects; }
    public boolean isNotifications() { return notifications; }

    // ---- setters (persist immediately)
    public void setDarkMode(boolean v)      { this.darkMode = v; p.putBoolean(K_DARK, v); }
    public void setUiScale(double v)        { this.uiScale = v; p.putDouble(K_SCALE, v); }
    public void setLanguage(String v)       { this.language = v; if (v!=null) p.put(K_LANG, v); }
    public void setSoundEffects(boolean v)  { this.soundEffects = v; p.putBoolean(K_SFX, v); }
    public void setNotifications(boolean v) { this.notifications = v; p.putBoolean(K_NOTI, v); }

    public void resetToDefaults() {
        setDarkMode(false);
        setUiScale(1.0);
        setLanguage("en");
        setSoundEffects(true);
        setNotifications(true);
    }


    public void applyToScene(Scene scene) {
        if (scene == null) return;

        // scale
        var root = scene.getRoot();
        root.setScaleX(uiScale);
        root.setScaleY(uiScale);


        var baseUrl = AppSettings.class.getResource("/com/example/projectcab302/style.css");
        if (baseUrl != null) {
            var css = baseUrl.toExternalForm();
            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }
        }

        // ensure the scene root has the app skin class
        var classes = root.getStyleClass();
        if (!classes.contains("root")) classes.add("root");
        if (!classes.contains("app-root")) classes.add("app-root");

        // toggle dark class
        if (darkMode) {
            if (!classes.contains("dark")) classes.add("dark");
        } else {
            classes.remove("dark");
        }
    }


    public void save() {
        try {
            p.flush(); // ensures values are persisted immediately
        } catch (java.util.prefs.BackingStoreException e) {
            e.printStackTrace(); // or log it
        }
    }

}
