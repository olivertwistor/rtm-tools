package nu.olivertwistor.todolisttools.rtmapi.auth;

import nu.olivertwistor.todolisttools.rtmapi.Request;
import nu.olivertwistor.todolisttools.rtmapi.rest.RestRequest;
import nu.olivertwistor.todolisttools.util.Config;
import org.dom4j.DocumentException;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

/**
 * This class handles checking the validity of an authentication token. Also,
 * handles the response from Remember The Milk.
 *
 * @since 1.0.0
 */
public final class CheckToken
{
    @NonNls
    private static final String METHOD_CHECK_TOKEN = "rtm.auth.checkToken";

    private final Request request;
    private final AuthResponse response;

    /**
     * Creates a fully formed REST request for checking the validity of the
     * provided authentication token, sends that request to Remember The Milk
     * and retrieves a response.
     *
     * @param config Config object for access to API key etc.
     * @param token  the authentication token to verify
     *
     * @since 1.0.0
     */
    public CheckToken(final Config config, final String token)
    {
        final String apiKey = config.getApiKey();

        this.request = new RestRequest(config, CheckToken.METHOD_CHECK_TOKEN);
        this.request.addParameter(Request.PARAM_API_KEY, apiKey);
        this.request.addParameter(Request.PARAM_AUTH_TOKEN, token);

        this.response = AuthResponse.createAuthResponse(this.request);
    }

    public boolean isResponseSuccess()
    {
        return this.response.isResponseSuccess();
    }

    boolean isResponseFailure()
    {
        return this.response.isResponseFailure();
    }

    public AuthResponse getResponse()
    {
        return this.response;
    }
}
