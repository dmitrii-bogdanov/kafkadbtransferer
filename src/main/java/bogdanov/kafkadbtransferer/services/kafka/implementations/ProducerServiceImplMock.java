package bogdanov.kafkadbtransferer.services.kafka.implementations;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ProducerService;

import java.util.List;

public class ProducerServiceImplMock implements ProducerService {
    @Override
    public void produceAll(List<OriginalRecordEntity> entities) {
        
    }
}
