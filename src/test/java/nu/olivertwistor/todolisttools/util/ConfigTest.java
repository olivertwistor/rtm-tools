package nu.olivertwistor.todolisttools.util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;

/**
 * Unit tests of the {@link Config} class.
 *
 * @author Johan Nilsson
 * @since  0.1.0
 */
@SuppressWarnings("StringConcatenation")
public final class ConfigTest
{
    private static Config config;

    /**
     * Sets up the foundation for all the tests in this class. A config file
     * containing an API key property is loaded.
     *
     * @throws Exception if anything goes wrong
     *
     * @since 0.1.0
     */
    @BeforeClass
    public static void setUp() throws Exception
    {
        final URL url = ConfigTest.class.getResource("/app.cfg");
        ConfigTest.config = new Config(url);
    }

    /**
     * Asserts that an instantiated Config class can read the API key
     * property.
     *
     * @since 0.1.0
     */
    @SuppressWarnings({"HardCodedStringLiteral", "MultiCatchCanBeSplit"})
    @Test
    public void getApiKey()
    {
        final String apiKey = ConfigTest.config.getApiKey();

        Assert.assertThat("The API key property should be readable from the " +
                "config file.", apiKey, CoreMatchers.notNullValue());
    }
}
