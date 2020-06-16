package Group6.Percept.Vision;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Point;
import Group6.WorldState.Object.AgentState;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Tomasz Darmetko
 */
public class ObjectPercepts {

    private Set<ObjectPercept> percepts;

    public ObjectPercepts(Set<ObjectPercept> percepts) {
        this.percepts = Collections.unmodifiableSet(percepts);
    }

    public ObjectPercepts(ObjectPercept ...percepts) {
        this(new HashSet<>(Arrays.asList(percepts)));
    }

    public Set<ObjectPercept> getAll() {
        return percepts;
    }

    public ObjectPercepts getVisibleInStraightLineFrom(Point point) {
        ObjectPercepts opaque = getOpaque();
        if(opaque.getAll().isEmpty()) return this; // there are no opaque objects to obscure the view
        Point closestOpaquePoint = opaque.toPoints().getClosest(point);
        double viewRange = point.getDistance(closestOpaquePoint).getValue();
        return filter(percept -> percept.getPoint().getDistance(point).getValue() <= viewRange);
    }

    public ObjectPercepts getOpaque() {
        return filter(percept -> percept.getType().isOpaque());
    }

    public ObjectPercepts shiftPerspective(Point newOrigin) {
        return map((percept -> { return percept.shiftPerspective(newOrigin); }));
    }

    public ObjectPercepts getInFieldOfView(FieldOfView fieldOfView) {
        return filter(percept -> fieldOfView.isInView(percept.getPoint().toInteropPoint()));
    }

    public ObjectPercept getClosestTo(Point point, ObjectPerceptType type) {
        ObjectPercepts filteredByType = getByType(type);
        ObjectPercepts closestFilteredByType = filteredByType.getByPoint(
            filteredByType.toPoints().getClosest(point)
        );
        return closestFilteredByType.getAll().iterator().next();
    }

    public ObjectPercepts getByType(ObjectPerceptType type) {
        return filter(objectPercept -> { return objectPercept.getType() == type; });
    }

    public ObjectPercepts getByPoint(Point point) {
        return filter(objectPercept -> { return objectPercept.getPoint().isEqualTo(point); });
    }

    public Set<ObjectPerceptType> getTypes() {
        return percepts.stream().map(ObjectPercept::getType).collect(Collectors.toSet());
    }

    public ObjectPercepts filter(Predicate<? super ObjectPercept> predicate) {
        return new ObjectPercepts(percepts.stream().filter(predicate).collect(Collectors.toSet()));
    }

    public ObjectPercepts map(Function<? super ObjectPercept, ObjectPercept> predicate) {
        return new ObjectPercepts(percepts.stream().map(predicate).collect(Collectors.toSet()));
    }

    public Points toPoints() {
        return new Points(percepts.stream().map(ObjectPercept::getPoint).collect(Collectors.toSet()));
    }

    public Interop.Percept.Vision.ObjectPercepts toInterop() {
        return new Interop.Percept.Vision.ObjectPercepts(
            percepts.stream().map(ObjectPercept::toInterop).collect(Collectors.toSet())
        );
    }

    public String toString() {
        return "ObjectPercepts=" + percepts;
    }

}
