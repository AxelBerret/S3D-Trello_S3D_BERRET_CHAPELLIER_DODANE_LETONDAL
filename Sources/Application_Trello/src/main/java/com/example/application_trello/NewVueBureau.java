package com.example.application_trello;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

import static javafx.application.Application.launch;

public class NewVueBureau extends HBox implements Observateur {

    private ArrayList<VueColonne> listColVue;
    private HBox rightHBox;
    private Tableau t;

    public NewVueBureau(Sujet t) {
        VBox leftVBox = new VBox(100);
        leftVBox.setAlignment(Pos.CENTER);
        this.t = (Tableau) t;

        for (int i = 1; i <= 5; i++) {
            Hyperlink link = new Hyperlink("Link " + i);
            link.setStyle("-fx-font-size: 14;-fx-padding: 50; -fx-border-color: transparent; -fx-background-color: transparent;");
            link.setStyle("-fx-text-fill: black; -fx-underline: none;");
            link.setOnMouseEntered(e -> link.setStyle(" -fx-background-color: white; -fx-text-fill: black;"));
            link.setOnMouseExited(e -> link.setStyle("-fx-background-color: transparent; -fx-text-fill: black;"));

            link.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
            VBox.setMargin(link, new Insets(30));
            link.setPadding(new Insets(10, 30, 10, 30));
            leftVBox.getChildren().add(link);
        }

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        VBox rightVBox = new VBox(20);
        rightVBox.setPadding(new Insets(20));
        rightVBox.setAlignment(Pos.CENTER);

        this.rightHBox = new HBox(20);

        //Création des 3 colonnes par défaut
        this.listColVue = new ArrayList<>(); //On initialise la liste de vues colonnes
        Colonne aFaire = new Colonne("A faire");//On crée un objet colonne
        VueColonne columnAfaire = createColumn(aFaire);//On le passe en paramètre de la méthode créer une colonne
        this.listColVue.add(columnAfaire);//On l'ajoute a l'attribut de la liste des colonnes présentes
        this.t.ajouterColonne(aFaire);
        Colonne enCours = new Colonne("En cours");
        VueColonne columnEnCours = createColumn(enCours);
        this.listColVue.add(columnEnCours);
        this.t.ajouterColonne(enCours);
        Colonne termine = new Colonne("Terminé");
        VueColonne columnTermine = createColumn(termine);
        this.listColVue.add(columnTermine);
        this.t.ajouterColonne(termine);
        this.t.enregistrerObservateur(columnTermine);

        // On ajoute des tâches par défaut à la première colonne pour servir d'exemple
        columnAfaire.addTask("Tache 1");
        this.t.ajouterTache("Tache 1", "A faire");
        columnAfaire.addTask("Tache 2");
        this.t.ajouterTache("Tache 2", "A faire");

/*
        LocalDate dateDebut = LocalDate.of(2022, 1, 1);
        LocalDate dateFin = LocalDate.of(2022, 1, 9);
        TacheSimple tache = new TacheSimple("Tâche 1" );
        tache.setDateDebut(dateDebut);
        tache.setDateFin(dateFin);
        columnAfaire.addTask(String.valueOf(tache));
*/



        // Colonne pour créer une nouvelle colonne
        Colonne cAjout = new Colonne("Ajout");
        VueColonne colonneAjout = createAddColumn(cAjout);

        rightHBox.setMargin(colonneAjout, new Insets(0, 0, 0, 50));

        Button vueListeButton = new Button("Vue Liste");
        vueListeButton.setStyle("-fx-font-size: 16; -fx-padding: 10 50; -fx-background-radius: 30 30 30 30; -fx-background-color: white; -fx-text-fill: black;");
        VBox.setMargin(vueListeButton, new Insets(30));
        vueListeButton.setOnMouseEntered(e -> vueListeButton.setStyle("-fx-font-size: 16; -fx-padding: 10 50; -fx-background-radius: 30 30 30 30; -fx-background-color: black; -fx-text-fill: white;"));
        vueListeButton.setOnMouseExited(e -> vueListeButton.setStyle("-fx-font-size: 16; -fx-padding: 10 50; -fx-background-radius: 30 30 30 30; -fx-background-color: white; -fx-text-fill: black;"));

        vueListeButton.setOnAction(event -> afficherVueListe());

        Button ganttButton = new Button("Création du Gantt");
        ganttButton.setStyle("-fx-font-size: 16; -fx-padding: 10 50; -fx-background-radius: 30 30 30 30; -fx-background-color: white; -fx-text-fill: black;");
        VBox.setMargin(ganttButton, new Insets(30));
        ganttButton.setOnMouseEntered(e -> ganttButton.setStyle("-fx-font-size: 16; -fx-padding: 10 50; -fx-background-radius: 30 30 30 30; -fx-background-color: black; -fx-text-fill: white;"));
        ganttButton.setOnMouseExited(e -> ganttButton.setStyle("-fx-font-size: 16; -fx-padding: 10 50; -fx-background-radius: 30 30 30 30; -fx-background-color: white; -fx-text-fill: black;"));
        ganttButton.setOnAction(event -> afficherVueGantt());
        this.setSpacing(20);
        this.getChildren().addAll(leftVBox, separator, rightVBox, vueListeButton, ganttButton);
        this.setStyle("-fx-background-color: linear-gradient(to top, rgba(50,0,255,0.45), rgba(200,0,200,0.45)); -fx-background-radius: 0;");
        rightVBox.getChildren().addAll(rightHBox);
    }

    private void afficherVueListe() {
        // Créez la VueListe en utilisant le premier objet Colonne (ici, la première colonne de la liste)
        if (!listColVue.isEmpty()) {
            VueListe vueListe = new VueListe(t, listColVue.get(0).getNomVueColonne());

            // Créez une nouvelle scène pour la VueListe
            Scene scene = new Scene(vueListe, 600, 600);
            Stage stage = new Stage();
            stage.setScene(scene);


            // Affichez la fenêtre
            stage.show();
        }
    }

    private void afficherVueGantt() {
        // Créez la VueGantt
        VueGantt vueGantt = new VueGantt(t);

        // Créez une nouvelle scène pour la VueGantt
        Scene scene = new Scene(vueGantt, 600, 600);
        // Créez une nouvelle fenêtre (Stage)
        Stage stage = new Stage();
        stage.setScene(scene);

        // Affichez la fenêtre
        stage.show();
    }




    @Override
    public void actualiser(Sujet s){
        System.out.println("Lancement de actualiser de newVueBureau");
        //On récupère les colonnes du modèle au moment où la méthode actualiser est appelée
        ArrayList<Colonne> listeCol = ((Tableau)s).getListeColonnes();
        ArrayList<String> listeNomCol = new ArrayList<>();
        for (Colonne c : listeCol) {
            listeNomCol.add(c.getNomColonne());// On crée une liste de noms des colonnes pour simplifier les comparaisons
        }
        for (Colonne c : listeCol){//On parcours les colonnes du modèle
            if (!containsColumn(c.getNomColonne())){//Si une colonne n'est pas présente dans cette vue, on l'ajoute. (On utilise une méthode auxiliaire pour simplifier le code)
                VueColonne vc = createColumn(c);//et la méthode la crée graphiquement
                ((Tableau)s).enregistrerObservateur(vc);//et on l'enregistre dans le modèle.
            }
        }
        for (VueColonne c : this.listColVue) {// On parcourt les colonnes de la vue
            String nomC = c.getNomVueColonne();// Pour chaque colonne, on extrait son nom afin de pouvoir comparer
            if (!listeNomCol.contains(nomC)) {// Si une colonne de la vue n'est plus présente dans le modèle, on la supprime.
                this.removeColumn(c);
            }
        }
    }

    private VueColonne createColumn(Colonne colonne) {// Cette méthode ajoute un objet colonne dans les données et renvoie une vueColonne
        VueColonne columnVBox = new VueColonne(colonne.getNomColonne(), this.t);
        // gestionnaire d'événements pour le drag and drop
        setDragDropHandlers(columnVBox);
        rightHBox.getChildren().add(columnVBox);
        return columnVBox;
    }

    private void removeColumn(VueColonne colonne) {
        this.listColVue.remove(colonne);
        this.rightHBox.getChildren().remove(colonne);
    }

    private VueColonne createAddColumn(Colonne colonne) {
        VueColonne specialColumnVBox = new VueColonne(colonne.getNomColonne(), this.t);
        // Ajoutez un gestionnaire d'événements pour le drag-and-drop
        setDragDropHandlers(specialColumnVBox);

        return specialColumnVBox;
    }

    // Méthode pour définir les gestionnaires d'événements de glisser-déposer
    private void setDragDropHandlers(VueColonne columnVBox) {
        columnVBox.setOnDragDetected(event -> {
            // Commence le glisser-déposer
            Dragboard db = columnVBox.startDragAndDrop(TransferMode.MOVE);

            // Ajoute les données à transférer (ici, le nom de la tâche)
            ClipboardContent content = new ClipboardContent();
            content.putString(columnVBox.getTaskName());
            db.setContent(content);

            event.consume();
        });

        columnVBox.setOnDragOver(event -> {
            // Autorise le déplacement si les données sont du type attendu
            if (event.getGestureSource() != columnVBox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        columnVBox.setOnDragDropped(event -> {
            // Récupère le contenu des données déposées
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                // Ajoute la tâche à la colonne actuelle
                columnVBox.addTask(db.getString());
                success = true;
            }

            // Indique que le déplacement est terminé
            event.setDropCompleted(success);
            event.consume();
        });

        columnVBox.setOnDragDone(DragEvent::consume);
    }

    private boolean containsColumn(String columnName) {
        for (VueColonne vueColonne : this.listColVue) {
            if (vueColonne.getNomVueColonne().equals(columnName)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch();
    }
}
