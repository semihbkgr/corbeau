package com.semihbkgr.corbeau.service;

import com.semihbkgr.corbeau.model.Moderator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ModeratorService {

    Mono<Moderator> save(Moderator moderator);

    Mono<Moderator> findByName(String name);

    Flux<Moderator> findAll();

}
