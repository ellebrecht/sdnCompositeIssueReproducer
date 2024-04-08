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
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
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
        repo.save(Entity.builder().name("test0").composite(Map.of("c1", false, "c2", 4)).build());
        repo.save(Entity.builder().name("test1").composite(Map.of("c1", true, "c2", 1)).build());
        repo.save(Entity.builder().name("test2").composite(Map.of("c1", true, "c2", 2)).build());
        repo.save(Entity.builder().name("test3").composite(Map.of("c1", false, "c2", 3)).build());
        repo.save(Entity.builder().name("test4").composite(Map.of("c1", true, "c2", 3)).build());
        repo.save(Entity.builder().name("test5").composite(Map.of("c1", true, "c2", 2)).build());
        repo.save(Entity.builder().name("test6").composite(Map.of("c1", false, "c2", 1)).build());
        log.info("Finding");
        List<Entity> found = repo.findAll(Sort.by("composite.c2", "name"));
        log.info("Found {}", found);
        List<String> expectedNamesOrder = found.stream()
            .sorted(
                Comparator.comparing((Entity e) -> Integer.valueOf(e.getComposite().get("c2").toString()))
                    .thenComparing(Entity::getName)
            )
            .map(Entity::getName)
            .toList();
        List<String> actualNamesOrder = found.stream()
            .map(Entity::getName)
            .toList();
        if (!actualNamesOrder.equals(expectedNamesOrder)) {
            log.error("Actual entity list order does not match expected {}", expectedNamesOrder);
            found.forEach(e -> log.info("    id:{} name:{} c2:{}", e.getId(), e.getName(), e.getComposite().get("c2")));
        }
    }

}

@Node
@Data
@Builder
class Entity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @CompositeProperty
    private Map<String, Object> composite;

}

@Repository
interface Repo extends Neo4jRepository<Entity, Long> {
}
