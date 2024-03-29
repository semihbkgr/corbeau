package com.semihbkgr.corbeau.repository;

import com.semihbkgr.corbeau.model.Subject;
import com.semihbkgr.corbeau.model.projection.SubjectDeep;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubjectRepository {

    String TABLE_NAME = "subjects";

    Mono<Subject> save(Subject subject);

    Mono<Subject> update(Subject subject);

    Mono<Subject> findById(int id);

    Mono<SubjectDeep> findByNameDeep(String name);

    Flux<Subject> findAll();

    Flux<SubjectDeep> findAllDeep();

    Mono<Void> deleteById(int id);

}

