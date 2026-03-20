package dolpi.CivicInsight.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.List;

// @Getter
// @Setter
// @Document(collection = "complaint")
// @NoArgsConstructor
// @AllArgsConstructor
// public class Complaints {

//     @Id
//     private String id;
//     @NonNull
//     private String Name;
//     @NonNull
//     private String city;
//     @NonNull
//     private String ward_number;
//     @NonNull
//     private String complaint;
//     private String OfficerName;
//     private String departmentName;
//     @Field("officer_id")
//     private String officerId;
//     private String urgency;
//     private List<String> listReport;

//     @Field("user_id")
//     private String userId;

//     private String assignByAdmin;
//     @CreatedDate
//     private LocalDateTime createdAt;
//     private boolean notified = false; // Important private String adminId; private String status;
//     private String adminId;
//     private String status;
// }

@Data
@Getter  
@Setter
@Document(collection = "complaint")
@NoArgsConstructor
public class Complaints {
    @Id
    private String id;
    private String name;        
    private String city;
    private String wardNumber;
    private String complaint;
    private String officerName;
    private String departmentName;
    @Field("officer_id")
    private String officerId;
    private String urgency;
    private List<String> listReport;
    @Field("user_id")
    private String userId;
    private String assignByAdmin;
    @CreatedDate
    private LocalDateTime createdAt;
    private boolean notified = false;
    private String adminId;
    private String status;
}
