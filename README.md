[English](#english) · [Français](#francais)

<a id="english"></a>

# CleanYourTV on the TCL 43P735


An Apple TV-inspired home screen for the TCL 43P735 (Smart TV Pro, BeyondTV4), running Android TV 11 with firmware V8-R51MT05-LF1V620.021315. This adaptation of [CleanYourTV](https://github.com/Gathin23/cleanyourtv) keeps Google TV available for the TCL remote shortcuts and settings.


The green lock shows that Android detects a VPN. Select it to open the detected VPN app. A kill switch can keep the indicator active when the VPN app is disconnected from its server.

![en-home](docs/tcl-43p735-screenshots/en-home.png)

## Default appearance

On a fresh install, the APK includes a TCL theme: the Glacier Glass wallpaper, six gallery images, a six-slot glass dock at 45% opacity, no visible rows below the dock and compact top buttons. Missing favourites are replaced by available apps. The VPN shortcut can discover an installed Android VPN app when no configured app is available. Fresh-install behaviour and detection with a second VPN provider still need device testing.

## Make the home screen yours

- Set four to seven apps per row, adjust dock opacity and choose how many grid rows remain visible. Press Down to enter the grid and Back to return to the dock in the same column.
- Pin Google Play Store to the dock for quick access to app updates.
- Preview, import or delete wallpapers from the launcher settings. USB access depends on the Android file picker on your TV.
- Choose a clock format and one of four intensity levels for advanced focus effects.
- Save up to three profiles on the TV, including the app order, appearance and wallpaper gallery. Each slot shows a date and an optional description.


![en-appearance](docs/tcl-43p735-screenshots/en-appearance.png)

## Ambient scenes

Open **Launcher settings → Ambience** to browse animations and still images separately. Press OK to play a scene and select it for automatic start. Its name appears above the delay slider. In CleanYourTV mode, the delay can be turned off or set to 1, 2, 5, 10, 15, 20, 30 or 60 minutes of inactivity on the launcher home screen. For the animated fireplace, place your own compatible MP4 in the launcher's external files directory as `christmas-fireplace.mp4`. The tested format was H.264 1080p at 30 fps with AAC audio. Media3 loops the local clip with sound. Download the [tested clip](https://github.com/julien-miro/cleanyourtv-tcl-43p735-public/releases/download/ambiance-fireplace/christmas-fireplace.mp4) or use your own.

### Automatic screensaver

- **Google TV** enables the system photo slideshow and uses the system delay.
- **CleanYourTV** disables the Google TV slideshow. It keeps the screen on while waiting on the launcher home screen, then starts the selected scene.
- **None** disables both automatic scenes. Android can still turn the screen off.

For the launcher to change the system setting, authorize it once from an authorized ADB shell:

    pm grant com.gathin.tvhome android.permission.WRITE_SECURE_SETTINGS

This permission allows writes to Android secure settings. In this feature it is used to change the system screensaver setting. Turn off network debugging after setup.

**Device test.** On one TCL 43P735 (BeyondTV4, Android TV 11), the menu switched the system setting off in CleanYourTV and None, and back on in Google TV. The local scene started after 1 minute and after 20 minutes with Google TV disabled. During the 20-minute run, passive checks at 10, 11 and 19 minutes found the launcher home screen awake. At the first check after 20 minutes, Ambience was in the foreground.


## Play Store update badge

The optional green dot reacts to recognized Play Store update notifications. Enable notification access under **System → Play Store update badge**. On the tested TCL, the settings screen was unavailable and access was granted from the Android shell with `cmd notification allow_listener com.gathin.tvhome/.StoreUpdateListener`. The service was verified. The dot itself still needs testing with a matching notification. Updates can be pending without a dot.

## Language and system settings

Use **System → Launcher language** to follow the TV language or select French or English. The TV's system menus retain their own language. The System panel also opens the default Home picker and offers shortcuts to reload Home and repair the Home button.


![en-system](docs/tcl-43p735-screenshots/en-system.png)

![en-language](docs/tcl-43p735-screenshots/en-language.png)

![en-profiles](docs/tcl-43p735-screenshots/en-profiles.png)

## Verified on this TV

Google TV briefly appears at startup before CleanYourTV takes over. The Home accessibility service needed TCL's APP_AUTO_START permission. HDMI 1, optical audio, Play Store and the tested physical remote shortcuts worked. The two disabled packages, com.tcl.bi and com.tcl.overseasappshow, remained disabled after a reboot. No lasting performance gain was established.

## Build on Windows

Start Docker Desktop and run from the repository root:

    powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\tools\docker-build.ps1

The APK is written to launcher/app/build/outputs/apk/release/. Keep the same signing key for in-place updates.

[Docker build guide](tools/docker-build.md) · [Restore guide](docs/restore.md) · [MIT license](LICENSE)

---

<a id="francais"></a>

# CleanYourTV sur TCL 43P735


Un accueil inspiré d’Apple TV, pilotable à la télécommande, sur une TCL 43P735 (Smart TV Pro, BeyondTV4), Android TV 11, firmware V8-R51MT05-LF1V620.021315. Cette adaptation de [CleanYourTV](https://github.com/Gathin23/cleanyourtv) conserve Google TV pour les raccourcis et les réglages de la TCL.

## CleanYourTV en action

**Accueil** — Le cadenas vert indique un VPN détecté par Android. Le dock vitré accueille six applis, dont le Play Store. Les curseurs et l’engrenage ouvrent deux menus distincts.


**Apparence** — Sélecteur de fonds avec miniatures, réglage de la grille, format de l’horloge et intensité des effets avancés.


**Système et profils** — Choix de l’accueil Android, réparation du bouton Home et sauvegardes locales datées avec récapitulatif.



## L’accueil au quotidien

- **Dock vitré et grille d’applications** avec 4 à 7 places par ligne, opacité réglable et 0 à 3 rangées visibles sous le dock. À zéro, les tuiles suivantes restent hors écran.
- **Navigation à la télécommande** avec Bas pour ouvrir la grille et Retour pour revenir directement au dock, dans la même colonne. Maintenir OK sur une tuile permet de la déplacer.
- **Google Play Store** dans la grille, épinglable au dock pour accéder aux mises à jour. Une pastille verte signale les notifications de mise à jour reconnues.
- **Cinq commandes en haut** dans la configuration testée — Sources, Ethernet, VPN, réglages du lanceur et paramètres TV. L’engrenage ouvre les paramètres TV et les trois curseurs ouvrent ceux du lanceur. Le cadenas reflète l’état du VPN vu par Android et ouvre le VPN détecté. Le kill switch peut maintenir cet état même sans connexion au serveur VPN.

![accueil](docs/tcl-43p735-screenshots/accueil.png)

![apparence](docs/tcl-43p735-screenshots/apparence.png)

![effets](docs/tcl-43p735-screenshots/effets.png)

## Apparence par défaut

Sur une installation neuve, l’APK embarque l’apparence des captures : fond Verre glacier, six images dans la galerie, dock vitré à six places et 45 % d’opacité, aucune rangée visible sous le dock et boutons compacts. Le lanceur complète les places des favoris absents avec les applis disponibles. Le cadenas peut détecter une appli VPN Android installée si aucune appli configurée ne répond. Le premier lancement et la détection avec un autre fournisseur restent à tester sur appareil.

## Personnaliser depuis la télé

| Rubrique | Commandes |
| --- | --- |
| **Apparence** | Sélecteur de fonds avec miniatures, galerie avec vignettes, import JPEG/PNG/WebP jusqu’à 20 Mo via le sélecteur Android, suppression avec confirmation. L’accès à une clé USB dépend de ce que le sélecteur expose sur le téléviseur. |
| **Dock et horloge** | Curseurs pour la disposition et l’opacité, réorganisation des applis et choix du format horaire système, 12 h ou 24 h. |
| **Animations et effets** | Animations désactivables et quatre niveaux d’intensité pour les effets avancés. L’intensité agit sur le zoom, le relief et le soulèvement de la tuile dans la grille. |
| **Système** | Sélecteur Android de l’accueil par défaut, accès aux paramètres TV, rechargement de l’accueil et raccourci vers les réglages d’accessibilité pour réparer Bouton Accueil. |
| **Profils** | Trois sauvegardes sur le stockage de la TV, avec date, description facultative et récapitulatif. La confirmation de restauration affiche l’ordre du dock lorsqu’il a été enregistré. Chaque sauvegarde comprend les réglages, l’ordre des applis, le thème et les images de la galerie. |

La remise aux valeurs du thème demande confirmation. Les profils restent dans les données de CleanYourTV et sont supprimés si ces données sont effacées.

## Ambiance

Ouvre **Paramètres du lanceur → Ambiance** pour parcourir les animations et les images fixes. OK lance la scène et la sélectionne pour le démarrage automatique. Son nom apparaît au-dessus du curseur. En mode CleanYourTV, le délai peut être désactivé ou réglé à 1, 2, 5, 10, 15, 20, 30 ou 60 minutes. Pour la cheminée animée, place ton propre MP4 dans le dossier de fichiers externes du lanceur sous le nom `christmas-fireplace.mp4`. Le format testé est H.264 en 1080p à 30 images/s avec du son AAC. Media3 lit le fichier local en boucle avec le son. Télécharge la [vidéo testée](https://github.com/julien-miro/cleanyourtv-tcl-43p735-public/releases/download/ambiance-fireplace/christmas-fireplace.mp4) ou utilise ton propre extrait.

### Veille automatique

- **Google TV** active le diaporama système et utilise le délai Android.
- **CleanYourTV** désactive le diaporama Google TV. Le lanceur garde l’écran allumé sur son accueil pendant le décompte, puis démarre la scène sélectionnée.
- **Aucune** coupe les deux déclenchements automatiques. Android peut toujours éteindre l’écran.

Pour permettre au lanceur de modifier le réglage système, accorde une fois cette autorisation depuis un shell ADB déjà autorisé :

    pm grant com.gathin.tvhome android.permission.WRITE_SECURE_SETTINGS

Cette autorisation permet de modifier les réglages sécurisés Android. Dans cette fonction, elle sert au réglage de la veille système. Coupe le débogage réseau après la configuration.

**Essai sur la TCL.** Sur une TCL 43P735 (BeyondTV4, Android TV 11), le menu a désactivé la veille système avec CleanYourTV et Aucune, puis l’a réactivée avec Google TV. La scène locale a démarré à 1 minute et à 20 minutes, avec Google TV désactivé. Lors de l’essai long, les relevés passifs à 10, 11 et 19 minutes ont trouvé l’écran allumé sur l’accueil. Au premier relevé après 20 minutes, Ambiance était au premier plan.


## Pastille de mise à jour Play Store

La pastille verte réagit aux notifications de mise à jour reconnues. Pour accorder l’accès aux notifications, ouvrir **Système → Pastille de mise à jour Play Store**. Sur la TCL testée, cet écran était indisponible et l’accès a été accordé dans le shell Android avec `cmd notification allow_listener com.gathin.tvhome/.StoreUpdateListener`. Le service a été vérifié. L’affichage de la pastille reste à confirmer avec une notification correspondante. Des mises à jour peuvent être disponibles même si la pastille est éteinte.

![systeme](docs/tcl-43p735-screenshots/systeme.png)

![langue](docs/tcl-43p735-screenshots/langue.png)

![profils](docs/tcl-43p735-screenshots/profils.png)

## Ce qui a été vérifié sur la TCL

- **Démarrage et Home** — Google TV apparaît brièvement, puis CleanYourTV prend la main. Le service d’accessibilité Bouton Accueil assure la redirection et le retour depuis les applications. Sur cette TCL, le firmware bloquait son démarrage automatique avant l’autorisation APP_AUTO_START pour CleanYourTV.
- **Fonctions TV** — HDMI 1 depuis Sources, son optique avec Iron TV Max, Play Store et raccourcis physiques Netflix, Prime Video, Guard, Media, YouTube et TCL Channel testés. L’appui prolongé sur le volume a également fonctionné.
- **Nettoyage ciblé** — com.tcl.bi et com.tcl.overseasappshow sont les deux paquets désactivés et vérifiés après redémarrage. Le profil 50P735 amont a servi de référence. Waterfall et Partner Customizer restent actifs, car le bouton TCL Channel passe par ces composants.
- **Performances** — La navigation et les passages entre applis ont paru fluides. Les relevés n’ont pas montré de saturation sur l’accueil. Safety Guard a libéré environ 282 Mio de RAM sur le moment et moins de 1 Mio de stockage, tout en interrompant Bouton Accueil. Aucun gain durable de vitesse n’a été établi.

## Compiler sous Windows

Démarrer Docker Desktop et lancer à la racine du dépôt :

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\tools\docker-build.ps1
```

L’APK est produit dans `launcher/app/build/outputs/apk/release/`. Une mise à jour conserve les données du lanceur lorsque l’APK garde la même signature. Les sauvegardes locales et les images importées après installation restent dans les données du lanceur.

Pour une autre TV, partir de la documentation amont et vérifier les paquets présents avant toute désactivation. Le dock compilé de cette adaptation contient des favoris propres à la configuration testée. Les applications absentes sont ignorées. Après une intervention ADB, désactiver le débogage et vérifier la fermeture du port avec `tools/adb-closed.py`.

[Guide Docker](tools/docker-build.md) · [Restauration](docs/restore.md) · [Projet officiel](https://github.com/Gathin23/cleanyourtv) · [Licence MIT](LICENSE)
