package com.wl.data.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * [
 * {
 * "resource":"TestResource",
 * "controlBehavior":0,
 * "count":5,
 * "grade":1,
 * "limitApp":"default",
 * "strategy":0
 * }
 * ]
 *
 * @author 南顾北衫
 */

@Component
public class ApplicationConfig {
    @PostConstruct
    public void init() {
        final String remoteAddress = "localhost:8848";
        final String groupId = "sentinel-demo";
        final String dataId = "flowrule";

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDatasourceConfig<>(remoteAddress, groupId,
                dataId, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
