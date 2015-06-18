/**@authors : rm *
 *  @date : 17 Juin 2015
 * @title : NuclearChickens
 **/

import extensions.CSVFile;
import extensions.*;
import java.awt.Dimension;

class MainGame extends Program {

	Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	final int HAUTEUR = (int)(tailleEcran.getWidth()/2);
	final int LARGEUR = (int)(tailleEcran.getHeight()/2);
	TransparentImage window = newTransparentImage(HAUTEUR, LARGEUR);
	TransparentImage background = newTransparentImage("./ressources/background.jpg");
	TransparentImage poulet = newTransparentImage("./ressources/poulet.png");
	//poulet
	int coo_y = (int)(tailleEcran.getHeight()/2);

	//Initialisation de mon monde
	final int MIN=20;
	final int MAX=20;
	final int MINLARG=60;
	final int MAXLARG=60;
	final int LONGUEURMONDE=MIN+HAUTEUR;
	final int LARGUEURMONDE=MIN+LARGEUR;
	//mon monde:
	final int DECOR=1;
	final int VIDE=3;

	int [][] monde = new int[HAUTEUR][LARGEUR];

	//Obstacle:
	final int OBSTACLE=10;
	final int APPARITIONOBSTACLE=3;
	int nbObst=15;

	//nombre de point
	int nbPoint =0;

	//carac ennemi bete
	final int ENNEMIBETE=15;
	final int APPARITIONENNEMIBETE=50;
	int nbEnnemiBaseBete=4;

	//carac tir ennemi base
	final int TIRENNEMI=6;
	final int PROBTIR=2;

	//carac ennemi suiveur
	final int ENNEMIBOUFFE=7;
	final int APPARITIONBOUFFEUR=10;
	int nbEnnemiBouffeur=3;

	//Mon vaisseau
	final int VAISSEAU=2;
	int pointDeVie=30;

	//coordonnées de mon vaisseau
	int x=window.getWidth()/42;
	int y=window.getHeight()/2;

	//Mon missile
	final int MISSILE=5;

	//Définir quand on arrete la partie
	boolean finished =false;

	//Nom du joueur
	String nomJoueur="";
	int niveau=1;

	///Mon main on se déroule mon processus
	void algorithm(){

		background = copyAndResize(background,"background-bigger", HAUTEUR,LARGEUR);
		//background = resizeImage(background);
		window.drawImage(background, 0, 0);
		//background = printPoulet(background);
		//initialisation de mes variables

		String [][]classement=chargerClassement("./ressources/classement.csv");
		String accueil="";
		int deplacement=0;
		String quitter=" ";

		while(!(accueil.equals("4"))){


			//ecran d'accueil

			accueil();
			accueil=readString();


			//Pour jouer


			if(accueil.equals("1")){


				//presentation
				nomJoueur();
				nomJoueur=readString();
				backGround(nomJoueur);
				monde=initMonde();


				//Code jeu

				//placement de mon vaisseau
				monde[x][y]=VAISSEAU;
				window.drawImage(background, 0, 0);
				window.drawImage(printPoulet(background),window.getWidth()/42,window.getHeight()/2);
				show(window);

				while(!finished){
					///Initialisation des principales variables pour le lancement du jeu
					//printMechant(background);
					
					//affichage et mise a jour du monde
					afficherMonde(monde);
					//enableKeyTypedInConsole(true);


					//deplacementEnnemiBouffeur();
					//deplacementEnnemiBete();

					//attaque adversaire
					//tirEnnemi();
					//attaqueBouffeur();

					//defaite
					//mortJoueur(classement);

					//condition de victoire
					if(!victoire(monde)){
						monde = initMonde();
						clearScreen();
						println("Bravo niveau "+niveau+" fini");
						delay(1000);
						monde[x][y]=VAISSEAU;
						niveau++;
						evolutionPartie();
					}
					delay(1000/30);
					//enableKeyTypedInConsole(false);
				}
				finished = false;
			}

			//Classement
			else if(accueil.equals("2")){
				while(quitter.equals(" ")){
					affichageTabString(classement);
					quitter=readString();
				}
				quitter=" ";
			}
			//Aide pour le jeu
			else if(accueil.equals("3")){
				while(quitter.equals(" ")){
					affichageAide();
					quitter=readString();
				}
				quitter=" ";
			}
			//Pour quitter
			else if(accueil.equals("4")){
				clearScreen();
				println("Au revoir");
				delay(2000);
				cursor(0,0);
				System.exit(1);
			}

			//En cas de mauvaise saisie
			else{
				println("Erreur de saisie");
			}
		}
	}

	TransparentImage printPoulet(TransparentImage img)
	{
		TransparentImage newImg = newTransparentImage(HAUTEUR,LARGEUR);
		newImg = copyAndResize(poulet, "poulet-petit",70,50);
		return newImg;
	}

	// TransparentImage printMechant(TransparentImage img)
	// {

	//}

	TransparentImage resizeImage(TransparentImage background)
	{
		TransparentImage image = newTransparentImage(HAUTEUR, LARGEUR);
		image = copyAndResize(background,"background-bigger", HAUTEUR,LARGEUR);
		return image;
	}


	///Initialisation d'un monde en deux dimensions, grace à un tableau de int qui va stocker tous les éléments de notre jeu grace à des identifiants
	int [][] initMonde (){

		//initialisation 
		int nbEnnemi=0;
		int nbEnnemiBouffe=0;
		int nbEnnemiBete=0;
		int nbObstacle=0;


		//lecture de mon tableau
		for(int i=0;i<monde.length;i++){
			for(int j=0;j<monde[i].length;j++){

				//fonction de probabilité pour l'apparition de l'ennemie
				int random=probaApparitionEnnemi();

				//forme de mon monde, dans une prochaine version en faire plusieurs ou une génération procédural 
				if(i<2||i>monde.length-3||j<2||j>monde[i].length-3){
					monde[i][j]=DECOR;
				}
				else{


					//prob apparition ennemi bouffeur
					if(random==APPARITIONBOUFFEUR&&j<monde[i].length-10&&nbEnnemiBouffe<nbEnnemiBouffeur){
						monde[i][j]=ENNEMIBOUFFE;
						nbEnnemiBouffe++;
					}
					//prob apparition ennemi fou
					else if(random==APPARITIONENNEMIBETE&&j<monde[i].length-10&&nbEnnemiBete<nbEnnemiBaseBete){
						monde[i][j]=ENNEMIBETE;
						nbEnnemiBete++;
					}
					//prob apparition obstacle
					else if(random==APPARITIONOBSTACLE&&nbObstacle<nbObst){
						monde[i][j]=OBSTACLE;
						nbObstacle++;
					}else{
						monde[i][j]=VIDE;
					}
				}
			}
		}
		return monde;
	}


	///fonction générant un nombre aléatoire pour l'apparition d'ennemi
	int probaApparitionEnnemi(){
		int random=(int)(Math.random()*100);
		return random;
	}

	///Fonction plus utilisé maintenant mais servant lors du débuggage, elle traduit chacun de mes identifiant pour les stocker dans une string 
	String toString (int [][]tab){ 
		String s="";
		for(int i=0;i<tab.length;i++){
			for(int j=0;j<tab[i].length;j++){
				if(tab[i][j]==DECOR)
					s+='*';
				if(tab[i][j]==VAISSEAU)
					s+='A';
				if(tab[i][j]==VIDE)
					s+=' ';
				if(tab[i][j]==MISSILE)
					s+='"';
				if(tab[i][j]==ENNEMIBETE)
					s+='Y';
				if(tab[i][j]==TIRENNEMI)
					s+='|';
				if(tab[i][j]==ENNEMIBOUFFE){
					s+='V';
				}
				if(tab[i][j]==OBSTACLE){
					s+='*';
				}
			}
			s+="\n";
		}
		return s;
	}
	///Fonction permettant l'affichage du monde, il traduit les identifiants en background de couleur
	void afficherMonde(int [][]tab){ 
		window.drawImage(background,0,0);
		for(int i=0;i<tab.length;i++){
			for(int j=0;j<tab[i].length;j++){

				if(tab[i][j]==VAISSEAU){
					set(background, j, i, rgbColor(255,255,0));
				}
				if(tab[i][j]==VIDE){
					set(background, j, i, rgbColor(0,0,0));
				}
				if(tab[i][j]==MISSILE){
					set(background, j, i, rgbColor(0,0,255));
				}
				if(tab[i][j]==ENNEMIBETE){
					set(background, j, i, rgbColor(0,255,0));
				}
				if(tab[i][j]==TIRENNEMI){
					set(background, j, i, rgbColor(255,0,0));
				}
				if(tab[i][j]==ENNEMIBOUFFE){
					set(background, j, i, rgbColor(0,255,255));
				}
				if(tab[i][j]==OBSTACLE){
					set(background, j, i, rgbColor(255,255,255));
				}
			}
		}
	}



	///Fonction d'interface utilisateurs et capure de touche
	/*void keyTypedInConsole(char c){

		switch(c){
		//touche minuscule
		case'z':
			if(vide(x-1,y))
				monter();
			break;
		case's':
			if(vide(x+1,y))
				descendre();
			break;

			//touche majuscule
		case'Z':
			if(vide(x-1,y))
				monter();
			break;
		case'S':
			if(vide(x+1,y))
				descendre();
			break;
			//clavier directionnel
		case ANSI_UP:
			if(vide(x-1,y))
				monter();
			break;    
		case ANSI_DOWN:
			if(vide(x+1,y))
				descendre();
			break;

			//pour quitter
		case 'w':
			finished=true;
			clearScreen();
			println("Au revoir !");
			delay(2000);
			cursor(0,0);
			break;
		case 'W':
			finished=true;
			clearScreen();
			println("Au revoir !");
			delay(2000);
			cursor(0,0);
			break;    

			//TIR VAISSEAU
		case 'p':
			missile(x,y);
		default:
			//
		}
		cursor(x,y);
	}*/

	//ENSEMBLE DE FONCTION DE DEPLACEMENT DU VAISSEAU

	///Fonction permettant a mon personnage de monter
	void  monter (){
		monde[x][y]=VIDE;
		x--;
		monde[x][y]=VAISSEAU;
	}
	///Fonction permettant a mon personnage de descendre
	void descendre (){
		monde[x][y]=VIDE;
		x++;
		monde[x][y]=VAISSEAU;
	}
	///Fonction permettant a mon personnage d'aller à droite
	void droite (){
		monde[x][y]=VIDE;
		y++;
		monde[x][y]=VAISSEAU;
	}
	///Fonction permettant a mon personnage d'aller à gauche
	void gauche (){
		monde[x][y]=VIDE;
		y--;
		monde[x][y]=VAISSEAU;
	}

	//ENSEMBLE DE FONCTION DE DEPLACEMENT DES ENNEMI BOUFFEUR

	///Fonction permettant au ennemi bouffeur de monter
	void  monterEnnemiBouffeur (int i,int j){
		monde[i][j]=VIDE;
		i--;
		monde[i][j]=ENNEMIBOUFFE;
	}
	///Fonction permettant au ennemi bouffeur de descendre
	void descendreEnnemiBouffeur (int i, int j){
		i++;
		monde[i][j]=ENNEMIBOUFFE;
		monde[i-1][j]=VIDE;
	}
	///Fonction permettant au ennemi bouffeur d'aller à droite
	void droiteEnnemiBouffeur (int i, int j){
		j++;
		monde[i][j]=ENNEMIBOUFFE;
		monde[i][j-1]=VIDE;
	}
	///Fonction permettant au ennemi bouffeur d'aller à gauche
	void gaucheEnnemiBouffeur (int i, int j){
		j--;
		monde[i][j]=ENNEMIBOUFFE;
		monde[i][j+1]=VIDE;
	}


	//ENSEMBLE DE FONCTION DE DEPLACEMENT DES ENNEMI BETE

	///Fonction permettant au ennemi fou de monter
	void  monterEnnemiBete (int i,int j){
		monde[i][j]=VIDE;
		i--;
		monde[i][j]=ENNEMIBETE;
	}
	///Fonction permettant au ennemi fou de descendre
	void descendreEnnemiBete (int i, int j){
		i++;
		monde[i][j]=ENNEMIBETE;
		monde[i-1][j]=VIDE;
	}
	///Fonction permettant au ennemi fou d'aller à drroite
	void droiteEnnemiBete (int i, int j){
		j++;
		monde[i][j]=ENNEMIBETE;
		monde[i][j-1]=VIDE;
	}
	///Fonction permettant au ennemi fou d'aller à gauche
	void gaucheEnnemiBete (int i, int j){
		j--;
		monde[i][j]=ENNEMIBETE;
		monde[i][j+1]=VIDE;
	}


	///Fonction detectant si une case est vide
	boolean vide(int x,int y){
		return (monde[x][y]==VIDE);
	}
	///fonction detectant si il y a une ennemi bouffeur dans une case
	boolean ennemiBouffeur(int x,int y){
		return (monde[x][y]==ENNEMIBOUFFE);
	}
	///fonction detectant si il y a un ennemi fou dans une case
	boolean ennemiBete(int x,int y){
		return (monde[x][y]==ENNEMIBETE);
	}
	///fonction detectant si il y a un tir ennemi dans une case
	boolean tirEnnemi(int x,int y){
		return (monde[x][y]==TIRENNEMI);
	}
	///fonction detectant si il y a notre vaisseau dans notre case
	boolean vaisseau(int x,int y){
		return (monde[x][y]==VAISSEAU);
	}
	///fonction detectant si il y a un decor dans une case
	boolean decor(int x,int y){
		return (monde[x][y]==DECOR);
	}
	///fonction detectant si il y a un obstacle dans une case
	boolean obstacle(int x,int y){
		return (monde[x][y]==OBSTACLE);
	}
	///fonction detectant si il y a un missile dans une case
	boolean estMissile(int x,int y){
		return (monde[x][y]==MISSILE);
	}

	///Fonction gerant les missiles de mon vaisseau ainsi que la destruction des ennemis
	void  missile (int i,int j){

		boolean MortEnnemi=false;
		for(int k=0;vide(i,j+1)&&MortEnnemi==false;k++){
			//DEPLACEMENT DU MISSILE
			j++;
			monde[i][j]=MISSILE;
			if(estMissile(i,j-1)&&!vaisseau(i,j-1)){
				monde[i][j-1]=VIDE;
				delay(100);//vitesse de tir
			}
			if(ennemiBouffeur(i,j+1)){
				monde[i][j+1]=VIDE;//DESTRUCTION DES ENNEMIS BOUFFEUR
				MortEnnemi=true;
				nbPoint+=2;
			}
			if(ennemiBete(i,j+1)){
				monde[i][j+1]=VIDE;//DESTRUCTION DES ENNEMIS FOU
				MortEnnemi=true;
				nbPoint+=2;
			}
		}
		monde[i][j]=VIDE;
	}


	///Fonction gerant le deplacement des ennemi bouffeur
	void deplacementEnnemiBouffeur(){


		for(int i=2;i<monde.length-2;i++){
			for(int j=2;j<monde[i].length-3;j++){

				if(ennemiBouffeur(i,j)&&vide(i-1,j)&&i>x){
					monterEnnemiBouffeur(i,j);

				}else if(ennemiBouffeur(i,j)&&vide(i,j+1)&&j<y){
					droiteEnnemiBouffeur(i,j);

				}else if(ennemiBouffeur(i,j)&&vide(i,j-1)&&j>y){
					gaucheEnnemiBouffeur(i,j);

				}
				if(ennemiBouffeur(i,j)&&decor(i,j-1)){
					monde[i][j]=VIDE;
					pointDeVie-=10;
				}
				if(decor(i,j-1)&&vide(i+1,j-2)){
					monde[i][j]=VIDE;
					monde[i+1][j]=ENNEMIBOUFFE;
				}
			}
		}
	}
	///Fonction gerant le deplacement des ennemis FOU
	void deplacementEnnemiBete (){

		for(int i=2;i<monde.length-2;i++){
			int xa=(int)(Math.random()*MAX);
			int ya=(int)(Math.random()*MAX);
			for(int j=2;j<monde[i].length-3;j++){

				if(ennemiBete(i,j)&&vide(i,j-1)&&j==monde[i].length-4){
					gaucheEnnemiBete(i,j);	
				}else if(ennemiBete(i,j)&&vide(i-1,j)&&i>xa){
					monterEnnemiBete(i,j);

				}else if(ennemiBete(i,j)&&vide(i,j+1)&&j<ya){
					droiteEnnemiBete(i,j);
				}else if(ennemiBete(i,j)&&vide(i,j-1)&&j>ya){
					gaucheEnnemiBete(i,j);

				}else if(ennemiBete(i,j)&&vide(i+1,j)&&i<xa){
					descendreEnnemiBete(i,j);
				}
				if(ennemiBouffeur(i,j)&&decor(i,j-1)){
					monde[i][j]=VIDE;
					pointDeVie-=10;
				}
			}
		}
	}
	///Fonction gerant le tir des ennemi de base
	void tirEnnemi (){
		for(int i=monde.length-3;i>3;i--){
			for(int j=2;j<monde[i].length-2;j++){
				int random=(int)(Math.random()*20);

				if((ennemiBete(i,j))&&vide(i,j-1)&&random<PROBTIR){//probabilité de tir d'un ennemi
					monde[i][j-1]=TIRENNEMI;
				}

				if(tirEnnemi(i,j)&&vide(i,j-1)){
					monde[i][j-1]=TIRENNEMI;
					monde[i][j]=VIDE;
				}

				//nettoyage des traces du tir ennemi
				nettoyageTirEnnemi(i,j);

				//Mort du joueur!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if(tirEnnemi(i,j)&&vaisseau(i,j-1)){
					pointDeVie-=10;//PUISSANCE TIR ENNEMI
					monde[i][j]=VIDE;
				}
			}
		}	  
	}

	///Fonction gerant l'attaque des ennemi bouffeur
	void attaqueBouffeur (){
		for(int i=monde.length-3;i>3;i--){
			for(int j=2;j<monde[i].length-2;j++){

				//Mort du joueur!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if(ennemiBouffeur(i,j)&&(vaisseau(i+1,j)||vaisseau(i-1,j)||vaisseau(i,j+1)||vaisseau(i,j-1))){
					pointDeVie--;//PUISSANCE ATTAQUE ENNEMI BOUFFEUR
				}
			}
		}	  
	}

	///Fonction détectant si le joueur à encore des point de vie, si ses points de vie sont null, elle déclenche la fin de la partie avec la fonction défaite
	void mortJoueur (String [][] classement){
		if(pointDeVie<=0){
			defaite(classement);
			renitialisation();
		}
	}

	///Fonction permetant de nettoyer les trace des tirs ennemi
	void nettoyageTirEnnemi(int i,int j){
		if(tirEnnemi(i,j)&&(decor(i,j-1)||obstacle(i,j-1)||ennemiBouffeur(i,j-1)||ennemiBete(i,j-1)))
			monde[i][j]=VIDE;
	}



	///Fonction permettant de vérifier si le joueur perds ou non, elle permet aussi d'enregister le score dans le classement et de l'afficher pour l'utilisateur
	void defaite (String [][] classement){
		saveCSV(modifierClassement(classement,nomJoueur,String.valueOf(nbPoint)),"./ressources/classement.csv");
		enableKeyTypedInConsole(false);
		finished=true;
		reset();
		clearScreen();
		println("Dommage vous avez perdu.....\nVous avez obtenues "+nbPoint+" point");
		delay(2000);

	}
	///Fonction permettant de renitialiser les variables d'environnement
	void renitialisation (){
		pointDeVie=30;
		nbPoint=0;
		niveau=1;
	}

	///Fonction permettant de vérifier si y a encore des ennemis et donc si le joueur a gagné ou non
	boolean victoire (int [][] tab){
		boolean continuJouer=false;
		for(int i=2;i<tab.length-3&&continuJouer==false;i++){
			for(int j=2;j<tab[i].length-2&&continuJouer==false;j++){
				if(ennemiBouffeur(i,j)||ennemiBete(i,j))
					continuJouer=true;
			}
		}
		return continuJouer;
	}

	///Fonction d'affichage du logo de départ stocké dans un fichier CSV
	void logo (){
		clearScreen();
		cursor(0,0);
		clearScreen();
		text("red");
		String [][] logo =chargerClassement("./ressources/logo.csv");
		for(int i=0;i<logo.length;i++){
			println();
			for(int j=0;j<logo[i].length;j++){
				print(logo[i][j]);
			}
		}
		println();
		println();
	}

	///Fonction d'affichage du titre du jeu socké dans un fichier CSV
	void titreJeu (){
		clearScreen();
		cursor(0,0);
		text("red");
		println();
		println();
		String [][] titre =chargerClassement("./ressources/titre.csv");
		for(int i=0;i<titre.length;i++){
			println();
			for(int j=0;j<titre[i].length;j++){
				print(titre[i][j]);
			}
		}
		println();
		println();
	}

	///Fonction d'affichage des différents choix dans l'accueil
	void accueil (){
		logo();
		println();
		text("green");
		println("                  (1):Nouveau");
		println("                  (2):Classement");
		println("                  (3):Aide");
		println("                  (4):Quitter");
		println();
	}

	///Fonction d'affichage du nom de joueur dans le but de le stocké dans le classement
	void nomJoueur(){
		titreJeu();
		text("blue");
		println("Bonjour bienvenue dans NuclearChickens, comment vous appelez-vous?");
	}

	///Fonction d'affichage du background stocké dans un CSV
	void backGround(String nom){
		clearScreen();
		titreJeu();
		text("blue");
		String [][] histoire=chargerClassement("./ressources/background.csv");
		for(int i=0;i<histoire.length;i++){
			for(int j=0;j<histoire[i].length;j++){
				print(histoire[i][j]);
			}
			println();
		}

		String backGround=readString();

	}

	///Fonction de chargement du classement stocké dans un CSV
	String [][]  chargerClassement(String filename){

		CSVFile classement=loadCSV(filename);//charge le fichier csv et le stocke dans une variable
		String [][] tab =new String[rowCount(classement)][columnCount(classement)];//On initialise seulement la longueur du tableau
		for(int i=0;i<rowCount(classement);i++){
			for(int j=0;j<columnCount(classement);j++){
				tab[i][j]=getCell(classement,i,j);
			}
		}
		return tab;
	}

	///Foncion de modification du classement, permet l'affichage du classement de manière croissante
	String [][] modifierClassement(String[][] tab, String nom, String score){
		boolean bool = false;
		int rez = Integer.parseInt(score);


		for(int i=1 ; bool==false ; ++i){
			int rez2 = Integer.parseInt(tab[i][2]);

			//ressort si new score n'est pas dans le top 
			if(i==(length(tab)-2))
				bool=true;

			//on compare le nouveau score avec le premier du classement
			else if(rez>rez2){
				//boucle qui décale le tableau des scores d'une place vers le bas
				for(int j=length(tab)-1 ; j>i ; --j){
					tab[j][1]=tab[j-1][1];
					tab[j][2]=tab[j-1][2];
				}

				//place le nouveau score dans le tableau
				tab[i][1]=nom;
				tab[i][2]=score;
				bool=true;
			}
			else if(rez==rez2){
				//boucle qui décale le tableau des scores d'une place vers le bas
				for(int j=length(tab)-1 ; j>i ; --j){
					tab[j][1]=tab[j-1][1];
					tab[j][2]=tab[j-1][2];
				}

				//place le nouveau score dans le tableau
				tab[i][1]=nom;
				tab[i][2]=score;
				bool=true;
			}			
		}
		return tab;
	}

	///Fonction d'affichage d'un tableau de String
	void affichageTabString(String [][] tab){
		clearScreen();
		cursor(0,0);
		text("red");
		titreJeu();
		for(int i=0;i<tab.length-1;i++){
			for(int j=0;j<tab[i].length;j++){
				if(i==0){
					text("green");
					print(tab[i][j]+" ");
				}else{
					text("blue");
					print(tab[i][j]+" ");
				}
			}
			println();
		}
		println();
		text("green");
		println("Appuyez sur q pour revenir au menu précédent.");
	}

	///Fonction d'affichage de l'aide du jeu stocké dans un CSV
	void affichageAide(){
		clearScreen();
		titreJeu();

		String [][] aideJeu = chargerClassement("./ressources/aideJeu.csv");
		for(int i=0;i<aideJeu.length;i++){
			for(int j=0;j<aideJeu[i].length;j++){
				if(i==0||i==aideJeu.length-1||i==5){
					text("green");
					println(aideJeu[i][j]);
				}else{
					text("blue");
					println(aideJeu[i][j]);
				}
			}
		}
		println();
	}
	///Fonction d'evolution de la partie entre chaque niveau
	void evolutionPartie(){
		nbEnnemiBaseBete+=1;
		nbPoint+=10;
		pointDeVie+=5;
		if(niveau%2==0)
			nbEnnemiBouffeur+=1;
	}
	/*
	 * Fonctions heritees 
	 */
	void refresh(){
		window.drawImage(background,0,0);
		window.drawImage(printPoulet(background),x,y);
		afficherMonde(monde);
	}
	
	void keyChanged(char c, String event){
		if (c == ANSI_UP){
			afficherMonde(monde);
			monter();
			y -= 5;
			refresh();
			delay(1000/30);
		}
		if (c == ' '){
			println("SPACE--");
		}
		if (c == ANSI_DOWN){
			afficherMonde(monde);
			descendre();
			y += 5;
			delay(1000/30);
		}
	}

	void mouseHasMoved(int x, int y){
	}
	void mouseIsDragged(int x,int y, int button, int clickCount){
	}
	void textEntered(String text){
	}

	void mouseChanged(String name, int x, int y, int button, String event){
		//println("Zone : " + name + " | Action : " + event + " | Bouton souris : "+button +" | STEP : "+step);
		if(name.equals("lol")){
			// Bouton Jouer
			if (x >= 329 && x<= 951 && y >= 420 && y<= 561 && button==1 && event.equals("PRESSED")){
				// Bouton Quitter
			}else if (x >= 489 && x<= 792 && y >= 611 && y<= 689 && button==1 && event.equals("PRESSED")){
				println("Vous partez deja :( !?");
			}
		}
	}
}