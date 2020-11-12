package control;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class KMLUtil {
    public static Iterable<KmlPlacemark> unwrap(int resourceID, GoogleMap map, Context context) throws IOException, XmlPullParserException {
        ArrayList<KmlPlacemark> output = new ArrayList<>();
        Queue<KmlContainer> queue = new LinkedList<>();
        KmlLayer layer = new KmlLayer(map, resourceID, context);
        layer.addLayerToMap();
        for (KmlContainer item : layer.getContainers()){
            queue.add(item);
        }
        while (!queue.isEmpty()){
            KmlContainer container = queue.poll();
            for (KmlPlacemark placemark : container.getPlacemarks()){
                output.add(placemark);
            }
            for (KmlContainer child : container.getContainers()){
                queue.add(child);
            }
        }
        layer.removeLayerFromMap();
        return output;
    }

    public static ArrayList<String> getAttributesValue(KmlPlacemark placemark, ArrayList<String> attributes){
        String description = placemark.getProperty("description");
        ArrayList<String> output = new ArrayList<>();
        for (String attribute : attributes) {
            String attribute_string = "<th>" + attribute + "</th>";
            int start = description.indexOf(attribute_string) + attribute_string.length() + 2;
            int end = description.indexOf("</td>", start);
            start += 4;
            output.add(description.substring(start, end));
        }
        return output;
    }
}
