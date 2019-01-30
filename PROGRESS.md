## process van Clint Nieuwendijk

#### 8 januari

design.md gemaakt, begonnen aan een grove versie van de app

#### 9 januari

ziek, wel rustig verder gewerkt aan de app

#### 10 januari

nog steeds ziek, alle schermen zijn er nu

#### 11 januari

Op github een soort van dynamisch bord gevonden: Deze is in portrait 3x3 en als je hem draait dan wordt hij 5x5. Als je hem terug draait dan wordt hij weer 3x3.

Deze vondst was wel handig, behalve dat het best wel raar geschreven code is, maar omdat het dynamisch instelbaar is kan ik het aanpassen.

#### 14 januari

Aan upgrade activity gewerkt. De upgrades waar ik aan zit te denken zijn: meer tokens, bordgrootte, AI speler, verdien tokens tewijl je weg bent. de adapter doet het nu

#### 15 januari

STYLE.md gemaakt

#### 16 januari

Het scoresysteem werkt nu, volledige interactie met de database.

#### 17 januari

Je kan nu daadwerkelijk upgrades kopen, de dynamische bordgrootte werkt.

#### 18 januari

Begonnen aan de multiplayer, ik heb besloten om het in 2 schermen te doen: een 'zoek game' en de daadwerkelijke game

#### 19-21 januari

Vooral nagedacht over de implementatie van de multiplayer qua serverinteractie.

Firebase schijnt handig te zijn, maar dan moet ik firebase leren.

Een flask REST server, dit heb ik al gedaan, maar ik weet niet hoe je een continue verbinding kan houden.

Uiteindelijk heb ik voor de laatste gekozen, dan maar veel aanvragen versturen, maar het is wel duizend keer makkelijker en ik heb al wel een beetje in mijn hoofd hoe dat werkt.

#### 22 januari

GameRequest en MoveRequest aangemaakt om aanvragen naar der server te versturen

#### 23 januari

Met Volley kan ik nu JSON requests posten naar de serverside

#### 24 januari

Server kan nu games aanmaken en toewijzen aan spelers als er een plekje is.

#### 25 januari

sql is moeilijk, maar de queries beginnen langzaam aan te werken

#### 26-27 januari

Alle interacties werken nu, gamestates zijn nu looking_for_game, player1, player2, finished

Uiteindelijk een state 'cancel' erbij gedaan om een gecancelde game aan te duiden

#### 28 januari

Het hele ding werkt nu echt? Het aflezen van die geleende code is moeilijk. Omdat ik elke keer update als er een request is gedaan krijgt de speler die niet aan de beurt is steeds punten.

#### 29 januari

De geleende code werkte niet goed voor gelijkspel, het was een kwestie van een 'if' en dan een 'else' te veranderen in een 'if else'

Het updaten is gefixt, de game checkt nu of een vakje nog niet is ingekleurd voordat hij die update.

Verder nog wat bugfixes gedaan. Als het goed is, is de app nu echt af.

Comments toegevoegd

#### 30 januari

Met margins gekloot zodat het overal past.
