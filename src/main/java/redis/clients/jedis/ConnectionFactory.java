package redis.clients.jedis;


import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.exceptions.JedisException;

/**
 * PoolableObjectFactory custom impl.
 */
public class ConnectionFactory implements PooledObjectFactory<Connection> {

  private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

  private final JedisSocketFactory jedisSocketFactory;

  private final JedisClientConfig clientConfig;

  public ConnectionFactory(final HostAndPort hostAndPort) {
    this.clientConfig = DefaultJedisClientConfig.builder().build();
    this.jedisSocketFactory = new DefaultJedisSocketFactory(hostAndPort);
  }

  public ConnectionFactory(final HostAndPort hostAndPort, final JedisClientConfig clientConfig) {
    this.clientConfig = DefaultJedisClientConfig.copyConfig(clientConfig);
    this.jedisSocketFactory = new DefaultJedisSocketFactory(hostAndPort, this.clientConfig);
  }

  public ConnectionFactory(final JedisSocketFactory jedisSocketFactory, final JedisClientConfig clientConfig) {
    this.clientConfig = DefaultJedisClientConfig.copyConfig(clientConfig);
    this.jedisSocketFactory = jedisSocketFactory;
  }

  @Override
  public void activateObject(PooledObject<Connection> pooledConnection) throws Exception {
    // what to do ??
  }

  @Override
  public void destroyObject(PooledObject<Connection> pooledConnection) throws Exception {
    final Connection jedis = pooledConnection.getObject();
    if (jedis.isConnected()) {
      try {
        jedis.close();
      } catch (RuntimeException e) {
        logger.debug("Error while close", e);
      }
    }
  }

  @Override
  public PooledObject<Connection> makeObject() throws Exception {
    Connection jedis = null;
    try {
      jedis = new Connection(jedisSocketFactory, clientConfig);
      return new DefaultPooledObject<>(jedis);
    } catch (JedisException je) {
      logger.debug("Error while makeObject", je);
      throw je;
    }
  }

  @Override
  public void passivateObject(PooledObject<Connection> pooledConnection) throws Exception {
    // TODO maybe should select db 0? Not sure right now.
  }

  @Override
  public boolean validateObject(PooledObject<Connection> pooledConnection) {
    final Connection jedis = pooledConnection.getObject();
    try {
      // check HostAndPort ??
      return jedis.isConnected() && jedis.ping();
    } catch (final Exception e) {
      logger.error("Error while validating pooled Connection object.", e);
      return false;
    }
  }
}
