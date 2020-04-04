package example.hoge;

import org.seasar.doma.Entity;
import org.seasar.doma.Transient;

@Entity
public class CommonEntity extends AbstractBean {

  String name;

  @Transient String transientString;

  static String staticString;

  @SuppressWarnings("unused")
  private String privateString;
}
