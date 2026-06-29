<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Création Carte">
    <h2>Bienvenue Client GESCOM</h2>
    <div class="info-client">
        <p><strong>Nom & Prénoms :</strong> ${nomClient}</p>
        <p><strong>Email :</strong> ${emailClient}</p>
        <p><strong>N°Carte Magnétique :</strong> ${numCarte}</p>
        <p><strong>Date Expiration Carte :</strong> ${dateExpiration}</p>
    </div>

    <p class="welcome-msg">Veuillez choisir une action :</p>
    <div style="display: flex; flex-direction: column; ">
        <form action="${commandeUrl}" method="get">
            <input type="submit" value="Passer commande" style="width: 100%;">
        </form>
        <form action="login" method="get">
            <input type="submit" value="Quitter l'application"  style="background-color: var(--orange); margin-top: 20px; width: 100%;">
        </form>
    </div>
</t:layout>
