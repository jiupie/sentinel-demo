package com.wl.data.config;

import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author 南顾北衫
 */
@Slf4j
public class NacosDatasourceConfig<T> extends AbstractDataSource<String, T> {
    private static final int DEFAULT_TIMEOUT = 3000;


    private final ExecutorService pool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), new NamedThreadFactory("sentinel-nacos-ds-update", true), new ThreadPoolExecutor.DiscardOldestPolicy());

    private final Listener configListener;
    private final String groupId;
    private final String dataId;
    private final Properties properties;


    private ConfigService configService = null;


    public NacosDatasourceConfig(final String serverAddr, final String groupId, final String dataId, Converter<String, T> parser) {
        this(NacosDatasourceConfig.buildProperties(serverAddr), groupId, dataId, parser);
    }


    public NacosDatasourceConfig(final Properties properties, final String groupId, final String dataId, Converter<String, T> parser) {
        super(parser);
        if (StringUtil.isBlank(groupId) || StringUtil.isBlank(dataId)) {
            throw new IllegalArgumentException(String.format("Bad argument: groupId=[%s], dataId=[%s]", groupId, dataId));
        }
        AssertUtil.notNull(properties, "Nacos properties must not be null, you could put some keys from PropertyKeyConst");
        this.groupId = groupId;
        this.dataId = dataId;
        this.properties = properties;
        this.configListener = new Listener() {
            @Override
            public Executor getExecutor() {
                return pool;
            }

            @Override
            public void receiveConfigInfo(final String configInfo) {
                log.info("[NacosDataSource] New property value received for (properties: {}) (dataId: {}, groupId: {}): {}", properties, dataId, groupId, configInfo);
                T newValue = NacosDatasourceConfig.this.parser.convert(configInfo);
                getProperty().updateValue(newValue);
            }
        };
        initNacosListener();
        loadInitialConfig();
    }

    private void loadInitialConfig() {
        try {
            T newValue = loadConfig();
            if (newValue == null) {
                log.warn("[NacosDataSource] WARN: initial config is null, you may have to check your data source");
            }
            getProperty().updateValue(newValue);
        } catch (Exception ex) {
            log.warn("[NacosDataSource] Error when loading initial config", ex);
        }
    }

    private void initNacosListener() {
        try {
            this.configService = NacosFactory.createConfigService(this.properties);
            // Add config listener.
            configService.addListener(dataId, groupId, configListener);
        } catch (Exception e) {
            log.warn("[NacosDataSource] Error occurred when initializing Nacos data source", e);
            e.printStackTrace();
        }
    }

    @Override
    public String readSource() throws Exception {
        if (configService == null) {
            throw new IllegalStateException("Nacos config service has not been initialized or error occurred");
        }
        return configService.getConfig(dataId, groupId, DEFAULT_TIMEOUT);
    }

    @Override
    public void close() {
        if (configService != null) {
            configService.removeListener(dataId, groupId, configListener);
        }
        pool.shutdownNow();
    }

    private static Properties buildProperties(String serverAddr) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.setProperty(PropertyKeyConst.NAMESPACE, "public");
        return properties;
    }
}
