#ifndef PARSE_H
#define PARSE_H

#include "../client.h"

/**
 * @version 1.0
 * Parse le message et remplie le tableau passé en arguments avec les différentes parties du message
 * Soit : [type_du_message, numero_du_message, identifiant, contenu]
 * @param res Le tableau à remplir
 * @param message Le message à parser (de type [DIFF])
 **/
void parse_diff(char **res, char *message);

/**
 * @version 1.0
 * Parse le message et remplie le tableau passé en arguments avec les différentes parties du message
 * Soit : [type_du_message, identifiant, ip_de_multidiffusion, port_de_multidiffusion, port_tcp]
 * @param res Le tableau à remplir
 * @param message Le message à parser (de type [ITEM])
 **/
void parse_item(char **res, char *message);

/**
 * @version 1.0
 * Parse le message et remplie le tableau passé en arguments avec les differentes parties du message
 * Soit : [type_du_message, num_du_message, identifiant, contenu_du_message]
 * @param res Le tableau à remplir
 * @param message Le message à parser (de type [OLDM])
 **/
void parse_oldm(char **res, char *message);

#endif
