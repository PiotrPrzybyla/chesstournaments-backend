package pwr.chesstournamentsbackend.conifg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("classpath:firebase-service-account.json")
    private Resource firebaseServiceAccount;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(firebaseServiceAccount.getInputStream());
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
