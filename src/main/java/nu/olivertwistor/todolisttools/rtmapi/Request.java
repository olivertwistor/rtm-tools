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
 * @since  0.1.0
 */
public abstract class Request
{
    /**
     * URL parameter name for API key.
     *
     * @since 0.1.0
     */
    @NonNls
    public static final String PARAM_API_KEY = "api_key";

    /**
     * URL parameter name for API signature.
     *
     * @since 0.1.0
     */
    @NonNls
    protected static final String param_api_signature = "api_sig";

    /**
     * URL parameter name for FROB.
     *
     * @since 0.1.0
     */
    @NonNls
    public static final String PARAM_FROB = "frob";

    /**
     * The Config object containing API key etc.
     *
     * @since 0.1.0
     */
    protected final Config config;

    /**
     * List of URL parameters in key/value pairs.
     *
     * @since 0.1.0
     */
    protected final List<Pair<String, String>> parameters;

    /**
     * Creates a request.
     *
     * @param config     Config object for access to API key etc.
     * @param parameters a list of additional parameters
     *
     * @since 0.1.0
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
     * @since 0.1.0
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
     * @since 0.1.0
     */
    public void addParameter(final String key, final String value)
    {
        this.parameters.add(Pair.of(key, value));
    }

    /**
     * Creates a URI object based on an endpoint, added parameters and a hashed
     * API signature parameter.
     *
     * @return URI object needed for making the request.
     *
     * @throws URISyntaxException       if the resulting URI is malformed.
     * @throws NoSuchAlgorithmException if the hashing algorith used by
     *                                  {@link #hash(String)} doesn't exist
     *
     * @since 0.1.0
     */
    public abstract URI toUri()
            throws URISyntaxException, NoSuchAlgorithmException;

    /**
     * Creates a URL object based on an endpoint, added parameters (sorted
     * alphanumerically by key) and a hashed API signature parameter using
     * {@link #hash(String)}.
     *
     * @return URL object needed for making the request.
     *
     * @throws MalformedURLException    if the resulting URL is malformed.
     * @throws NoSuchAlgorithmException if the hashing algorith used by
     *                                  {@link #hash(String)} doesn't exist
     *
     * @since 0.1.0
     */
    public URL toUrl() throws MalformedURLException, NoSuchAlgorithmException
    {
        try
        {
            return this.toUri().toURL();
        }
        catch (final URISyntaxException e)
        {
            throw new MalformedURLException(e.getMessage());
        }
    }

    /**
     * Hashes a UTF-8 encoded message, using the MD5 hashing algorithm.
     *
     * @param message the message string to hash
     *
     * @return The message, hashed using MD5.
     *
     * @throws NoSuchAlgorithmException if the MD5 hashing algorithm couldn't
     *                                  be found
     *
     * @since 0.1.0
     */
    protected static String hash(final String message)
            throws NoSuchAlgorithmException
    {
        final Charset charset = StandardCharsets.UTF_8;

        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(message.getBytes(charset));
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
     * @throws NoSuchAlgorithmException if the hashing algorith used by
     *                                  {@link #hash(String)} doesn't exist
     *
     * @since 0.1.0
     */
    protected String generateSignature() throws NoSuchAlgorithmException
    {
        // Put all parameters into a SortedMap to have them sorted (temporarily
        // for this method only).
        final SortedMap<String, String> sortedParameters = new TreeMap<>();
        this.parameters.forEach(
                (item) -> sortedParameters.put(item._1, item._2));

        final StringBuilder beforeHash = new StringBuilder(
                this.config.getSharedSecret());
        sortedParameters.forEach(
                (key, value) -> beforeHash.append(key).append(value));

        return hash(beforeHash.toString());
    }

    @Override
    public String toString()
    {
        return "RestRequest{config=" + this.config +
                ", parameters=" + this.parameters + "}";
    }
}
