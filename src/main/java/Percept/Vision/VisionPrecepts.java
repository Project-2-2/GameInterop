package Percept.Vision;

import Utils.Require;

import java.util.Objects;

/**
 * Represents perception of the world by vision.
 */
public class VisionPrecepts {

    private FieldOfView fieldOfView;
    private ObjectPercepts objectPercepts;

    public VisionPrecepts(FieldOfView fieldOfView, ObjectPercepts objectPercepts) {
        Require.notNull(fieldOfView);
        Require.notNull(objectPercepts);
        this.fieldOfView = fieldOfView;
        this.objectPercepts = objectPercepts;
    }

    public FieldOfView getFieldOfView() {
        return fieldOfView;
    }

    public ObjectPercepts getObjects() {
        return objectPercepts;
    }

}
