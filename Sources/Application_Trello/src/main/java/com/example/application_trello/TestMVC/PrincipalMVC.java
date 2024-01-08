package com.example.application_trello.TestMVC;

import com.example.application_trello.*;

public class PrincipalMVC {
    public static void main(String[] args) {
        // Création du modèle
        TableauTest tableau = new TableauTest("Tableau de Test");

        // Création de la vue
        VueTacheConsoleTest vue = new VueTacheConsoleTest();

        // Création du contrôleur et enregistrement de la vue comme observateur
        ControlCreationTacheTest controlCreationTache = new ControlCreationTacheTest(tableau);
        tableau.enregistrerObservateur(vue);

        // Simulation de la création d'une tâche
        controlCreationTache.creerTache("Colonne1", "Tâche1");
    }
}
