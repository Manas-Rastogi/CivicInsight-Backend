package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.GroqAnalysis;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SendComplaintService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private CompliantService compliantService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_COMPLAINTS = 5;

    public String sendcomplaint(Complaints complaints) {

        // STEP 1: User dhundo MongoDB se
        UserEnity user = userRepo.findById(complaints.getUserId())
                .orElseThrow(() -> new ResourcesNotFound("User not found"));

        // STEP 2: 1 ghante purane timestamps hatao
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        user.getComplaintTimestamps()
                .removeIf(time -> time.isBefore(oneHourAgo));

        // STEP 3: Rate limit check karo
        if (user.getComplaintTimestamps().size() >= MAX_COMPLAINTS) {
            throw new RuntimeException(
                    "1 ghante mein sirf 5 complaints bhej sakte ho. " +
                    "Thodi der baad try karo."
            );
        }

        // STEP 4: MD5 hash banao
        String md5Key = "complaint:exact:" +
                DigestUtils.md5DigestAsHex(
                        complaints.getComplaint()
                                  .toLowerCase()
                                  .trim()
                                  .getBytes()
                );

        // STEP 5: Redis mein check karo
        GroqAnalysis cachedAnalysis =
                (GroqAnalysis) redisTemplate.opsForValue().get(md5Key);

        if (cachedAnalysis != null) {
            log.info("✅ Redis mein mili - Groq skip!");
        } else {
            log.info("❌ Redis mein nahi mili - Groq call hogi");
        }

        // STEP 6: ID ke liye pehle save karo
        complaints.setStatus("Processing");
        reportRepo.save(complaints);

        // STEP 7: Background mein process karo
        compliantService.processComplaint(complaints, cachedAnalysis, md5Key);

        // Timestamp save
        user.getComplaintTimestamps().add(LocalDateTime.now());
        userRepo.save(user);

        int remaining = MAX_COMPLAINTS - user.getComplaintTimestamps().size();

        return "Complaint received! Tracking ID: " + complaints.getId() +
               " | In This Hours " + remaining + " Submit This Complaint.";
    }
}
