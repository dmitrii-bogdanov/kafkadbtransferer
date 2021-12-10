package bogdanov.kafkadbtransferer.configs;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;
import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    @Value("${inputDataTableName}")
    private String originalTableName;

    @Value("${destinationTableName}")
    private String copiedTableName;

    @Value("${columnName.id}")
    private String idColumnName;

    @Value("${columnName.name}")
    private String nameColumnName;

    @Value("${columnName.timestamp}")
    private String timestampColumnName;

    private static final String ORIGINAL_TABLE_ENTITY_NAME = OriginalRecordEntity.class.getSimpleName();
    private static final String COPIED_TABLE_ENTITY_NAME = CopiedRecordEntity.class.getSimpleName();

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_TIMESTAMP = "timestamp";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name.getText().equalsIgnoreCase(ORIGINAL_TABLE_ENTITY_NAME)) {
            return Identifier.toIdentifier(originalTableName);
        }
        if (name.getText().equalsIgnoreCase(COPIED_TABLE_ENTITY_NAME)) {
            return Identifier.toIdentifier(copiedTableName);
        }
        return super.toPhysicalTableName(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name.getText().equalsIgnoreCase(COLUMN_NAME_ID)) {
            return Identifier.toIdentifier(idColumnName);
        }
        if (name.getText().equalsIgnoreCase(COLUMN_NAME_NAME)) {
            return Identifier.toIdentifier(nameColumnName);
        }
        if (name.getText().equalsIgnoreCase(COLUMN_NAME_TIMESTAMP)) {
            return Identifier.toIdentifier(timestampColumnName);
        }
        return super.toPhysicalColumnName(name, jdbcEnvironment);
    }
}
