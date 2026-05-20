package dolpi.CivicInsight.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection ="user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEnity {
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

    private List<String> roles;

    private LocalDateTime createdAt;

    private List<LocalDateTime> complaintTimestamps = new ArrayList<>();

}
