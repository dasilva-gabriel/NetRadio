#ifndef MULT_H
#define MULT_H

#include "../client.h"

/**
 * @version 1.0
 * Réceptionne les messages du diffuseur et les affiches sur un autre terminal
 * Cette fonction est appelée dans un thread
 **/
void *mult_reception(void *args);

/**
 * @version 1.0
 * Initialise un thread pour la réception et l'affichage des messages d'un diffuseur
 * @param infos Les infos du diffuseur 
 **/
void init_thread(struct broadcaster *infos);

#endif
