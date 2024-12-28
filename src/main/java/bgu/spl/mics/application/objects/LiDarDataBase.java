package bgu.spl.mics.application.objects;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {
    private static LiDarDataBase instance = null;
    private List<StampedCloudPoints> cloudPoints;

    private LiDarDataBase() {
        cloudPoints = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public static LiDarDataBase getInstance(String filePath) {
        if (instance == null) {
            instance = new LiDarDataBase();
            instance.loadData(filePath);
        }
        return instance;
    }

    /**
     * 
     * @return all the StampedCloudPoints from the database
     */
    public List<StampedCloudPoints> getCloudPoints() {
        return cloudPoints;
    }

    /**
     * 
     * @param id of the object which we need to find his coordinates
     * @return object with {@param id} coordinates if exists, null otherwise
     */
    public List<CloudPoint> getCoordinatesById(String id) {
        for (StampedCloudPoints point : cloudPoints) {
            if (point.getId() == id) {
                return point.getCloudPoints();
            }
        }
        return null;
    }

    /**
     * loadData load all the lidar data from the resource file and saves it on this class fields
     * 
     * @param filePath of the lidar data
     */
    public void loadData(String filePath) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(filePath);
            Type stampedCloudPointsType = new TypeToken<List<StampedCloudPoints>>() {
            }.getType();
            cloudPoints = gson.fromJson(reader, stampedCloudPointsType);
        } catch (IOException e) {
            e.printStackTrace();
            cloudPoints = null;
        }
    }
}
