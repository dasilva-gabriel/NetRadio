#include "../libs-headers/config.h"

ssize_t setup(char *buf, size_t line_size, FILE *fd, int min, int max) {
    memset(buf, '\0', line_size);
    ssize_t length = getline(&buf, &line_size, fd);

    if (length == -1) {
        print_error(errno, "Configuration file is invalid. No more lines (\"config.c\": line 5).");
    }
    if (buf[length - 1] == '\n') {
        buf[--length] = '\0';
    }
    if (length < min || length > max) {
        return -1;
    }
    return length;
}

char *get_id(char *buf, size_t line_size, FILE *fd) {
    ssize_t length = setup(buf, line_size, fd, 1, 8);
    char *res = malloc(sizeof(char) * 9);
    
    if (length == -1) {
        free(res);
        return NULL;
    }
    if (res == NULL) {
        print_error(errno, "Malloc Error (\"config.c\": line 21).");
    }
    memset(res, '\0', 9);
    strcpy(res, buf);
    if (length != 8) {
        for (int i = length; i < 8; i++) {
            res[i] = '#';
        }
    }
    return res;
}

char *get_ip(char *buf, size_t line_size, FILE *fd) {
    ssize_t length = setup(buf, line_size, fd, 7, 15);
    char *res = malloc(sizeof(char) * 16);
    char *tok_buf;
    int index = 0;

    if (length == -1) {
        free(res);
        return NULL;
    }
    if (res == NULL) {
        print_error(errno, "Malloc Error (\"config.c\": line 42).");
    }
    memset(res, '\0', 15);
    tok_buf = strtok(buf, ".");

    for (int i = 0; i < 4; i++) {
        if (tok_buf == NULL) {
            free(res);
            return NULL;
        }
        int size = strlen(tok_buf);

        if (size < 1 || size > 3) {
            free(res);
            return NULL;
        }
        for (int j = 0; j < size; j++) {
            if (!isdigit(tok_buf[j])) {
                free(res);
                return NULL;
            }
            res[index++] = tok_buf[j];
        }
        if (i != 3) {
            res[index++] = '.';
        }
        tok_buf = strtok(NULL, ".");
    }
    if (strtok(NULL, ".") != NULL) {
        free(res);
        return NULL;
    }
    erase_leading_zero_from_ip(res);
    return res;
}

char *get_port(char *buf, size_t line_size, FILE *fd) {
    ssize_t length = setup(buf, line_size, fd, 4, 4);
    char *res = malloc(sizeof(char) * 5);
    
    if (length == -1) {
        free(res);
        return NULL;
    }
    if (res == NULL) {
        print_error(errno, "Malloc Error (\"config.c\": line 89).");
    }
    memset(res, '\0', 5);
    for (int i = 0; i < 4; i++) {
        if (!isdigit(buf[i])) {
            free(res);
            return NULL;
        }
    }
    strcpy(res, buf);
    return res;
}

char *get_path(char *buf, size_t line_size, FILE *fd) {
    ssize_t length = setup(buf, line_size, fd, 1, 512);
    char *res = malloc(sizeof(char) * (length+ 1));

    if (length == -1) {
        free(res);
        return NULL;
    }
    if (res == NULL) {
        print_error(errno, "Malloc Error (\"config.c\": line 111).");
    }
    memset(res, '\0', (length + 1));
    strcpy(res, buf);
    return res;
}

void parse_config(char *path) {
    FILE *fd = fopen(path, "r");
    size_t line_size = 512;
    char *buf = malloc(sizeof(char) * line_size);

    if (fd == NULL){
        free(buf);
        print_error(errno, "Cannot open the configuration file. Invalid path (\"config.c\": line 126).");
    }
    if (buf == NULL) {
        print_error(errno, "Malloc Error (\"config.c\": line 128).");
    }
    ID = get_id(buf, line_size, fd);
    if (ID == NULL) {
        printf("\nInvalid format: Incorrect ID.\n");
        free(buf);
        fclose(fd);
        exit(1);
    }
    MANAGER_IP = get_ip(buf, line_size, fd);
    if (MANAGER_IP == NULL) {
        printf("\nInvalid format: Incorrect manager IP.\n");
        free_all(2, buf, ID);
        fclose(fd);
        exit(1);
    }
    MANAGER_PORT = get_port(buf, line_size, fd);
    if (MANAGER_PORT == NULL) {
        printf("\nInvalid format: Incorrect manager port.\n");
        free_all(3, buf, ID, MANAGER_IP);
        fclose(fd);
        exit(1);
    }
    PATH_TO_DISPLAY = get_path(buf, line_size, fd);
    if (PATH_TO_DISPLAY == NULL) {
        printf("\nInvalid format: Incorrect path.\n");
        free_all(4, buf, ID, MANAGER_IP, MANAGER_PORT);
        fclose(fd);
        exit(1);
    }
    free(buf);
    fclose(fd);
}

void init_lists() {
    BROADCASTERS = malloc(sizeof(struct broad_list *));
    THREADS = malloc(sizeof(struct thread_list *));

    if (BROADCASTERS == NULL || THREADS == NULL) {
        print_error(errno, "Malloc Error (\"config.c\": line 170-171).");
    }
    BROADCASTERS->first = NULL;
    THREADS->first = NULL;
}
