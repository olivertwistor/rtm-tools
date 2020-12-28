package nu.olivertwistor.todolisttools.util;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Configuration values from a CSV parsing config file are read via this class.
 *
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class CsvConfig extends Config
{
    @NonNls
    private static final String SECTION_CSV = "csv";

    @NonNls
    private static final String OPTION_DELIMITER = "delimiter";

    @NonNls
    private static final String OPTION_SKIP_LINES = "skip-lines";

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
    public CsvConfig(final URL url)
            throws InvalidFileFormatException, IOException, URISyntaxException
    {
        super(url);
    }

    public String getDelimiter()
    {
        return this.get(CsvConfig.SECTION_CSV, CsvConfig.OPTION_DELIMITER);
    }

    /**
     *
     * @return
     *
     * @throws NumberFormatException
     */
    public int getSkipLines()
    {
        final String rawString = this.get(
                CsvConfig.SECTION_CSV, CsvConfig.OPTION_SKIP_LINES);
        return Integer.parseInt(rawString);
    }
}
