package nu.olivertwistor.todolisttools.rtmapi.rest;

import ch.rfin.util.Pair;
import nu.olivertwistor.todolisttools.rtmapi.Request;
import nu.olivertwistor.todolisttools.util.AppConfig;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a specialized version of {@link Request} for requests to Remember
 * The Milk's REST service. See the base class for further information.
 *
 * @author Johan Nilsson
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging"})
public class RestRequest extends Request
{
    private static final String ENDPOINT_REST =
            "https://api.rememberthemilk.com/services/rest/";
    private static final String PARAM_METHOD = "method";

    /**
     * Creates a REST request.
     *
     * @param config     Config object for access to API key etc.
     * @param methodName name of the method to call
     * @param parameters a list of additional parameters
     *
     * @since 0.1.0
     */
    private RestRequest(final AppConfig config,
                        final String methodName,
                        final List<Pair<String, String>> parameters)
    {
        super(config, parameters);
        this.parameters.add(Pair.of(RestRequest.PARAM_METHOD, methodName));
    }

    /**
     * Creates a REST request.
     *
     * @param config     Config object for access to API key etc.
     * @param methodName name of the method to call
     *
     * @since 0.1.0
     */
    public RestRequest(final AppConfig config, final String methodName)
    {
        this(config, methodName, new LinkedList<>());
    }

    /**
     * Creates a URI object based on the REST endpoint, added parameters and a
     * hashed API signature parameter.
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
    public final URI toUri()
            throws URISyntaxException, NoSuchAlgorithmException
    {
        final URIBuilder builder = new URIBuilder(RestRequest.ENDPOINT_REST);
        this.parameters.forEach((Pair<String, String> item) ->
                builder.addParameter(item._1, item._2));
        builder.addParameter(Request.PARAM_API_SIGNATURE,
                this.generateSignature());

        return builder.build();
    }

    @SuppressWarnings("DesignForExtension")
    @Override
    public String toString()
    {
        return "RestRequest{super=" + super.toString() + '}';
    }
}
