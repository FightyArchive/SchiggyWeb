package de.schiggy.web.auth;

import java.util.UUID;

public class WebsiteAuth {

    private final AuthManager authManager;
    private final VerificationService verificationService;

    public WebsiteAuth() {
        this.authManager = new AuthManager();
        this.verificationService = new VerificationService();
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public VerificationService getVerificationService() {
        return verificationService;
    }

    public boolean isVerified(UUID playerUuid) {
        return authManager.getSessionForPlayer(playerUuid) != null;
    }
}
