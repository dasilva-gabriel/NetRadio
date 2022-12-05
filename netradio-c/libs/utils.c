#include "../libs-headers/utils.h"

void print_error(int error_code, char *desc) {
    printf("\nAn error occured\nQuick description: %s\nError code: %i\nError code description: %s\n", desc, error_code, strerror(error_code));
    exit(error_code);
}

void free_all(int count, ...) {
    va_list args;
    va_start(args, count);

    for (int i = 0; i < count; i++) {
        free(va_arg(args, void *));
    }
    va_end(args);
}

void free_broadcasters() {
    struct thread_infos *thread_current = THREADS->first;
    struct broadcaster *broad_current = BROADCASTERS->first;

    while (thread_current != NULL) {
        struct thread_infos *to_kill = thread_current;

        thread_current = thread_current->next;
        if (pthread_kill(to_kill->thread, SIGUSR1) == -1) {
            print_error(errno, "Cannot kill a thread (\"utils.c\": line 26).");
        }
        free(to_kill);
    }
    while (broad_current != NULL) {
        struct broadcaster *to_free = broad_current;

        broad_current = broad_current->next;
        close(to_free->fd);
        close(to_free->sock);
        free_all(5, to_free->thread_ressources[0], to_free->thread_ressources[1], to_free->thread_ressources[2], to_free->thread_ressources[3], to_free->thread_ressources);
        free_all(6, to_free->id, to_free->mult_ip, to_free->mult_port, to_free->tcp_ip, to_free->tcp_port, to_free);
    }
}

void allocate_memory(char *res[], int size, ...) {
    va_list args;
    va_start(args, size);

    for (int i = 0; i < size; i++) {
        int temp = va_arg(args, int);

        res[i] = malloc(sizeof(char) * temp);

        if (res[i] == NULL) {
            print_error(errno, "Malloc Error (\"utils.c\": line 63).");
        }
        memset(res[i], '\0', temp);
    }
    va_end(args);
}

void init_new_broadcaster(struct broadcaster *new_broadcaster) {
    new_broadcaster->id = malloc(sizeof(char) * 9);
    new_broadcaster->mult_ip = malloc(sizeof(char) * 16);
    new_broadcaster->mult_port = malloc(sizeof(char) * 5);
    new_broadcaster->tcp_ip = malloc(sizeof(char) * 16);
    new_broadcaster->tcp_port = malloc(sizeof(char) * 5);
    new_broadcaster->thread_ressources = malloc(sizeof(char *) * 4);
    allocate_memory(new_broadcaster->thread_ressources, 4, 5, 5, 9, 141);

    if (new_broadcaster->id == NULL || new_broadcaster->mult_ip == NULL || new_broadcaster->mult_port == NULL || new_broadcaster->tcp_ip == NULL || new_broadcaster->tcp_port == NULL || new_broadcaster->thread_ressources == NULL) {
        print_error(errno, "Malloc Error (\"utils.c\": line 74-80).");
    }
    memset(new_broadcaster->id, '\0', 9);
    memset(new_broadcaster->mult_ip, '\0', 16);
    memset(new_broadcaster->mult_port, '\0', 5);
    memset(new_broadcaster->tcp_ip, '\0', 16);
    memset(new_broadcaster->tcp_port, '\0', 5);
}

int connect_tcp(char *ip, char *port) {
    int sock = socket(PF_INET, SOCK_STREAM, 0);
    struct sockaddr_in adress;

    if (sock == -1) {
        print_error(errno, "Cannot create a TCP socket (\"utils.c\": line 93).");
    }
    adress.sin_family = AF_INET;
    adress.sin_port = htons(atoi(port));
    inet_aton(ip, &adress.sin_addr);
    int resp = connect(sock, (struct sockaddr *) &adress, sizeof(struct sockaddr_in));

    if (resp == -1) {
        close(sock);
        return -1;
    }
    return sock;
}

int connect_udp(char *ip, char *port) {
    int sock = socket(PF_INET, SOCK_DGRAM, 0);
    struct sockaddr_in adress;

    if (sock == -1) {
        print_error(errno, "Cannot create an UDP socket (\"utils.c\": line 111).");
    }
    int ok = 1;
    int resp = setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &ok, sizeof(ok));
    
    if (resp == -1) {
        print_error(errno, "Cannot set option SO_REUSEADDR to the UDP socket (\"utils.c\": line 118).");
    }
    adress.sin_family = AF_INET;
    adress.sin_port = htons(atoi(port));
    adress.sin_addr.s_addr = htonl(INADDR_ANY);

    resp = bind(sock, (struct sockaddr *) &adress, sizeof(struct sockaddr_in));
    struct ip_mreq mreq;

    if (resp == -1) {
        close(sock);
        return -1;
    }
    mreq.imr_multiaddr.s_addr = inet_addr(ip);
    mreq.imr_interface.s_addr = htonl(INADDR_ANY);

    resp = setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq));
    if (resp == -1) {
        close(sock);
        return -1;
    }
    return sock;
}

void erase_hash(char *message) {
    int length = strlen(message);

    for (int i = 0; i < length; i++) {
        if (message[i] == '#') {
            message[i] = '\0';
        }
    }
}

void erase_leading_zero_from_ip(char *message) {
    char *new_buffer = malloc(sizeof(char) * 16);
    char *tok_buf = strtok(message, ".");
    int index = 0;

    if (new_buffer == NULL) {
        print_error(errno, "Malloc Error (\"utils.c\": line 154).");
    }
    memset(new_buffer, '\0', 16);
    for (int i = 0; i < 4; i++) {
        int found = 0;

        for (int j = 0; j < strlen(tok_buf); j++) {
            if (found != 0) {
                new_buffer[index++] = tok_buf[j];
            } else {
                if (tok_buf[j] != '0') {
                    found++;
                    new_buffer[index++] = tok_buf[j];
                }
            }
        }
        if (found == 0) {
            new_buffer[index++] = '0';
        }
        if (i != 3) {
            new_buffer[index++] = '.';
        }
        tok_buf = strtok(NULL, ".");
    }
    memset(message, '\0', 16);
    strcpy(message, new_buffer);
    free(new_buffer);
}

int are_char_in_ascii_128(char *message) {
    for (int i = 0; i < strlen(message); i++) {
        if (message[i] > 128) {
            return 0;
        }
    }
    return 1;
}
