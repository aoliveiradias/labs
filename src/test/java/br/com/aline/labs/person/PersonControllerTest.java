package br.com.aline.labs.person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import br.com.aline.labs.facebook.FacebookClient;
import br.com.aline.labs.facebook.UserFacebookResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.FeignException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private PersonRepository personRepository;

    @MockBean
    private FacebookClient facebookClient;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        personRepository.deleteAll();
    }

    @Test
    public void shouldGetAllPersonWithLimitWithSuccess() throws Exception {
        CollectionType typeReference =
                TypeFactory.defaultInstance().constructCollectionType(List.class, PersonGetResponseDto.class);

        Person alinePerson = createPerson("Aline");
        Person devPerson = createPerson("Dev");
        String limit = "5";

        String resultString = mvc.perform(get("/person?limit=" + limit)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PersonGetResponseDto> personGetResponseDtos = objectMapper.readValue(resultString, typeReference);
        Assert.assertEquals(2, personGetResponseDtos.size());
        Assert.assertEquals(alinePerson.getName(), personGetResponseDtos.get(0).getName());
        Assert.assertEquals(alinePerson.getFacebookId(), personGetResponseDtos.get(0).getFacebookId());
        Assert.assertEquals(devPerson.getName(), personGetResponseDtos.get(1).getName());
        Assert.assertEquals(devPerson.getFacebookId(), personGetResponseDtos.get(1).getFacebookId());
    }

    @Test
    public void shouldReturnOneResultWhenGetAllPersonWithLimitOne() throws Exception {
        CollectionType typeReference =
                TypeFactory.defaultInstance().constructCollectionType(List.class, PersonGetResponseDto.class);

        Person alinePerson = createPerson("Aline");
        createPerson("Dev");
        String limit = "1";

        String resultString = mvc.perform(get("/person?limit=" + limit)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PersonGetResponseDto> personGetResponseDtos = objectMapper.readValue(resultString, typeReference);
        Assert.assertEquals(1, personGetResponseDtos.size());
        Assert.assertEquals(alinePerson.getName(), personGetResponseDtos.get(0).getName());
        Assert.assertEquals(alinePerson.getFacebookId(), personGetResponseDtos.get(0).getFacebookId());
    }

    @Test
    public void shouldReturnBadRequestWhenLimitIs0OnGetAllPersonWithLimit() throws Exception {
        createPerson("Aline");
        String limit = "0";

        mvc.perform(get("/person?limit=" + limit)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        Assert.assertEquals(1, personList.size());

    }

    @Test
    public void shouldReturnNotContentWhenDeleteUser() throws Exception {
        Person alinePerson = createPerson("Aline");

        List<Person> personList = personRepository.findAll();
        Assert.assertEquals(1, personList.size());

        mvc.perform(delete("/person/" + alinePerson.getFacebookId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        personList = personRepository.findAll();
        Assert.assertEquals(0, personList.size());
    }


    @Test
    public void shouldCreatePersonWithSuccess() throws Exception {
        String uuid = UUID.randomUUID().toString();

        UserFacebookResponse userFacebookResponse = new UserFacebookResponse(uuid, "Dev");
        Mockito.when(facebookClient.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(userFacebookResponse);

        mvc.perform(MockMvcRequestBuilders.multipart("/person").param("facebookId", uuid))
                .andExpect(status().isCreated());
        Person person = personRepository.findById(uuid).get();
        Assert.assertEquals(userFacebookResponse.getId(), person.getFacebookId());
        Assert.assertEquals(userFacebookResponse.getName(), person.getName());
    }

    @Test
    public void shouldReturn400WhenCreatePersonWithFacebookNotFound() throws Exception {
        String uuid = UUID.randomUUID().toString();
        FeignException feignException = Mockito.mock(FeignException.class);
        Mockito.when(feignException.status()).thenReturn(HttpStatus.BAD_REQUEST.value());
        Mockito.doThrow(feignException).when(facebookClient).getUser(Mockito.anyString(), Mockito.anyString());

        mvc.perform(MockMvcRequestBuilders.multipart("/person").param("facebookId", uuid))
                .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        Assert.assertEquals(0, personList.size());
    }

    @Test
    public void shouldReturn500WhenCreatePersonWithFacebookDoesOtherError() throws Exception {
        String uuid = UUID.randomUUID().toString();
        FeignException feignException = Mockito.mock(FeignException.class);
        Mockito.when(feignException.status()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Mockito.doThrow(feignException).when(facebookClient).getUser(Mockito.anyString(), Mockito.anyString());

        mvc.perform(MockMvcRequestBuilders.multipart("/person").param("facebookId", uuid))
                .andExpect(status().isInternalServerError());

        List<Person> personList = personRepository.findAll();
        Assert.assertEquals(0, personList.size());
    }

    @Test
    public void shouldGetByIdWithSuccess() throws Exception {
        Person alinePerson = createPerson("Aline");

        String resultString = mvc.perform(get("/person/" + alinePerson.getFacebookId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PersonGetResponseDto personGetResponseDto = objectMapper.readValue(resultString, PersonGetResponseDto.class);
        Assert.assertEquals(alinePerson.getName(), personGetResponseDto.getName());
        Assert.assertEquals(alinePerson.getFacebookId(), personGetResponseDto.getFacebookId());
    }

    @Test
    public void shouldReturnNotFoundWhenGetById() throws Exception {
        createPerson("Aline");
        mvc.perform(get("/person/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        List<Person> personList = personRepository.findAll();
        Assert.assertEquals(1, personList.size());

    }

    private Person createPerson(String name) {
        Person person = new Person(UUID.randomUUID().toString(), name);
        personRepository.save(person);
        return person;
    }
}
