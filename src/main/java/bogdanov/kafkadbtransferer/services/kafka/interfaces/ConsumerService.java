package bogdanov.kafkadbtransferer.services.kafka.interfaces;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;

import java.util.List;

public interface ConsumerService {

    List<CopiedRecordEntity> consumeAll();

}
