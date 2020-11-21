package controllers;

import models.Person;
import models.LoginRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.libs.Json.toJson;
public class LoginController extends Controller {

    private final FormFactory formFactory;
    private final LoginRepository personRepository;
    private final HttpExecutionContext ec;

    @Inject
    public LoginController(FormFactory formFactory, LoginRepository personRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.personRepository = personRepository;
        this.ec = ec;
    }

    public Result index(final Http.Request request) {
        return ok(views.html.index.render(request));
    }
    public Result login(final Http.Request request) {
        return ok(views.html.login.render(request));
    }
    public CompletionStage<Result> registerUser(final Http.Request request) {
        Person person = formFactory.form(Person.class).bindFromRequest(request).get();
        return personRepository
                .register(person)
                .thenApplyAsync((Person p) -> {
                    if (p.id!=null)
                        return ok("successfuly registered").as("text/html");
                    else
                        return ok("registration failed").as("text/html");
                }, ec.current());
    }

    public CompletionStage<Result> loginUser(final Http.Request request) {
        Person person = formFactory.form(Person.class).bindFromRequest(request).get();
        return personRepository
                .login(person)
                .thenApplyAsync((Boolean result) -> {
                    if (result)
                         return ok("successfuly logged in").as("text/html");
                    else
                    return ok("credentials invalid").as("text/html");
                }, ec.current());
    }

}
