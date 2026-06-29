package com.gescom.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.services.createClient.url}")
    private String createClientUrl;

    @Value("${app.services.getIdClient.url}")
    private String getIdClientUrl;

    @Value("${app.services.notification.url}")
    private String notificationUrl;

//    @Value("${app.services.commande.url}")
    private String commandeUrl;

//    @Value("${app.services.recherchePaiement.url}")
    private String recherchePaiementUrl;

    @GetMapping("/")
    public String afficherLogin() {
        return "login";
    }

    @GetMapping("/home")
    public String afficherHome(
            jakarta.servlet.http.HttpServletRequest request,
            Model model) {

        // Récupération des attributs flash si présents
        java.util.Map<String, ?> flashAttributes =
                org.springframework.web.servlet.support.RequestContextUtils.getInputFlashMap(request);

        if (flashAttributes != null) {
            model.addAttribute("nomClient", flashAttributes.get("nomClient"));
            model.addAttribute("emailClient", flashAttributes.get("emailClient"));
        }
        model.addAttribute("commandeUrl", commandeUrl);
        model.addAttribute("recherchePaiementUrl", recherchePaiementUrl);
        return "home";
    }

    @PostMapping("/login")
    public String connecter(
            @RequestParam("username") String nomClient,
            @RequestParam("password") String emailClient,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes,
            Model model) {

        try {
            Map<String, String> clientData = new HashMap<>();
            clientData.put("nomClient", nomClient);
            clientData.put("emailClient", emailClient);
            String url = getIdClientUrl.replace("{email}", emailClient);

            ResponseEntity<String> response = restTemplate.postForEntity(url, clientData, String.class);

            redirectAttributes.addFlashAttribute("idClient", response.getBody());
            redirectAttributes.addFlashAttribute("nomClient", nomClient);
            redirectAttributes.addFlashAttribute("emailClient", emailClient);
            return "redirect:/home";
        } catch (Exception e) {
            return handleException(e, "login", "service de connexion", nomClient, emailClient, model);
        }
    }

    @GetMapping("/login")
    public String annuler() {
        return "redirect:/";
    }

    @GetMapping("/createClient")
    public String afficherCreateClient(Model model) {
        return "createClient";
    }

    @PostMapping("/saveClient")
    public String saveClient(
            @RequestParam("nomClient") String nomClient,
            @RequestParam("emailClient") String emailClient,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes,
            Model model) {

        Map<String, String> clientData = new HashMap<>();
        clientData.put("nomClient", nomClient);
        clientData.put("emailClient", emailClient);
        try {
            restTemplate.postForEntity(createClientUrl, clientData, String.class);
            redirectAttributes.addFlashAttribute("nomClient", nomClient);
            redirectAttributes.addFlashAttribute("emailClient", emailClient);
            return "redirect:/home";
        } catch (Exception e) {
            return handleException(e, "createClient", "service de création de client", nomClient, emailClient, model);
        }



    }

    private String handleException(Exception e, String viewName, String serviceName, String nomClient, String emailClient, Model model) {
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException httpEx = (HttpStatusCodeException) e;
            if (httpEx.getStatusCode() == HttpStatus.NOT_FOUND || httpEx.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED) {
                model.addAttribute("error", "Erreur : Le client n'existe pas.");
            } else if (httpEx.getStatusCode() == HttpStatus.CONFLICT) {
                model.addAttribute("error", "Erreur : Le client existe déjà (Conflict).");
            } else {
                model.addAttribute("error", "Erreur du " + serviceName + ".");
            }
        } else if (e instanceof ResourceAccessException) {
            model.addAttribute("error", "Le " + serviceName + " est injoignable (Down).");
        } else {
            model.addAttribute("error", "Une erreur inattendue est survenue : " + e.getMessage());
        }

        model.addAttribute("nomClient", nomClient);
        model.addAttribute("emailClient", emailClient);
        return viewName;
    }
/*
    @GetMapping("/createCard")
    public String afficherCreateCard(
            jakarta.servlet.http.HttpServletRequest request,
            Model model) {

        // Récupération des attributs flash si présents
        java.util.Map<String, ?> flashAttributes =
                org.springframework.web.servlet.support.RequestContextUtils.getInputFlashMap(request);

        if (flashAttributes != null) {
            model.addAttribute("nomClient", flashAttributes.get("nomClient"));
            model.addAttribute("emailClient", flashAttributes.get("emailClient"));
        }

        model.addAttribute("createCardUrl", createCardUrl);
        return "createCard";
    }

    @PostMapping("/saveCard")
    public String saveCard(
            String nomClient, String emailClient,
            String numCarte, String dateExpiration,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        // Logique de persistance de la Carte à implémenter
        redirectAttributes.addFlashAttribute("nomClient", nomClient);
        redirectAttributes.addFlashAttribute("emailClient", emailClient);
        redirectAttributes.addFlashAttribute("numCarte", numCarte);
        redirectAttributes.addFlashAttribute("dateExpiration", dateExpiration);
        return "redirect:/createNotif";
    }
    */
    @GetMapping("/createNotif")
    public String afficherCreateNotif(
            jakarta.servlet.http.HttpServletRequest request,
            Model model) {
        
        // Récupération des attributs flash si présents
        java.util.Map<String, ?> flashAttributes = 
            org.springframework.web.servlet.support.RequestContextUtils.getInputFlashMap(request);
        
        if (flashAttributes != null) {
            model.addAttribute("nomClient", flashAttributes.get("nomClient"));
            model.addAttribute("emailClient", flashAttributes.get("emailClient"));
        }

        model.addAttribute("commandeUrl", commandeUrl);
        return "createNotif";
    }
}
