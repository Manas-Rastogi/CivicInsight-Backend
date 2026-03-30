package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import dolpi.CivicInsight.JwtFilter.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GoogleAuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyUserDetails myUserDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private OfficerRepo officerRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public String handleGoogleCallback(String code, String check) {

        // Step 1: Google token 
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "https://developers.google.com/oauthplayground");
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse =
                restTemplate.postForEntity(tokenEndpoint, request, Map.class);

        if (!tokenResponse.getStatusCode().is2xxSuccessful()
                || tokenResponse.getBody() == null) {
            throw new RuntimeException("Failed to fetch Google token");
        }

        // Step 2: Email fetch 
        String idToken = (String) tokenResponse.getBody().get("id_token");
        String userInfoUrl =
                "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

        ResponseEntity<Map> userInfoResponse =
                restTemplate.getForEntity(userInfoUrl, Map.class);

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()
                || userInfoResponse.getBody() == null) {
            throw new RuntimeException("Invalid Google user");
        }

        String email = (String) userInfoResponse.getBody().get("email");

        // Step 3: User exist or NO
        UserEnity user       = userRepo.findByUsername(email);
        OfficerEnty officer  = officerRepo.findByUsername(email);
        Admin admin          = adminRepo.findByUsername(email);

        // Step 4: Nahi hai toh create
        if (user == null && officer == null && admin == null) {
            if (check.equals("user")) {
                createUser(email);
            } else if (check.equals("officer")) {
                createOfficer(email);
            } else if (check.equals("admin")) {
                createAdmin(email);
            }
        }

        // step 5: JWT token generate
        UserDetails userDetails = myUserDetails.loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }

    // ── User create 
    private void createUser(String email) {
        UserEnity user = new UserEnity();
        user.setEmail(email);
        user.setUsername(email);
        user.setName(email);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRoles(List.of("USER"));
        userRepo.save(user);
    }

    // ── Officer create
    private void createOfficer(String email) {
        OfficerEnty officer = new OfficerEnty();
        officer.setEmail(email);
        officer.setUsername(email);
        officer.setName(email);
        officer.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        officer.setRoles(List.of("OFFICER"));
        officerRepo.save(officer);
    }

    // ── Admin create
    private void createAdmin(String email) {
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setUsername(email);
        admin.setName(email);
        admin.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        admin.setRoles(List.of("ADMIN"));
        adminRepo.save(admin);
    }
}
