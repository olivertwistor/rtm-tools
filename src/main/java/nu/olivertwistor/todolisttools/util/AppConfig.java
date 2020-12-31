package nu.olivertwistor.todolisttools.util;

import org.ini4j.InvalidFileFormatException;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Configuration values from the app config fil are read via this class.
 *
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class AppConfig extends Config
{
    @NonNls
    private static final String API = "api";
    
    @NonNls
    private static final String AUTH_TOKEN = "auth.token";

    /**
     * Creates a new app configuration object based on the resource file
     * "/app.cfg".
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    @SuppressWarnings("JavaDoc")
    public AppConfig()
            throws IOException, URISyntaxException, InvalidFileFormatException
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
    public @NonNls String getApiKey()
    {
        return this.get(AppConfig.API, "key");
    }

    /**
     * Gets the shared secret from the config file.
     *
     * @return The shared secret; or null if the config file key couldn't be
     *         found.
     *
     * @since 0.1.0
     */
    public @NonNls String getSharedSecret()
    {
        return this.get(AppConfig.API, "shared.secret");
    }

    /**
     * Gets the authentication token from the config file.
     *
     * @return The authentication token; or null if the config file key
     *         couldn't be found.
     *
     * @since 0.1.0
     */
    public @NonNls String getToken()
    {
        return this.get(AppConfig.API, AppConfig.AUTH_TOKEN);
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
        this.set(AppConfig.API, AppConfig.AUTH_TOKEN, token);
    }
}
