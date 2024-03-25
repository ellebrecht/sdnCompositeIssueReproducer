package com.ellebrecht.sdnCompositeIssueReproducer;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class SdnCompositeIssueReproducerApplication implements CommandLineRunner {

    @Autowired
    private Repo repo;

    public static void main(String[] args) {
        log.info("Starting");
        SpringApplication.run(SdnCompositeIssueReproducerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Saving");
        repo.save(Entity.builder().name("test1").composite(Map.of("c1", true, "c2", 1)).build());
        repo.save(Entity.builder().name("test2").composite(Map.of("c1", true, "c2", 2)).build());
        repo.save(Entity.builder().name("test3").composite(Map.of("c1", false, "c2", 3)).build());
        log.info("Finding");
        List<Entity> found = repo.findAll(Sort.by("composite.c2"));
        log.info("Found {}", found);
    }

}

@Node
@Data
@Builder
class Entity {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String uuid;

    private String name;

    @CompositeProperty
    private Map<String, Object> composite;

}

@Repository
interface Repo extends Neo4jRepository<Entity, String> {
}
