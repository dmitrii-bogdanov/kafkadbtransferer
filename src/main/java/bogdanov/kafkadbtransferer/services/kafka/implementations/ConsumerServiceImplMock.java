package bogdanov.kafkadbtransferer.services.kafka.implementations;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ConsumerService;

import java.util.List;

public class ConsumerServiceImplMock implements ConsumerService {
    @Override
    public List<CopiedRecordEntity> consumeAll() {
        return null;
    }
}
