
package dolpi.CivicInsight.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    public void sendEmail(String to, String subject, String body) {
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("CivicInsight <onboarding@resend.dev>")
                    .to(to)
                    .subject(subject)
                    .html(body)
                    .build();

            resend.emails().send(params);
            System.out.println("Email sent to: " + to);

        } catch (ResendException e) {
            System.err.println("Email send failed: " + e.getMessage());
        }
    }
}
