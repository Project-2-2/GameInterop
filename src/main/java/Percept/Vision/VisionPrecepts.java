package Percept.Vision;

/**
 * Represents perception of the world by vision.
 */
public class VisionPrecepts {

    private FieldOfView fieldOfView;
    private AgentPercepts agentPercepts;
    private ObjectPercepts objectPercepts;

    public VisionPrecepts(FieldOfView fieldOfView, AgentPercepts agentPercepts, ObjectPercepts objectPercepts) {
        this.fieldOfView = fieldOfView;
        this.agentPercepts = agentPercepts;
        this.objectPercepts = objectPercepts;
    }

    public FieldOfView getFieldOfView() {
        return fieldOfView;
    }

    public AgentPercepts getAgents() {
        return agentPercepts;
    }

    public ObjectPercepts getObjects() {
        return objectPercepts;
    }

}
