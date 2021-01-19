package nu.olivertwistor.todolisttools;

import nu.olivertwistor.todolisttools.menus.MainMenu;
import nu.olivertwistor.todolisttools.util.Config;
import nu.olivertwistor.todolisttools.util.Session;
import org.ini4j.InvalidFileFormatException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Main class for this app. Contains the main method.
 *
 * @since 1.0.0
 */
final class App
{
    /**
     * Prints a short privacy policy and then creates the main menu.
     *
     * @param args unused
     *
     * @since 1.0.0
     */
    public static void main(final String[] args)
    {
        // Load the config. Also, start a new session for this run of the
        // application.
        Config config = null;
        Session session = null;
        final URL configPath = App.class.getResource("/app.cfg");
        config = new Config(configPath);
        session = new Session(config);

        System.out.println("Todo List Tool");
        System.out.println("==============");
        System.out.println();
        System.out.println(String.join(System.lineSeparator(),
                "This program is collecting data from your Remember the Milk ",
                "account, in order to present aggregations and calculations ",
                "on lists and smartlists. For more information, please read ",
                "the file \"privacy-policy.md\" located in the root folder."));
        System.out.println();
        System.out.println(String.join(System.lineSeparator(),
                "This product uses the Remember The Milk API but is not ",
                "endorsed or certified by Remember The Milk."));
        System.out.println();

        final MainMenu mainMenu = new MainMenu(config, session);
        boolean exit;
        do
        {
            mainMenu.show();
            exit = mainMenu.act();
        }
        while (!exit);
    }

    /**
     * Empty constructor. This class doesn't need to be instantiated, because
     * it's the entry point for this app.
     *
     * @since 1.0.0
     */
    private App() { }
}
