package bogdanov.kafkadbtransferer.database.repositories;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginalRecordRepository extends JpaRepository<OriginalRecordEntity, Long> {
}
