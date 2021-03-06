package nu.olivertwistor.todolisttools.menus;

import nu.olivertwistor.todolisttools.rtmapi.AuthRequest;
import nu.olivertwistor.todolisttools.rtmapi.auth.GetFrob;
import nu.olivertwistor.todolisttools.rtmapi.auth.GetToken;
import nu.olivertwistor.todolisttools.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * This class handles authentication to the Remember The Milk service. The way
 * to use this class is to first call
 * {@link #generateAuthRequest(Config, String)}. That method returns a URL the
 * user should go to and give this application necessary permissions to access
 * the user's RTM account. When that is done, {@link #obtainToken(Config)} may
 * be called to obtain a token, with which all subsequent API calls to the RTM
 * service will be made.
 *
 * @since 1.0.0
 */
final class Authentication
{
    private static final @NonNls Logger LOG = LogManager.getLogger(
            Authentication.class);

    private AuthRequest authRequest;
    private String frobString;

    /**
     * Generates a URL with which the user may give this application the
     * necessary permissions to access the user's Remember The Milk account.
     *
     * @param config     Config object for access to the API key etc.
     * @param permission which permission level to ask for
     *
     * @return The URL the user need to visit to give this application the
     *         necessary permissions.
     *
     * @throws IOException if the FROB necessary for authentication couldn't be
     *                     retrieved from Remember The Milk.
     *
     * @since 1.0.0
     */
    URL generateAuthRequest(final Config config, final String permission)
            throws IOException
    {
        LOG.trace("Entering generateAuthRequest(Config, String)...");

        // First, retrieve a FROB.
        final GetFrob getFrob = new GetFrob(config);
        this.frobString = getFrob.getFrob();
        LOG.debug("Retrieved FROB: {}", this.frobString);

        // Then, construct an AuthRequest object and store it in this class.
        // Return the URL.
        this.authRequest = new AuthRequest(config, permission, this.frobString);
        final URL url = this.authRequest.toUrl();
        LOG.info("Generated authentication URL.");

        return url;
    }

    /**
     * If {@link #generateAuthRequest(Config, String)} has been called
     * beforehand, an authentication token is retrieved from Remember the Milk
     * for use with all subsequent calls to the RTM API.
     *
     * @param config Config object for access to the API key etc.
     *
     * @return The authentication token.
     *
     * @throws IOException if connection to Remember The Milk failed.
     *
     * @since 1.0.0
     */
    String obtainToken(final Config config) throws IOException
    {
        LOG.trace("Entering obtainToken(Config)...");

        Objects.requireNonNull(this.authRequest, "Authentication has not " +
                "yet been obtained. Please call #generateAuthRequest() " +
                "before calling this method.");

        final GetToken getToken = new GetToken(config, this.frobString);
        final String token = getToken.getToken();
        LOG.info("Retrieved token: {}", token);

        return token;
    }

    @Override
    public String toString()
    {
        return "Authentication{" +
                "authRequest=" + this.authRequest +
                ", frobString='" + this.frobString + '\'' +
                '}';
    }
}
