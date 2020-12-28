package nu.olivertwistor.todolisttools.menus;

import ch.rfin.util.Pair;
import nu.olivertwistor.java.tui.UnclosableInputStream;
import nu.olivertwistor.todolisttools.util.AppConfig;
import nu.olivertwistor.todolisttools.util.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The main menu for this application. Menu items are created in the
 * constructor. Call {@link #show()} to print the menu. Call {@link #act()} to
 * ask for user input and do different things depending on the chosen menu item.
 *
 * @since  0.1.0
 */
@SuppressWarnings({"HardCodedStringLiteral", "PublicMethodWithoutLogging", "StringConcatenation", "ClassWithoutLogger"})
public final class MainMenu
{
    private final AppConfig config;
    private final Session session;
    private final SortedMap<String, Pair<String, MenuAction>> menuItems;

    /**
     * Creates a new map for the menu items and adds the menu items.
     *
     * Each menu item consists of the following:
     * <ul>
     *     <li>character or short string the user writes to select the menu
     *     item</li>
     *     <li>longer description displayed in the menu</li>
     *     <li>{@link MenuAction} to execute in {@link #act()}</li>
     * </ul>
     *
     * @param config  Config object to use throughout this app
     * @param session Session containing the timeline for this app run
     *
     * @since 0.1.0
     */
    public MainMenu(final AppConfig config, final Session session)
    {
        this.config = config;
        this.session = session;

        // Add the menu items.
        this.menuItems = new TreeMap<>();
        this.menuItems.put("a", Pair.of("[A]dd tasks from file",
                new CsvAddTasksAction()));
        this.menuItems.put("o", Pair.of("[O]btain authentication",
                new ObtainAuthenticationAction()));
        this.menuItems.put("q", Pair.of("[Q]uit", new QuitAction()));
    }

    /**
     * Prints out the main menu to stdout.
     *
     * @since 0.1.0
     */
    public void show()
    {
        System.out.println("Main menu");
        System.out.println("---------");
        for (final Pair<String, MenuAction> pair :
                this.menuItems.values())
        {
            final String key = pair.get_1();
            System.out.println(key);
        }
        System.out.println();
    }

    /**
     * Reads a line from user input and calls the corresponding function from
     * the menu items Map.
     *
     * @return Whether this action should lead to the current menu (level)
     *         exiting, for example when a "Quit" or "Up one level" menu item
     *         is selected.
     *
     * @since 0.1.0
     */
    @SuppressWarnings("NestedMethodCall")
    public boolean act()
    {
        // Temporarily set the system InputStream to our own InputStream that
        // doesn't close.
        System.setIn(new UnclosableInputStream(System.in));

        try (final BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)))
        {
            boolean validItem = false;

            while (!validItem)
            {
                // Read user input.
                System.out.print("? ");
                final String input = br.readLine().toLowerCase(Locale.ENGLISH);

                // Call the appropriate method.
                final Pair<String, MenuAction> pair = this.menuItems.get(input);
                if (pair != null)
                {
                    final MenuAction item = pair._2;
                    if (item != null)
                    {
                        validItem = true;
                        final boolean exit = item.execute(
                                this.config, this.session);
                        if (exit)
                        {
                            // Reset the system InputStream to System.in.
                            System.setIn(System.in);

                            return true;
                        }
                    }
                }
                else
                {
                    System.out.println("That menu item doesn't exist.");
                }
            }
            System.out.println();
        }
        catch (final IOException e)
        {
            System.err.println(
                    "Failed to read user input: " + e.getLocalizedMessage());
        }
        finally
        {
            // Reset the system InputStream to System.in.
            System.setIn(System.in);
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "MainMenu{" +
                "config=" + this.config +
                ", session=" + this.session +
                ", menuItems=" + this.menuItems +
                '}';
    }
}
