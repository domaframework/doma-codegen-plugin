package org.seasar.doma.gradle.codegen.task;

import java.util.ArrayList;
import java.util.List;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.TaskAction;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.desc.DaoDesc;
import org.seasar.doma.gradle.codegen.desc.DaoDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityDesc;
import org.seasar.doma.gradle.codegen.extension.DaoConfig;

public class CodeGenDaoDescTask extends DefaultTask {

  private final List<DaoDesc> daoDescList = new ArrayList<>();

  private final ListProperty<EntityDesc> entityDescList =
      getProject().getObjects().listProperty(EntityDesc.class);

  private final Property<GlobalFactory> globalFactory =
      getProject().getObjects().property(GlobalFactory.class);

  private DaoConfig daoConfig;

  @Internal
  public Provider<List<DaoDesc>> getDaoDescList() {
    return getProject().provider(() -> daoDescList);
  }

  @Internal
  public ListProperty<EntityDesc> getEntityDescList() {
    return entityDescList;
  }

  @Internal
  public Property<GlobalFactory> getGlobalFactory() {
    return globalFactory;
  }

  @Nested
  public DaoConfig getDaoConfig() {
    return daoConfig;
  }

  public void setDaoConfig(DaoConfig daoConfig) {
    this.daoConfig = daoConfig;
  }

  @TaskAction
  public void create() {
    DaoDescFactory daoDescFactory = createDaoDescFactory();
    entityDescList.get().stream().map(daoDescFactory::createDaoDesc).forEach(daoDescList::add);
  }

  private DaoDescFactory createDaoDescFactory() {
    return globalFactory
        .get()
        .createDaoDescFactory(
            daoConfig.getPackageName().get(),
            daoConfig.getSuffix().get(),
            daoConfig.getConfigClassName().getOrNull());
  }
}
