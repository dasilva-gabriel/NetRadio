#include "../libs-headers/communication.h"

void get_manager_list() {
    int sock = connect_tcp(MANAGER_IP, MANAGER_PORT);
    char buf[512];
    char *res[6];
    char type[5];
    char count[3];

    if (sock == -1) {
        print_error(errno, "Cannot connect the socket to the specified address (\"communication.c\": line 11).");
    }
    allocate_memory(res, 6, 5, 9, 16, 5, 16, 5);
    memset(buf, '\0', 512);
    memset(type, '\0', 5);
    memset(count, '\0', 3);
    if (send(sock, "LIST\r\n", strlen("LIST\r\n"), 0) == -1) {
        print_error(errno, "Cannot send a message to the manager (\"communication.c\": line 14).");
    }
    int received = recv(sock, buf, 9, 0);

    if (received <= 0) {
        print_error(errno, "Received nothing from the manager (\"communication.c\": line 19).");
    }
    memcpy(type, &buf[0], 4);
    memcpy(count, &buf[5], (strlen(buf) == 6) ? 1 : 2);
    if (strcmp(type, "LINB") != 0) {
        printf("\nERROR: This manager does not respect the protocol.\n");
        // Here don't care about the free: the manager is invalid
        exit(1);
    }
    printf("\n---=== Broadcasters list: ===---\n\n");
    printf("- [ID] -> [IP1]~[PORT1] and [IP2]~[PORT2] ~This is an example~\n");
    for (int i = 0; i < atoi(count); i++) {
        memset(buf, '\0', 512);
        received = recv(sock, buf, 57, 0);
        if (received != 57 || (are_char_in_ascii_128(buf) == 0)) {
            printf("\nERROR: This manager does not respect the protocol.\n");
            // Here don't care about the free: the manager is invalid
            exit(1);
        }
        parse_item(res, buf);
        if (strcmp(res[0], "ITEM") != 0) {
            printf("\nERROR: This manager does not respect the protocol.\n");
            // Here don't care about the free: the manager is invalid
            exit(1);
        }
        printf("- [%s] -> [%s]~[%s] and [%s]~[%s]\n", res[1], res[2], res[3], res[4], res[5]);
    }
    printf("\n---==========================---\n");
    close(sock);
    free_all(6, res[0], res[1], res[2], res[3], res[4], res[5]);
}

void get_last_messages(char *message) {
    char send_id[9];
    char count[4];
    struct broadcaster *current = BROADCASTERS->first;
    int size = strlen(message) - 11;
    int acc = 1;

    memset(send_id, '\0', 9);
    memset(count, '0', 3);
    memcpy(send_id, &message[2], 8);
    for (int i = 3 - size; i < 3; i++) {
        count[i] = message[10 + acc++];
    }
    count[3] = '\0';
    if (is_broadcaster_present(send_id) != 1) {
        printf("\nERROR: the ID is incorrect (maybe you are not subscribed to this broadcaster).\n");
        return;
    }
    while (current != NULL) {
        if (strcmp(current->id, send_id) == 0) {
            break;
        }
        current = current->next;
    }
    int sock = connect_tcp(current->tcp_ip, current->tcp_port);
    char *to_send = malloc(sizeof(char) * 11);
    char buf[512];
    char *res[4];
    int received;

    if (sock == -1) {
        printf("\nERROR: Cannot connect the socket to [%s:%s].\n", current->tcp_ip, current->tcp_port);
        delete_broadcaster(1, send_id);
        free(to_send);
        return;
    }
    if (to_send == NULL) {
        print_error(errno, "Malloc Error (\"communication.c\": line 77).");
    }
    allocate_memory(res, 4, 5, 5, 9, 141);
    memset(to_send, '\0', 11);
    strcpy(to_send, "LAST ");
    strcat(to_send, count);
    strcat(to_send, "\r\n");
    if (send(sock, to_send, strlen(to_send), 0) == -1) {
        printf("\nERROR: The broadcaster [%s] is disconnected.\n", send_id);
        delete_broadcaster(1, send_id);
        close(sock);
        free(to_send);
        return;
    }
    printf("\n---=== Last messages: ===---\n\n");
    printf("- [OLDM] (N. num) from \"ID\" -> This is an example\n");
    for (int i = 0; i < atoi(count); i++) {
        memset(buf, '\0', 512);
        received = recv(sock, buf, 161, 0);
        if (strcmp(buf, "ENDM\r\n") == 0) {
            break;
        }
        if (received == -1) {
            printf("\nERROR: The broadcaster [%s] is disconnected.\n", send_id);
            delete_broadcaster(1, send_id);
            break;
        }
        if (received != 161 || (are_char_in_ascii_128(buf) == 0)) {
            printf("\nERROR: The broadcaster [%s] does not respect the protocol.\n", send_id);
            delete_broadcaster(1, send_id);
            break;
        }
        parse_oldm(res, buf);
        if (strcmp(res[0], "OLDM") != 0) {
            printf("\nERROR: The broadcaster [%s] does not respect the protocol.\n", send_id);
            delete_broadcaster(1, send_id);
            break;
        }
        printf("- [OLDM] (N. %s) from \"%s\" -> %s\n", res[1], res[2], res[3]);
    }
    printf("\n---======================---\n");
    close(sock);
    free_all(5, to_send, res[0], res[1], res[2], res[3]);
}

void send_message(char *message) {
    char send_id[9];
    struct broadcaster *current = BROADCASTERS->first;

    memset(send_id, '\0', 9);
    memcpy(send_id, &message[2], 8);
    if (is_broadcaster_present(send_id) != 1) {
        printf("\nERROR: The ID is incorrect (maybe you are not subscribed to this broadcaster).\n");
        return;
    }
    while (current != NULL) {
        if (strcmp(current->id, send_id) == 0) {
            break;
        }
        current = current->next;
    }
    int sock = connect_tcp(current->tcp_ip, current->tcp_port);
    char *to_send = malloc(sizeof(char) * 157);
    char buf[512];

    if (sock == -1) {
        printf("\nERROR: Cannot connect the socket to [%s:%s].\n", current->tcp_ip, current->tcp_port);
        delete_broadcaster(1, send_id);
        free(to_send);
        return;
    }
    if (to_send == NULL) {
        print_error(errno, "Malloc Error (\"communication.c\": line 145).");
    }
    memset(to_send, '\0', 157);
    strcpy(to_send, "MESS ");
    strcat(to_send, ID);
    strcat(to_send, " ");
    strcat(to_send, &message[11]);
    int length = strlen(to_send);

    for (int i = length; i < 154; i++) {
        to_send[i] = '#';
    }
    strcat(to_send, "\r\n");
    if (send(sock, to_send, strlen(to_send), 0) == -1) {
        printf("\nERROR: The broadcaster [%s] is disconnected.\n", send_id);
        delete_broadcaster(1, send_id);
        close(sock);
        free(to_send);
        return;
    }
    memset(buf, '\0', 512);
    if (recv(sock, buf, 6, 0) == -1) {
        printf("\nERROR: The broadcaster [%s] is disconnected.\n", send_id);
        delete_broadcaster(1, send_id);
        close(sock);
        free(to_send);
        return;
    }
    if (strcmp(buf, "ACKM\r\n") != 0) {
        printf("\nERROR: The broadcaster [%s] does not respect the protocol.\n", send_id);
        delete_broadcaster(1, send_id);
        close(sock);
        free(to_send);
        return;
    }
    close(sock);
    free(to_send);
    printf("\nYour message has been successfully delivered to the broadcaster [%s]!\n", send_id);
}
