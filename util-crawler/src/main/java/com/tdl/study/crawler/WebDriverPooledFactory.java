package com.tdl.study.crawler;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class WebDriverPooledFactory implements PooledObjectFactory<WebDriver> {

    @Override // 创建一个对象
    public PooledObject<WebDriver> makeObject() throws Exception {
        return new DefaultPooledObject<>(WebDriverFactory.getWebDriver());
    }

    @Override // 销毁一个对象
    public void destroyObject(PooledObject<WebDriver> p) throws Exception {
        p.getObject().quit();
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
        WebDriver driver = p.getObject();
        Set<String> handles = driver.getWindowHandles();
        String[] handlesArray = handles.toArray(new String[handles.size()]);
        int size = handles.size();
        System.out.println(">>>>>> passivate Object, current page size:" + size);
        // 留一个窗口，否则driver会退出
        for (int i = 1; i < handles.size(); i++) {
            driver.switchTo().window(handlesArray[size - i]);
            driver.close();
        }
    }
}
