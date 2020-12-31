package nu.olivertwistor.todolisttools.menus;

import nu.olivertwistor.todolisttools.rtmapi.AuthRequest;
import nu.olivertwistor.todolisttools.rtmapi.auth.GetFrob;
import nu.olivertwistor.todolisttools.rtmapi.auth.GetToken;
import nu.olivertwistor.todolisttools.util.AppConfig;
import org.dom4j.DocumentException;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class handles authentication to the Remember The Milk service. The way
 * to use this class is to first call
 * {@link #generateAuthRequest(AppConfig, String)}. That method returns a URL
 * the user should go to and give this application necessary permissions to
 * access the user's RTM account. When that is done,
 * {@link #obtainToken(AppConfig)} may be called to obtain a token, with which
 * all subsequent API calls to the RTM service will be made.
 *
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging", "ConstantExpression"})
final class Authentication
{
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
     * @throws NoSuchElementException
     * @throws DocumentException
     * @throws NoSuchAlgorithmException
     * @throws MalformedURLException
     * @throws IOException
     */
    @SuppressWarnings({"JavaDoc", "MethodWithTooExceptionsDeclared"})
    URL generateAuthRequest(final AppConfig config,
                            final String permission)
            throws DocumentException, NoSuchAlgorithmException,
            MalformedURLException, IOException
    {
        // First, retrieve a FROB.
        final GetFrob getFrob = new GetFrob(config);
        this.frobString = getFrob.getFrob();

        // Then, construct an AuthRequest object and store it in this class.
        // Return the URL.
        this.authRequest = new AuthRequest(config, permission, this.frobString);
        return this.authRequest.toUrl();
    }

    /**
     * If {@link #generateAuthRequest(AppConfig, String)} has been called
     * beforehand, an authentication token is retrieved from Remember the Milk
     * for use with all subsequent calls to the RTM API.
     *
     * @param config Config object for access to the API key etc.
     *
     * @return The authentication token.
     *
     * @throws UnsupportedOperationException if an auth request hasn't been
     *                                       generated prior to calling this
     *                                       method
     * @throws NoSuchElementException
     * @throws DocumentException
     * @throws NoSuchAlgorithmException
     * @throws MalformedURLException
     * @throws IOException
     */
    @SuppressWarnings({"JavaDoc", "MethodWithTooExceptionsDeclared"})
    String obtainToken(final AppConfig config)
            throws DocumentException, NoSuchAlgorithmException,
            MalformedURLException, IOException
    {
        Objects.requireNonNull(this.authRequest, "Authentication has not " +
                "yet been obtained. Please call #generateAuthRequest() " +
                "before calling this method.");

        final GetToken getToken = new GetToken(config, this.frobString);
        return getToken.getToken();
    }

    @Override
    public @NonNls String toString()
    {
        return "Authentication{authRequest=" + this.authRequest +
                ", frobString=" + this.frobString + '}';
    }
}
