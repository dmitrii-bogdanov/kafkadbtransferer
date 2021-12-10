package bogdanov.kafkadbtransferer.services.kafka.interfaces;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;

import java.util.List;

public interface ConsumerService {

    void consumeBatch(List<OriginalRecordEntity> entities);

}
