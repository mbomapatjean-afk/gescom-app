package com.gescom.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.services.createClient.url}")
    private String createClientUrl;

    @Value("${app.services.getIdClient.url}")
    private String getIdClientUrl;

    @Value("${app.services.notification.url}")
    private String notificationUrl;

    @Value("${app.services.produit.listUrl}")
    private String commandeUrl;

    @Value("${app.services.produit.getProduit}")
    private String rechercheCommandeUrl;
    
    @Value("${app.services.home.url}")
    private String homeUrl;

    @GetMapping("/")
    public String afficherLogin() {
        return "login";
    }

    @GetMapping("/home")
    public String afficherHome(Model model) {
        Map<String, Object> flashAttributes = model.asMap();
        
        if (flashAttributes.isEmpty()) {
            return "redirect:/";
        }

        String finalUrl = UriComponentsBuilder.fromUriString(homeUrl)
                .buildAndExpand(flashAttributes)
                .encode()
                .toUriString();

        return "redirect:" + finalUrl;
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

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
                redirectAttributes.addFlashAttribute("idClient", responseBody.get("idClient"));
                redirectAttributes.addFlashAttribute("nomClient", responseBody.get("nomClient"));
                redirectAttributes.addFlashAttribute("emailClient", responseBody.get("emailClient"));
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Erreur : La connexion a échoué.");
                model.addAttribute("nomClient", nomClient);
                model.addAttribute("emailClient", emailClient);
                return "login";
            }
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

        try {
            Map<String, String> clientData = new HashMap<>();
            clientData.put("nomClient", nomClient);
            clientData.put("emailClient", emailClient);

            ResponseEntity<String> response = restTemplate.postForEntity(createClientUrl, clientData, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
                redirectAttributes.addFlashAttribute("idClient", responseBody.get("idClient"));
                redirectAttributes.addFlashAttribute("nomClient", responseBody.get("nomClient"));
                redirectAttributes.addFlashAttribute("emailClient", responseBody.get("emailClient"));

                response = restTemplate.postForEntity(notificationUrl, clientData, String.class);
                if(response.getStatusCode().is2xxSuccessful()){
                    redirectAttributes.addFlashAttribute("notif", "Vous avez reçu un mail de confirmation.");
                }

                return "redirect:/home";
            } else {
                model.addAttribute("error", "Erreur : La connexion a échoué.");
                model.addAttribute("nomClient", nomClient);
                model.addAttribute("emailClient", emailClient);
                return "createClient";
            }
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
