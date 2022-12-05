#ifndef UTILS_H
#define UTILS_H

#include "../client.h"

/**
 * @version 1.0
 * Affiche une description de l'erreur et quite le programme (à utiliser en cas d'erreur critique)
 * @param error_code Le code de l'erreur (99.999% des cas c'est errno mais au cas où je laisse le choix)
 * @param desc Un petite description de l'erreur
 **/
void print_error(int error_code, char *desc);

/**
 * @version 1.0
 * @param count Le nombre d'éléments à free
 * @param ... Les éléments à free 
 **/
void free_all(int count, ...);

/**
 * @version 1.0
 * Libère toutes les ressources associées au stockage des informations des diffuseurs auxquels le client est abonné
 **/
void free_broadcasters();

/**
 * @version 1.0
 * Alloue la mémoire nécessaire à chaque case du tableau de char *
 * @param res Le tableau 
 * @param size La taille du tableau
 * @param ... La taille de chaque case
 **/
void allocate_memory(char *res[], int size, ...);

/**
 * @version 1.0
 * Alloue la mémoire nécessaire à chaque emplacement de la structure
 * @param new_broadcaster La structure à initialiser
 **/
void init_new_broadcaster(struct broadcaster *new_broadcaster);

/**
 * @version 1.0
 * Se connecte à l'adresse spécifiée avec le port spécifié et renvoie la socket (à close évidement)
 * @param ip L'IP (sous forme de char *)
 * @param port Le port (sous forme de char *)
 * @return La socket ou -1 si le couple ip/port est invalide
 **/
int connect_tcp(char *ip, char *port);

/**
 * @version 1.0
 * Se connecte à l'adresse spécifiée avec le port spécifie et renvoie la socket (à close évidement)
 * @param ip L'IP de multidiffusion (sous forme de char *)
 * @param port Le port de multidiffusion (sous forme de char *)
 * @return La socket ou -1 si le couple ip/port est invalide
 **/
int connect_udp(char *ip, char *port);

/**
 * @version 1.0
 * Enlève les caracteres '#' du message
 * @param message Le message à modifier
 **/
void erase_hash(char *message);

/**
 * @version 1.0
 * Enlève les 0 inutiles de l'adresse ip
 * @param message L'adresse ip dont il faut enlever les 0
 **/
void erase_leading_zero_from_ip(char *message);

/**
 * @version 1.0
 * Verifie que tous les caractères font partis de la table ASCII 128
 * @param message Le message à vérifier
 * @return 0 si il y a des caractères non conformes, 1 sinon
 **/
int are_char_in_ascii_128(char *message);

#endif
