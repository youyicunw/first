package redis.clients.jedis.commands.unified.pooled;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import redis.clients.jedis.commands.unified.StringValuesCommandsTestBase;

public class PooledStringValuesCommandsTest extends StringValuesCommandsTestBase {

  @BeforeClass
  public static void prepare() throws InterruptedException {
    jedis = PooledCommandsTestHelper.getPooled();
  }

  @AfterClass
  public static void cleanUp() {
    jedis.close();
  }

  @Before
  public void setUp() {
    PooledCommandsTestHelper.clearData();
  }
}
