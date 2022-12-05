#ifndef BROADCASTER_H
#define BROADCASTER_H

#include "../client.h"

/**
 * @version 1.0
 * Parcours la liste des diffuseurs "suivis", et renvoie 1 si le diffuseur est présent, et 0 sinon
 * @param id L'ID du diffuseur recherché
 * @return Un entier entre 0 et 1
 **/
int is_broadcaster_present(char *id);

/**
 * @version 1.0
 * Ajoute un nouveau diffuseur à la liste des diffuseurs "suivis" (si celui-ci n'est pas déjà suivis, et que l'id fourni est correct)
 * @param message Le message brut du client
 **/
void add_new_broadcaster(char *message);

/**
 * @version 1.0
 * Supprime un diffuseur de la liste des diffuseurs "suivis" (si celui-ci est suivi, et que l'id fourni est correct)
 * @param direct_id Si le message est brut (input du client), alors mettre direct_id à 0, sinon (message = id) le mettre à 1
 * @param message Le message brut du client ou l'id direct
 **/
void delete_broadcaster(int direct_id, char *message);

/**
 * @version 1.0
 * Affiche la liste de tous les diffuseurs suivis
 **/
void display_followed_broadcasters();

#endif
