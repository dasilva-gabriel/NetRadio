## Indications

### Notation

Ce document détaille le bon fonctionnement du programme, ainsi que des instructions de compilation et d'exécution. 
Afin de rendre votre lecture plus facile, voici quelques détails sur certains caractères utilisés :
* Les caractères compris entre `<>` indique que c'est à l'utilisateur de compléter ces champs (par exemple : `<identifiant>` signifie que l'utilisateur doit renseigner son identifiant).
* Nous utilisons parfois le terme `manager` pour désigner le `gestionnaire`. 
* De la même façon, le terme `broadcaster` correspond à un `diffuseur`.
* Il peut manquer des accents dans les documentations et dans ce rapport (é, è, ê, î, à, ô, û, ù, etc...) : certains d'entres nous sont en QWERTY.

### Prérequis

Plusieurs points sont à noter afin de garantir le bon fonctionnement de notre programme :
* les identifiants et messages acceptés doivent faire partis de la table ASCII 128 : en effet, un caractère en dehors de cette table est encodé sur plusieurs octets en C, ce qui implique que la valeur renvoyée par `strlen` ne correspondra pas à la valeur renvoyée par `length` en Java, et donc que lors de l'ajout des caractères `#`, il se peut que les identifiants ou messages n'ai pas le bon nombre de `#` (respectivement 8 et 140), ou qu'un message reçu soit considéré "hors-protocole" car ne respectant pas les conditions de tailles (par exemple, `strlen("test°") = 5` là ou `"test°".length() = 4"`).
* vous ne devez à AUCUN MOMENT redéfinir le comportement du programme face au signal `SIGUSR1` (je ne sais pas pourquoi vous le feriez, mais on le précise au cas où).

### Séparation C/Java

Nous avons choisi de développer certaines entitées en Java, à savoir le gestionnaire et le diffuseur, et de faire le client en C.

Au niveau des fichiers sources, ils sont séparés en deux dossiers : 
* un dossier `netradio-java` pour le gestionnaire et le diffuseur
* un dossier `netradio-c` pour le client

Pour la compilation, nous avons fait le choix d'un unique Makefile : plus pratique et plus rapide.

Dans le cas où ce rapport ne répondrait pas à une de vos questions, n'hésitez pas à contacter l'un d'entre nous (voir coordonnées ci-dessous).

## Identifiants

| Prénom | NOM | 
| --- | --- | --- | 
| Gabriel | DA SILVA |
| Hugo | DEPREZ | 
| Fabio | DE SOUSA LIMA |

## Compilation & Exécution

### Compilation

Pour compiler le projet, tapez simplement :
```
make
```
Vous pouvez également nettoyer le répertoire avec :
```
make clean
```

### Exécution

#### Client

Pour lancez le client, il faut d'abord remplir le fichier de configuration comme détaillé ci-dessous :
```
<identifiant>
<ip_du_gestionnaire>
<port_du_gestionnaire>
<chemin_du_terminal_d'affichage>
```
* L'identifiant doit contenir entre 1 et 8 caractères (en dessous de 8, il sera automatiquement complété par des `#`).
* L'IP du gestionnaire correspond... à l'IP de la machine sur laquelle tourne le gestionne (vous n'avez pas besoin de la compléter par des `0`).
* Le port du gestionnaire correspond... au port sur lequel tourne le gestionnaire.
* Pour le dernier paramètre, un peu d'explication : nous avons fait le choix d'afficher les messages des diffuseurs dans un autre terminal pour améliorer la lisibilité. Vous devez donc fournir le chemin d'un terminal afin que le programme puisse écrire dans celui-ci. Pour ce faire, ouvrez un terminal (par défaut, `Ctrl + Alt + t`), puis tapez :
```
tty
```
Copiez ensuite la ligne dans le fichier de configuration.
Au passage, si vous faites tourner plusieurs client en même temps, évitez d'utiliser le même terminal d'affichage : cela fonctionne dans certains cas mais pas tout le temps : en effet, nous écrivons via la fonction `write`, et bien que l'écriture soit garantie de façon atomique entre des threqds d'un même programme, ça ne l'est pas entre des programmes différents, et il se peut que l'affichage devienne vite incompréhensible.

Une fois le fichier de configuration rempli, vous pouvez lancer le client en faisant :
```
./client <chemin_vers_le_fichier_de_configuration>
```
Le client vous propose alors une liste de commande : le client est lancé !
Pour quitter, tapez `Q`.

#### Diffuseur

Pour le diffuseur, le lancement se fait grâce au Makefile. Vous pouvez choisir d'utiliser les arguments ou un fichier de configuration. Pour le lancement via arguments, tapez :
```
make ARGS="DIFFUSER <identifiant> <port_de_multidiffusion> <ip_de_multidiffusion> <port_de_communication> <fréquence_de_diffusion> <mode_debug>" run
```
Avec les arguments suivants :
* L'identifiant doit contenir entre 1 et 8 caractères (en dessous de 8, il sera automatiquement complété par des `#`).
* Pas besoin d'expliquer les significations des ports de multidiffusion et de communication, simplement assurez vous qu'ils soient valides.
* L'IP de multidiffusion n'a pas besoin d'être complétée par des `0`.
* La fréquence de diffusion doit être spécifiée en millisecondes (donc rentrez 1000 pour que le diffuseur diffuse toutes les 1 secondes).
* Le mode débug peut valoir `true` ou `false` : si il est activé, le diffuseur affichera ce qui se passe de façon interne (réception d'un message, envoie d'un packet...).
Pour le lancement via le fichier de configuration, tapez simplement :
```
make ARGS="DIFFUSER <chemin_vers_le_fichier_de_configuration>" run
```
Avec un fichier de configuration contenant les informations suivantes (contenant exactement ce qui était décrit plus tôt) :
```
<identifiant>
<port_de_multidiffusion>
<ip_de_multidiffusion>
<port_de_communication>
<fréquence_de_diffusion>
<mode_debug>
```

#### Gestionnaire

Le gestionnaire s'exécute sur le même principe :
```
make ARGS="MANAGER <port_de_communication> <nombre_de_diffuseurs_max> <fréquence_de_verification> <mode_debug>" run
```
Où bien pour la version avec un fichier de configuration :
```
make ARGS="MANAGER <chemin_vers_le_fichier_de_configuration>" run
```
Avec un fichier de configuration comme détaillé ci-dessous:
```
<port_de_communication>
<nombre_de_diffuseurs_max>
<fréquence_de_verification>
<mode_debug>
```
* Le port de communication correspond au port sur lequel le gestionnaire écoute les requêtes.
* Le nombre de diffuseurs max correspond au nombre de diffuseurs que le gestionnaire peut accepter.
* La fréquence de vérification correspond à la fréquence d'envoi des messages de vérification pour savoir si un diffuseur est toujours connecté, et est exprimée en millisecondes également.
* Le mode debug correspond à la même chose que pour le diffuseur.

## Client

### Structure globale

Tout le code C est inclus dans le dossier `netradio-c`.

Celui-ci comporte plusieurs fichiers et répertoires :
* `client.c` : le fichier "principal" du client (dans le sens où il contient le `main`)
* `client.h` : le header "principal" : il contient la totalité des `include` (hors include de headers), la définition des structures utilisées, la définition des variables globales, et la documentation des rares fonctions de `client.c`
* `libs/` : le répertoire de toutes les sources du client soient :
  * `broadcaster.c` : les fonctions en rapport avec la gestion des diffuseurs par le client (s'abonner, se désabonner, est-ce que ce diffuseur est déjà "suivis" par le client...)
  * `communication.c` : les fonctions qui gèrent la communication avec les diffuseurs et le gestionnaire (envoyer un message à un diffuseur, recevoir la liste des diffuseurs d'un gestionnaire...)
  * `config.c` : les fonctions qui s'occupent de parser le fichier de configuration et de vérifier que tout est de la bonne forme 
  * `mult.c` : les fonctions associées à la répection des messages des diffuseurs (multidiffusion) et du lancement des threads (voir plus bas)
  * `parse.c` : les fonctions de "parsing" des messages reçus (les messages de type [ITEM], [OLDM]...)
  * `utils.c` : les fonctions "utiles" (allocations de mémoire multiples, gestion des erreurs, écriture dans un autre terminal, `free` multiples...)
* `libs-headers/` : à chaque "librairie", on associe son header, qui contient un lien vers les autres fonctions du client (un include de `client.h`), et les documentations de chaque fonction (pas besoin d'une liste comme au dessus : un header de la forme `xxx.h` correspond au fichier `xxx.c`)

### Fonctionnement

Dans un premier temps, le client "parse" le fichier de configuration et rentre dans sa boucle d'écoute.

#### Boucle d'écoute

Celle-ci est assez simple : on lit les entrées de l'utilisateur, et si cela correspond à quelque chose de "reconnu", on lance la fonction associée. Sinon, on affiche la liste des commandes disponibles. 

Les commandes sont identifiées par le premier caractère : cela limite donc le nombre de commandes mais nous nous sommes dit qu'avant d'atteindre le stade où il n'y aurait plus assez de caractères ASCII pour stocker toutes nos commandes, on aurait le temps de développer quelque chose de plus évolué...

Si le premier caractère correspond à une commande reconnue, le programme teste ensuite la taille de l'entrée de l'utilisateur, mais pas son contenu : ce test est délégué à la fonction appellée si l'entrée est reconnue et de la bonne taille.

De ce fait, il est assez simple d'ajouter de nouvelles commandes : il suffit d'ajouter une `case` dans la boucle d'écoute, et d'y associer une fonction.

#### Communication avec les diffuseurs

Pour communiquer avec un diffuseur, il suffit de connaitre son identifiant. Cela permet de ne pas laisser l'utilisateur rentrer lui-même les informations "sensibles" du diffuseur (son IP, ses ports etc). Cela implique évidement, à chaque abonnement, de vérifier que l'utilisateur n'est pas déjà abonné à ce diffuseur, et de demander la liste des diffuseurs au gestionnaire pour vérifier que l'identifiant est correct.

Les diffuseurs auxquels le client est "abonné" sont stockés dans une liste : cela permet de tester facilement si un abonnement/désabonnement est possible, et d'accéder à leurs informations rapidement via leur identifiant (un peu comme un dictionnaire).

Comme vous avez du vous en douter, le client ne supporte qu'un gestionnaire, tout simplement pour éviter un diffuseur enregistré sur plusieurs gestionnaire, ou tout simplement deux diffuseurs avec le même identifiant sur des gestionnaires différents : comme spécifié plus haut, le client reconnaît les diffuseurs grâce à leur identifiant. Autoriser plusieurs diffuseurs à avoir le même identifiant reviendrait à devoir abandonner notre système de reconnaissance d'un diffuseur via son identifiant, et obligerait donc l'utilisateur à rentrer toutes les informations du diffuseur...

#### Gestion des messages multidiffusés

A chaque enregistrement d'un diffuseur, un thread est créé afin de gérer la réception des messages.
Evidement, celui-ci est stoppé si l'utilisateur demande à se désabonner du diffuseur.

Le thread sont détachés du thread principal, et garantissent ainsi de libérer leur mémoire une fois leur exécution terminée. Le principal problème de ce client a été la façon de gérer leur clotûre : nous avons d'abord pensé à une variable globale, qui contenqit les identifiants des threads qui devaient se fermer, et de faire en sorte qu'avant chaque diffusion de message que le thread vérifiait si son identifiant n'était pas dans cette variable. Néanmoins un problème s'est rapidement posé : les threads ne faisait ce check qu'à la réception d'un message, et si un diffuseur avait une fréquence trop élevée, le thread mettait trop longtemps à se fermer. Nous avons donc pensé à transformer la socket qui gère la réception de messages en non-bloquante, et ainsi écouter constament la variable globale. Cependant, la variable étant partagée, l'accès au verrou était trop fréquent et causait des problèmes de performances pour certains threads qui n'y avait jamais accès. Nous avons donc opté pour quelque chose de plus radical : envoyer un signal `SIGUSR1` au thread, après avoir redéfini son comportement par rapport à celui-ci lors de sa création. Mais il nous restait un problème : la mémoire allouée DANS le thread était perdue... C'est pourquoi nous allouons la mémoire AVANT de créer le thread, et lui passons en paramètres, afin de la libérer lors de la suppression d'un thread.

Les messages diffusés sont de la forme :
```
[type_du_message] (N. numero_du_message) from "identifiant" -> message
```
Ici, le type du message est toujours [DIFF] (le choix a été laissé pour d'éventuelles extensions).
L'identifiant peut être celui d'un diffuseur, ou bien celui d'un utilisateur (dans le cas des messages personnalisés).

### Mémoire et erreurs

#### Gestion de la mémoire

Un point relativement bref (mais qui mérite néanmoins sa place dans un rapport sur un projet mené en partie en C). La gestion de la mémoire a été assez dure à mettre en place dans un programme multithreadé (nous n'avions jamais fait ça), mais le résultat est là : aucune fuite de mémoire (check via `valgrind --leak-check=full --show-leak-kinds=all --track-origins=yes` en version 3.16.1) !

#### Gestion des erreurs

Le client distingue trois types d'erreurs :
* faibles : une mauvaise entrée de l'utilisateur
* dérangeantes : un diffuseur qui ne respecte pas le protocole
* critiques : une allocation de mémoire qui échoue, une réponse du gestionnaire qui ne suis pas le protocole, un mauvais chemin fourni dans le fichier de configuration...

Elles sont gérées différement :
* faibles : on indique simplement à l'utilisateur qu'il s'est trompé 
* dérangeantes : on clôt la communication avec le diffuseur en question
* critiques : fin du programme

### Commandes

Voici une liste des commandes détaillées (vous pouvez retrouver cette liste en tapant simplement `H` une fois le client lancée) :
* `Q` : quitte le programme (cela peut prendre un peu de temps, il faut attendre que tout les threads finissent et que toutes les ressources allouées soient libérées
* `L` : interroge le gestionnaire et affiche la liste des diffuseurs ([LIST])
* `N <identifiant> <nombre>` : demande au diffuseur correspondant à l'identifiant fourni ses derniers messages  (il faut que le nombre fourni soit valide, et que l'identifiant corresponde à un diffuseur suivi)
* `M <identifiant> <message>` : envoie un message personnalisé au diffuseur correspondant à l'identifiant fourni (il faut que le message fasse entre 1 et 140 caractères et que l'identifiant corresponde à un diffuseur suivi)
* `A <identifiant>` : s'abonne au diffuseur correspondant à l'identifiant indiqué (si celui-ci ne fait pas déjà partie des diffuseurs suivis et que le diffuseur est enregistré auprès du gestionnaire)
* `D <identifiant>` : se désabonne du diffuseur spécifié (si celui-ci fait partie des diffuseurs suivis)
* `P` : affiche la liste des diffuseurs suivis
* `H` : affiche la liste des commandes avec une courte description (comme ici mais dans un anglais approximatif)

# Diffuseur & Gestionnaire

Les deux entités Diffuseur (nommé `diffuser`) et Gestionnaire (nommé `manager`) ont été effectuées dans un seul et même projet Java. Le projet se sépare alors en 3 partie:

 - Partie commune (ou `common`) - [lien](#commun)
 -  Partie du Diffuseur (ou `diffuser`) - [lien](#diffuseur-1)
 -  Partie du Gestionnaire (ou `manager`) - [lien](#gestionnaire-1)

## Commun

### NetPacket: système de paquets (ou packets)
Les paquets (envoyés comme reçus) sont tous énumérés dans la classe `common.packets.PacketID`, dans laquelle chaque paquet est associé a sa classe. En effet, chaque paquet est représenté par une unique classe qui étend de la classe abstraite `common.packets.NetPacket`.
Ainsi a chaque réception d'un message, nous pourrons lui associé sa classe qui pourra traiter ce dernier en suivant le protocole, et le modifier a tout moment.

### PacketListener: traitement et invocation des méthodes
Dès lorsqu'un message est reçu par l'entité, nous nous devons de connaitre le `NetPacket` décrit dans ce message pour en invoquer la méthode qui traitera le message.
Pour cela, au lancement du programme, les entités enregistre les classes qui contiennent des méthodes annotées avec un `@ListeningPacket` (`common.protocols.listener.ListeningPacket`) qui correspondent a une méthode qui traite un certain paquet. Ces méthodes annotées, seront invoquées a chaque réception du paquet qui leurs sont associé.

## Diffuseur
### La communication
Le gestionnaire doit gérer 3 méthode de réception de paquets: la communication avec le client, la communication avec le gestionnaire et les entrées de l'utilisateur.

#### La communication avec le client
Le client communique avec le diffuseur via son port d'écoute TCP. Cette communication est gérée par la classe `diffuser.tasks.TCPTask` qui est une classe Runnable. Dès qu'il y a une connexion a ce port, le traitement de la connexion va se faire dans le Thread `diffuser.handlers.ReceptionRunnable`. En effet, dans ce Thread on va y attendre les messages, et dès lorsqu'un message est transmis, il sera analysé par le PacketListener (cf. [ici](#packetlistener-traitement-et-invocation-des-m%C3%A9thodes)).

#### Les entrées de l'utilisateur
Avant de passer a la communication avec le gestionnaire, il faut parler des entrées de l'utilisateur. Puisque, c'est l'utilisateur qui va donner l'ordre au diffuseur de communiquer avec tel ou tel gestionnaire.
Pour communiquer avec un gestionnaire, l'utilisateur doit entrer une commande pour l'enregistrer avec ce dernier:

    REGI <IPv4> <port>

Dès qu'une entrée de cette forme est interprété par le Thread `diffuser.tasks.ConsoleTask` (qui s'occupe de lire les entrées utilisateur) c'est aux threads de communication avec les gestionnaire de prendre le relai.

#### La communication avec les gestionnaires
##### L'enregistrement auprès du gestionnaire
Lorsque la demande est effectuée par l'utilisateur, on a premièrement voir si la Socket est disponible et que le port est atteignable. Le diffuseur va donc ensuite lancé un Thread `diffuser.tasks.RegisterTask`. Ce dernier va alors agir de la manière suivante:  il va lancer un Thread qui va envoyé le packet `REGI`, il va ensuite attendre quelques secondes et va analyser le résultat de ce Thread.
Si le Thread n'a pas reçu de réponse, que le Thread ne s'est pas terminé (donc pas de réponse), ou que la réponse a été `RENO` alors on va renvoyé a l'utilisateur l'erreur. Sinon, si la réponse est bien `REOK` il va lancer le Thread de KeepAlive.

##### Le KeepAlive
Le gestionnaire se doit de vérifier que l'entité est toujours active. Dès que l'enregistrement a été effectué correctement, le Thread `diffuser.tasks.KeepAliveTask` va attendre en boucle de recevoir le paquet `RUOK`venant du gestionnaire afin de lui répondre avec un paquet `IMOK`

### Gestion des messages et diffusion
Les messages transmis via le paquet `MESS` sont enregistrés dans le gestionnaire de message `diffuser.handlers.messages.MessagesHandler` dans une liste de `Messages`. 

La diffusion de ces messages se fait via Multicast. Lors du lancement du diffuseur, un Thread est chargé de diffuser toutes les X millisecondes un message (`X` étant entré en argument, cf. [ici](#diffuseur)).
Dès lorsque un message vient a être diffusé, il est alors enregistré dans le gestionnaire de l'historique `diffuser.handlers.messages.history.HistoryHandler`. Il y est stocké avec un numéro `num_mess` dans une liste de `HistoryContent`.

## Gestionnaire
Le gestionnaire doit gérer 2 type de communication: la communication avec le client, la communication avec le diffuseur.

### La communication avec un diffuseur
Le diffuseur communique avec le gestionnaire via son port d'écoute TCP. Cette communication est gérée par la classe `manager.packets.ManagerProtocol` qui est une classe Runnable. Dès qu'il y a une connexion a ce port, le traitement de la connexion va se faire dans le Thread `manager.service.ServiceManager`. En effet, dans ce Thread on va y attendre les messages, et dès lorsqu'un message est transmis, il sera analysé par le PacketListener (cf. [ici](#packetlistener-traitement-et-invocation-des-m%C3%A9thodes)).
Le diffuseur attend une message de la forme `REGI id ip1 port1 ip2 port2` il stock ensuite les informations du diffuseur dans une liste.

#### Message RUOK
Lorsque le gestionnaire est lancé la class `manager.service.RUOKService` (Implemente Runnable) s'executera.
Le gestionnaire enverra donc toute les x secondes une message de la forme `RUOK` à tout les sockets stockées dans la liste de diffuseur et attendra une reponse de la forme `IMOK` sinon il fermera la socket et supprimera le diffuseur de sa liste.


### La communication avec le client
La communication avec le client fonctionne comme la communication avec le diffuseur, mais le gestionnaire ne peux recevoir que des message de la forme `LIST` de la part d'un client.
Le gestionnaire enverra deux message: `LINB nb_diffuseur` puis `ITEM id ip1 port1 ip2 port2` avec les informations de chaque diffuseur enregistré.
Après cela la socket se fermera.
