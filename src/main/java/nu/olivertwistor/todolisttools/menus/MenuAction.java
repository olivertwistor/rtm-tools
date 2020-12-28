package nu.olivertwistor.todolisttools.menus;

import nu.olivertwistor.todolisttools.util.AppConfig;
import nu.olivertwistor.todolisttools.util.Session;
import nu.olivertwistor.todolisttools.util.Config;

/**
 * Classes implementing this interface define some action that can be executed
 * when a menu item is selected.
 *
 * @since 0.1.0
 */
@FunctionalInterface
interface MenuAction
{
    /**
     * Executes the action.
     *
     * @param config  AppConfig object to use throughout this app
     * @param session Session containing the timeline for this app run
     *
     * @return Whether this action should lead to the current menu (level)
     *         exiting, for example when a "Quit" or "Up one level" menu item
     *         is selected.
     *
     * @since 0.1.0
     */
    boolean execute(AppConfig config, Session session);
}
