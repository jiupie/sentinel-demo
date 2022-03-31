package com.wl.sentinel.config;

import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/3/30
 */
@Configuration
public class SentinelConfig {

    @PostConstruct
    public void init() {
        ArrayList<FlowRule> list = new ArrayList<>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("hello");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(3);
        list.add(flowRule);
        FlowRuleManager.loadRules(list);
    }
}
