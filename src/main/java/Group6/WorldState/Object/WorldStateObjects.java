package Group6.WorldState.Object;

import Group6.Geometry.Collection.Quadrilaterals;
import Group6.Geometry.Distance;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;
import Group6.Percept.Vision.ObjectPercept;
import Group6.WorldState.Contract.Object;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WorldStateObjects {

    private Collection<Object> objects;

    public WorldStateObjects(Collection<Object> objects) {
        this.objects = Collections.unmodifiableCollection(objects);
    }

    public WorldStateObjects(Quadrilaterals quadrilaterals, ObjectPerceptType objectPerceptType) {
        objects = new HashSet<>();
        for(Quadrilateral quadrilateral: quadrilaterals.getAll()) {
            objects.add(new QuadrilateralObject(
                quadrilateral,
                objectPerceptType
            ));
        }
    }

    public WorldStateObjects(WorldStateObjects ...collections) {
        objects = new HashSet<>();
        for (WorldStateObjects collection: collections) {
            objects.addAll(collection.getAll());
        }
    }

    public Collection<Object> getAll() {
        return objects;
    }

    public WorldStateObjects getWithout(Object object) {
        return filter((otherObject -> { return otherObject != object; }));
    }

    public WorldStateObjects getInRange(Point point, Distance distance) {
        return filter((object) -> object.isInRange(point, distance));
    }

    public WorldStateObjects filter(Predicate<? super Object> predicate) {
        return new WorldStateObjects(objects.stream().filter(predicate).collect(Collectors.toSet()));
    }

}
