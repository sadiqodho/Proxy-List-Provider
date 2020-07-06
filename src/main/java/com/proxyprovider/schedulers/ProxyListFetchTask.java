package com.proxyprovider.schedulers;

import com.proxyprovider.entities.Anonymity;
import com.proxyprovider.entities.Country;
import com.proxyprovider.entities.ProxyProvider;
import com.proxyprovider.repositories.AnonymityRepository;
import com.proxyprovider.repositories.CountryRepository;
import com.proxyprovider.repositories.ProxyProviderRepository;
import com.proxyprovider.repositories.ProxyRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Abro on 6/20/2020.
 */
@Component
public class ProxyListFetchTask {
    private static final Logger log = LoggerFactory.getLogger(ProxyListFetchTask.class);

    @Autowired
    private ProxyRepository proxyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ProxyProviderRepository proxyProviderRepository;

    @Autowired
    private AnonymityRepository anonymityRepository;

    // 1 Orbit API # https://proxy-orbit1.p.rapidapi.com/v1/
    // Every 10 Minutes
    //@Scheduled(fixedRate = (1000*60*10))
    public void orbitAPITask() {

        ProxyProvider proxyProvider = proxyProviderRepository.findByObjById(1);
        // Provider
        proxyProvider.setLastAttempt(new Date());

        int counter = 0;
        while (counter < 20){
            try {
                TimeUnit.SECONDS.sleep(5);
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://proxy-orbit1.p.rapidapi.com/v1/")
                            .get()
                            // Need to be updated according to key and host
                            .addHeader("x-rapidapi-host", "your host")
                            .addHeader("x-rapidapi-key", "your key")
                            .build();

                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();

                    JSONObject jsonObject = new JSONObject(jsonData);
                    com.proxyprovider.entities.Proxy proxy = proxyRepository.findByIp(jsonObject.getString("ip"));
                    if(proxy == null){
                        proxy = new com.proxyprovider.entities.Proxy();
                        proxy.setFirstFound(new Date());
                        proxyProvider.setNumberOfRecords(proxyProvider.getNumberOfRecords() + 1);
                    }

                    // Set Anonymity
                    if(jsonObject.getBoolean("anonymous")){
                        proxy.setAnonymity(anonymityRepository.findWithId(2));
                    }

                    // Country relation save
                    if(jsonObject.has("location")){
                        Country country = countryRepository.findByAlpha2Code(jsonObject.getString("location"));
                        proxy.setCountry(country);
                    }

                    // Provider relation here
                    proxy.setProvider(proxyProvider);
                    proxy.setIp(jsonObject.getString("ip"));
                    if(jsonObject.has("port")){
                        proxy.setPort(jsonObject.getInt("port"));
                    }
                    if(jsonObject.has("protocol")){
                        proxy.setProtocol(jsonObject.getString("protocol"));
                    }
                    proxy.setLastFound(new Date());
                    proxyRepository.save(proxy);

                    log.info("********* Orbit API Task *********");
                    log.info("Fetched IP address: " + proxy.getIp());
                    log.info("**********************************");

                    proxyProvider.setLastSuccessfull(new Date());
                }catch (Exception exception){
                    exception.getMessage();
                    proxyProvider.setErrorIndication(exception.getMessage());
                }

                counter++;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

        }

        proxyProviderRepository.save(proxyProvider);

    }

    // 2 Proxy 11 # http://proxy11.com
//    @Scheduled(fixedRate = (1000*60*10))
    public void getOroxy11() {

        // Provider relation here
        ProxyProvider proxyProvider = proxyProviderRepository.findByObjById(2);

        try{

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // Need to be updated your key
                    .url("https://proxy11.com/api/proxy.json?key=your_key").get().build();
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();

            JSONObject jsonObject1 = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject1.getJSONArray("data");

            //Add length to provider
            proxyProvider.setLastAttempt(new Date());

            for(int i=0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                com.proxyprovider.entities.Proxy proxy = proxyRepository.findByIp(jsonObject.getString("ip"));
                if(proxy == null){
                    proxy = new com.proxyprovider.entities.Proxy();
                    proxy.setFirstFound(new Date());
                    proxyProvider.setNumberOfRecords(proxyProvider.getNumberOfRecords() + 1);
                }

                proxy.setIp(jsonObject.getString("ip"));
                // Country relation save
                Country country = countryRepository.findByAlpha2Code(jsonObject.getString("country_code"));
                proxy.setCountry(country);
                proxy.setPort(jsonObject.getInt("port"));

                Anonymity anonymity = null;

                switch (jsonObject.getInt("type")){
                    case 0:
                        anonymity = anonymityRepository.findWithId(2);
                        break;

                    case 1:
                        anonymity = anonymityRepository.findWithId(1);
                        break;

                    case 2:
                        anonymity = anonymityRepository.findWithId(3);
                        break;
                }

                proxy.setAnonymity(anonymity);
                proxy.setSpeed(jsonObject.getDouble("time"));
                proxy.setProvider(proxyProvider);
                proxy.setLastFound(new Date());
                proxyRepository.save(proxy);
            }

            proxyProvider.setLastSuccessfull(new Date());

            log.info("********* Proxy 11 *********");
            log.info("Fetched IP address count: " + jsonArray.length());
            log.info("**********************************");

        }catch (Exception exception){
            exception.printStackTrace();
            proxyProvider.setErrorIndication(exception.getMessage());
        }

        proxyProviderRepository.save(proxyProvider);

    }

    // 3 https://rapidapi.com/ugproxy/api/ugproxy
//    @Scheduled(fixedRate = (1000*60*10))
    public void freeProxy(){

        // Provider relation here
        ProxyProvider proxyProvider = proxyProviderRepository.findByObjById(3);
        proxyProvider.setLastAttempt(new Date());
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://free-proxy.p.rapidapi.com/proxy/")
                    .get()
                    // Need to be updated according to key and host
                    .addHeader("x-rapidapi-host", "your host")
                    .addHeader("x-rapidapi-key", "your key")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for(int i=0; i<jsonArray.length(); i++){
                String ob1 = jsonArray.getString(i);
                String values[] = ob1.split(":");
                com.proxyprovider.entities.Proxy proxy = proxyRepository.findByIp(values[0]);

                if(proxy == null){
                    proxy = new com.proxyprovider.entities.Proxy();
                    proxy.setFirstFound(new Date());
                    proxyProvider.setNumberOfRecords(proxyProvider.getNumberOfRecords() + 1);
                }

                proxy.setIp(values[0]);
                proxy.setPort(Integer.valueOf(values[1]));
                proxy.setLastFound(new Date());
                proxyRepository.save(proxy);
            }

            proxyProvider.setLastSuccessfull(new Date());
            log.info("********* Proxy 11 *********");
            log.info("Fetched IP address count: " + jsonArray.length());
            log.info("**********************************");

        }catch (Exception ex){
            ex.printStackTrace();
            proxyProvider.setErrorIndication(ex.getMessage());
        }

        proxyProviderRepository.save(proxyProvider);
    }

    // 4 http://pubproxy.com/api/proxy
//    @Scheduled(fixedRate = (1000*60*10))
    public void getPubproxy(){

        ProxyProvider proxyProvider = proxyProviderRepository.findByObjById(4);
        proxyProvider.setLastAttempt(new Date());

        int counter = 0;
        while (counter < 20) {
            try {
                TimeUnit.SECONDS.sleep(5);


                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url("http://pubproxy.com/api/proxy").get().build();
                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();

                    JSONObject jsonObject1 = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        com.proxyprovider.entities.Proxy proxy = proxyRepository.findByIp(jsonObject.getString("ip"));
                        if (proxy == null) {
                            proxy = new com.proxyprovider.entities.Proxy();
                            proxy.setFirstFound(new Date());
                            proxyProvider.setNumberOfRecords(proxyProvider.getNumberOfRecords() + 1);
                        }

                        proxy.setIp(jsonObject.getString("ip"));
                        // Country relation save
                        Country country = countryRepository.findByAlpha2Code(jsonObject.getString("country"));
                        proxy.setCountry(country);
                        proxy.setPort(jsonObject.getInt("port"));

                        Anonymity anonymity = null;
                        if (jsonObject.has("proxy_level")) {
                            switch (jsonObject.getString("proxy_level")) {
                                case "anonymous":
                                    anonymity = anonymityRepository.findWithId(2);
                                    break;

                                case "elite":
                                    anonymity = anonymityRepository.findWithId(1);
                                    break;

                                case "transparent":
                                    anonymity = anonymityRepository.findWithId(3);
                                    break;
                            }
                            proxy.setAnonymity(anonymity);
                        }
                        proxy.setSpeed(jsonObject.getDouble("speed"));
                        proxy.setProtocol(jsonObject.getString("type"));
                        proxy.setLastFound(new Date());

                        // Provider relation here
                        proxy.setProvider(proxyProvider);
                        proxyRepository.save(proxy);

                        log.info("********* pubproxy *********");
                        log.info("Fetched IP address: " + proxy.getIp());
                        log.info("**********************************");

                    }

                    proxyProvider.setLastSuccessfull(new Date());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    proxyProvider.setErrorIndication(ex.getMessage());
                }


                counter++;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        proxyProviderRepository.save(proxyProvider);

    }

    // 5 https://api.getproxylist.com/proxy
//    @Scheduled(fixedRate = (1000*60*10))
    public void getProxylist(){

        // Provider relation here
        ProxyProvider proxyProvider = proxyProviderRepository.findByObjById(5);
        proxyProvider.setLastAttempt(new Date());
        JSONObject jsonObject = null;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://proxy-ip-list.p.rapidapi.com/devtoolservice/ipagency")
                    .get()
                    // update your host and key
                    .addHeader("x-rapidapi-host", "your host")
                    .addHeader("x-rapidapi-key", "your key")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();

            jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for(int i=0; i<jsonArray.length(); i++){
                String ob1 = jsonArray.getString(i);
                String values[] = ob1.split("://");
                String values2[] = values[1].split(":");
                com.proxyprovider.entities.Proxy proxy = proxyRepository.findByIp(values2[0]);

                if(proxy == null){
                    proxy = new com.proxyprovider.entities.Proxy();
                    proxy.setFirstFound(new Date());
                    proxyProvider.setNumberOfRecords(proxyProvider.getNumberOfRecords() + 1);
                }

                proxy.setIp(values2[0]);
                proxy.setPort(Integer.valueOf(values2[1]));
                proxy.setProtocol(values[0]);
                proxy.setLastFound(new Date());
                proxyRepository.save(proxy);
            }

            proxyProvider.setLastSuccessfull(new Date());
            log.info("********* Proxy-ip-list *********");
            log.info("Fetched IP address count: " + jsonArray.length());
            log.info("**********************************");


        }catch (Exception ex){
            ex.printStackTrace();
            proxyProvider.setErrorIndication(jsonObject.getString("message"));
        }

        proxyProviderRepository.save(proxyProvider);
    }
}
