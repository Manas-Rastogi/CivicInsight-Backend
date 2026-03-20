package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintSlaScheduler {

    @Autowired
    private ReportRepo complaintRepository;

    @Autowired
    private AdminRepo adminrepo;

    @Autowired
    private UserRepo userRepo; // User fetch karne ke liye

    @Autowired
    private EmailService emailService; //Email bhejne ke liye

    @Scheduled(fixedRate = 1800000)
    public void checkSlaBreaches() {
        LocalDateTime now = LocalDateTime.now();
        processUrgency("HIGH", now.minusHours(24));
        processUrgency("MEDIUM", now.minusDays(3));
        processUrgency("LOW", now.minusWeeks(7));
    }

    private void processUrgency(String urgency, LocalDateTime threshold) {
        List<Complaints> complaints = complaintRepository
                .findByStatusAndUrgencyAndCreatedAtBeforeAndNotifiedFalse(
                        "Pending",
                        urgency,
                        threshold
                );

        System.out.println("Urgency: " + urgency);
        System.out.println("Found: " + complaints.size());

        for (Complaints complaint : complaints) {

            Admin admin = adminrepo.findByCity(complaint.getCity());
            if (admin != null) {
                complaint.setAdminId(admin.getId());
                complaint.setAssignByAdmin("Your Complaint Assigned By Admin");
                complaint.setNotified(true);
                complaintRepository.save(complaint);

                // Admin ko email bhejo
                emailService.sendEmail(
                    admin.getEmail(),
                    "SLA Breach Alert - CivicInsight",
                    "<h2>Hello " + admin.getName() + "!</h2>" +
                    "<p>Ek complaint SLA breach kar gayi hai aur aapko assign ki gayi hai.</p>" +
                    "<p><b>Complaint ID:</b> " + complaint.getId() + "</p>" +
                    "<p><b>Complaint:</b> " + complaint.getComplaint() + "</p>" +
                    "<p><b>City:</b> " + complaint.getCity() + "</p>" +
                    "<p><b>Urgency:</b> " + urgency + "</p>" +
                    "<p><b>Status:</b> Pending ⚠️</p>" +
                    "<p>Please jald se jald action lein.</p>" +
                    "<p>CivicInsight Team</p>"
                );

                // User ko email bhejo
                Optional<UserEnity> optionalUser = userRepo.findById(complaint.getUserId());
                if (optionalUser.isPresent()) {
                    UserEnity user = optionalUser.get();
                    emailService.sendEmail(
                        user.getEmail(),
                        "Aapki Complaint Escalate Ho Gayi - CivicInsight",
                        "<h2>Hello " + user.getName() + "!</h2>" +
                        "<p>Aapki complaint ka abhi tak koi jawab nahi aaya, isliye ise admin ko escalate kar diya gaya hai.</p>" +
                        "<p><b>Complaint ID:</b> " + complaint.getId() + "</p>" +
                        "<p><b>Complaint:</b> " + complaint.getComplaint() + "</p>" +
                        "<p><b>City:</b> " + complaint.getCity() + "</p>" +
                        "<p><b>Urgency:</b> " + urgency + "</p>" +
                        "<p><b>Status:</b> Admin ke paas bhej di gayi hai ⚠️</p>" +
                        "<p>Hum jald se jald aapki complaint resolve karne ki koshish karenge.</p>" +
                        "<p>CivicInsight Team</p>"
                    );
                }
            }
        }
    }
}
