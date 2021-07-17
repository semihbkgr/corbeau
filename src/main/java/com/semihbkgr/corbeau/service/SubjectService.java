package com.semihbkgr.corbeau.service;

import com.semihbkgr.corbeau.exception.IllegalValueException;
import com.semihbkgr.corbeau.model.Subject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SubjectService {

    Flux<Subject> findAll();

    Mono<Subject> save(Subject subject);

    Mono<Subject> update(int id,Subject subject) throws IllegalValueException;

    Mono<Void> delete(int id) throws IllegalValueException;

}