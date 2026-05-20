package dolpi.CivicInsight.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroqAnalysis {
    private String Department;
    private String Urgency;
    private String Category;
    private String Summary;
    private String Suggested_Action;
}
