package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfficerAninalyisisComplaintsService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private OfficerRepo officerRepo;

    @Autowired
    private UserRepo userRepo; //User fetch karne ke liye

    @Autowired
    private EmailService emailService; // Email bhejne ke liye

    public List<Complaints> FetchComplaintOfficer(String officerId) {
        List<Complaints> listComplaints = reportRepo.findByOfficerId(officerId);
        if (listComplaints == null || listComplaints.isEmpty())
            throw new ResourcesNotFound("NOT FOUND");
        return listComplaints;
    }

    public Complaints FetchComplaints(String Id) {
        Optional<Complaints> listComplaints = reportRepo.findById(Id);
        if (!listComplaints.isPresent()) throw new ResourcesNotFound("NOT FOUND");
        return listComplaints.get();
    }

   public String AnalysisComplaint(String Id, String reply) {
    Optional<Complaints> optionalComplaint = reportRepo.findById(Id);
    if (optionalComplaint.isPresent()) {
        Complaints complaint = optionalComplaint.get();

        if (complaint.getListReport() == null || complaint.getListReport().isEmpty()) {
            complaint.setListReport(new ArrayList<>());
        }

        // Officer fetch karo
        Optional<OfficerEnty> optionalofficer = officerRepo.findById(complaint.getOfficerId());
        if (!optionalofficer.isPresent()) throw new ResourcesNotFound("Officer Not Handle Report");
        OfficerEnty officer = optionalofficer.get();
        officer.setCountReport(officer.getCountReport() - 1);

        // ⭐ RATING LOGIC
        LocalDateTime deadline = getDeadline(complaint);
        LocalDateTime now = LocalDateTime.now();

        double currentRating = officer.getRating();
        int total = officer.getTotalRatings();

        if (now.isBefore(deadline)) {
            // Time se pehle solve — +1 
            double newRating = Math.min(5.0, ((currentRating * total) + 1.0) / (total + 1));
            officer.setRating(newRating);
            officer.setCompletedOnTime(officer.getCompletedOnTime() + 1);
        }
        // SLA miss ka -1 

        officer.setTotalRatings(total + 1);

        complaint.getListReport().add(reply);
        complaint.setStatus("Analyzed");
        officerRepo.save(officer);
        reportRepo.save(complaint);

        // Officer ko email bhejo
        emailService.sendEmail(
            officer.getEmail(),
            "Complaint Analyzed - CivicInsight",
            "<h2>Hello " + officer.getName() + "!</h2>" +
            "<p>Aapne complaint <b>#" + complaint.getId() + "</b> analyze kar di hai.</p>" +
            "<p><b>Complaint:</b> " + complaint.getComplaint() + "</p>" +
            "<p><b>City:</b> " + complaint.getCity() + "</p>" +
            "<p><b>Reply:</b> " + reply + "</p>" +
            "<p>CivicInsight Team</p>"
        );

        // User ko email bhejo
        Optional<UserEnity> optionalUser = userRepo.findById(complaint.getUserId());
        if (optionalUser.isPresent()) {
            UserEnity user = optionalUser.get();
            emailService.sendEmail(
                user.getEmail(),
                "Aapki Complaint ka Jawab Aa Gaya! - CivicInsight",
                "<h2>Hello " + user.getName() + "!</h2>" +
                "<p>Aapki complaint <b>#" + complaint.getId() + "</b> analyze ho gayi hai.</p>" +
                "<p><b>Aapki Complaint:</b> " + complaint.getComplaint() + "</p>" +
                "<p><b>Officer ka Reply:</b> " + reply + "</p>" +
                "<p><b>Status:</b> Analyzed ✅</p>" +
                "<p>CivicInsight Team</p>"
            );
        }

        return "Complaint updated successfully!";
    } else {
        return "Complaint not found with ID: " + Id;
    }
}

    public OfficerEnty findOfficer(String Id) {
        Optional<OfficerEnty> optionalOfficer = officerRepo.findById(Id);
        if (!optionalOfficer.isPresent()) throw new ResourcesNotFound("Officer Null");
        return optionalOfficer.get();
    }
}
