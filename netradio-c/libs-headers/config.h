#ifndef CONFIG_H
#define CONFIG_H

#include "../client.h"

/**
 * @version 1.0
 * Récupère la prochaine chaîne de caractères dans le fichier de configuration, et vérifie qu'elle est de la bonne forme (seulement la taille)
 * @param buf Le buffer pour lire le fichier
 * @param line_size La taille de la ligne à lire
 * @param fd Le fichier à lire (sous forme de FILE *)
 * @param min La taille minimum que doit avoir la ligne
 * @param max La taille maximum que doit avoir la ligne
 * @return La taille de la ligne
 **/
ssize_t setup(char *buf, size_t line_size, FILE *fd, int min, int max);

/**
 * @version 1.0
 * Récupère l'ID du client dans le fichier de configuration, et vérifie qu'il est de la bonne forme (contenu), puis le renvoie
 * @param buf Le buffer pour lire le fichier
 * @param line_size La taille de la ligne à lire
 * @param fd Le fichier à lire (sous forme de FILE *)
 * @return L'ID dans la bonne forme
 **/
char *get_id(char *buf, size_t line_size, FILE *fd);

/**
 * @version 1.0
 * Récupère l'ip du manager dans le fichier de configuration, et vérifie qu'elle est de la bonne forme (contenu), puis la renvoie
 * @param buf Le buffer pour lire le fichier
 * @param line_size La taille de la ligne à lire
 * @param fd Le fichier à lire (sous forme de FILE *)
 * @return L'ip dans la bonne forme
 **/
char *get_ip(char *buf, size_t line_size, FILE *fd);

/**
 * @version 1.0
 * Récupère le port dans le fichier de configuration, et vérifie qu'il est de la bonne forme, puis le renvoie
 * @param buf Le buffer pour lire le fichier
 * @param line_size La taille de la ligne à lire
 * @param fd Le fichier à lire (sous forme de FILE *)
 * @return Le port dans la bonne forme
 **/
char *get_port(char *buf, size_t line_size, FILE *fd);

/**
 * @version 1.0
 * Récupère le path vers le deuxième terminal dans le fichier de configuration, et vérifie qu'il est de la bonne forme, puis le renvoie
 * @param buf Le buffer pour lire le fichier
 * @param line_size La taille de la ligne à lire
 * @param fd Le fichier à lire (sous forme de FILE *)
 * @return Le path dans la bonne forme
 **/
char *get_path(char *buf, size_t line_size, FILE *fd);

/**
 * @version 1.0
 * Parse le fichier de config, vérifie qu'il est de la bonne forme et initialises les variables globales (id, port de multidiffusion etc...)
 * @param path Le chemin vers le fichier de config
 **/
void parse_config(char *path);

/**
 * @version 1.0
 * Initialise la liste chaînée de diffuseurs et la liste chaînée de threads
 **/
void init_lists();

#endif
