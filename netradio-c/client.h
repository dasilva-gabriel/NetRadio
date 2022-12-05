#ifndef CLIENT_H
#define CLIENT_H

#define _OPEN_THREADS

#include <arpa/inet.h>
#include <ctype.h>
#include <errno.h>
#include <fcntl.h>
#include <netdb.h>
#include <netinet/in.h>
#include <pthread.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

// Déclaration des structures
struct broadcaster {
    int sock;
    int fd;
    char *id;
    char *mult_ip;
    char *mult_port;
    char *tcp_ip;
    char *tcp_port;
    char **thread_ressources;
    struct broadcaster *next;
};

struct thread_infos {
    char *id;
    pthread_t thread;
    struct thread_infos *next;
};

struct broad_list {
    struct broadcaster *first;
};

struct thread_list {
    struct thread_infos *first;
};

// Déclaration des variables globales
extern char *ID;
extern char *MANAGER_IP;
extern char *MANAGER_PORT;
extern char *PATH_TO_DISPLAY;
extern struct broad_list *BROADCASTERS;
extern struct thread_list *THREADS;

// Import des librairies APRES déclaration des structures et des variables globales (et des autres imports)
#include "libs-headers/broadcaster.h"
#include "libs-headers/communication.h"
#include "libs-headers/config.h"
#include "libs-headers/mult.h"
#include "libs-headers/parse.h"
#include "libs-headers/utils.h"

/**
 * @version 1.0
 * Affiche la liste des commandes disponibles et une courte description
 **/
void help();

/**
 * @version 1.0
 * Mauvais input de l'utilisateur
 **/
void help_retarded_user(char *input);

/**
 * @version 1.0
 * Ecoute les entrées utilisateur et agit en conséquence
 * Liste des entrées "reconnues":
 * Q : quitte le programme
 * L : demande au gestionnaire la liste des diffuseurs
 * N [id] [number] : demande au diffuseur specifié les [number] derniers messages (à condition que number soit compris entre 0 et 999 et que l'ID soit correct et qu'il fasse partie de la liste)
 * M [id] [message] : envoie un nouveau message au diffuseur specifié (à condition que le message respecte la spec et que l'ID soit correct et qu'il fasse partie de la liste)
 * A [id] : ajoute le diffuseur à la liste des diffuseurs "suivis" (à condition que l'ID soit correct et qu'il ne fasse pas partie de la liste)
 * D [id] : supprime le diffuseur de la liste des diffuseurs "suivis" (à condition que l'ID soit correct et qu'il fasse partie de la liste)
 * P : affiche la liste des diffuseurs "suivis"
 * H : affiche la liste des inputs "reconnus"
 **/
void listen_to_usr_inputs();

#endif
