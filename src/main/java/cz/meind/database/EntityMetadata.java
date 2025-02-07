package cz.meind.database;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents metadata about an entity used for database operations.
 * It contains information about the table name, columns, and relations.
 */
public class EntityMetadata {
    private String tableName;
    private final Map<String, String> columns = new HashMap<>(); // ColumnName -> FieldName
    private final Map<String, Field> relations = new HashMap<>(); // RelationType -> Field

    /**
     * Returns the name of the table associated with this entity.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the name of the table associated with this entity.
     *
     * @param tableName the table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns a map of column names to field names for this entity.
     *
     * @return a map of column names to field names
     */
    public Map<String, String> getColumns() {
        return columns;
    }

    /**
     * Adds a column mapping to this entity's metadata.
     *
     * @param columnName the name of the column
     * @param fieldName  the name of the corresponding field
     */
    public void addColumn(String columnName, String fieldName) {
        this.columns.put(columnName, fieldName);
    }

    /**
     * Returns a map of relation types to fields for this entity.
     *
     * @return a map of relation types to fields
     */
    public Map<String, Field> getRelations() {
        return relations;
    }

    /**
     * Adds a relation mapping to this entity's metadata.
     *
     * @param relationType the type of the relation
     * @param field        the corresponding field
     */
    public void addRelation(String relationType, Field field) {
        this.relations.put(relationType, field);
    }
}
