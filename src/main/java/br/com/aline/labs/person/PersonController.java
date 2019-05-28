package br.com.aline.labs.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PersonController {

    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);
    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonGetResponseDto>> listWithLimit(@RequestParam int limit) {
        LOG.info("Request to list users with limit: {} ", limit);
        List<Person> personList = personService.findAllWithLimit(limit);
        List<PersonGetResponseDto> personGetResponseDtoList = personList.stream()
                .map(person -> new PersonGetResponseDto(person.getFacebookId(), person.getName()))
                .collect(Collectors.toList());
        LOG.info("List users with limit: {} was completed! Total of users found: {}", limit, personGetResponseDtoList.size());
        return new ResponseEntity<>(personGetResponseDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonGetResponseDto> findById(@PathVariable("id") String id) {
        LOG.info("Request to find user with id: {} ", id);
        Person person = personService.findById(id);
        LOG.info("User: {} found", person.toString());
        return ResponseEntity.ok(new PersonGetResponseDto(person.getFacebookId(), person.getName()));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestParam("facebookId") String facebookId) {
        LOG.info("Request to create person with facebookId: {}", facebookId);
        Person person = personService.save(facebookId);
        LOG.info("User with facebookId: {} was created", person.getFacebookId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(person.getFacebookId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        LOG.info("Request to delete user: {} ", id);
        personService.delete(id);
        LOG.info("User with id: {} was deleted", id);
        return ResponseEntity.noContent().build();
    }
}
