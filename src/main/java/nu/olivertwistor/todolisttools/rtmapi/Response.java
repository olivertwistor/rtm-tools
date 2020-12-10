package nu.olivertwistor.todolisttools.rtmapi;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A REST response takes a {@link Request} and reads the XML response, storing
 * the {@link Element root element} for later use.
 *
 * @author Johan Nilsson
 * @since  0.1.0
 */
public class Response
{
    @NonNls
    private static final String attrib_error_code = "code";

    @NonNls
    private static final String attrib_error_message = "msg";

    /**
     * XML attribute name for response status.
     *
     * @since 0.1.0
     */
    @NonNls
    public static final String ATTRIB_STATUS = "stat";

    @NonNls
    private static final String tag_error = "err";

    @NonNls
    private static final String tag_response = "rsp";

    @NonNls
    private static final String val_status_failure = "fail";

    @NonNls
    private static final String val_status_success = "ok";

    /**
     * The root element of the XML response.
     *
     * @since 0.1.0
     */
    protected final Element rootElement;

    private final Document document;

    /**
     * Creates a REST response object based off of a REST request. The XML
     * response is parsed into a {@link Element} tree, from where the caller
     * may retrieve the elements they wish.
     *
     * @param contentStream an InputStream with the XML response to parse
     *
     * @throws DocumentException if the response couldn't be parsed into XML
     *
     * @since 0.1.0
     */
    public Response(final InputStream contentStream) throws DocumentException
    {
        final SAXReader reader = new SAXReader();
        this.document = reader.read(contentStream);
        this.rootElement = this.document.getRootElement();
    }

    /**
     * Creates a REST response object based off of an XML file. The XML is
     * parsed into a {@link Element} tree, from where the caller may retrieve
     * the elements they wish.
     *
     * @param file a file containing the XML response to parse
     *
     * @throws DocumentException if the file couldn't be parsed into XML
     *
     * @since 0.1.0
     */
    public Response(final File file) throws DocumentException
    {
        final SAXReader reader = new SAXReader();
        this.document = reader.read(file);
        this.rootElement = this.document.getRootElement();
    }

    /**
     * Creates a REST response object based off of a REST request. The XML
     * response is parsed into a {@link Element} tree, from where the caller
     * may retrieve the elements they wish.
     *
     * @param request the {@link Request}, which XML response to parse
     *
     * @return The created Response object.
     *
     * @since 0.1.0
     */
    @SuppressWarnings("JavaDoc")
    public static Response createResponse(final Request request)
            throws MalformedURLException, NoSuchAlgorithmException,
            IOException, DocumentException
    {
        // Make an HTTP request to get the response.
        final URLConnection connection = request.toUrl().openConnection();
        final InputStream contentStream = connection.getInputStream();

        return new Response(contentStream);
    }

    /**
     * Determines whether this Response was successful.
     *
     * @return True if successful; false otherwise.
     *
     * @throws NoSuchElementException if {@link #ATTRIB_STATUS} couldn't be
     *                                found in the response
     *
     * @since 0.1.0
     */
    public boolean isResponseSuccess() throws NoSuchElementException
    {
        final String status = this.rootElement.attributeValue(ATTRIB_STATUS);
        if (status == null)
        {
            throw new NoSuchElementException(
                    "Failed to find a status attribute on the root element.");
        }

        return val_status_success.equals(status);
    }

    /**
     * Determines whether this Response returned a failure code.
     *
     * @return True if a failure; false otherwise.
     *
     * @throws NoSuchElementException if {@link #ATTRIB_STATUS} couldn't be
     *                                found in the response
     *
     * @since 0.1.0
     */
    public boolean isResponseFailure() throws NoSuchElementException
    {
        final String status = this.rootElement.attributeValue(ATTRIB_STATUS);
        if (status == null)
        {
            throw new NoSuchElementException(
                    "Failed to find a status attribute on the root element.");
        }

        return val_status_failure.equals(status);
    }

    /**
     * Searches for an element in the XML tree based on a tag name in a tag
     * tree.
     *
     * @param base the base element for which to start search
     * @param tags a list of tags describing the tree in which to search, based
     *             in the base; it's the last tag in the list that is the
     *             desired tag
     *
     * @return The element that is found after starting at the base and
     *         traversing the tag tree.
     *
     * @throws NoSuchElementException if either the base element doesn't exist,
     *                                the tags list is empty or if any of the
     *                                tags in the tags list doesn't exist
     *
     * @since 0.1.0
     */
    public Element getElement(final Element base, final Deque<String> tags)
            throws NoSuchElementException
    {
        if (base == null || tags.isEmpty())
        {
            throw new NoSuchElementException(
                    "No base element, or the list is empty.");
        }

        // Take a look at the first tag in the list. We also remove it from the
        // list, to prepare for the recursion further on.
        final String firstTag = tags.pop();
        final Element element = base.element(firstTag);
        if (element == null)
        {
            throw new NoSuchElementException(firstTag + " does not exist.");
        }

        // If the remaining list is empty, it means that we have reached our
        // final level in the hierarchy and can stop the recursion.
        if (tags.isEmpty())
        {
            return element;
        }

        // We have more levels in the hierarchy, so let's do one more recursive
        // step. We use the previously fetched Element as our new base.
        return this.getElement(element, tags);
    }

    /**
     * Searches for an element in the XML tree based on a tag name in a tag
     * tree.
     *
     * @param base the base element for which to start search
     * @param tags a list of tags describing the tree in which to search, based
     *             in the base; it's the last tag in the list that is the
     *             desired tag
     *
     * @return The element that is found after starting at the base and
     *         traversing the tag tree.
     *
     * @throws NoSuchElementException if either the base element doesn't exist,
     *                                the tags list is empty or if any of the
     *                                tags in the tags list doesn't exist
     *
     * @since 0.1.0
     */
    public Element getElement(final String base, final Deque<String> tags)
            throws NoSuchElementException
    {
        return this.getElement(this.rootElement.element(base), tags);
    }

    /**
     * Gets an element in the XML tree based on a parent tag and a child tag.
     *
     * @param base the parent tag; the tag directly above the desired tag in
     *             the XML tree
     * @param tag  the desired (child) tag
     *
     * @return The element identified by the parent tag and the child tag.
     *
     * @throws NoSuchElementException if either the base tag or the child tag
     *                                doesn't exist
     *
     * @since 0.1.0
     */
    public Element getElement(final String base, final String tag)
            throws NoSuchElementException
    {
        final Deque<String> tags = new LinkedList<>();
        tags.add(tag);

        return this.getElement(base, tags);
    }

    /**
     * Searches for an element in the XML tree based on a tag name in a tag
     * tree.
     *
     * @param tags a list of tags describing the tree in which to search, based
     *             in the root node; it's the last tag in the list that is the
     *             desired tag
     *
     * @return The element that is found after starting at the root node and
     *         traversing the tag tree.
     *
     * @throws NoSuchElementException if the tags list is empty or if any of
     *                                the tags in the tags list doesn't exist
     *
     * @since 0.1.0
     */
    public Element getElement(final Deque<String> tags)
            throws NoSuchElementException
    {
        return this.getElement(this.rootElement, tags);
    }

    /**
     * Gets an element based on a tag name directly under the root name.
     *
     * @param tag the desired tag
     *
     * @return The element identified by the desired tag.
     *
     * @throws NoSuchElementException if the tag doesn't exist
     *
     * @since 0.1.0
     */
    public Element getElement(final String tag) throws NoSuchElementException
    {
        final Element element = this.rootElement.element(tag);
        if (element == null)
        {
            throw new NoSuchElementException(tag + " does not exist.");
        }

        return element;
    }

    /**
     * Returns this response's entire XML tree.
     *
     * @return The XML tree as a string.
     *
     * @since 0.1.0
     */
    public String toXmlString()
    {
        return this.document.asXML();
    }

    @Override
    public String toString()
    {
        return "Response{" +
                "rootElement=" + this.rootElement +
                ", document=" + this.document +
                "}";
    }
}
