package nu.olivertwistor.todolisttools.rtmapi.rest;

import nu.olivertwistor.todolisttools.rtmapi.Request;
import nu.olivertwistor.todolisttools.rtmapi.Response;
import nu.olivertwistor.todolisttools.util.AppConfig;
import nu.olivertwistor.todolisttools.util.Session;
import org.dom4j.DocumentException;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

/**
 * This class handles adding tasks to Remember The Milk.
 *
 * @since 0.1.0
 */
@SuppressWarnings({"MethodWithTooExceptionsDeclared", "ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class AddTask
{
    @NonNls
    private static final String METHOD_ADD_TASK = "rtm.tasks.add";

    @NonNls
    private static final String PARAM_TIMELINE = "timeline";

    @NonNls
    private static final String PARAM_NAME = "name";

    @NonNls
    private static final String PARAM_PARSE = "parse";

    private final Request request;
    private final Response response;

    /**
     * Creates a fully formed REST request for adding tasks to Remember The
     * Milk, sends that request and retrieves a response.
     *
     * @param config   Config object for access to API key etc.
     * @param session  Session containing the timeline for this app run
     * @param smartAdd Smart Add string representing the task to add
     *
     * @since 0.1.0
     */
    @SuppressWarnings("JavaDoc")
    public AddTask(final AppConfig config,
                   final Session session,
                   final String smartAdd)
            throws DocumentException, NoSuchAlgorithmException, IOException,
            MalformedURLException
    {
        final String apiKey = config.getApiKey();
        final String token = config.getToken();

        this.request = new RestRequest(config, AddTask.METHOD_ADD_TASK);
        this.request.addParameter(Request.PARAM_API_KEY, apiKey);
        this.request.addParameter(
                AddTask.PARAM_TIMELINE, session.getTimeline());
        this.request.addParameter(AddTask.PARAM_NAME, smartAdd);
        this.request.addParameter(AddTask.PARAM_PARSE, "1");
        this.request.addParameter(Request.PARAM_AUTH_TOKEN, token);

        this.response = Response.createResponse(this.request);
    }

    public boolean isResponseSuccess()
    {
        return this.response.isResponseSuccess();
    }

    public Response getResponse()
    {
        return this.response;
    }

    @Override
    public @NonNls String toString()
    {
        return "AddTask{" +
                "request=" + this.request +
                ", response=" + this.response +
                '}';
    }
}
