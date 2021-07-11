<사용 설명>
1. 	“Alpha_O_실행파일.zip” 의 압축을 푸십시오.
2. 	“Alpha_O” 폴더 안에 있는 “Alpha_O.jar” 파일을 실행시키십시오. 
*(만약, 이미지가 깨지는 경우) : “Alpha_O” 파일 안에 “img” 폴더와 “Alpha_O.jar” 파일이 같이 있는지 확인하십시오. 폴더와 실행파일이 같은 폴더 안에 들어있어야 합니다.
3. 	즐기십시오.

<게임의 구조>
메인 창 -> pvp/pve 선택창 
(pvp 선택 시) 플레이어간에 게임 시작.
	(pve 선택 시) -> player의 turn 선택창 (black : 선공 / white : 후공)
	(turn 선택 시) -> computer의 난이도 선택 창(easy / hard)
	컴퓨터와 게임 시작.

<버튼 설명>
1. 	Back to Main Menu : 메인 창으로 돌아갑니다.
2. 	Undo : 플레이어의 돌을 한 수 무릅니다. (pve의 경우, computer의 돌까지 하여 2턴이 물러집니다.)
3. 	New Game : 오목판을 초기화시키고 새 게임을 진행합니다.
4. 	Show Process : 게임판에 대한 정보(mode/turn/level)가 나오고 게임 플레이 상황이 명시됩니다. 
(주의 : Back To Main Menu 버튼을 눌러 새로운 모드로 게임을 진행할 땐 반드시 show process창을 종료한 후, 다시 켜주시기 바랍니다.)
5. 	Exit : 프로그램을 종료합니다.




Class files description
1. 	Start.java
main()이 들어있는 실행파일입니다. new MainMenu()로 MainMenu class의 constructor부분이 실행되도록 구현하였습니다.
2. 	MainMenu.java
메인 바탕화면을 담당하고 있는 class입니다. Enter 또는 click을 통해 다음 화면으로 넘어갑니다. 다음 화면은 new SelectUser()로 실행됩니다.
3. 	SelectUser.java
컴퓨터와 대전할 것인지, 플레이어끼리 대전할 것인지 선택할 수 있는 창입니다. 플레이어를 선택할 경우, 바로 게임이 진행되고 컴퓨터를 선택했을 경우, new SelectTurn()으로 넘어갑니다.
4. 	SelectTurn.java
컴퓨터와의 대전에서 플레이어가 선공을 할지 후공을 할지 선택하는 창입니다. 선택 후에는 new SelectLevel()로 넘어갑니다.
5. 	SelectLevel.java
컴퓨터의 난이도를 설정할 수 있는 창입니다. Easy와 hard로 나뉘어 있으며, easy의 경우, 3수 앞을 보고 돌을 두도록, hard의 경우, 5수 앞을 보고 돌을 두도록 설정해 두었습니다.
모든 선택이 완료되면, 컴퓨터와 게임이 시작됩니다.
(주의 : hard난이도의 경우, 컴퓨터의 연산속도가 길어질 수 있습니다. 자세한 사항은 게임중에 show process 버튼을 참고하십시오.)
6. 	Board_PvP.java
PvP모드에서 게임의 총괄을 맡은 class입니다. 번갈아가면서 돌이 두어지도록 했고, 버튼에 대한 자세한 설명은 이 파일의 <버튼 설명>을 참고하십시오. 승패가 결정났을 경우, 승패에 따라 black win 또는 white win 이 팝업으로 출력됩니다. (undo 버튼과 show process버튼은 비활성화됩니다.) New Game으로 새 게임을 시작하거나 Back To Main Menu로 돌아가 새 모드를 선택하실 수 있습니다. 
7. 	CheckGameOver_PvP.java
PvP모드에서 사용되는 class입니다. 게임이 끝났나 판별하여 true 또는 false를 반환합니다.
8. 	Board_PvE.java
PvE모드에서 게임의 총괄을 맡은 class입니다. 컴퓨터와 번갈아가면서 돌이 두어지도록 했고, 컴퓨터의 경우, Minimax class를 통해 놓을 곳을 계산합니다. 나머지는 Board_PvP class와 동일합니다.
9. 	CheckGameOver_PvE.java 
PvE모드에서 사용되는 class입니다. 게임이 끝났나 판별하여 true 또는 false를 반환합니다.
10. 	Minimax.java
컴퓨터의 계산을 담당하는 class입니다. 현재 상태의 오목판(matrix)를 가지고 수를 예측합니다. 기본적으로 돌이 놓여있는 부분과 인접한 부분에 한정하여 계산되도록 하였습니다.(인접한 부분 == tree의 가지의 갯수) Easy와 hard 각각 tree의 깊이가 3과 5로 설정되어 있습니다. (깊이 == tree의 depth) 원하는 깊이에 도달하였을 때, 판에 대한 정보를 가지고 자신에게 얼마나 유리한지 숫자로 계산합니다.(상태함수는 코드 가장 아랫부분에 있습니다.) 이 수치에 맞추어서 minimax algorithm을 시행합니다. 더불어 연산시간을 줄이기 위해 alpha-bata pruning 기법을 사용하였습니다. 컴퓨터는 자신의 턴이 올 때 마다 계속하여 이 class를 실행하게 됩니다.
11. 	ShowProcess.java
진행중인 게임의 상황과 정보를 보여주는 class입니다. 게임 안에서 show process 버튼을 눌러 실행시킬 수 있으며, 게임에 대한 모드, 선/후공, 난이도가 보여집니다. PvP와PvE모드에 동일하게 게임의 상태(어디에 두었는지, 게임이 끝났는지, undo버튼 사용 등)를 보여줍니다. 또한 PvE의 경우, 컴퓨터의 연산시간과 연산횟수를 보여줍니다. 
(주의 : 새로 프로그램을 실행하거나, 다른 모드를 선택한 후 게임을 하게 되었을 시, 반드시 전에 창을 닫고 다시 열어주십시오.)