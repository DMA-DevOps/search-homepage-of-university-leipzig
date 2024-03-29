# Backend of the Searchengine for University Leipzig

*The backend of the search engine with a small thymeleaf ui.

## Start It

You could start the backend by leveraging the command:
* `mvn clean install spring-boot:run`
  * For an arbitrary operating system will this command compile the project, execute all unit tests and starting the jar after that (with the [example-index](../example_indices/lips_informatik_uni_leipzig)).

Then you could point your browser to `localhost:8080/api/v1/search` to see the debug user interface of the search engine.
This debug user interface has all functionalities of the production ui, but it is rendered in a more rudimentary fashion using a small thymeleaf template.
If you use this setup solely log-messages are not published to kafka, but to the console.

## Onboarding

If you want to work on the project you find some important tools that are used for the development here.
You dont need a deep understanding of them (basically you should be fine if you have a rough idea what they do).

### General Stuff

* [Maven](https://maven.apache.org/) as build tool
* [Spring Boot](https://projects.spring.io/spring-boot/)
  * Dependency injection
  * Embedded Tomcat
* [Spring MVC](https://spring.io/guides/gs/serving-web-content/)
  * Web-Services
  * Views with [Thymeleaf](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)
* [Spring HATEOAS](http://projects.spring.io/spring-hateoas/) to create links between services
* [Lucene](https://lucene.apache.org/core/) to index and search the documents

### Development Practices

* [Project Lombok](https://projectlombok.org/) to reduce boilerplate code
* [Aspect Oriented Programming](https://en.wikipedia.org/wiki/Aspect-oriented_programming)

### Testing

* [JUnit](http://junit.org/junit4/) + [mockito](http://site.mockito.org/)
* [Approval Tests](https://github.com/approvals/ApprovalTests.Java)
