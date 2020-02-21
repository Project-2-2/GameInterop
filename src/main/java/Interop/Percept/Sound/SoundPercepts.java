package Interop.Percept.Sound;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a set of sound percepts.
 */
public final class SoundPercepts {

    private Set<SoundPercept> soundPercepts;

    public SoundPercepts(Set<SoundPercept> soundPercepts) {
        // the perception of agents must not be modified after construction!
        this.soundPercepts = Collections.unmodifiableSet(soundPercepts);
    }

    public Set<SoundPercept> getAll() {
        return soundPercepts;
    }

    /**
     * Allows to select sounds that fulfil a given predicate.
     *
     * @param predicate Receives a sound percept; Decides whether to include the percept in the return set.
     *                  The return value of true allows to include the percept.
     *                  The return value of false allows to exclude the percept.
     *
     * @return The new set of sound percepts filtered out by the result of evaluating the given predicate.
     */
    public SoundPercepts filter(Predicate<? super SoundPercept> predicate) {
        return new SoundPercepts(soundPercepts.stream().filter(predicate).collect(Collectors.toSet()));
    }

}
