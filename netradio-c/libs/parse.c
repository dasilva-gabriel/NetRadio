#include "../libs-headers/parse.h"

void parse_diff(char **res, char *message) {
    memset(res[0], '\0', 5);
    memset(res[1], '\0', 5);
    memset(res[2], '\0', 9);
    memset(res[3], '\0', 141);
    memcpy(res[0], &message[0], 4);
    memcpy(res[1], &message[5], 4);
    memcpy(res[2], &message[10], 8);
    memcpy(res[3], &message[19], 140);
    erase_hash(res[3]);
}

void parse_item(char **res, char *message) {
    memset(res[0], '\0', 5);
    memset(res[1], '\0', 9);
    memset(res[2], '\0', 16);
    memset(res[3], '\0', 5);
    memset(res[4], '\0', 16);
    memset(res[5], '\0', 5);
    memcpy(res[0], &message[0], 4);
    memcpy(res[1], &message[5], 8);
    memcpy(res[2], &message[14], 15);
    memcpy(res[3], &message[30], 4);
    memcpy(res[4], &message[35], 15);
    memcpy(res[5], &message[51], 4);
    erase_leading_zero_from_ip(res[2]);
    erase_leading_zero_from_ip(res[4]);
}

void parse_oldm(char **res, char *message) {
    memset(res[0], '\0', 5);
    memset(res[1], '\0', 5);
    memset(res[2], '\0', 9);
    memset(res[3], '\0', 141);
    memcpy(res[0], &message[0], 4);
    memcpy(res[1], &message[5], 4);
    memcpy(res[2], &message[10], 8);
    memcpy(res[3], &message[19], 140);
    erase_hash(res[3]);
}
