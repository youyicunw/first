package redis.clients.jedis.args;

import static redis.clients.jedis.Protocol.toByteArray;

import java.util.Arrays;
import redis.clients.jedis.util.SafeEncoder;

/**
 * Factory class to get {@link Rawable} objects.
 */
public final class RawableFactory {

  /**
   * Get a {@link Rawable} from an {@code int}.
   * @param i integer value
   * @return raw
   */
  public static Rawable from(int i) {
    return from(toByteArray(i));
  }

  /**
   * Get a {@link Rawable} from a {@code double}.
   * @param d numeric value
   * @return raw
   */
  public static Rawable from(double d) {
    return from(toByteArray(d));
  }

  /**
   * Get a {@link Rawable} from a byte array.
   * @param binary value
   * @return raw
   */
  public static Rawable from(byte[] binary) {
    return new Raw(binary);
  }

  /**
   * Get a {@link Rawable} from a {@link String}.
   * @param string value
   * @return raw
   */
  public static Rawable from(String string) {
    return new RawString(string);
  }

  /**
   * Default implementation of {@link Rawable}.
   */
  public static class Raw implements Rawable {

    private final byte[] raw;

    public Raw(byte[] raw) {
      this.raw = Arrays.copyOf(raw, raw.length);
    }

    @Override
    public byte[] getRaw() {
      return raw;
    }
  }

  /**
   * A {@link Rawable} wrapping a {@link String}.
   */
  public static class RawString implements Rawable {

    private final byte[] raw;

    public RawString(String str) {
      this.raw = SafeEncoder.encode(str);
    }

    @Override
    public byte[] getRaw() {
      return raw;
    }
  }

  private RawableFactory() {
    throw new InstantiationError();
  }
}
