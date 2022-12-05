package common.protocols.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import common.packets.PacketID;

/**
 * Annotation qui permet de "tag" les méthodes qui écoutent la lecture d'un
 * paquet. Lorsque qu'un paquet est reçu on fait appel a toutes les méthodes qui
 * seront taguer par cette annotation.
 */

// Spécifie que l'annotation ne peut être faites que sur des méthode
@Target(value = ElementType.METHOD)

// Spécifie que l'interprétation se fait par la JVM
@Retention(RetentionPolicy.RUNTIME)

public @interface ListeningPacket {

	public PacketID packetID();

}
