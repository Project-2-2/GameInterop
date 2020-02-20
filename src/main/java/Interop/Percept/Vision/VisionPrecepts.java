package Interop.Percept.Vision;

import Interop.Utils.Require;

/**
 * Represents perception of the world by vision.
 *
 * See: https://en.wikipedia.org/wiki/Ray_casting
 *
 * You should also take a look at:
 * @see Interop.Percept.Vision.FieldOfView
 */
public final class VisionPrecepts {

    private FieldOfView fieldOfView;
    private ObjectPercepts objectPercepts;

    public VisionPrecepts(FieldOfView fieldOfView, ObjectPercepts objectPercepts) {
        Require.notNull(fieldOfView);
        Require.notNull(objectPercepts);
        requireObjectPerceptsToBeInFieldOfView(fieldOfView, objectPercepts);
        this.fieldOfView = fieldOfView;
        this.objectPercepts = objectPercepts;
    }

    /**
     * This method verifies that the list of object percepts is indeed in the field of view.
     * This check will allow to avoid potential implementation issues related to the vision algorithm.
     *
     * @param fieldOfView The field of view.
     * @param objectPercepts The object percepts.
     */
    private void requireObjectPerceptsToBeInFieldOfView(FieldOfView fieldOfView, ObjectPercepts objectPercepts) {
        ObjectPercepts objectPerceptsInFieldOfView = objectPercepts.getInFieldOfView(fieldOfView);
        if(objectPerceptsInFieldOfView.getAll().size() != objectPercepts.getAll().size()) {
            throw new RuntimeException(
                "Some object percepts are not in the field of view!\n" +
                objectPercepts.getNotInFieldOfView(fieldOfView)
            );
        }
    }

    public FieldOfView getFieldOfView() {
        return fieldOfView;
    }

    public ObjectPercepts getObjects() {
        return objectPercepts;
    }

}
