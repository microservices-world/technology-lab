package org.ms.webfluxdemo.repository;

import org.ms.webfluxdemo.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author Zhenglai
 * @since 2019-04-09 01:19
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Flux<User> findByAgeBetween(int start, int end);

    @Query("{'age':{'$gte': 0, '$lte': 2}}")
    Flux<User> oldUsers();
}
