package dolpi.CivicInsight.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;  
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Getter
@Setter  
@Document(collection = "admin")
public class Admin {
    @Id
    private String id;


    private String username;


    private String password;


    private String email;

    private String Name;

    private String city;

    private String complaintId;

    private List<String> info;

    private List<String> roles;
}
