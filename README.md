# Jeu de Gestion de Ressources en Java ( AUTO MINER )

Ce projet consiste en la création d'un jeu de gestion de ressources en Java, où le joueur passe progressivement de la récolte manuelle de ressources à leur automatisation grâce à des machines. Le jeu est conçu en utilisant les bibliothèques `AWT` et `Swing` pour l'interface graphique et Java 11 pour la compilation.

## Description du jeu

Dans le jeu, le joueur évolue dans un monde où il peut récolter différentes ressources sur une carte, fabriquer de nouvelles ressources, vendre ces dernières et acheter des machines pour automatiser ces processus.

### Fonctionnalités principales

- **Carte du jeu** : Une carte générée aléatoirement avec des ressources de base, une case de démarrage et une case marché.
- **Système de récolte** : Le joueur peut récolter des ressources manuellement ou à l’aide de machines.
- **Inventaire et Craft** : Le joueur peut fabriquer de nouvelles ressources à partir des ressources récoltées.
- **Marché** : Le joueur peut vendre des ressources et acheter des machines pour automatiser la récolte ou la fabrication.
- **Machines** : Les machines peuvent être des récolteuses ou des usines, chacune avec des caractéristiques spéciales.
- **Sauvegardes de parties** : Le joueur peut sauvegarder et reprendre ses parties à tout moment.

## Prérequis

- **Java 11** : Le projet doit être compilé sous Java 11, sans utiliser de build tools comme Gradle ou Maven.
- **Bibliothèques** : Le projet utilise exclusivement les bibliothèques `AWT` et `Swing` pour l'interface graphique.

## Fonctionnalités détaillées

### 1. **Écran d'accueil**
L'écran d'accueil permet au joueur de :
- Poursuivre une partie sauvegardée.
- Démarrer une nouvelle partie.
- Accéder aux réglages du jeu.
- Quitter l'application.

### 2. **Écran de jeu**
L'écran de jeu affiche :
- La carte sous forme de grille , avec des ressources, des machines, un market et une case de démarage.
- Le joueur peut se déplacer avec les touches directionnelles ou cliquer sur une case vide pour se déplacer automatiquement avec le plus court chemin.
- Il peut récolter des ressources, interagir avec des machines et accéder à son inventaire.

### 3. **Inventaire et Craft**
Dans l'écran d'inventaire, le joueur peut :
- Voir ses ressources.
- Voir sa machine s'il en possède une ( il peut en posséder qu'une seule )
- Combiner des ressources pour créer de nouvelles ressources via des recettes (instantanées ou temporisées).
  
### 4. **Marché**
Le marché permet au joueur de :
- Acheter et vendre des ressources.
- Acheter des machines avec des caractéristiques comme la rapidité, la capacité XL, la polyvalence, ou la fragilité.

### 5. **Machines**
Les machines peuvent être :
- **Récolteuses** : Placées près des ressources pour récolter automatiquement.
- **Usines** : Produisent de nouvelles ressources en suivant des recettes, à partir des ingrédients fournis par le joueur.

### 6. **Sauvegarde**
Le jeu permet de sauvegarder l'état de la partie dans un fichier `Monde_n.txt` au format texte. Lors du prochain lancement, le joueur pourra choisir une sauvegarde de partie et la reprendre là où il l'a laissée.

## Comment exécuter le projet

1. Clonez ce projet sur votre machine locale :
    ```bash
    git clone <URL du dépôt>
    ```

2. Compilez et exécutez le projet en se mettant dans le fichier Projet-Java :
    ```bash
    javac -d bin -cp src src/Modele/*.java src/Vue/*.java src/Main.java
    ```
    Sous Windows : 
    ```bash
    java -cp "bin;.;assets" Main
    ```
    Sous Linux : 
    ```bash
    java -cp "bin:.:assets" Main
    ```

## Structure du projet

- `src/` : Contient les fichiers source Java organisés en packages :  
  - `Modele/` : Logique principale du jeu.  
    - `Enregistrer/` : Gestion des sauvegardes.  
    - `Machine/` : Modélisation des machines .  
    - `Resource/` : Ressources du jeu.  
    - Autres classes importantes : `Game.java`, `Player.java`, `Carte.java`, ...   
  - `Vue/` : Interface utilisateur .  

- `bin/` : Contient les fichiers compilés `.class`.  
- `Save/` : Dossier contenant les sauvegardes  .  
- `assets/` : Ressources additionnelles du jeu .  

    ```bash
    ├───assets
    ├───bin
    ├───Save
    └───src
        ├───Modele
        │   ├───Enregistrer
        │   ├───Machine
        │   └───Resource
        └───Vue
    ```


## Développement et Collaboration

- **Git** : Le projet est développé et versionné via Git.

## Auteurs

- **POTIER Nathan**
- **RAMANATHAN Jonas**

