package BankingApp.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LoggedUserPrinter {

    private final SessionRegistry sessionRegistry;

    public LoggedUserPrinter(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Scheduled(fixedRate = 5000) // alle 5 Sekunden
    public void printLoggedUsers() {
        var principals = sessionRegistry.getAllPrincipals();
        System.out.println("Principal" + principals);
        System.out.println("Eingeloggte Nutzer:");
        for (Object principal : principals) {
            var sessions = sessionRegistry.getAllSessions(principal, false);
            if (!sessions.isEmpty()) {
                System.out.println(principal); // prinzipiell UserDetails
            }
        }
    }





}