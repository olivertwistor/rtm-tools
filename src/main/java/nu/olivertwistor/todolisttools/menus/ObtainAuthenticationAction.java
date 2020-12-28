package nu.olivertwistor.todolisttools.menus;

import nu.olivertwistor.java.tui.Terminal;
import nu.olivertwistor.todolisttools.rtmapi.auth.CheckToken;
import nu.olivertwistor.todolisttools.util.AppConfig;
import nu.olivertwistor.todolisttools.util.Session;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

/**
 * Asks the user to go to the provided URL to give this application the
 * required permissions. When that is done, an authentication token is
 * retrieved from the Remember The Milk service and it is stored in the config
 * file for this application.
 *
 * Before all of this, the existence and validity of an authentication token is
 * determined. If there is an existing and valid authentication token, no
 * further authentication will be done.
 *
 * @since 0.1.0
 */
@SuppressWarnings({"PublicMethodWithoutLogging", "HardCodedStringLiteral", "ConstantExpression", "StringConcatenation", "ClassWithoutLogger", "MultiCatchCanBeSplit"})
public final class ObtainAuthenticationAction implements MenuAction
{
    private static final String VAL_WRITE_PERMISSIONS = "write";

    @Override
    public boolean execute(final AppConfig config, final Session session)
    {
        try
        {
            ObtainAuthenticationAction.checkExistingToken(config);
        }
        catch (final DocumentException | NoSuchAlgorithmException |
                IOException e)
        {
            System.out.println("Failed to check for existing token.");
            return false;
        }

        // We have no valid authentication token, so let's proceed with
        // obtaining one.
        System.out.println(String.join(System.lineSeparator(),
                "In order to use this program, first you have to go to the ",
                "Remember The Milk website and give it this program ",
                "permission to access your account. Please enter the ",
                "following URL in a web browser and follow the instructions. ",
                "Afterwards, return to this program."));
        System.out.println();

        final Authentication authentication = new Authentication();
        try
        {
            final URL authUrl = authentication.generateAuthRequest(
                    config, ObtainAuthenticationAction.VAL_WRITE_PERMISSIONS);
            final String urlString = authUrl.toExternalForm();
            System.out.println(urlString);
        }
        catch (final NoSuchElementException | DocumentException |
                NoSuchAlgorithmException | IOException e)
        {
            System.out.println("Failed to generate an authentication request.");
            return false;
        }

        try
        {
            Terminal.readString("Please confirm that you have visited the " +
                    "URL (any character will do): ");
        }
        catch (final IOException e)
        {
            System.err.println("Failed to read user input.");
            return false;
        }

        // Now when we have obtained authentication and the user have confirmed
        // that, we can retrieve the token we will use for any subsequent calls
        // to the API.
        try
        {
            final String token = authentication.obtainToken(config);

            // Store the retrieved token to the config file.
            config.setToken(token);
            System.out.println("Obtained an authentication token.");
        }
        catch (final DocumentException | NoSuchAlgorithmException |
                IOException e)
        {
            System.out.println("Failed to either obtain an authentication " +
                    "token or store it in the config file.");
            return false;
        }

        return false;
    }

    /**
     * Determines whether there is an existing authentication token and whether
     * it is valid.
     *
     * @return True if the authentication token exists and is valid; false
     *         otherwise.
     *
     * @since 0.1.0
     */
    @SuppressWarnings({"JavaDoc", "MethodWithTooExceptionsDeclared"})
    private static boolean checkExistingToken(final AppConfig config)
            throws DocumentException, NoSuchAlgorithmException,
            MalformedURLException, IOException
    {
        final String existingToken = config.getToken();
        final CheckToken checkToken =
                new CheckToken(config, existingToken);

        if (checkToken.isResponseSuccess())
        {
            System.out.println("You have already authenticated this " +
                    "application.");
            return true;
        }

        return false;
    }
}
