package bogdanov.kafkadbtransferer.services.interfaces;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;

import java.util.List;

public interface CopyingService {

    void addAll(List<CopiedRecordEntity> entities);

    void cleanTable();

}
