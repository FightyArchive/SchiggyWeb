package de.schiggy.web;

import de.schiggy.web.auth.WebsiteAuth;
import de.schiggy.web.auth.WebsiteCommand;
import de.schiggy.web.api.ApiServer;
import org.bukkit.plugin.java.JavaPlugin;

public final class SchiggyWeb extends JavaPlugin {

    private WebsiteAuth websiteAuth;
    private ApiServer apiServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        websiteAuth = new WebsiteAuth();

        if (getCommand("website") != null) {
            getCommand("website").setExecutor(
                    new WebsiteCommand(
                            websiteAuth.getVerificationService()
                    )
            );
        } else {
            getLogger().severe("Der Befehl 'website' fehlt in der plugin.yml!");
        }

        apiServer = new ApiServer(this);
        apiServer.start();

        getLogger().info("SchiggyWeb wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        if (apiServer != null) {
            apiServer.stop();
        }
    }

    public WebsiteAuth getWebsiteAuth() {
        return websiteAuth;
    }
}
