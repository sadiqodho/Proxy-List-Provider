package com.proxyprovider.schedulers;

import com.proxyprovider.entities.Proxy;
import com.proxyprovider.entities.ProxyProvider;
import com.proxyprovider.entities.TestURL;
import com.proxyprovider.repositories.ProxyProviderRepository;
import com.proxyprovider.repositories.ProxyRepository;
import com.proxyprovider.repositories.TestURLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Abro on 6/27/2020.
 */
@Component
public class TestingProxiesTask {
    @Autowired
    private ProxyRepository proxyRepository;

    @Autowired
    private ProxyProviderRepository proxyProviderRepository;

    @Autowired
    private TestURLRepository testURLRepository;

    private static final Logger log = LoggerFactory.getLogger(ProxyListFetchTask.class);

//    @Scheduled(fixedRate = (1000*60*10))
    public void testingProxies() {
        Iterable<Proxy> proxies = proxyRepository.findAll();
        for (Proxy proxy: proxies) {
            try {

                URL weburl = new URL("https://www.google.com/");

                java.net.Proxy webProxy = null;
                if("http".equalsIgnoreCase(proxy.getProtocol()) || "https".equalsIgnoreCase(proxy.getProtocol())){
                    webProxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxy.getIp(), proxy.getPort()));
                    HttpURLConnection webProxyConnection = (HttpURLConnection) weburl.openConnection(webProxy);
                    webProxyConnection.getContent();
                }

                else if("direct".equalsIgnoreCase(proxy.getProtocol())) {
                    HttpURLConnection directConnection = (HttpURLConnection) weburl.openConnection(java.net.Proxy.NO_PROXY);
                    directConnection.getContent();
                }

                else if("socks".equalsIgnoreCase(proxy.getProtocol())){
                    webProxy = new java.net.Proxy(java.net.Proxy.Type.SOCKS, new InetSocketAddress(proxy.getIp(), proxy.getPort()));
                    HttpURLConnection socksConnection = (HttpURLConnection) weburl.openConnection(webProxy);
                    socksConnection.getContent();
                }

                proxy.setLastBasicFunctionalityTest(new Date());

                TestURL testURL = new TestURL();
                ProxyProvider proxyProvider = proxy.getProvider();
                testURL.setProvider(proxyProvider);
                proxy.setTestURL(proxyProvider.getBaseAddress());
                testURL.setValidationDetails("Valid");
                testURLRepository.save(testURL);
                proxyRepository.save(proxy);
                log.info("Successfully valid!");

            }catch (Exception exp){

                TestURL testURL = new TestURL();
                testURL.setProvider(proxy.getProvider());
                Date foundDate = proxy.getLastFound();
                Date now = new Date();
                long diff = now.getTime() - foundDate.getTime();
                if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 7){
                    proxyRepository.delete(proxy);
                    ProxyProvider proxyProvider = proxyProviderRepository.findByObjById(proxy.getId());
                    proxyProvider.setNumberOfRecords(proxyProvider.getNumberOfRecords() - 1);
                    proxyProviderRepository.save(proxyProvider);
                    testURL.setValidationDetails("Removed Proxy: " + proxy.getIp());
                    log.info("Removed Proxy: " + proxy.getIp());
                } else {
                    proxy.setLastBasicFunctionalityTest(null);
                    testURL.setValidationDetails(exp.getMessage());
                }

                proxy.setTestURL(proxy.getProvider().getBaseAddress());
                testURLRepository.save(testURL);
                proxyRepository.save(proxy);
                log.info("Invalid: " + exp.getMessage());
            }
        }

    }
}
