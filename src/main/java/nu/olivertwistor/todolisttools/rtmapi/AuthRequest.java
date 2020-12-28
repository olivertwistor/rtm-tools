package nu.olivertwistor.todolisttools.rtmapi;

import ch.rfin.util.Pair;
import nu.olivertwistor.todolisttools.util.AppConfig;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NonNls;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a specialized version of {@link Request} for requests to Remember
 * The Milk's authentication service. See the base class for further
 * information.
 *
 * @author Johan Nilsson
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class AuthRequest extends Request
{
    private static final String ENDPOINT_AUTH =
            "https://www.rememberthemilk.com/services/auth/";
    private static final String PARAM_PERMISSIONS = "perms";

    /**
     * Creates an authentication request.
     *
     * @param config      Config object for access to API key etc.
     * @param permissions which permissions this app should get
     * @param frob        FROB used for this authentication
     * @param parameters  a sorted map of additional parameters
     *
     * @since 0.1.0
     */
    private AuthRequest(final AppConfig config,
                        final String permissions,
                        final String frob,
                        final List<Pair<String, String>> parameters)
    {
        super(config, parameters);

        final String apiKey = config.getApiKey();
        this.parameters.add(Pair.of(Request.PARAM_API_KEY, apiKey));
        this.parameters.add(Pair.of(
                AuthRequest.PARAM_PERMISSIONS, permissions));
        this.parameters.add(Pair.of(Request.PARAM_FROB, frob));
    }

    /**
     * Creates an authentication request.
     *
     * @param config      Config object for access to API key etc.
     * @param permissions which permissions this app should get
     * @param frob        FROB used for this authentication
     *
     * @since 0.1.0
     */
    public AuthRequest(final AppConfig config,
                       final String permissions,
                       final String frob)
    {
        this(config, permissions, frob, new LinkedList<>());
    }

    /**
     * Creates a URI object based on the authentication endpoint, added
     * parameters and a hashed API signature parameter.
     *
     * @return URI object needed for making the request.
     *
     * @throws URISyntaxException       if the resulting URI is malformed.
     * @throws NoSuchAlgorithmException if the hashing algorith used doesn't
     *                                  exist
     *
     * @since 0.1.0
     */
    @SuppressWarnings("NestedMethodCall")
    @Override
    public URI toUri() throws URISyntaxException, NoSuchAlgorithmException
    {
        final URIBuilder builder = new URIBuilder(AuthRequest.ENDPOINT_AUTH);
        this.parameters.forEach((Pair<String, String> item) ->
                builder.addParameter(item._1, item._2));
        builder.addParameter(
                Request.PARAM_API_SIGNATURE, this.generateSignature());

        return builder.build();
    }

    @Override
    public @NonNls String toString()
    {
        return "AuthRequest{super=" + super.toString() + '}';
    }
}
