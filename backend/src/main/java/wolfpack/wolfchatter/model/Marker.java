package wolfpack.wolfchatter.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "Marker")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Marker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}
