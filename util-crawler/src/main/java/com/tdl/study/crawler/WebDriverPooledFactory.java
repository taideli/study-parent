package com.tdl.study.crawler;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;

public class WebDriverPooledFactory implements PooledObjectFactory<WebDriver> {

    @Override // 创建一个对象
    public PooledObject<WebDriver> makeObject() throws Exception {
        return new DefaultPooledObject<>(WebDriverUtil.getWebDriver());
    }

    @Override // 销毁一个对象
    public void destroyObject(PooledObject<WebDriver> p) throws Exception {
        p.getObject().close();
    }

    @Override // 对象是否有效
    public boolean validateObject(PooledObject<WebDriver> p) {
        return null != p.getObject();
    }

    @Override //激活一个对象
    public void activateObject(PooledObject<WebDriver> p) throws Exception {
    }

    @Override // 钝化一个对象，在归还前做一起清理动作。
    public void passivateObject(PooledObject<WebDriver> p) throws Exception {
    }
}
