package wolfpack.wolfchatter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditMessageDto {
    private Integer messageId;
    private String message;
}
