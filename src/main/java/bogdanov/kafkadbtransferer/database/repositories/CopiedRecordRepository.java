package bogdanov.kafkadbtransferer.database.repositories;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopiedRecordRepository extends JpaRepository<CopiedRecordEntity, Long> {
}
