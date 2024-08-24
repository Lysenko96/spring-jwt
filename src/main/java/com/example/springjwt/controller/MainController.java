package com.example.springjwt.controller;

import com.example.springjwt.service.MainService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/salesAndTrafficByAsin")
//    @Cacheable(value = "getSalesAndTrafficByAsin")
    public List<Document> getSalesAndTrafficByAsin(){
        return mainService.getSalesAndTrafficByAsin();
    }

    @GetMapping("/salesAndTrafficByAsin/{parentAsin}")
//    @Cacheable("getSalesAndTrafficByAsinParent")
    public Document getSalesAndTrafficByAsinParent(@PathVariable("parentAsin") String parentAsin){
        return mainService.getSalesAndTrafficByAsinParent(parentAsin);
    }

    @GetMapping("/salesAndTrafficByDate/{date}")
//    @Cacheable("getSalesAndTrafficByDateForDate")
    public Document getSalesAndTrafficByDateForDate(@PathVariable("date") String date){
        return mainService.getSalesAndTrafficByDateForDate(date);
    }

    @GetMapping("/salesAndTrafficByDate")
//    @Cacheable("getSalesAndTrafficByDate")
    public List<Document> getSalesAndTrafficByDate() {
        return mainService.getSalesAndTrafficByDate();
    }

    @GetMapping("/resetCache")
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    @PostConstruct
//    @CacheEvict(cacheNames = {
//            "getSalesAndTrafficByAsin",
//            "getSalesAndTrafficByDate",
//            "getSalesAndTrafficByAsinParent",
//            "getSalesAndTrafficByDateForDate"})
    public void resetCache(){
        mainService.resetCache();
        log.info("Reset cache");
    }

    @GetMapping("/admin")
    public String adminData() {
        return "Admin data";
    }

    @GetMapping("/upload")
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    @PostConstruct
    public void uploadTestReport() {
        mainService.uploadTestReport();
        log.info("Upload collection");
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}
