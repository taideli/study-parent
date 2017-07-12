package com.tdl.study.crawler;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverFactory {

    public static RemoteWebDriver getPhantomJs(String path) {
        System.out.println("OS name: " + System.getProperty("os.name", "Unknown"));
        System.setProperty("phantomjs.binary.path", path);

        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
//        capabilities.setPlatform(Platform.ANDROID);
        capabilities.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        capabilities.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 　　Firefox/50.0");
        /*if (Constant.isProxy) {
            org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
            proxy.setProxyType(Proxy.ProxyType.MANUAL);
            proxy.setAutodetect(false);
            String proxyStr = "";
            do {
                proxyStr = ProxyUtil.getProxy();
            } while (proxyStr.length() == 0);
            proxy.setHttpProxy(proxyStr);
            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }*/
        return new PhantomJSDriver(capabilities);
    }






















}
