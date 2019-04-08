package org.ms.webfluxdemo.repository;

import org.ms.webfluxdemo.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zhenglai
 * @since 2019-04-09 01:19
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {}
