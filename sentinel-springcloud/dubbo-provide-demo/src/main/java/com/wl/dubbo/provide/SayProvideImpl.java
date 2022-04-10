package com.wl.dubbo.provide;

import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author 南顾北衫
 * @description
 * @date 2022/4/10
 */
@DubboService
public class SayProvideImpl implements SayProvide {
    @Override
    public String sayChinese(String name) {
        return "说中文：" + name;
    }
}
