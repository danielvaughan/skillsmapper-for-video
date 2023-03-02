package org.skillsmapper.factservice;

import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class FactController {

    private final FactRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(FactController.class);
    FactController(FactRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            //model.addAttribute("leaderMessage", leaderMessage);

        } catch (DataAccessException e) {
            String message =
                    "Error while connecting to the Cloud SQL database. "
                            + "Check that your username and password are correct and that the "
                            + "PostgreSQL instance, database, and table exists and are ready for use: "
                            + e.toString();
            logger.error(message);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Unable to load page; see logs for more details.", e);
        }
        return "index";
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/facts")
    @ResponseBody
    Iterable<Fact> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/facts")
    @ResponseBody
    Fact createFact(@RequestHeader Map<String, String> headers, @RequestBody FactDTO factDTO) {
        String uid = authenticateJwt(headers);
        logger.info("uid : {}", uid);
        Fact fact = new Fact();
        fact.setLevel(factDTO.getLevel());
        fact.setSkill(factDTO.getSkill());
        return repository.save(fact);
    }

    // Single item
    @GetMapping("/facts/{id}")
    @ResponseBody
    Fact one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new FactNotFoundException(id));
    }

    @PutMapping("/facts/{id}")
    @ResponseBody
    Fact replaceFact(@RequestBody Fact newFact, @PathVariable Long id) {

        return repository.findById(id)
                .map(fact -> {
                    fact.setLevel(newFact.getLevel());
                    fact.setSkill(newFact.getSkill());
                    return repository.save(fact);
                })
                .orElseGet(() -> {
                    newFact.setId(id);
                    return repository.save(newFact);
                });
    }

    @DeleteMapping("/facts/{id}")
    @ResponseBody
    void deleteFact(@PathVariable Long id) {
        repository.deleteById(id);
    }

    /** Extract and verify Id Token from header */
    private String authenticateJwt(Map<String, String> headers) {
        String authHeader =
                (headers.get("authorization") != null)
                        ? headers.get("authorization")
                        : headers.get("Authorization");
        if (authHeader != null) {
            String idToken = authHeader.split(" ")[1];
            // If the provided ID token has the correct format, is not expired, and is
            // properly signed, the method returns the decoded ID token
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                String uid = decodedToken.getUid();
                return uid;
            } catch (FirebaseAuthException e) {
                logger.error("Error with authentication: " + e.toString());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "", e);
            }
        } else {
            logger.error("Error no authorization header");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
