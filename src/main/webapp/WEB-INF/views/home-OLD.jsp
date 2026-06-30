<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Accueil">
    <div class="info-client">
        <p><strong>${nomClient}</strong> </p>
        <p>${notif}</p>
    </div>
    <h2>Bienvenue dans l'application GESCOM</h2>
    <p class="welcome-msg">Veuillez choisir une action :</p>

    <div style="display: flex; flex-direction: column;">
        <form action="${commandeUrl}" method="get">
            <input type="submit" value="Nouvelle commande" style="width: 100%;">
        </form>

        <form action="${rechercheCommandeUrl}" method="get">
            <input type="submit" value="Rechercher commande"  style="background-color: var(--orange); margin-top: 20px; width: 100%;">
        </form>

        <form action="login" method="get">
            <input type="submit" value="Quitter l'application"  style="background-color: lightgrey; margin-top: 20px; width: 100%;">
        </form>
    </div>
</t:layout>
