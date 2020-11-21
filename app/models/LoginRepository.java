package models;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Person;
import play.db.ebean.EbeanConfig;
import play.db.*;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 *
 */
public class LoginRepository {



    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public LoginRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Person> register(Person person) {
        person.id= new Random().nextLong();
        return supplyAsync(() ->  {ebeanServer.save(person);
                return person;}, executionContext);
    }


    public CompletionStage<Boolean> login(Person person) {
        return   supplyAsync(() -> ebeanServer.find(Person.class).findList())
                    .thenApplyAsync(list -> {
                        for (Person c : list) {
                            if(c.name.equals(person.name)&&c.password.equals(person.password))
                                return true;
                        }
                        return false;
                    }, executionContext);
    }

}
