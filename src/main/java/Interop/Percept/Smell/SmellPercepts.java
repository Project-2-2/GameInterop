package Interop.Percept.Smell;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a set of smell percepts.
 */
public final class SmellPercepts {

    private Set<SmellPercept> smellPercepts;

    public SmellPercepts(Set<SmellPercept> smellPercepts) {
        // the perception of agents must not be modified after construction!
        this.smellPercepts = Collections.unmodifiableSet(smellPercepts);
    }

    public Set<SmellPercept> getAll() {
        return smellPercepts;
    }

    /**
     * Allows to select smells that fulfil a given predicate.
     *
     * @param predicate Receives a smell percept; Decides whether to include the percept in the return set.
     *                  The return value of true allows to include the percept.
     *                  The return value of false allows to exclude the percept.
     *
     * @return The new set of smell percepts filtered out by the result of evaluating the given predicate.
     */
    public SmellPercepts filter(Predicate<? super SmellPercept> predicate) {
        return new SmellPercepts(smellPercepts.stream().filter(predicate).collect(Collectors.toSet()));
    }

}
