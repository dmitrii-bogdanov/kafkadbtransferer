package bogdanov.kafkadbtransferer.configs;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ProducerService;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class TestProducerImpl implements ProducerService {

    List<OriginalRecordEntity> produced = new LinkedList<>();

    @Override
    public void produce(OriginalRecordEntity entity) {
        produced.add(entity);
        log.info("Test producing: " + entity);
    }

    public void clear() {
        produced.clear();
    }

    public List<OriginalRecordEntity> getList() {
        return produced;
    }
}
