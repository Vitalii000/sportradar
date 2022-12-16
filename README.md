## Live Football World Cup Scoreboard library that can show all the ongoing matches and their scores.

###### How to use

1) Inject ScoreboardService in your code:
```
@Autowired
private ScoreboardService scoreboardService;
```
2) Create game:
```
scoreboardService.startNewGame(team_name1,team_name2);
```
3) Update score:
```
scoreboardService.updateScore(gameId, scores_team1,scores_team2);
```
 - gameId - value from result of game creation in step 2
4) Finish game: 
```
scoreboardService.stopGame(gameId);
```
5) Get a summary of games in progress:
```
scoreboardService.gamesSummary();
```

#### How Build library

1) install maven and jdk17 or make sure that they already installed
2) run command ```mvn surefire-report:report``` to execute tests and build test report (will be available in *target/site/surefire-report.html*)
3) run command ```mvn clean install``` to build *.jar artifact

