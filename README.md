UpYourMood 
=====================================
UpYourMood est une application se basant sur le [framework Play v2.2.1](http://www.playframework.com).
C'est un site web innovant qui propose une nouvelle expérience à travers l'écoute musicale.
En effet, il vous propose d'écouter jusqu'à 100 titres de musiques non-stop c'est à dire environ 5h de 
musique et tout cela gratuitement et en toute légalité grâce à l'utilisation de l'API Jamendo.
Lors de votre écoute, vous pourrez donner libre court à vos émotions et/ou à votre inspiration: 
posez des mots sur ce que vous ressentez, puis associez à ce mot une image qui vous semble le 
plus convenir au mot que vous venez d'entrer. Vous voyez rien de plus enfantin!

Détails techniques pour les développeurs
----------------------------------------
Vous êtes développeurs, et vous voulez tester et/ou améliorer notre application chez vous?
Pour cela rien de plus simple:

1. Tout d'abord si ce n'est déjà fait installer la base de données [Postgres](http://www.postgresql.org/download/),
nous vous conseillons fortement de prendre la version "Graphical" pour votre système d'exploitation.
Lors de l'installation de la BD, si vous effectuez votre propre configuration, pensez à changer le fichier
de configuration "application.conf".

2. Ensuite créez une nouvelle base de données appelée "UpYourMood_DB" (là encore vous pouvez l'appeler
autrement, mais ce sera à vous de modifier le fichier de configuration.

3. Dans cette BD vous devez effectuer les créations suivantes:

	3.1. CREATE TABLE "User"
(
  pseudo character varying(255) NOT NULL,
  mdp character varying(255),
  nom character varying(255),
  prenom character varying(255),
  email character varying(255),
  CONSTRAINT pseudo PRIMARY KEY (pseudo)
)
WITH (
  OIDS=FALSE
);

	3.2. CREATE TABLE "Image"
(
  "urlImage" character varying(255) NOT NULL,
  "motAssoc" character varying(255),
  CONSTRAINT "urlImage" PRIMARY KEY ("urlImage")
)
WITH (
  OIDS=FALSE
);

	3.3. CREATE OR REPLACE VIEW "UserInfo" AS 
 SELECT "User".pseudo, 
    "User".mdp, 
    "User".nom, 
    "User".prenom, 
    "User".email
   FROM "User";
   
   3.4 CREATE TABLE "Compteur"
(
  compteur bigint NOT NULL,
  CONSTRAINT compteur PRIMARY KEY (compteur)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "Compteur"
  OWNER TO loda;
  
  puis faire update "Compteur"
set compteur=0; dans un terminal SQL
   
 4. Vous êtes sous Mac ou sur Linux? Félicitation, vous pouvez tester toute notre application.
 En effet, vous devez installer le logiciel [Graphviz](http://www.graphviz.org/Download..php), 
 la "current stable release" (correspondant au GUI). Pour l'instant nous n'avons pas encore
 réussi à trouver la bonne configuration (dans le fichier config.properties dans le package 
 app/models/graphviz) pour Windows. Si de votre côté vous arrivez à faire fonctionner et à 
 obtenir un graphe "propre" merci de nous communiquez le configuration requise.   
 
 5. Pour pouvoir obtenir un graphe contenant les images des pochettes d'album et les images
 choisies par le ou les utilisateurs, veuillez créer dans le dossier un public deux sous-dossiers.
 Le premier s'appelant downloadImages le deuxième pochette (merci de respecter la casse).
 
 6. Pour pouvoir générer les différents hyper-graphes, veuillez copier les fichiers colors.xml et 
 ConnectionBase.java dans /public/rdf dans un projet à part puis exécuter la classe java
 qui vous est donnée.
 
 7. Vous voulez utiliser le EndPoint mis en place par [Fuseki](http://jena.apache.org/documentation/serving_data/)?
 Pour cela rien de plus simple, dézipper le fichier fuseki.zip se trouvant dans /public/fuseki sur
 votre Bureau par exemple. Attribuez les droit ainsi: chmod 777 fuseki* puis chmod 777 s-*  .
 Ensuite lancer le serveur ainsi: ./fuseki-server --update --mem /ds (à partir du dossier de fuseki).
 Enfin rendez-vous dans la section "Dévellopeurs" et cliquez sur le lien adéquat. Remarque:
 attention si vous déployez ce serveur vous vous exposez à de grande faille de sécurité. 
 C'est pour cela qu'il vaut mieux utiliser ce serveur <b>seulement en local</b>.