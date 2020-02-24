This is the common API that will allow to implement agents independently.

**Important!**
You are allowed to make changes **only** in your group directories:
- `src/main/java/GroupX` (here goes the main code of group X)
- `src/test/java/GroupX` (here go the tests of group X)


The main interfaces of an intruder and guard are very simple:
```$java
/**
 * The interface of an intruder.
 *
 * You need to implement this interface in order to allow your agent play a role of an intruder.
 * This interface limits your actions to the actions allowed by intruders.
 */
public interface Intruder {

    /**
     * In order to decide an action the implementing agent receives percepts.
     *
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return The action that the agent decides on taking.
     */
    IntruderAction getAction(IntruderPercepts percepts);

}
```
```$java
/**
 * The interface of a guard.
 *
 * You need to implement this interface in order to allow your agent play a role of a guard.
 * This interface limits your actions to the actions allowed by guards.
 */
public interface Guard {

    /**
     * In order to decide an action the implementing agent receives percepts.
     *
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return The action that the agent decides on taking.
     */
    GuardAction getAction(GuardPercepts percepts);

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
Feel free to use the adaptor pattern to adjust the API to your liking inside your own code. Be warned that extending `Interop` classes is not allowed, and that they are made `final` intentionally in order to guarantee interoperability.

Please, inspect the packages:
 - `Interop.Agent`
 - `Interop.Percepts`
 - `Interop.Action`

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

You can see how that works in practice e.g. in `src/test/java/Interop/Percept/PerceptsTest.java` `PerceptsTest#main`.
