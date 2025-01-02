package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents a landmark in the environment map.
 * Landmarks are identified and updated by the FusionSlam service.
 */
public class LandMark {
    private final String id;
    private final String description;
    private final List<CloudPoint> coordinates;

    public LandMark(String id, String description, List<CloudPoint> coordinates) {
        this.id = id;
        this.description = description;
        this.coordinates = coordinates;
    }

    /**
     * 
     * @return this landmark id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @return this landmark description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @return this landmark coordinate list
     */
    public List<CloudPoint> getCoordinates() {
        return coordinates;
    }

    /**
     * 
     * @param newCoordinates which need to get added to the current coordinate list
     *                       of this landmar
     */
    public void updateCoordinates(List<CloudPoint> newCoordinates) {
        for (CloudPoint point : newCoordinates) {
            coordinates.add(point);
        }
    }

    /**
     * 
     * @return the precise location of this landmark based on all the times it was
     *         caught on the lidar
     *         calculated by the average
     */
    public CloudPoint getPreciseCoordinates() {
        float sumX = 0;
        float sumY = 0;
        for (CloudPoint point : coordinates) {
            sumX += point.getX();
            sumY += point.getY();
        }
        float currentCoordinatesSize = (float) coordinates.size();
        float exactX = sumX / currentCoordinatesSize;
        float exactY = sumY / currentCoordinatesSize;

        return new CloudPoint(exactX, exactY);
    }
}