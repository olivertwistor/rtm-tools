package nu.olivertwistor.todolisttools.util;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * Configuration values from config files are read via this class.
 *
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging"})
public class Config
{
    private final File file;
    private final Ini ini;

    /**
     * Creating a new instance of this object based on a certain URL pointing
     * to configuration.
     *
     * @param url URL pointing to configuration
     *
     * @throws InvalidFileFormatException if the configuration isn't formatted
     *                                    correctly
     * @throws IOException                if the given URL couldn't be found or
     *                                    read
     *
     * @since 0.1.0
     */
    @SuppressWarnings("JavaDoc")
    public Config(final URL url)
            throws InvalidFileFormatException, IOException, URISyntaxException
    {
        final URI uri = url.toURI();
        this.file = new File(uri);
        this.ini = new Ini(url);
    }

    public String get(final String section, final String option)
    {
        return this.ini.get(section, option);
    }

    public void set(final String section,
                    final String option,
                    final Object value) throws IOException
    {
        this.ini.put(section, option, value);
        this.ini.store(this.file);
    }

    @Override
    public @NonNls String toString()
    {
        return "Config{" +
                "file=" + this.file +
                ", ini=" + this.ini +
                '}';
    }
}
