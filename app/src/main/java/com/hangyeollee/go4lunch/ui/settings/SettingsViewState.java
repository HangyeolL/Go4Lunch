package com.hangyeollee.go4lunch.ui.settings;

public class SettingsViewState {
    private final String photoUrl;
    private final String name;
    private final String email;
    private final boolean switchBoolean;

    public SettingsViewState(String photoUrl, String name, String email, boolean switchBoolean) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.email = email;
        this.switchBoolean = switchBoolean;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSwitchBoolean() {
        return switchBoolean;
    }
}
