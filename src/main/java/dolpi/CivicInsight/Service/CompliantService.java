package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.OllamaResponse;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import java.util.*;

@Service
public class CompliantService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private OfficerRepo officerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendcomplaint(Complaints complaints) {
        String url = "https://deciduously-unled-delila.ngrok-free.dev/analyze_letter";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("letter", complaints.getComplaint());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        OllamaResponse response = restTemplate.postForObject(url, entity, OllamaResponse.class);

        List<OfficerEnty> listofofficer = officerRepo.findByCityAndDepartment(
            complaints.getCity(), response.getAnalysis().getDepartment()
        );

        if (listofofficer == null || listofofficer.isEmpty()) {
            throw new ResourcesNotFound("Officer Not Found");
        }

        String officerName = "";
        String departmentName = "";

        for (OfficerEnty officer : listofofficer) {
            if (officer.getCountReport() < 5) {

                // Step 1: Complaint fields set karo
                complaints.setUrgency(response.getAnalysis().getUrgency());
                complaints.setStatus("Pending");
                complaints.setOfficerId(officer.getId());
                complaints.setOfficerName(officer.getName());
                complaints.setDepartmentName(officer.getDepartment());

                // 2: PEHLE complaint save karo MongoDB ID yahan generate hogi
                Complaints savedComplaint = reportRepo.save(complaints);

                //Step 3: AB officer mein ID daalo 
                officer.getReportIds().add(savedComplaint.getId());
                officer.setCountReport(officer.getCountReport() + 1);
                officerRepo.save(officer);

                officerName = officer.getName();
                departmentName = officer.getDepartment();

                // Step 4: User ko email bhejo
                Optional<UserEnity> optionalUser = userRepo.findById(savedComplaint.getUserId());
                if (optionalUser.isPresent()) {
                    UserEnity user = optionalUser.get();
                    emailService.sendEmail(
                        user.getEmail(),
                        "Complaint Successfully Submitted - CivicInsight",
                        "<h2>Hello " + user.getName() + "!</h2>" +
                        "<p>Aapki complaint successfully submit ho gayi hai.</p>" +
                        "<p><b>Complaint ID:</b> " + savedComplaint.getId() + "</p>" +
                        "<p><b>Complaint:</b> " + savedComplaint.getComplaint() + "</p>" +
                        "<p><b>Assigned Officer:</b> " + officer.getName() + "</p>" +
                        "<p><b>Department:</b> " + officer.getDepartment() + "</p>" +
                        "<p><b>City:</b> " + savedComplaint.getCity() + "</p>" +
                        "<p><b>Urgency:</b> " + response.getAnalysis().getUrgency() + "</p>" +
                        "<p><b>Status:</b> Pending ⏳</p>" +
                        "<p>CivicInsight Team</p>"
                    );
                }

                break;
            }
        }

        return "Your Report Is Submitted By " + officerName + " Department Name " + departmentName;
    }

    public Complaints checkstatus(String id) {
        Optional<Complaints> complaint = reportRepo.findById(id);
        if (!complaint.isPresent()) throw new ResourcesNotFound("NOT FOUND");
        return complaint.get();
    }
}
