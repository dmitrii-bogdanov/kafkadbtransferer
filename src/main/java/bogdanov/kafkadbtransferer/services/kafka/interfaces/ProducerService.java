package bogdanov.kafkadbtransferer.services.kafka.interfaces;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;

public interface ProducerService {

    void produce(OriginalRecordEntity entity);

}
