package cz.meind.database;

import cz.meind.interfaces.*;

import java.lang.reflect.Field;


public class EntityParser {
    /**
     * Parses a given entity class and extracts relevant metadata such as table name, column names, and relationships.
     *
     * @param clazz The entity class to be parsed.
     * @return An instance of {@link EntityMetadata} containing the extracted metadata.
     */
    public static EntityMetadata parseEntity(Class<?> clazz) {
        EntityMetadata metadata = new EntityMetadata();

        // Check if the class is annotated with @Entity
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            // Set the table name based on the @Entity annotation or the class name
            metadata.setTableName(entity.tableName().isEmpty() ? clazz.getSimpleName() : entity.tableName());
        }

        // Iterate through the fields of the class
        for (Field field : clazz.getDeclaredFields()) {
            // Check if the field is annotated with @Column
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                // Set the column name based on the @Column annotation or the field name
                String columnName = column.name().isEmpty() ? field.getName() : column.name();
                metadata.addColumn(columnName, field.getName());
            }

            // Check if the field is annotated with @OneToMany
            if (field.isAnnotationPresent(OneToMany.class)) {
                metadata.addRelation("OneToMany", field);
            }

            // Check if the field is annotated with @ManyToMany
            if (field.isAnnotationPresent(ManyToMany.class)) {
                metadata.addRelation("ManyToMany", field);
            }

            // Check if the field is annotated with @ManyToOne
            if (field.isAnnotationPresent(ManyToOne.class)) {
                metadata.addRelation("ManyToOne", field);
            }
        }

        return metadata;
    }
}
