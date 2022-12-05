#include "../libs-headers/mult.h"

void sig_handler(int signum) {
    if (signum != SIGUSR1) {
        printf("\nERROR: Unexpected signal received.\n");
        // Here we don't care about the free: the handler don't have to cancel the signal
        exit(1);
    }
    pthread_exit(0);
}

void *mult_reception(void *args) {
    struct broadcaster *infos = (struct broadcaster *) args;
    struct sigaction action;
    int sock = connect_udp(infos->mult_ip, infos->mult_port);
    int fd = open(PATH_TO_DISPLAY, O_WRONLY);
    char **res = infos->thread_ressources;
    char buf[512];
    int received;

    if (fd == -1) {
        print_error(errno, "Invalid path to the display terminal (\"mult.c\": line 16).");
    }
    memset(&action, '\0', sizeof(struct sigaction));
    action.sa_handler = sig_handler;
    if (sigaction(SIGUSR1, &action, NULL) == -1) {
        print_error(errno, "Cannot handle the signal SIGUSR1 (\"mult.c\": line 26).");
    }
    if (sock == -1) {
        printf("\nERROR: Cannot connect the socket to [%s:%s].\n", infos->mult_ip, infos->mult_port);
        delete_broadcaster(1, infos->id);
        while (1) {
            // just wait for the thread to be killed
        }
    }
    infos->fd = fd;
    infos->sock = sock;
    while (1) {
        memset(buf, '\0', 512);
        received = recv(sock, buf, 161, 0);
        if (received != 161 || (are_char_in_ascii_128(buf) == 0)) {
            printf("\nERROR: The broadcaster [%s] does not respect the protocol.\n", infos->id);
            delete_broadcaster(1, infos->id);
            break;
        }
        parse_diff(res, buf);
        if (strcmp(res[0], "DIFF") != 0) {
            printf("\nERROR: The broadcaster [%s] does not respect the protocol.\n", infos->id);
            delete_broadcaster(1, infos->id);
            break;
        }
        char str[177];

        memset(str, '\0', 177);
        strcpy(str, "[");
        strcat(str, res[0]);
        strcat(str, "] (N. ");
        strcat(str, res[1]);
        strcat(str, ") from \"");
        strcat(str, res[2]);
        strcat(str, "\" -> ");
        strcat(str, res[3]);
        strcat(str, "\n");
        if (write(fd, str, 177) == -1) {
            print_error(errno, "Cannot write into the display terminal (\"mult.c\": line 64).");
        }
    }
    return NULL;
}

void init_thread(struct broadcaster *infos) {
    struct thread_infos *new_thread = malloc(sizeof(struct thread_infos));
    pthread_t display;
    pthread_attr_t attr;

    if (new_thread == NULL) {
        print_error(errno, "Malloc Error (\"mult.c\": line 51).");
    }
    memset(new_thread, '\0', sizeof(struct thread_infos));
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED);
    int fd_th = pthread_create(&display, &attr, mult_reception, infos);

    if (fd_th != 0) {
        print_error(fd_th, "Cannot create a thread (\"mult.c\": line 61).");
    }
    new_thread->id = infos->id;
    new_thread->thread = display;
    new_thread->next = THREADS->first;
    THREADS->first = new_thread;
}
