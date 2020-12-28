package nu.olivertwistor.todolisttools.menus;

import nu.olivertwistor.todolisttools.util.AppConfig;
import nu.olivertwistor.todolisttools.util.Session;

/**
 * Prints out a goodbye message and exits the application.
 *
 * @since 0.1.0
 */
@SuppressWarnings({"PublicMethodWithoutLogging", "HardCodedStringLiteral", "ClassWithoutLogger"})
public final class QuitAction implements MenuAction
{
    @Override
    public boolean execute(final AppConfig config, final Session session)
    {
        System.out.println("Goodbye!");
        return true;
    }
}
