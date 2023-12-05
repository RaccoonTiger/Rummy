package rummy;

import java.util.Arrays;
import java.util.Scanner;

import util.Util;

import java.util.Random;


/**
 * @author atoudeft
 *
 */
public class Rummy {

	//La pioche :
	public static Pioche pioche = new Pioche();
	
	//La table de jeu :
	//public static TableDeJeu tableDeJeu = new TableDeJeu();

	//Combinaisons qui sont sur la table :
	public static Piece[][] tableDeJeu = 
		new	Piece[Constantes.MAX_COMBINAISONS][Constantes.LONGUEUR_MAX_COMBINAISON];
	
	//Indique le nombre effectif de combinaisons sur la table :
	public static int nombreCombinaisonsSurLaTable = 0;
	
	//Indique le nombre de pieces dans chaque combinaison :
	public static int[] nombresPieces = new int[Constantes.MAX_COMBINAISONS];	
	
	// Pour les saisies au clavier en mode console:
	public static Scanner clavier = new Scanner(System.in);
	
	public static Joueur joueur1 = new Joueur(),
						 joueur2 = new Joueur(),
						 joueurActif;

	public static void main(String[] args) {

		System.out.print("Nom premier joueur : ");
		joueur1.nom = clavier.nextLine();
		System.out.print("Nom deuxième joueur : ");
		joueur2.nom = clavier.nextLine();
		
		initialiserPioche(pioche);
		melangerPioche(pioche);
		distribuerMain(pioche,joueur1,Constantes.TAILLE_MANNE_DEPART);
		distribuerMain(pioche,joueur2,Constantes.TAILLE_MANNE_DEPART);

		joueurActif = joueur1;
		while (!mainVide(joueur1) && !mainVide(joueur2)) {
			faireJouer(joueurActif);
			passerAuSuivant();
		}
		if (mainVide(joueur1)) {
			System.out.println("Partie terminée. Le gagnant est "+ joueur1.nom);
		} else {
			System.out.println("Partie terminée. Le gagnant est "+ joueur2.nom);
		}
	}
	
/***** Méthodes de déroulement du jeu *****/
	
	/**
	 * Donne le tour au joueur inactif, qui devient le joueur actif
	 */
	public static void passerAuSuivant() {

		if(joueur1 == joueurActif){
			joueurActif = joueur2;
		}
		else{
			joueurActif = joueur1;
		}
	}
	
	/**
	 * Fait jouer un tour au joueur.
	 * @param joueur
	 */
	public static void faireJouer(Joueur joueur) {

		// Pour etre sur de ne pas garder les inputs du joueur precedent on fait un nextLine()
		clavier.nextLine();

		String listeDuJoueur = null;
		String nouvelleCombinaison;
		int noDeLaCombinaison;
		String pieceChoisie;
		boolean valideEntree = false;
		boolean listePlacee = false;

		afficherTable();
		afficherMain(joueur);

		System.out.println("Choisir une liste de pieces dans votre main");
		listeDuJoueur = clavier.nextLine();
		valideEntree = saisieCorrecte(listeDuJoueur);

		while(valideEntree && listeDuJoueur != null && !listeDuJoueur.isEmpty()){
			Piece[] piecesListeJoueur = extrairePieces(listeDuJoueur);
			if(estUneCombinaison(piecesListeJoueur)){

				System.out.println("Voulez-vous constituez une nouvelle combinaison? (oui/non)");
				nouvelleCombinaison = clavier.nextLine();

				if(nouvelleCombinaison.equals("oui")){
					if(ajouterNouvelleCombinaisonALaTable(piecesListeJoueur)){
						// Piece[] piecesListeJoueur = extrairePieces(listeDuJoueur);
						for (int i = 0; i < joueur.manne.length; i++) {
							for (int j = 0; j < piecesListeJoueur.length; j++){

								if (joueur.manne[i] != null && piecesListeJoueur[j] != null){
									if(piecesListeJoueur[j].couleur == joueur.manne[i].couleur && piecesListeJoueur[j].numero == joueur.manne[i].numero){
										//vide ou nulle
										joueur.manne[i] = null;
									}
								}
							}
						}
					}
				}
				else{
					System.out.println("As-quelle combinaison sur la table vous voulez l'ajouter?");
					noDeLaCombinaison = clavier.nextInt();
					// On reduite le nombre entre par l'utilisateur de 1 tant qu'il est plus grand 0
					if (noDeLaCombinaison - 1 >= 0)
					{
						noDeLaCombinaison -= 1;

						if(ajouterPiecesALaCombinaison(piecesListeJoueur,noDeLaCombinaison)){
							for (int i = 0; i < joueur.manne.length; i++) {

								for (int j = 0; j < piecesListeJoueur.length; j++){

									if (joueur.manne[i] != null && piecesListeJoueur[j] != null){
										if(piecesListeJoueur[j].couleur == joueur.manne[i].couleur && piecesListeJoueur[j].numero == joueur.manne[i].numero){
											//vide ou nulle
											joueur.manne[i] = null;
										}
									}
								}

							}
						}
					}
					// Si il est plus petit que 0 cela veut dire que l'utilisateur veut creer une nouvelle combinaison
					else{
						if(ajouterNouvelleCombinaisonALaTable(piecesListeJoueur)){
							// Piece[] piecesListeJoueur = extrairePieces(listeDuJoueur);
							for (int i = 0; i < joueur.manne.length; i++) {
								for (int j = 0; j < piecesListeJoueur.length; j++){

									if (joueur.manne[i] != null && piecesListeJoueur[j] != null){
										if(piecesListeJoueur[j].couleur == joueur.manne[i].couleur && piecesListeJoueur[j].numero == joueur.manne[i].numero){
											//vide ou nulle
											joueur.manne[i] = null;
										}
									}
								}
							}
						}
					}

				}

				listePlacee = true;
			}

			afficherTable();
			afficherMain(joueur);

			System.out.println("Choisir une liste de pieces dans votre main");
			listeDuJoueur = clavier.nextLine();
			valideEntree = saisieCorrecte(listeDuJoueur);
		}

		if(!listePlacee){
			System.out.println("Choisir une de vos pièces qui sera échangée avec une pièce de la pioche");
			pieceChoisie = clavier.nextLine();
			afficherPieces(extrairePieces(pieceChoisie), extrairePieces(pieceChoisie).length);
			echanger(pioche, extrairePieces(pieceChoisie)[0]);
		}
	}
	
/***** Méthodes de manipulation de pièces *****/
	
	/**
	 * Retourne la valeur d'une piece.
	 * @param piece la pièce dont on retourne la valeur
	 * @return la valeur de la pièce
	 */
	public static int getValeur(Piece piece) {
		return piece.numero;
	}
	
	/**
	 * Retourne une chaine de caractère décrivant une piece.
	 * @param piece la pièce dont on retourne la représentation
	 * @return la chaine décrivant la pièce
	 */
	public static String toString(Piece piece) {
		return "["+piece.numero+""+piece.couleur+"]";
	}
	
	/**
	 * Retourne un tableau contenant les pièces décrites dans la saisie.
	 * Cette methode suppose que la saisie a été vérifiée.
	 * 
	 * @param saisie Chaîne de caractères décrivant une liste de pi�ces.
	 * 		  Exemple : 3V12J5V
	 * @return tableau contenant les pièces décrites dans la saisie.
	 */
	public static Piece[] extrairePieces(String saisie) {

		Piece[] extrait = new Piece[Constantes.LONGUEUR_MAX_MAIN];
		int compteurPieces = 0;

		// Cette fonction split est assez spéciale;
		// le regex assure qu'il sépare les lettres et nombres.
		// Ensuite, on met les éléments séparés dans un tableau.
		String[] tabSplit = saisie.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		for(int i=0;i<tabSplit.length;i++) {

			// Si impair, donc il va etre une lettre.
			if (i%2 == 1){
				// Puisqu'une lettre est toujours precede d'un nombre, on peut incrementer le compteur de pieces
				extrait[compteurPieces].couleur = tabSplit[i].charAt(0);
				compteurPieces++;
			}

			// Si pair, alors automatiquement il sera un nombre.
			if (i%2 == 0){
				try{
					Piece nouvPiece = new Piece();
					int nombre = Integer.parseInt(tabSplit[i]);
					nouvPiece.numero = nombre;
					extrait[compteurPieces] = nouvPiece;
				}
				catch(NumberFormatException exception){
					exception.printStackTrace();
				}
			}
		}
		// System.out.println(extrait);
		return extrait;

	}
	
	/**
	 * Ajouter une pièce à la main d'un joueur.
	 * @param joueur le joueur qui recevra la pièce dans sa main
	 * @param piece la pièce à ajouter
	 * @return true si la pièce a été ajoutée, false si la pièce n'a pas été
	 * 		   ajoutée, car la main est pleine.
	 */
	public static boolean ajouterPiece(Joueur joueur, Piece piece) {

		if(joueur.nombrePieces < Constantes.LONGUEUR_MAX_MAIN){

			joueur.manne[joueur.nombrePieces] = piece;
			return true;
		}
		return false;
	}	
		
	/**
	 * Ajouter une liste de piéces à une combinaison de la table de jeu.
	 * @param pieces tableau contenant les pièces à ajouter
	 * @param numeroCombinaison le numéro de la combinaison sur la table à
	 * 							laquelle les pièces vont être ajoutées. La
	 * 							première combinaison porte le numéro 1.
	 * @return true si toutes les pièces ont été ajoutées, false sinon.
	 */
	public static boolean ajouterPiecesALaCombinaison(Piece[] pieces, int numeroCombinaison) {
		Piece[] copie = pieces;

		if (estUneCombinaison(copie)){
			for (int i = 0; i < copie.length; i++){
				if (copie[i] != null) tableDeJeu[numeroCombinaison][i] = copie[i];
			}

			nombreCombinaisonsSurLaTable++;
			return true;
		}

		return false;
	}

	/**
	 * Ajouter une liste de pieces dans une nouvelle combinaison de la table de
	 * jeu.
	 * @param pieces tableau contenant les pièces composant la combinaison.
	 * @return true si la nouvelle combinaison a été ajoutée, false sinon.
	 */
	public static boolean ajouterNouvelleCombinaisonALaTable(Piece[] pieces) {

		// On vérifie si la combinaison est valide
		if (estUneCombinaison(pieces)){
			for (int i = 0; i < tableDeJeu.length; i++){
				// Si l'index 0 a ligne est vide cela veut dire que la reste de ligne aussi et qu'on peut y rajouter la nouvelle combinaison
				if (tableDeJeu[i][0] == null){
					for (int j = 0; j < tableDeJeu[i].length; j++){
						// On vérifie que la piece existe
						if (pieces[i] != null){
							tableDeJeu[i][j] = pieces[j];
							// On enlève la pièce de la main du joueur
							// pieces[j] = null;
						}
					}

					// Retourne true si on ajouter des pieces a la nouvelle combinaison a l'index i
					System.out.println("true");
					return true;
				}
			}
		}

		return false;
	}
	
/***** Méthodes de vérification *****/
	
	/**
	 * Vérifie si la main d'un joueur est vide ou non.
	 * @param joueur le joueur
	 * @return true si la main du joueur est vide, false sinon.
	 */
	public static boolean mainVide(Joueur joueur) {
		int compteurVide = 0;
		for(int i = 0; i<joueur.manne.length; i++) {
			if(joueur.manne[i] == null){
				compteurVide++;
			}
		}
		if (compteurVide == joueur.manne.length){
			return true;
		}
		return false;
	}
	
	/**
	 * Vérifie si un caractère correspond à une couleur du jeu.
	 * @param caractere
	 * @return true si le caractère est une couleur valide, false sinon.
	 */
	public static boolean estUneCouleurValide(char caractere) {

		// On crée une boucle pour comparer toutes les couleurs au caractère reçu
		// Vu que la couleur "Noir" n'est pas incluse dans le tableau, il faut que le tableau soit plus grand d'un index
		// que le tableau de couleurs de base
		for (int i = 0; i < Constantes.COULEURS.length + 1; i++){
			char couleur;
			if (i == Constantes.COULEURS.length) couleur = Constantes.NOIR;
			else couleur = Constantes.COULEURS[i];

			if (couleur == caractere) return true;
		}
		return false;
	}	

	/**
	 * Vérifie si une chaine de caractères décrit correctement une liste de
	 * pièces.
	 * @param chaine la chaine de caractères � vérifier
	 * @return true, si la chaine est une description correcte d'une liste de
	 * 			pièces, false sinon.
	 */
	public static boolean saisieCorrecte(String chaine) {
		
		boolean saisieValide = true;
		
		if(chaine == null || chaine.isEmpty()){
			return  false;
		}

		//On extrait la chaine de caractères
		Piece[] pieces = extrairePieces(chaine);

		//Validation des couleurs pour chaque pièce
		for (int i = 0; i < pieces.length; i++) {

			if (pieces[i] != null){
				if(!estUneCouleurValide(pieces[i].couleur)){
					saisieValide = false;
				}
			}
		}

		//Validation que les pieces sont bien dans la main du joueur
		if(saisieValide){
			saisieValide = valide(joueurActif, pieces);
		}
		// System.out.println("test saisie valide:" + saisieValide);
		return saisieValide;
	}
	
	/**
	 * V�rifie si une liste de pièces fait partie de la main d'un joueur.
	 * @param joueur le joueur
	 * @param pieces la liste des pièces
	 * @return true si toutes les pièces de la liste sont dans la main du
	 * 		   joueur, false sinon.
	 */
	public static boolean valide(Joueur joueur, Piece[] pieces) {
		// On crée deux boucles pour comparer les pieces de la liste a celles du joueur
		for (int i = 0; i < pieces.length; i++){
			boolean estValide = false;
			Piece pieceNonValidee = pieces[i];
			if (pieceNonValidee != null){
				// sSystem.out.printf("[Piece %d]: %d%c\n", i, pieceNonValidee.numero, pieceNonValidee.couleur);
				// On vérifie si la piece a valider fait partie de la main du joueur
				for (int j = 0; j < joueur.manne.length; j++){
					Piece pieceAComparer = joueur.manne[j];

					if (pieceAComparer != null){
						// System.out.printf("\t[Piece %d]: %d%c\n", i, pieceAComparer.numero, pieceAComparer.couleur);
						if (pieceNonValidee.numero == pieceAComparer.numero && pieceNonValidee.couleur == pieceAComparer.couleur){
							estValide = true;
						}
					}
				}
				// Si le bool est toujours false a ce stade, cela veut dire que la fonction n'a pas trouvé de piece équivalente
				// On retourne donc false vu qu'une des pieces n'existe pas dans la main du joueur
				if (!estValide) return false;
			}

		}
		// Si la fonction n'a pas deja retourne, cela signifie que toutes les pieces sont valides
		return true;
	}
	
	/**
	 * V�rifie si une liste de pi�ces constitue une combinaison (suite ou s�rie)
	 * @param pieces la liste des pi�ces
	 * @return true si la liste est une combinaison, false sinon.
	 */
	public static boolean estUneCombinaison(Piece[] pieces) {
		boolean valide = false;

		boolean estUneSuite = false;
		boolean estUneSerie = false;
		// Le nombre de pieces non nuls
		int nbPieces = 0;
		// On vérifie si la liste est une suite de 3 nombres ou plus qui se suivent et qui ont la meme couleur
		// ou une série de 3 ou 4 pieces qui ont le meme numéro, mais de couleurs toutes différentes
		int nbDansLaSuite = 0;
		int nbDansLaSerie = 0;
		for (int i = 0; i < pieces.length; i++){
			if (pieces[i] != null){
				// On augmente le nombre de pieces valide
				nbPieces++;

				// A l'index 0, c'est le premier element dans le tableau donc le tableau pourrait contenir
				// une suite ou une série
				if (i == 0){
					nbDansLaSuite++;
					nbDansLaSerie++;
				}
				// Si la couleur de la piece a l'index actuel (i) est la meme que celle de la piece précédente (i - 1),
				// on incrémente nbDansLaSuite
				else if (pieces[i].couleur == pieces[i - 1].couleur) nbDansLaSuite++;
				// Sinon, si la valeur du numéro de la piece a l'index actuel (i) est la meme que celle de la piece précédente (i - 1),
				// on incrémente nbDansLaSerie
				else if (pieces[i].numero == pieces[i - 1].numero) nbDansLaSerie++;
			}

		}


		// Si le nombre de pieces valide est plus petit ou egal a zero il ne peut pas y avoir de suite ou de serie
		if (nbPieces > 0){
			if (nbDansLaSuite == nbPieces) estUneSuite = true;
			else if (nbDansLaSerie == nbPieces && nbPieces <= 4) estUneSerie = true;


			// Si c'est une suite ou une serie on retourne true
			if (estUneSerie || estUneSuite) valide = true;
		}

		return valide;
	}




/***** Méthodes de manipulation de la pioche *****/
	
	/**
	 * Retire des pièces d'une pioche et les place dans la main d'un joueur.
	 * @param pioche la pioche
	 * @param joueur le joueur
	 * @param nombrePieces le nombre de pièces à extraire de la pioche
	 */
	public static void distribuerMain(Pioche pioche, Joueur joueur, int nombrePieces) {
			for(int j= 0;j<nombrePieces;j++){
				joueur.manne[j] = pioche.pieces[j];
			}
		}
	
	/**
	 * Extrait une pièce d'une pioche. Le choix de la pièce dépend de
	 * l'implémentation.
	 * @param pioche la pioche
	 * @return la pièce extraite
	 */
	public static Piece piocher(Pioche pioche) {

		Piece piecePigee = pioche.pieces[(pioche.nombrePieces - 1)];
		pioche.pieces[(pioche.nombrePieces - 1)] = null;

		pioche.nombrePieces--;
		return piecePigee;
	}
	
	/**
	 * Remplace une pièce d'une pioche par une autre pièce. La pièce
	 * remplacée est retournée. La stratégie de choix de la pièce à retirer
	 * dépend de l'implémentation.
	 * @param pioche La pioche d'où la pièce va être retirée.
	 * @param piece La pièce à placer dans la pioche.
	 * @return La pièce retirée de la pioche.
	 */
	public static Piece echanger(Pioche pioche, Piece piece) {

		// On copie la piece en haut de la pile dans une variable
		Piece pieceRetire = piocher(pioche);
		ajouterPiece(pioche, piece);
		return pieceRetire;
	}
	
	/**
	 * Génère les 106 pièces du jeu et les place dans une pioche.
	 * @param pioche La pioche où les pièces vont être placées.
	 */
	//Place les 106 pieces dans la pioche (incluant les 2 jokers) :
	public static void initialiserPioche(Pioche pioche) {
		int nbPioche;
		int compteur = 0;

		for (nbPioche = 0; nbPioche < 2; nbPioche++) {

			for (int i = 0; i < Constantes.COULEURS.length; i++) {

				for (int j = 0; j < Constantes.NUMEROS.length; j++) {
					Piece nouvellePiece = new Piece();

					nouvellePiece.couleur = Constantes.COULEURS[i];
					nouvellePiece.numero = Constantes.NUMEROS[j];

					pioche.pieces[compteur] = nouvellePiece;
					compteur++;

				}
			}
		}
		
		//Creation des Jokers
		Piece premierJoker = new Piece();
		Piece deuxiemeJoker = new Piece();

		premierJoker.couleur = 'N';
		deuxiemeJoker.couleur = 'N';

		//Ajout des jokers dans la pioche, ici le compteur est deja a 105
		pioche.pieces[compteur] = premierJoker;
		//Ici on augmente le compteur pour aller chercher la dernière position de la pioche
		compteur++;
		pioche.pieces[compteur] = deuxiemeJoker;
		pioche.nombrePieces = compteur;
	}
	
	/**
	 * Vide une pioche et y retirant toutes les pi�ces.
	 * @param pioche La pioche � vider
	 */
	public static void vider(Pioche pioche) {
		for (int i=0; i < Constantes.NOMBRE_TOTAL_PIECES; i++){
			pioche.pieces[i] = null;
		}
		pioche.nombrePieces = 0;
	}
	
	/**
	 * Ajoute une pièce à une pioche.
	 * @param pioche La pioche où la pièce va être ajoutée
	 * @param piece La pièce � ajouter
	 * @return true si l'ajout a réussi, false sinon (faute de place)
	 */
	public static boolean ajouterPiece(Pioche pioche, Piece piece) {
		if(pioche.nombrePieces < Constantes.NOMBRE_TOTAL_PIECES){

			pioche.pieces[pioche.nombrePieces] = piece;
			pioche.nombrePieces++;
			return true;
		}

		// Si on est pas rentré dans le if cela signifie qu'il y a un problème
		// On retourne donc false
		return false;
	}
	
	/**
	 * M�lange al�atoirement toutes les pieces de la pioche.
	 * @param pioche La pioche
	 */
	public static void melangerPioche(Pioche pioche) {

		Random rand = new Random();

		Piece[] tabTemp = new Piece[pioche.pieces.length];
		// On cree un tableau de booleans de la meme taille que le tableau temporaire pour savoir qu'elles pieces ont ete echanges ou non
		boolean[] piecesEchangees = new boolean[tabTemp.length];

		for (int i=0; i < tabTemp.length; i++){
			int indexAlea = rand.nextInt(pioche.pieces.length);
			// On verifie si la piece a deja ete echange auparavant, on recommence tant que ce n'est pas une nouvelle piece
			while (piecesEchangees[indexAlea]){
				indexAlea = rand.nextInt(pioche.pieces.length);
			}
			// Si la piece a l'index aleatoire n'a pas deja ete selectionne on peut la copier dans le tableau temporaire
			tabTemp[i] = pioche.pieces[indexAlea];
			piecesEchangees[indexAlea] = true;
		}

		// On copie le tableau temporaire dans la pioche
		pioche.pieces = tabTemp;
	}


/***** M�thodes d'affichage *****/
	
	/**
	 * Affiche � l'�cran les premi�res pi�ces d'une liste.
	 * @param pieces La liste des pi�ces
	 * @param nombre Le nombre de pi�ces de la liste � prendre en consid�ration.
	 */
	public static void afficherPieces(Piece[] pieces, int nombre) {
		int compteur = 0;
		while (compteur < nombre){
			if (pieces[compteur] != null){
				System.out.printf("\tPiece %d: %s \n", compteur, toString(pieces[compteur]), pieces[compteur].numero, pieces[compteur].couleur);
			}
			compteur++;
		}
	}	

	/**
	 * Affiche � l'�cran la main d'un joueur.
	 * @param joueur Le joueur
	 */
	public static void afficherMain(Joueur joueur) {
		System.out.printf("[Main de %s]: \n", joueur.nom);
		afficherPieces(joueur.manne, joueur.manne.length);
	}	
	
	/**
	 * Affiche le contenu de la table de jeu.
	 */
	public static void afficherTable() {
		System.out.printf("[Cartes sur la table]: \n");
		for (int i = 0; i < tableDeJeu.length; i++){
			for (int k = 0; k < tableDeJeu[i].length; k++){
				if (tableDeJeu[i][k] != null)
					// On rajoute 1 a l'index de combinaison pour que le compte commence a 1 et non 0
					System.out.printf("\tCombinaison %d: %s\n", i + 1, toString(tableDeJeu[i][k]));
			}
		}

	}
}
