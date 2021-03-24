package wolfpack.wolfchatter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import wolfpack.wolfchatter.model.Marker;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarkerDto {
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;

    public static MarkerDto toDto(Marker marker) {
        return new MarkerDto(marker.getId(), marker.getName(), marker.getLatitude(), marker.getLongitude());
    }

    public static Marker toEntity(MarkerDto markerDto) {
        return new Marker(markerDto.getId(), markerDto.getName(), markerDto.getLatitude(), markerDto.getLongitude());
    }

    public static String toJson(MarkerDto markerDto) {
        JSONObject marker = new JSONObject();
        marker.put("id", markerDto.getId());
        marker.put("name", markerDto.getName());
        marker.put("latitude", markerDto.getLatitude());
        marker.put("longitude", markerDto.getLongitude());
        return marker.toString();
    }
}
