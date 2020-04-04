package example.hoge;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Entity
public class ParentEntity {

  @Column(name = "EMP_NAME")
  String hoge;
}
