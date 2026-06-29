<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Création Carte">
    <h2>Création de compte GESCOM</h2>
    <div class="info-client">
        <p><strong>Nom & Prénoms :</strong> ${nomClient}</p>
        <p><strong>Email :</strong> ${emailClient}</p>
    </div>
    <p class="welcome-msg">Informations de la carte : </p>
    <form action="saveCard" method="post">
        <input type="hidden" name="nomClient" value="${nomClient}">
        <input type="hidden" name="emailClient" value="${emailClient}">
        <label for="numCarte">N°Carte Magnétique :</label>
        <input type="text" id="numCarte" name="numCarte" placeholder="XXXX XXXX XXXX XXXX" required>

        <label for="dateExpiration">Date Expiration Carte :</label>
        <input type="date" id="dateExpiration" name="dateExpiration" placeholder="jj/mm/aaaa" required>

        <input type="submit" value="Ajouter Carte">
    </form>

    <form action="login" method="get">
        <input type="submit" value="Annuler opération"  style="background-color: var(--orange); margin-top: 20px; width: 100%;">
    </form>
</t:layout>
