package ma.formations.jms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee implements Serializable {
    private String name;
    private Double salaire;
    private String fonction;
}
