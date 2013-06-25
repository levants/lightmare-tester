package org.lightmare.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.lightmare.entities.Person;
import org.lightmare.rest.providers.ObjectMapperProvider;
import org.lightmare.utils.PersonUtils;

/**
 * REST client class
 * 
 * @author levan
 * 
 */
public class RestClient {

    private static final String REST_URL = "http://localhost:8080/rest/lightmare/";

    private static final String REST_URL_LIST = "list";

    private static final String QUERY_LIST_LAST = "last";

    private static final String QUERY_LIST_FIRST = "first";

    private static final String QUERY_PERSON = "person";

    public static void put() {

	try {
	    Person person = PersonUtils.createPersonToAdd();
	    ClientConfig config = new ClientConfig();
	    config.register(ObjectMapperProvider.class);
	    config.register(JacksonFeature.class);
	    Client client = ClientBuilder.newClient(config);
	    WebTarget webTarget = client.target(REST_URL).queryParam(
		    QUERY_PERSON, person);
	    Invocation.Builder builder = webTarget
		    .request(MediaType.APPLICATION_JSON_TYPE);
	    Invocation invocation = builder.buildPut(Entity.entity(person,
		    MediaType.APPLICATION_JSON_TYPE));
	    Response response = invocation.invoke();
	    System.out.println(response.getStatus());
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    public static void getList() {

	try {
	    ClientConfig config = new ClientConfig();
	    config.register(ObjectMapperProvider.class);
	    config.register(JacksonFeature.class);
	    Client client = ClientBuilder.newClient(config);
	    WebTarget webTarget = client.target(REST_URL).path(REST_URL_LIST)
		    .queryParam(QUERY_LIST_LAST, "last")
		    .queryParam(QUERY_LIST_FIRST, "first");
	    Invocation.Builder builder = webTarget
		    .request(MediaType.APPLICATION_JSON_TYPE);
	    Invocation invocation = builder.buildGet();
	    Response response = invocation.invoke();
	    @SuppressWarnings("unchecked")
	    List<Person> persons = (List<Person>) response
		    .readEntity(List.class);
	    System.out.println(response.getStatus());
	    System.out.println(persons);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    public static void main(String[] args) {

	RestClient.getList();
	RestClient.put();
    }
}
