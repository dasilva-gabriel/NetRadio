#include "client.h"

char *ID = NULL;
char *MANAGER_IP = NULL;
char *MANAGER_PORT = NULL;
char *PATH_TO_DISPLAY = NULL;

struct broad_list *BROADCASTERS = NULL;
struct thread_list *THREADS = NULL;

void help() {
    printf("\n---=== Commands list: ===---\n\n");
    printf("- \"Q\": Quit the program\n");
    printf("- \"L\": List the broascasters\n");
    printf("- \"N [id] [number]\": Ask the specified broadcaster for the last messages (number has to be between 0 and 999 and the broadcaster has to be in the personnal list)\n");
    printf("- \"M [id] [content]\": Add a message to the specified broadcaster (the message has to be under 140 characters and the broadcaster has to be in the personnal list)\n");
    printf("- \"A [id]\": Subscribe to the broadcaster\n");
    printf("- \"D [id]\": Unsubscribe from the broadcaster\n");
    printf("- \"P\": Display the followed broadcasters\n");
    printf("- \"H\": Display the commands list (this one)\n");
    printf("\n---======================---\n");
}

void help_retarded_user(char *input) {
    printf("\nInvalid input: \"%s\" is not a recognized command.\n", input);
    help();
}

void listen_to_usr_inputs() {
    size_t line_size = 512;
    char *buf = malloc(sizeof(char) * line_size);
    int end = 0;

    if (buf == NULL) {
        print_error(errno, "Malloc Error (\"client.c\": line 31).");
    }
    help();
    while (1) {
        if (end != 0) {
            break;
        }
        printf("\n> ");
        memset(buf, '\0', line_size);
        getline(&buf, &line_size, stdin);

        if (buf[strlen(buf) - 1] == '\n') {
            buf[strlen(buf) - 1] = '\0';
        }
        int length = strlen(buf);

        switch (buf[0]) {
            case 'Q':
                if (length == 1) {
                    end++;
                    printf("\nBye! (Wait a few seconds until all communications are closed and all ressources are free'd).\n");
                    free_broadcasters();
                } else {
                    help_retarded_user(buf);
                }
                break;
            case 'L':
                if (length == 1) {
                    get_manager_list();
                } else {
                    help_retarded_user(buf);
                }
                break;
            case 'N':
                if (length >= 12 && length <= 14) {
                    get_last_messages(buf);
                } else {
                    printf("\nERROR: Either:\n - the ID is incorrect (not 8 characters).\nOr:\n - the number is incorrect (not between 0 and 999).\n");
                }
                break;
            case 'M':
                if (length >= 12 && length <= 151 && (are_char_in_ascii_128(buf) == 1)) {
                    send_message(buf);
                } else {
                    printf("\nERROR: Either:\n - the ID is incorrect (not 8 characters).\nOr:\n - the message is incorrect (not between 1 and 140 ASCII-128 characters).\n");
                }
                break;
            case 'A':
                if (length == 10) {
                    add_new_broadcaster(buf);
                } else {
                    help_retarded_user(buf);
                }
                break;
            case 'D':
                if (length == 10) {
                    delete_broadcaster(0, buf);
                } else {
                    help_retarded_user(buf);
                }
                break;
            case 'P':
                if (length == 1) {
                    display_followed_broadcasters();
                } else {
                    help_retarded_user(buf);
                }
                break;
            case 'H':
                if (length == 1) {
                    help();
                } else {
                    help_retarded_user(buf);
                }
                break;
            default:
                help_retarded_user(buf);
                break;
        }
    }
    free(buf);
}

int main(int ac, char **av) {
    if (ac != 2) {
        printf("Invalid format: Please give the path to the configuration file.\n");
        exit(1);
    }
    parse_config(av[1]);
    init_lists();
    listen_to_usr_inputs();
    free_all(6, ID, MANAGER_IP, MANAGER_PORT, PATH_TO_DISPLAY, BROADCASTERS, THREADS);
    sleep(1);
    pthread_exit(0);
}
