package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.DTO.officerDTO;
import dolpi.CivicInsight.DTO.userdto;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.OfficerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class OfficerService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OfficerRepo officerRepo;

    public String Createofficer(officerDTO officerDTO){
        String encodepassword=passwordEncoder.encode(officerDTO.getPassword());
        OfficerEnty user=new OfficerEnty();
        user.setName(officerDTO.getName());
        user.setUsername(officerDTO.getUsername());
        user.setPassword(encodepassword);
        user.setDepartment(officerDTO.getDepartment());
        user.setEmail(officerDTO.getEmail());
        user.setCity(officerDTO.getCity());
        user.setRoles(Arrays.asList("OFFICER"));
        officerRepo.save(user);

        // Welcome email bhejo
        emailService.sendEmail(
            officerDTO.getEmail(),
            "Welcome to CivicInsight!",
            "<h2>Hello " + officerDTO.getName() + "!</h2>" +
            "<p>Aapka officer account successfully create ho gaya hai.</p>" +
            "<p><b>Department:</b> " + officerDTO.getDepartment() + "</p>" +
            "<p><b>City:</b> " + officerDTO.getCity() + "</p>" +
            "<p>CivicInsight Team</p>"
        );

        
        return "SuccesFully Create OFFICER";
    }
}
