package com.example.springjwt.service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.util.JSON;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonArray;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@PropertySource("classpath:application.yaml")
@Transactional
public class MainService {

    private final MongoTemplate mongoTemplate;
    @Value("${path_to_json}")
    private String PATH_TO_JSON;

    @Value("${fieldName1}")
    private String fieldName1;
    @Value("${fieldName2}")
    private String fieldName2;
    @Value("${key1}")
    private String key1;
    @Value("${key2}")
    private String key2;
    @Value("${collectionName}")
    private String collectionName;

    private Map<String, List<Document>> cache = new ConcurrentHashMap<>();

    public MainService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Document> getSalesAndTrafficByAsin() {
        if (cache.containsKey(fieldName1)) return cache.get(fieldName1);
        List<Document> documents = new ArrayList<>();
        DistinctIterable<Document> findIterable = mongoTemplate.getCollection(collectionName)
                .distinct(fieldName1, Document.class);
        for (Document doc : findIterable) documents.add(doc);
        cache.put(fieldName1, documents);
        return documents;
    }

    public Document getSalesAndTrafficByAsinParent(String parentAsin) {
        if (cache.containsKey(key1)) return cache.get(key1).get(0);
        Document document = new Document();
        DistinctIterable<Document> findIterable = mongoTemplate.getCollection(collectionName)
                .distinct(fieldName1, Document.class);
        for (Document doc : findIterable) {
            if (doc.getString(key1).equals(parentAsin)) document = doc;
        }
        cache.put(key1, List.of(document));
        return document;
    }

    public Document getSalesAndTrafficByDateForDate(String date) {
        if (cache.containsKey(key2)) return cache.get(key2).get(0);
        Document document = new Document();
        DistinctIterable<Document> findIterable = mongoTemplate.getCollection(collectionName)
                .distinct(fieldName2, Document.class);
        for (Document doc : findIterable) {
            if (doc.getString(key2).equals(date)) document = doc;
        }
        cache.put(key2, List.of(document));
        return document;
    }

    public List<Document> getSalesAndTrafficByDate() {
        if (cache.containsKey(fieldName2)) return cache.get(fieldName2);
        List<Document> documents = new ArrayList<>();
        DistinctIterable<Document> findIterable = mongoTemplate.getCollection(collectionName)
                .distinct(fieldName2, Document.class);
        for (Document doc : findIterable) documents.add(doc);
        cache.put(fieldName2, documents);
        return documents;
    }

    public void uploadTestReport() {
        BufferedReader reader;
        String lines;
        try {
            reader = new BufferedReader(new FileReader(PATH_TO_JSON));
            StringBuilder json = new StringBuilder();
            while ((lines = reader.readLine()) != null) {
                json.append(lines);
            }
            DBObject dbObject = BasicDBObject.parse(json.toString());
            mongoTemplate.dropCollection(collectionName);
            mongoTemplate.insert(dbObject, collectionName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetCache() {
        cache = new ConcurrentHashMap<>();
    }
}
