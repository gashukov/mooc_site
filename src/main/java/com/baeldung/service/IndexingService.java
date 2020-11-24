package com.baeldung.service;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class IndexingService {

    private final EntityManager em;

    public IndexingService(EntityManager em) {
        this.em = em;
    }


    @Transactional
    public void initiateIndexing() {
        System.out.println("Indexing!");
        try {
            FullTextEntityManager fullTextEntityManager =
                    Search.getFullTextEntityManager(em);
            fullTextEntityManager.getSearchFactory().getStatistics().setStatisticsEnabled(true);
            fullTextEntityManager.createIndexer().startAndWait();

            for (String id :fullTextEntityManager.getSearchFactory().getStatistics().getIndexedClassNames()) {
                System.out.println("indexed class " + id);
            }
            for (Map.Entry<String, Integer> kv : fullTextEntityManager.getSearchFactory().getStatistics().indexedEntitiesCount().entrySet()) {
                System.out.println("indexed entities " + kv.getKey() + " " + kv.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Indexing ends");
    }
}
