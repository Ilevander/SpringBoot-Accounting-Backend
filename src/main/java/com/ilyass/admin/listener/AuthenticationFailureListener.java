package com.ilyass.admin.listener;

import com.ilyass.admin.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * Ce mécanisme aide à suivre les tentatives de connexion échouées et peut être utilisé en conjonction avec le LoginAttemptService pour verrouiller les comptes après un certain nombre d'échecs, améliorant ainsi la sécurité de l'application.
 * *********************************************
 * Échec d'authentification :
 *--------------------------
 * Lorsqu'un utilisateur tente de se connecter avec des identifiants incorrects, un AuthenticationFailureBadCredentialsEvent est déclenché par Spring Security.
 * Écouteur d'événements :
 *------------------------
 * L'écouteur AuthenticationFailureListener intercepte cet événement grâce à la méthode onAuthenticationFailure.
 * Mise à jour du cache :
 *-------------------------
 * La méthode récupère le nom d'utilisateur à partir de l'événement et appelle loginAttemptService.addUserToLoginAttemptCache(username) pour incrémenter le compteur de tentatives de connexion échouées pour cet utilisateur.
 */
@Component
public class AuthenticationFailureListener {
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService){
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) throws ExecutionException {
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof String){
            String username = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(username);
        }
    }
}
