package settings;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SettingsLoader {

    private static final String SETTINGS_FILE = "data/settings.properties";
    private String settingsPath;

    public SettingsLoader(){
        this.settingsPath = SETTINGS_FILE;
    }

    public String getSettingsPath() {
        return settingsPath;
    }

    public Properties loadSettings() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(SETTINGS_FILE));
        return properties;
    }
}
