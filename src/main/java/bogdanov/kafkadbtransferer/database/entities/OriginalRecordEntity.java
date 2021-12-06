package bogdanov.kafkadbtransferer.database.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "table_original")
public class OriginalRecordEntity extends RecordEntity{
}
