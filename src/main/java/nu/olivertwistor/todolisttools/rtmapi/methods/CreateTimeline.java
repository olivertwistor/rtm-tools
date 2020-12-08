package nu.olivertwistor.todolisttools.rtmapi.methods;

import nu.olivertwistor.todolisttools.rtmapi.Request;
import nu.olivertwistor.todolisttools.rtmapi.Response;
import nu.olivertwistor.todolisttools.rtmapi.RestRequest;
import nu.olivertwistor.todolisttools.util.Config;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

public class CreateTimeline
{
    @NonNls
    private static final String method_create_timeline = "rtm.timelines.create";

    @NonNls
    private static final String param_auth_token = "auth_token";

    @NonNls
    private static final String tag_timeline = "timeline";

    private final Request request;
    private final Response response;

    public CreateTimeline(final Config config)
            throws DocumentException, NoSuchAlgorithmException, IOException,
            MalformedURLException
    {
        this.request = new RestRequest(config, method_create_timeline);
        this.request.addParameter(Request.PARAM_API_KEY, config.getApiKey());
        this.request.addParameter(param_auth_token, config.getToken());

        this.response = Response.createResponse(this.request);
    }

    public String getTimeline() throws NoSuchElementException
    {
        final Element timelineElement = this.response.getElement(tag_timeline);

        return timelineElement.getText();
    }

    public Response getResponse()
    {
        return this.response;
    }
}
