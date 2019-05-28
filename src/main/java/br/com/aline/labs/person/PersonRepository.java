package br.com.aline.labs.person;

import org.springframework.data.mongodb.repository.MongoRepository;

interface PersonRepository extends MongoRepository<Person, String> {
}
