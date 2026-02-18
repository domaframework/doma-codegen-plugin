package base;

import java.time.LocalDateTime;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/**
 * Base entity listener that automatically sets audit fields.
 */
public class AbstractEntityListener<E extends AbstractEntity> implements EntityListener<E> {

    @Override
    public void preInsert(E entity, PreInsertContext<E> context) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setCreatedBy("system");
        entity.setUpdatedBy("system");
        if (entity.getDeleted() == null) {
            entity.setDeleted(false);
        }
    }

    @Override
    public void preUpdate(E entity, PreUpdateContext<E> context) {
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("system");
    }
}


