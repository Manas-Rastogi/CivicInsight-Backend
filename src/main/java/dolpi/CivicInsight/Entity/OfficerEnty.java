package dolpi.CivicInsight.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection ="officer_department")
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name="city_department_idx",def="{'city':1,'department':1}")
public class OfficerEnty {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String email;


    private String department;

    @NonNull
    private String city;

    private int countReport;

    private List<String> roles;

    private double rating;

    private int totalRatings;

    private int completedOnTime;

    private int missed;

    private List<String>reportIds=new ArrayList<>();

    private LocalDateTime createdAt;
}
