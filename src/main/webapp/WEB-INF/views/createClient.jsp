<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Création Client">
    <h2>Création de compte GESCOM</h2>
    <p class="welcome-msg">Informations du client</p>
    <p class="error-msg"  style="color: red; text-align: center">${error}</p>

    <form action="saveClient" method="post">
        <label for="nomClient">Nom & Prénoms :</label>
        <input type="text" id="nomClient" name="nomClient" placeholder="Votre nom et prénoms" value="${nomClient}" required>

        <label for="emailClient">Email :</label>
        <input type="email" id="emailClient" name="emailClient" placeholder="Votre adresse mail" value="${emailClient}" required>

        <input type="submit" value="Créer Compte Client">
    </form>

    <form action="login" method="get">
        <input type="submit" value="Annuler opération"  style="background-color: var(--orange); margin-top: 20px; width: 100%;">
    </form>
</t:layout>
