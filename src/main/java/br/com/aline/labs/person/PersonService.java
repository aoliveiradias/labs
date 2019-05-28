package br.com.aline.labs.person;

import br.com.aline.labs.facebook.FacebookClient;
import br.com.aline.labs.facebook.UserFacebookResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PersonService {

    private Logger LOG = LoggerFactory.getLogger(PersonService.class);
    private PersonRepository personRepository;

    private FacebookClient facebookClient;

    @Value("${facebook.access.token}")
    private String facebookAccessToken;

    PersonService(PersonRepository personRepository, FacebookClient facebookClient) {
        this.personRepository = personRepository;
        this.facebookClient = facebookClient;
    }

    Person save(String facebookId) {
        try {
            UserFacebookResponse userFacebookResponse = facebookClient.getUser(facebookId, facebookAccessToken);
            Person person = personRepository.save(new Person(userFacebookResponse.getId(), userFacebookResponse.getName()));
            return person;
        } catch (FeignException feignException) {
            LOG.info("Feign error status: {}", feignException.status());
            throw feignException;
        }
    }

    List<Person> findAllWithLimit(Integer limit) {
        return personRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    void delete(String facebookId) {
        personRepository.deleteById(facebookId);
    }

    Person findById(String id) {
        return personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    }
}
