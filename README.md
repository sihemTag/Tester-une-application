# Tester-une-application
Tester une application fullstack (Java/Angular)

# Backend
Pour lancer les tests backend il faut lancer la commande `mvn clean test`  
Le rapport de couverture généré se trouve dans le fichier `target\site\jacoco\index.html`

# Frontend
Executer d'abord la commande `npm install`  
Pour lancer les tests frontend il faut executer la commande : `npm run test`  
Le rapport de couverture généré se trouve dans le fichier `coverage\jest\lcov-report\index.html`

# End2End
Pour lancer les tests end to end il fault executer la commande : `e2e:coverage`  
Le rapport de couverture généré se trouve dans le fichier `coverage\lcov-report\index.html`
