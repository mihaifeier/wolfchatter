package wolfpack.wolfchatter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "Message")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String message;

    @ManyToOne(targetEntity = Marker.class)
    @JoinColumn(name = "Marker", referencedColumnName = "id")
    public Marker marker;

    @Column(name = "username")
    private String user;

    @Column
    private Timestamp timestamp;

    @Column
    private Boolean deleted;
}
