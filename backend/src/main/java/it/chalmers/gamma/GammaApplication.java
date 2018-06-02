package it.chalmers.gamma;

import it.chalmers.gamma.db.entity.Whitelist;
import it.chalmers.gamma.db.repository.ITUserRepository;
import it.chalmers.gamma.exceptions.CIDAlreadyWhitelistedException;
import it.chalmers.gamma.service.ITUserService;
import it.chalmers.gamma.service.WhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class GammaApplication {

    @Autowired
    private ITUserService itUserService;
    @Autowired
    private WhitelistService whitelistService;

    public static void main(String[] args) {
        SpringApplication.run(GammaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        //itUserService.createUser("Portals", "svenel");
        try {
            whitelistService.addWhiteListedCID("engsmyre");
        } catch (CIDAlreadyWhitelistedException e) {
            e.printStackTrace();
        }
        whitelistService.isCIDWhiteListed("engsmyre");
    }

}
