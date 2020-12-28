package nu.olivertwistor.todolisttools.util;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Configuration values from the app config fil are read via this class.
 *
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class AppConfig extends Config
{
    @NonNls
    private static final String SECTION_API = "api";

    @NonNls
    private static final String OPTION_KEY = "key";

    @NonNls
    private static final String OPTION_SHARED_SECRET = "shared-secret";

    @NonNls
    private static final String OPTION_AUTH_TOKEN = "auth-token";

    public AppConfig() throws IOException, URISyntaxException
    {
        super(AppConfig.class.getResource("/app.cfg"));
    }

    /**
     * Gets the API key from the config file.
     *
     * @return The API key; or null if the config file key couldn't be found.
     *
     * @since 0.1.0
     */
    public String getApiKey()
    {
        return this.get(AppConfig.SECTION_API, AppConfig.OPTION_KEY);
    }

    /**
     * Gets the shared secret from the config file.
     *
     * @return The shared secret; or null if the config file key couldn't be
     *         found.
     *
     * @since 0.1.0
     */
    public String getSharedSecret()
    {
        return this.get(AppConfig.SECTION_API, AppConfig.OPTION_SHARED_SECRET);
    }

    /**
     * Gets the authentication token from the config file.
     *
     * @return The authentication token; or null if the config file key
     *         couldn't be found.
     *
     * @since 0.1.0
     */
    public String getToken()
    {
        return this.get(AppConfig.SECTION_API, AppConfig.OPTION_AUTH_TOKEN);
    }

    /**
     * Sets the authentication token and writes it to the config file.
     *
     * @param token the authentication token
     *
     * @throws IOException if the config file couldn't be written to
     *
     * @since 0.1.0
     */
    public void setToken(final String token) throws IOException
    {
        this.set(AppConfig.SECTION_API, AppConfig.OPTION_AUTH_TOKEN, token);
    }
}
