package ninja.kyle.projectchain.internallib;

import java.util.Map;
import java.util.Objects;

public class Pair<S, T> {

  private final S left;
  private final T right;

  public Pair(S left, T right) {
    this.left = left;
    this.right = right;
  }

  public Pair(Map.Entry<S, T> entry) {
    this.left = entry.getKey();
    this.right = entry.getValue();
  }

  public S getLeft() {
    return left;
  }

  public T getRight() {
    return right;
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  @Override
  public boolean equals(Object object) {
    if (! (object instanceof Pair)) {
      return false;
    }

    Pair p = (Pair) object;

    return getLeft().equals(p.getLeft()) && getRight().equals(p.getRight());
  }

}
