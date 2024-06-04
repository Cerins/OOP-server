# API serveris projektam "StuddyBuddy"

### Kas ir StudyBuddy?
Mobilā lietotne, kuras galvenais mērķis ir sniegt lietotājiem iespēju komunicēt ar citiem lietotājiem studiju un mācību laikā radušos problēmu risināšanai. Studenti spēj sazināties ar citiem studentiem, kā arī privātskolotājiem. Lietotāji ar vecāku lomu var sazināties tikai ar privātskolotājiem. Vecāki ir aizbildņi skolēniem, kas nav sasnieguši 16 gadu vecumu. Privātskolotāji nespēj iesākt sarunu, bet var atbildēt studentiem un vecākiem. Komunikācija notiek privātas sarakstes formā.
Kas ir galvenās funkcionalitātes?
- Reģistrācija, autentifikācija, autorizācija;
- Datu izveidošana, dzēšana, rediģēšana, lasīšana;
- Tērzēšana;
- Dalīšanās ar failiem;
- Spēja meklēt citus lietotājus;
- Filtrēšana;
- „Draudzības” funkcionalitāte;
- Sūdzību izveidošana un apstrāde.


### Kāda bija motivācija veidot šādu projektu?
Mūsdienās ir daudz veidu un iespēju atrast privātskolotāju dažādiem studiju kursiem un mācību priekšmetiem, piemēram, izmantojot sludinājumus platformās „Facebook” jeb „Meta” un SS.LV. Tomēr šīs platformas bieži prasa ievērojamu piepūli lietotāju kontu izveidē un bieži vien nesniedz gaidītos rezultātus.
Mūsu izstrādātā sistēma „StudyBuddy” piedāvā efektīvu risinājumu šai problēmai, izmantojot ērtu lietotni, kas nodrošina ātru reģistrēšanos ar konkrētu studiju kursu vai mācību priekšmetu mērķi. Tas palīdz izvairīties no liekām sarunām par nesvarīgiem jautājumiem un koncentrēties uz mācībām.
Ekonomiskās un energoresursu krīzes dēļ vidējo iedzīvotāju rīcībā esošie brīvie līdzekļi ir būtiski samazinājušies, kas nozīmē, ka daudzi vecāki vairs nevar atļauties privātskolotājus. Tāpēc mūsu lietotne ir īpaši paredzēta studentiem, lai viņi varētu savstarpēji atrast domu biedrus un kopīgi risināt studiju problēmas.
Lai gan lietotne „StudyBuddy” nav universāls risinājums visiem, tās galvenais mērķis ir palīdzēt cilvēkiem veidot draudzīgas un atbalstošas attiecības šobrīd ļoti sadrumstalotajā sabiedrībā.

### Projekta tehniskie prasījumi:
Java 17
### Projekta instalācija:
Git clone https://github.com/Cerins/OOP-server.git
```
docker compose up -d   // (setup the postgres database)
./gradlew
./gradlew bootRun
```
If you wanna stop the postgres -> “docker compose down”

### Datu sagatave

Piemēru dati jau sagatavoti, tos var izveidot, ja datubāze ir palaista un izpildot komandu no tās pašas direktorijas kā projekts.
```
docker compose exec database psql -U user -d studybuddy -c "$(cat data.sql)"
```

Sagatave izveido 3 students, 1 vecāku, 1, administratoru un 3 privātskolotājus.

Studenti lietotājvārdi - maijabite, vilnis97, skujinsbaba. Visu lietotāju paroles ir "password".
 
### Kā dabūt projektu uz lokālās sistēmas?
Datubāze un DockerCompose:
Lai palaistu Postgres datubāzi -> “docker compose”. Konfigurācija failā “docker-compose.yaml”
Tabulas tiek automātiski izveidotas datubāzē. Vērtības nepieciešams savadīt datubāzē.
Vairāk info par “docker compose” un modeļiem:
https://spring.io/blog/2023/06/21/docker-compose-support-in-spring-boot-3-1.
https://spring.io/guides/gs/accessing-data-mysql
https://docs.docker.com/compose/install/
 
### Papildus informācija:

- Daļa no “Back-End” funkcionalitātēm vēl netiek atbalstīta “Front-End” pusē;
- Darbs tika izstrādāts grupā.
- Projekts balstīts uz projektējumu, kas izstrādāts "Programminženierija" kursa ietvaros. Dokuments papildināts ar izvietojuma un programmas modeļu klašu diagrammu:
  https://docs.google.com/document/d/1Yi3KF1tdhZ42VxTFy1Twajkgzo7QjPu6B03mGTWt80s/edit?usp=sharing

