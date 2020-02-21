package Interop.Percept.Vision;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents perception of a set of objects.
 */
public final class ObjectPercepts {

    private Set<ObjectPercept> objectPercepts;

    public ObjectPercepts(Set<ObjectPercept> objectPercepts) {
        // the perception of agents must not be modified after construction!
        this.objectPercepts = Collections.unmodifiableSet(objectPercepts);
    }

    public Set<ObjectPercept> getAll() {
        return objectPercepts;
    }

    ObjectPercepts getInFieldOfView(FieldOfView fieldOfView) {
        return filter((ObjectPercept percept) -> {
            return fieldOfView.isInView(percept.getPoint());
        });
    }

    ObjectPercepts getNotInFieldOfView(FieldOfView fieldOfView) {
        return filter((ObjectPercept percept) -> {
            return !fieldOfView.isInView(percept.getPoint());
        });
    }

    /**
     * Allows to select objects that fulfil a given predicate.
     *
     * @param predicate Receives an object percept; Decides whether to include the percept in the return set.
     *                  The return value of true allows to include the percept.
     *                  The return value of false allows to exclude the percept.
     *
     * @return The new set of object percepts filtered out by the result of evaluating the given predicate.
     */
    public ObjectPercepts filter(Predicate<? super ObjectPercept> predicate) {
        return new ObjectPercepts(objectPercepts.stream().filter(predicate).collect(Collectors.toSet()));
    }

    public String toString() {
        return objectPercepts.toString();
    }
}
