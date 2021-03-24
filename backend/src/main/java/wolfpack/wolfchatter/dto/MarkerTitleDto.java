package wolfpack.wolfchatter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarkerTitleDto {
    private Integer markerId;
    private String newTitle;
}
