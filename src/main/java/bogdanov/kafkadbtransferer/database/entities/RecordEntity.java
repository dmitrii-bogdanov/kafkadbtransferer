package bogdanov.kafkadbtransferer.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class RecordEntity {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

}
