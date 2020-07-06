package com.proxyprovider.controllers;

import com.proxyprovider.entities.Country;
import com.proxyprovider.entities.ProxyProvider;
import com.proxyprovider.repositories.*;
import com.proxyprovider.schedulers.ProxyListFetchTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

import org.xml.sax.InputSource;


/**
 * Created by Abro on 6/20/2020.
 */
@Controller
public class MainController {
    @Autowired
    private ProxyRepository proxyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private AnonymityRepository anonymityRepository;

    @Autowired
    private TestURLRepository testURLRepository;

    @Autowired
    private ProxyProviderRepository proxyProviderRepository;

    private static final Logger log = LoggerFactory.getLogger(ProxyListFetchTask.class);

    @GetMapping(value = "/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("pages/index");
        Iterable<Country> countryIterator = countryRepository.findAll();
        modelAndView.addObject("anonymityList", anonymityRepository.findAll());
        modelAndView.addObject("countryList", countryIterator);
        modelAndView.addObject("portList", proxyRepository.findAllByDistinct());
        return modelAndView;
    }

    @GetMapping(value = "/proxies")
    @ResponseBody
    public Map<String, Object> proxies(
            @RequestParam(value = "country", required = false) Integer country,
            @RequestParam(value = "portNumber", required = false) Integer portNumber,
            @RequestParam(value = "anonymity", required = false) Integer anonymity,
            @RequestParam(value = "proxyType", required = false) String proxyType){
        Map<String, Object> objectMap = new HashMap<>();
        try {
             objectMap.put("proxyList", proxyRepository.findByParameters(country, portNumber, anonymity, proxyType));
             objectMap.put("status", 200);
             objectMap.put("message", "Successfully retrieved data!");
        }catch (Exception exception){
            exception.printStackTrace();
            objectMap.put("status", 500);
            objectMap.put("message", exception.getMessage());
        }

        return objectMap;
    }

    @GetMapping(value = "/test-urls")
    public ModelAndView testURLs(){
        ModelAndView modelAndView = new ModelAndView("pages/testUrl");
        return modelAndView;
    }

    @GetMapping(value = "/test-url-list")
    @ResponseBody
    public Map<String, Object> urlList(){
        Map<String, Object> objectMap = new HashMap<>();
        try {
            objectMap.put("testList", testURLRepository.findAll());
            objectMap.put("status", 200);
            objectMap.put("message", "Successfully retrieved data!");
        }catch (Exception exception){
            exception.printStackTrace();
            objectMap.put("status", 500);
            objectMap.put("message", exception.getMessage());
        }
        return objectMap;
    }

    @GetMapping(value = "/list-providers")
    public ModelAndView listProvider(){
        ModelAndView modelAndView = new ModelAndView("pages/listProvider");
        return modelAndView;
    }

    @GetMapping(value = "/list-providers-list")
    @ResponseBody
    public Map<String, Object> listProviderList(){
        Map<String, Object> objectMap = new HashMap<>();
        try {
            objectMap.put("providerList", proxyProviderRepository.findAll());
            objectMap.put("status", 200);
            objectMap.put("message", "Successfully retrieved data!");
        }catch (Exception exception){
            exception.printStackTrace();
            objectMap.put("status", 500);
            objectMap.put("message", exception.getMessage());
        }
        return objectMap;
    }

    @GetMapping(value = "/about-us")
    public ModelAndView aboutUs(){
        ModelAndView modelAndView = new ModelAndView("pages/about-us");
        return modelAndView;
    }
}