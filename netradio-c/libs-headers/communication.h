#ifndef COMMUNICATION_H
#define COMMUNICATION_H

#include "../client.h"

/**
 * @version 1.0
 * Interroge le gestionnaire pour avoir la liste des diffuseurs puis l'affiche
 **/
void get_manager_list();

/**
 * @version 1.0
 * Récupère les n derniers messages au diffuseur spécifié
 * @param message Le message brut du client
 **/
void get_last_messages(char *message);

/**
 * @version 1.0
 * Envoie le message au diffuseur spécifié
 * @param message Le message brut du client
 **/
void send_message(char *message);

#endif
