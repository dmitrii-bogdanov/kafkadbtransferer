package bogdanov.kafkadbtransferer.testutils;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import org.springframework.boot.test.context.TestComponent;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestUtil {

    public List<OriginalRecordEntity> generateEntities(int size, String namePrefix) {
        if (size < 0) {
            throw new RuntimeException("Negative size");
        }
        if (Objects.isNull(namePrefix)) {
            throw new RuntimeException("Null namePrefix");
        }
        List<OriginalRecordEntity> entities = new ArrayList<>(size);
        OriginalRecordEntity entity;
        for (int i = 0; i < size; i++) {
            entity = new OriginalRecordEntity();
            entity.setId(i);
            entity.setName(namePrefix + i);
            entity.setTimestamp(LocalDateTime.now());
            entities.add(entity);
        }
        return entities;
    }

    public void setObjectProperty(Object object, String fieldName, Object fieldValue) {
        Class<?> cl = object.getClass();
        if (cl != null) {
            try {
                Field field = cl.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
