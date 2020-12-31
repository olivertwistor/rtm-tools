package nu.olivertwistor.todolisttools.rtmapi.rest;

import nu.olivertwistor.todolisttools.rtmapi.Request;
import nu.olivertwistor.todolisttools.rtmapi.Response;
import nu.olivertwistor.todolisttools.util.AppConfig;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

/**
 * This class handles creating a timeline. A timeline is a set point in time
 * after which actions can be undone. Also, handles the response from Remember
 * The Milk.
 *
 * @since 0.1.0
 */
@SuppressWarnings({"MethodWithTooExceptionsDeclared", "ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class CreateTimeline
{
    @NonNls
    private static final String METHOD_CREATE_TIMELINE = "rtm.timelines.create";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    @NonNls
    private static final String TAG_TIMELINE = "timeline";

    private final Request request;
    private final Response response;

    /**
     * Creates a fully formed REST request for creating a timeline, sends that
     * request to Remember The Milk and retrieves a response.
     *
     * @param config Config object for access to API key etc.
     *
     * @since 0.1.0
     */
    @SuppressWarnings("JavaDoc")
    public CreateTimeline(final AppConfig config)
            throws DocumentException, NoSuchAlgorithmException, IOException,
            MalformedURLException
    {
        final String apiKey = config.getApiKey();
        final String token = config.getToken();

        this.request = new RestRequest(
                config, CreateTimeline.METHOD_CREATE_TIMELINE);
        this.request.addParameter(Request.PARAM_API_KEY, apiKey);
        this.request.addParameter(Request.PARAM_AUTH_TOKEN, token);

        this.response = Response.createResponse(this.request);
    }

    /**
     * Gets the timeline from the REST response.
     *
     * @return The timeline as a string.
     *
     * @throws NoSuchElementException if a timeline couldn't be found in the
     *                                response
     *
     * @since 0.1.0
     */
    public String getTimeline()
    {
        final Element timelineElement = this.response.getElement(
                CreateTimeline.TAG_TIMELINE);

        return timelineElement.getText();
    }

    boolean isResponseSuccess()
    {
        return this.response.isResponseSuccess();
    }

    public Response getResponse()
    {
        return this.response;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    @Override
    public @NonNls String toString()
    {
        return "CreateTimeline{" +
                "request=" + this.request +
                ", response=" + this.response +
                '}';
    }
}
