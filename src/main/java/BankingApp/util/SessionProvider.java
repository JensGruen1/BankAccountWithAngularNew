package BankingApp.util;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionProvider {
    @Autowired
    private EntityManagerFactory emf;

    public Session getSession() {
        return emf.unwrap(SessionFactory.class).openSession();
    }
}
