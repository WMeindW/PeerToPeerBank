package cz.meind.database;

import cz.meind.application.Application;
import cz.meind.interfaces.Entity;
import org.reflections.Reflections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the context for interacting with a database.
 * It manages the connection to the database, keeps track of entities,
 * and provides methods for connecting and closing the connection.
 */
public class DatabaseContext {

    /**
     * A map of entity classes to their corresponding metadata.
     */
    public Map<Class<?>, EntityMetadata> entities = new HashMap<>();

    /**
     * The database connection.
     */
    private Connection connection;

    /**
     * Attempts to establish a connection to the database.
     * If a connection is already established, it returns the existing connection.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        try {
            if (connection == null)
                connection = DriverManager.getConnection(Application.dbUrl, Application.dbUser, Application.dbPassword);
            Application.logger.info(DatabaseContext.class, "Connected to database " + Application.dbUrl + " as " + Application.dbUser);
            return connection;
        } catch (SQLException e) {
            Application.logger.error(DatabaseContext.class, e);
        }
        return null;
    }

    /**
     * Closes the database connection if it is open.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Application.logger.error(DatabaseContext.class, e);
        }
    }

    /**
     * Initializes the database context by scanning for entities annotated with {@link Entity}
     * and parsing their metadata using {@link EntityParser}.
     */
    public DatabaseContext() {
        Application.logger.info(DatabaseContext.class, "Initializing database context");
        Reflections reflections = new Reflections("cz.meind");
        reflections.getTypesAnnotatedWith(Entity.class).forEach(entity -> entities.put(entity, EntityParser.parseEntity(entity)));
    }
}
