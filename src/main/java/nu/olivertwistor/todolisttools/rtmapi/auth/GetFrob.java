package nu.olivertwistor.todolisttools.rtmapi.auth;

import nu.olivertwistor.todolisttools.rtmapi.Request;
import nu.olivertwistor.todolisttools.rtmapi.rest.RestRequest;
import nu.olivertwistor.todolisttools.util.AppConfig;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

/**
 * This class handles the generation of a request for a FROB string and also
 * handles the response from Remember The Milk.
 *
 * @since 0.1.0
 */
@SuppressWarnings({"MethodWithTooExceptionsDeclared", "ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class GetFrob
{
    @NonNls
    private static final String METHOD_GET_FROB = "rtm.auth.getFrob";

    @NonNls
    private static final String TAG_FROB = "frob";

    private final Request request;
    private final AuthResponse response;

    /**
     * Creates a fully formed REST request for getting a FROB string, sends
     * that request to Remember The Milk and retrieves a response.
     *
     * @param config Config object for access to API key etc.
     *
     * @throws DocumentException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws MalformedURLException
     */
    @SuppressWarnings("JavaDoc")
    public GetFrob(final AppConfig config)
            throws DocumentException, NoSuchAlgorithmException, IOException,
            MalformedURLException
    {
        final String apiKey = config.getApiKey();

        this.request = new RestRequest(config, GetFrob.METHOD_GET_FROB);
        this.request.addParameter(Request.PARAM_API_KEY, apiKey);

        this.response = AuthResponse.createAuthResponse(this.request);
    }

    /**
     * Gets the FROB string from the response.
     *
     * @return The FROB string.
     *
     * @throws NoSuchElementException if the FROB couldn't be found in the
     *                                response.
     *
     * @since 0.1.0
     */
    public String getFrob()
    {
        final Element frobElement = this.response.getElement(GetFrob.TAG_FROB);

        return frobElement.getText();
    }

    @Override
    public @NonNls String toString()
    {
        return "GetFrob{request=" + this.request + ", response=" +
                this.response + '}';
    }
}
