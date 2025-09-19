package com.example.projectcab302.z_Misc;

import javafx.scene.Scene;
import java.util.prefs.Preferences;

public class AppSettings {
    private static final AppSettings INSTANCE = new AppSettings();
    public static AppSettings getInstance() { return INSTANCE; }

    private static final String NODE   = "com.example.projectcab302.settings";
    private static final String K_DARK = "ui.dark";
    private static final String K_SCALE= "ui.scale";
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

    // getters
    public boolean isDarkMode()      { return darkMode; }
    public double  getUiScale()      { return uiScale; }
    public String  getLanguage()     { return language; }
    public boolean isSoundEffects()  { return soundEffects; }
    public boolean isNotifications() { return notifications; }

    // setters (persist immediately)
    public void setDarkMode(boolean v)      { darkMode = v; p.putBoolean(K_DARK, v); }
    public void setUiScale(double v)        { uiScale = v; p.putDouble(K_SCALE, v); }
    public void setLanguage(String v)       { language = v; if (v != null) p.put(K_LANG, v); }
    public void setSoundEffects(boolean v)  { soundEffects = v; p.putBoolean(K_SFX, v); }
    public void setNotifications(boolean v) { notifications = v; p.putBoolean(K_NOTI, v); }

    public void resetToDefaults() {
        setDarkMode(false);
        setUiScale(1.0);
        setLanguage("en");
        setSoundEffects(true);
        setNotifications(true);
    }

    /** Apply non-theme UI tweaks (scale + root classes). */
    public void applyToScene(Scene scene) {
        if (scene == null) return;

        var root = scene.getRoot();
        root.setScaleX(uiScale);
        root.setScaleY(uiScale);

        var classes = root.getStyleClass();
        if (!classes.contains("root"))     classes.add("root");
        if (!classes.contains("app-root")) classes.add("app-root");
    }

    public void save() {
        try { p.flush(); } catch (java.util.prefs.BackingStoreException e) { e.printStackTrace(); }
    }
}