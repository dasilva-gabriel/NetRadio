BUILD_DIR = build/

CC_JAVA = javac
RUN = java
BUILD_JAVA_DIR = $(BUILD_DIR)java/
JAVA_MAIN = NetRadio

CC_C = gcc
CFLAGS = -g3 -Wall -pthread

default: clear dir make

clear:
	touch client
	rm -rf $(BUILD_DIR)
	rm client

clean: clear

dir:
	mkdir $(BUILD_DIR) && mkdir $(BUILD_JAVA_DIR)

make:
	@echo "Building Java project... (Diffuser & Diffuser Manager)"
	find -name "*.java" > sources.txt
	$(CC_JAVA) @sources.txt -d $(BUILD_JAVA_DIR)
	rm sources.txt
	@echo "Building C project... (Client)"
	find -name "*.c" > sources.txt
	$(CC_C) -o client @sources.txt $(CFLAGS)
	rm sources.txt

run:
	cd $(BUILD_JAVA_DIR) && $(RUN) $(JAVA_MAIN) $(ARGS)
