This is a proposition of a common API that will allow to implement agents independently.

The main interface of an agent is very simple:
```$java
/**
 * The common interface for agents.
 */
public interface Agent {

    /**
     * In order to decide an action the implementing agent receives percepts.
     *
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return The action that the agent decides on taking.
     */
    Action getAction(Percepts percepts);

}
```

This API makes heavy use of concepts of:
- Immutability: https://en.wikipedia.org/wiki/Immutable_object
- Value Object / Data Transfer Object:
    - https://en.wikipedia.org/wiki/Value_object
    - https://www.martinfowler.com/bliki/ValueObject.html
    - https://en.wikipedia.org/wiki/Data_transfer_object
    - https://www.martinfowler.com/eaaCatalog/dataTransferObject.html

The value objects allow to create very clean interface for agent, but at the same time allow to precisely define what agents can perceive and do.

Please, inspect the package `Percepts`. Especially the `Percepts.Vision` has very advanced implementation.

The actions still need to be defined.

I have also included very simple implementation of an automated tests.

The idea is very simple:
```$java
    public static void main(String[] args) {

        it("should do this (explanation of what you are testing)", () -> {
            boolean testCondition = true;
            assertTrue(testCondition, "explanation of your assertion");
        });
    
    }
```

You can see how that works in practice in `src/test/java/PerceptsTest.java PerceptsTest#main`.
