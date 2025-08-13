package com.onenth.OneNth.global.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.admin.service-account-path}")
    private String serviceAccountPath;

    @PostConstruct
    public void init() throws IOException {

        InputStream serviceAccount = getClass()
                .getClassLoader()
                .getResourceAsStream(serviceAccountPath);

        if (serviceAccount == null) {
            throw new RuntimeException("Firebase service key file not found");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        serviceAccount.close();
    }
}