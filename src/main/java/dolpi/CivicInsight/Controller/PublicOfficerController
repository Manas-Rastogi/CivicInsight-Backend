package dolpi.CivicInsight.Controller;

import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Repository.OfficerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/open/officer")
public class PublicOfficerController {

    @Autowired
    private OfficerRepo officerRepo;

    @GetMapping("/rating")
    public ResponseEntity<?> getOfficerRating(@RequestParam String Id) {
        try {
            Optional<OfficerEnty> opt = officerRepo.findById(Id);
            if (!opt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            OfficerEnty o = opt.get();

            Map<String, Object> result = new HashMap<>();
            result.put("name",            o.getName());
            result.put("department",      o.getDepartment());
            result.put("city",            o.getCity());
            result.put("rating",          o.getRating());
            result.put("totalRatings",    o.getTotalRatings());
            result.put("completedOnTime", o.getCompletedOnTime());
            result.put("missed",          o.getMissed());
            result.put("countReport",     o.getCountReport());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            // Error ka exact message return karo
            return ResponseEntity.internalServerError()
                .body("Error: " + e.getMessage());
        }
    }
}
