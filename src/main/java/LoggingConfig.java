import org.apache.logging.log4j.core.config.Configurator;

public class LoggingConfig {
    public static void configureLogging() {
        Configurator.setLevel("io.javalin", org.apache.logging.log4j.Level.ERROR);
        Configurator.setLevel("org.eclipse.jetty", org.apache.logging.log4j.Level.ERROR);
    }
}
