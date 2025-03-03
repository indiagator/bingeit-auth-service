package com.secor.bingeitauthservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class MainRestController {

    private static final Logger log = LoggerFactory.getLogger(MainRestController.class);


    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    TokenService tokenService;



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Credential credential)
    {
        log.info("Received request to signup: {}", credential);
        credentialRepository.save(credential);
        log.info("Credential saved: {}", credential);
        return ResponseEntity.ok("New Signup Successful");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credential credential)
    {
        log.info("Received request to login: {}", credential);
        if(credentialRepository.findById(credential.getUsername()).isPresent())
        {
            log.info("Credential exists: {}", credential);
            Credential fetchedCredential = credentialRepository.findById(credential.getUsername()).get();
            log.info("Fetched credential: {}", fetchedCredential);
            if(credential.getPassword().equals(fetchedCredential.getPassword()))
            {
                log.info("Login Successful: {}", credential);
                Token token=  tokenService.generateToken(credential.getUsername());
                log.info("Token generated: {}", token);
                return ResponseEntity.ok().header("Authorization",token.getToken()).body("Login Successful");
            }
            else
            {
                log.info("Login Failed: {}", credential);
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            log.info("Credential does not exist: {}", credential);
            return ResponseEntity.ok("Credential does not exist");
        }
    }


}
