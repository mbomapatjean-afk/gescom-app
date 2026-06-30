<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="Connexion">
    <h2>Bienvenue sur GESCOM</h2>
    <p class="welcome-msg">Veuillez vous identifier</p>
    <p class="error-msg" style="color: red; text-align: center">${error}</p>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">Identifiant :</label>
        <input type="text" id="username" name="username" placeholder="Votre identifiant" value="${nomClient}">

        <label for="password">Mot de passe :</label>
        <div class="password-container">
            <input type="password" id="password" name="password" placeholder="Votre mot de passe" value="${emailClient}" required>
            <i class="fas fa-eye" id="togglePassword"></i>
        </div>

        <input type="submit" value="SE CONNECTER">
    </form>

    <script>
        const togglePassword = document.querySelector('#togglePassword');
        const password = document.querySelector('#password');

        togglePassword.addEventListener('click', function (e) {
            // basculer le type d'input
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);
            // basculer l'icône
            this.classList.toggle('fa-eye-slash');
        });
    </script>

    <form action="createClient" method="get">
        <input type="submit" value="Créer un nouveau compte"  style="background-color: var(--orange); width: 100%;">
    </form>
</t:layout>
