package com.hangyeollee.go4lunch.ui.settings;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "SettingsViewState{" +
            "photoUrl='" + photoUrl + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", switchBoolean=" + switchBoolean +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsViewState that = (SettingsViewState) o;
        return switchBoolean == that.switchBoolean && Objects.equals(photoUrl, that.photoUrl) && Objects.equals(name, that.name) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoUrl, name, email, switchBoolean);
    }
}
