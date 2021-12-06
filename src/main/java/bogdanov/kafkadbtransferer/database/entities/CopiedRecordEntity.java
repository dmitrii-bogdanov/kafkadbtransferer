package bogdanov.kafkadbtransferer.database.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "table_copied")
public class CopiedRecordEntity extends RecordEntity{
}
