package nu.olivertwistor.todolisttools.rtmapi;

import ch.rfin.util.Pair;
import nu.olivertwistor.todolisttools.util.Config;
import org.jetbrains.annotations.NonNls;

import javax.xml.bind.DatatypeConverter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This is the base class for a request to any of the services Remember the
 * Milk provides.
 *
 * A Request consists of parameters represented by key/value pairs stored in a
 * List. When all desired parameters have been added, call {@link #toUri()} or
 * {@link #toUrl()} to construct the request as a URI/URL string.
 *
 * @since 1.0.0
 */
public abstract class Request
{
    /**
     * URL parameter name for API key.
     *
     * @since 1.0.0
     */
    @NonNls
    public static final String PARAM_API_KEY = "api_key";

    /**
     * URL parameter name for API signature.
     *
     * @since 1.0.0
     */
    @NonNls
    protected static final String PARAM_API_SIGNATURE = "api_sig";

    /**
     * URL parameter name for the authentication token.
     *
     * @since 1.0.0
     */
    @NonNls
    public static final String PARAM_AUTH_TOKEN = "auth_token";

    /**
     * URL parameter name for FROB.
     *
     * @since 1.0.0
     */
    @NonNls
    public static final String PARAM_FROB = "frob";

    /**
     * The Config object containing API key etc.
     *
     * @since 1.0.0
     */
    private final Config config;

    /**
     * List of URL parameters in key/value pairs.
     *
     * @since 1.0.0
     */
    protected final List<Pair<String, String>> parameters;

    /**
     * Creates a request.
     *
     * @param config     Config object for access to API key etc.
     * @param parameters a list of additional parameters
     *
     * @since 1.0.0
     */
    protected Request(final Config config,
                      final List<Pair<String, String>> parameters)
    {
        this.config = config;
        this.parameters = new LinkedList<>(parameters);
    }

    /**
     * Creates a request.
     *
     * @param config Config object for access to API key etc.
     *
     * @since 1.0.0
     */
    protected Request(final Config config)
    {
        this(config, new LinkedList<>());
    }

    /**
     * Adds a URL parameter.
     *
     * @param key   the parameter key
     * @param value the parameter value
     *
     * @since 1.0.0
     */
    public final void addParameter(final String key, final String value)
    {
        this.parameters.add(Pair.of(key, value));
    }

    /**
     * Creates a URI object based on an endpoint, added parameters and a hashed
     * API signature parameter.
     *
     * @return URI object needed for making the request.
     *
     * @throws URISyntaxException if the constructed URI would be malformed.
     *
     * @since 1.0.0
     */
    protected abstract URI toUri() throws URISyntaxException;

    /**
     * Creates a URL object based on an endpoint, added parameters (sorted
     * alphanumerically by key) and a hashed API signature parameter using
     * {@link #hash(String)}.
     *
     * @return URL object needed for making the request.
     *
     * @throws MalformedURLException if the constructed URL would be malformed.
     *
     * @since 1.0.0
     */
    public final URL toUrl() throws MalformedURLException
    {
        final URI uri;
        try
        {
            uri = this.toUri();
        }
        catch (final URISyntaxException e)
        {
            throw new MalformedURLException(e.getMessage());
        }
        final URL url = uri.toURL();

        return url;
    }

    /**
     * Hashes a UTF-8 encoded message, using the MD5 hashing algorithm.
     *
     * @param message the message string to hash
     *
     * @return The message, hashed using MD5.
     *
     * @throws NoSuchAlgorithmException if the MD5 hashing algorithm couldn't
     *                                  be found.
     *
     * @since 1.0.0
     */
    static String hash(final String message) throws NoSuchAlgorithmException
    {
        final Charset charset = StandardCharsets.UTF_8;

        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] bytes = message.getBytes(charset);
        md.update(bytes);
        final byte[] digest = md.digest();

        return DatatypeConverter.printHexBinary(digest)
                .toLowerCase(Locale.ENGLISH);
    }

    /**
     * Generates a API signature to be used in the URL. The signature is made
     * up by a hashed concatenation of the shared secret and each URL parameter
     * key/value pair (sorted alphanumerically by key).
     *
     * @return An API signature.
     *
     * @since 1.0.0
     */
    protected final String generateSignature()
    {
        // Put all parameters into a SortedMap to have them sorted (temporarily
        // for this method only).
        final SortedMap<String, String> sortedParameters = new TreeMap<>();
        this.parameters.forEach((Pair<String, String> item) ->
                sortedParameters.put(item._1, item._2));

        final StringBuilder beforeHash = new StringBuilder(
                this.config.getSharedSecret());
        sortedParameters.forEach((String key, String value) ->
                beforeHash.append(key).append(value));

        try
        {
            final String hash = Request.hash(beforeHash.toString());
            return hash;
        }
        catch (final NoSuchAlgorithmException e)
        {
            throw new RequestSignatureException(
                    "Failed to hash the signature.", e);
        }
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "config=" + this.config +
                ", parameters=" + this.parameters +
                '}';
    }
}
