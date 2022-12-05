#include "../libs-headers/broadcaster.h"

int is_broadcaster_present(char *id) {
    struct broadcaster *current = BROADCASTERS->first;

    while (current != NULL) {
        if (strcmp(current->id, id) == 0) {
            return 1;
        }
        current = current->next;
    }
    return 0;
}

void add_new_broadcaster(char *message) {
    char id[9];

    memset(id, '\0', 9);
    memcpy(id, &message[2], 8);

    if (is_broadcaster_present(id) != 0) {
        printf("\nERROR: You are already subscribed to this broadcaster.\n");
        return;
    }
    struct broadcaster *new_broadcaster = malloc(sizeof(struct broadcaster));

    if (new_broadcaster == NULL) {
        print_error(errno, "Malloc Error (\"broadcaster.c\": line 25).");
    }
    int sock = connect_tcp(MANAGER_IP, MANAGER_PORT);
    int ok = 0;
    char buf[512];
    char *res[6];
    char type[5];
    char count[3];

    if (sock == -1) {
        print_error(errno, "Cannot connect the socket to the specified address (\"broadcaster.c\": line 30).");
    }
    allocate_memory(res, 6, 5, 9, 16, 5, 16, 5);
    memset(buf, '\0', 512);
    memset(type, '\0', 5);
    memset(count, '\0', 3);
    memset(new_broadcaster, '\0', sizeof(struct broadcaster));
    if (send(sock, "LIST\r\n", strlen("LIST\r\n"), 0) == -1) {
        print_error(errno, "Cannot send a message to the manager (\"broadcaster.c\": line 42).");
    }
    int received = recv(sock, buf, 9, 0);

    if (received == -1) {
        print_error(errno, "Received nothing from the manager (\"broadcaster.c\": line 47).");
    }
    memcpy(type, &buf[0], 4);
    memcpy(count, &buf[5], (strlen(buf) == 6) ? 1 : 2);
    if (strcmp(type, "LINB") != 0) {
        printf("\nERROR: This manager does not respect the protocol.\n");
        // Here don't care about the free: the manager is invalid
        exit(1);
    }
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
        if (strcmp(res[1], id) == 0) {
            ok = 1;
            init_new_broadcaster(new_broadcaster);
            strcpy(new_broadcaster->id, res[1]);
            strcpy(new_broadcaster->mult_ip, res[2]);
            strcpy(new_broadcaster->mult_port, res[3]);
            strcpy(new_broadcaster->tcp_ip, res[4]);
            strcpy(new_broadcaster->tcp_port, res[5]);
        }
    }
    if (ok == 0) {
        printf("\nERROR: The ID does not belong to any broadcaster registered to the manager.\n");
        close(sock);
        free_all(7, res[0], res[1], res[2], res[3], res[4], res[5], new_broadcaster);
        return;
    }
    new_broadcaster->next = BROADCASTERS->first;
    BROADCASTERS->first = new_broadcaster;

    init_thread(new_broadcaster);
    close(sock);
    free_all(6, res[0], res[1], res[2], res[3], res[4], res[5]);
    printf("\nSuccessfully subscribed to the broadcaster [%s]!\n", new_broadcaster->id);
}

void delete_broadcaster(int direct_id, char *message) {
    char id[9];

    memset(id, '\0', 9);
    if (direct_id == 0) {
        memcpy(id, &message[2], 8);
    } else {
        strcpy(id, message);
    }

    if (is_broadcaster_present(id) != 1) {
        printf("\nERROR: The ID is incorrect (maybe you are not subscribed to this broadcaster).\n");
        return;
    }
    struct thread_infos *thread_current = THREADS->first;
    struct broadcaster *broad_current = BROADCASTERS->first;

    if (strcmp(thread_current->id, id) == 0) {
        THREADS->first = thread_current->next;
        if (pthread_kill(thread_current->thread, SIGUSR1) == -1) {
            print_error(errno, "Cannot kill a thread (\"broadcaster.c\": line 115).");
        }
        free(thread_current);
    } else {
        while (thread_current != NULL) {
            if (thread_current->next != NULL) {
                if (strcmp(thread_current->next->id, id) == 0) {
                    struct thread_infos *to_kill = thread_current->next;

                    thread_current->next = thread_current->next->next;
                    if (pthread_kill(to_kill->thread, SIGUSR1) == -1) {
                        print_error(errno, "Cannot kill a thread (\"broadcaster.c\": line 126).");
                    }
                    free(to_kill);
                    break;
                }
            }
            thread_current = thread_current->next;
        }
    }
    if (strcmp(broad_current->id, id) == 0) {
        BROADCASTERS->first = broad_current->next;
        close(broad_current->fd);
        close(broad_current->sock);
        free_all(5, broad_current->thread_ressources[0], broad_current->thread_ressources[1], broad_current->thread_ressources[2], broad_current->thread_ressources[3], broad_current->thread_ressources);
        free_all(6, broad_current->id, broad_current->mult_ip, broad_current->mult_port, broad_current->tcp_ip, broad_current->tcp_port, broad_current);
    } else {
        while (broad_current != NULL) {
            if (broad_current->next != NULL) {
                if (strcmp(broad_current->next->id, id) == 0) {
                    struct broadcaster *to_free = broad_current->next;

                    broad_current->next = broad_current->next->next;
                    close(to_free->fd);
                    close(to_free->sock);
                    free_all(5, to_free->thread_ressources[0], to_free->thread_ressources[1], to_free->thread_ressources[2], to_free->thread_ressources[3], to_free->thread_ressources);
                    free_all(6, to_free->id, to_free->mult_ip, to_free->mult_port, to_free->tcp_ip, to_free->tcp_port, to_free);
                    break;
                }
            }
            broad_current = broad_current->next;
        }
    }
    printf("\nSuccessfully unsubscribed to the broadcaster [%s]!\n", id);
}

void display_followed_broadcasters() {
    struct broadcaster *current = BROADCASTERS->first;

    if (current == NULL) {
        printf("\nYou are not following a broadcaster yet...\n");
        return;
    }
    printf("\n---=== Followed broadcasters list: ===---\n\n");
    printf("- [ID] -> [IP1]~[PORT1] and [IP2]~[PORT2] ~This is an example~\n");
    while (current != NULL) {
        printf("- [%s] -> [%s]~[%s] and [%s]~[%s]\n", current->id, current->mult_ip, current->mult_port, current->tcp_ip, current->tcp_port);
        current = current->next;
    }
    printf("\n---===================================---\n");
}
