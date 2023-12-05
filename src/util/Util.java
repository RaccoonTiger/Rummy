package util;

/**
 * @author atoudeft
 *
 */
public class Util {

	/**
	 * G�n�re un nombre entier al�atoire situ� dans un intervalle.
	 * @param min La valeur minimale de l'intervalle
	 * @param max La valeur maximale de l'intervalle
	 * @return nombre entier al�atoire entre min, inclus, et max, exclu
	 */
	public static int getNbAleatoireEntre(int min, int max) {
		return (int)(Math.random() * (max-min) + min);
	}

	/**
	 * G�n�re un nombre entier al�atoire situ� dans un intervalle commen�ant � 0
	 * @param max La valeur maximale de l'intervalle
	 * @return nombre entier al�atoire entre 0, inclus, et max, exclu
	 */
	public static int getNbAleatoireEntre(int max) {
		return getNbAleatoireEntre(0, max);
	}
}
