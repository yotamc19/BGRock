package bgu.spl.mics.application.objects;

/**
 * DetectedObject represents an object detected by the camera.
 * It contains information such as the object's ID and description.
 */
public class DetectedObject {
    private String id;
    private String description;

    public DetectedObject(String id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * @return the object id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the object description
     */
    public String getDescription() {
        return description;
    }

    public String toString() {
        return "id: " + id + ", description: " + description;
    }
}