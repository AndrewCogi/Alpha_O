package omok;

public class CheckGameOver_PvE {
	private static int[][] matrix = Board_PvE.matrix;
	   private static int posX;
	   private static int posY;
	   private static int col;
	   private static int row;
	   private static int rrc;
	   private static int lrc;

	   // constructor
	   public CheckGameOver_PvE(int[][] matrix, int x, int y) {
	      CheckGameOver_PvE.matrix = matrix;
	      CheckGameOver_PvE.posX = x;
	      CheckGameOver_PvE.posY = y;
	   }

	   // 함수!
	   public static boolean checkGameOver(int[][] matrix, int x, int y,int count) {
	      col = 0;
	      row = 0;
	      rrc = 0;
	      lrc = 0;
	      left(x, y);
	      right(x, y);
	      up(x, y);
	      down(x, y);
	      RightUp(x, y);
	      LeftDown(x, y);
	      RightDown(x, y);
	      LeftUp(x, y);
	      if(count==1)
	    	  return false;
	      else if (col == 4 || row == 4 || rrc == 4 || lrc == 4)
	         return true;
	      else
	         return false;

	      // 게임이 끝났으면 return true;
	      // 게임이 끝나지 않았으면 return false;
	      // (누가 이겼는지는 이 함수사용 직전에 누가 돌을 놓았는지를 확인하면 된다.)

	      // temporary return;
	   }

	   public static void left(int x, int y) {
	      if (y - 1 >= 0) {
	         if (CheckGameOver_PvE.matrix[x][y - 1] == CheckGameOver_PvE.matrix[x][y]) {
	            row++;
	            left(x, y - 1);
	         }
	      }
	   }

	   public static void right(int x, int y) {
	      if (y + 1 <= 14) {
	         if (CheckGameOver_PvE.matrix[x][y + 1] == CheckGameOver_PvE.matrix[x][y]) {
	            row++;
	            right(x, y + 1);
	         }
	      }
	   }

	   public static void up(int x, int y) {
	      if (x - 1 >= 0) {
	         if (CheckGameOver_PvE.matrix[x - 1][y] == CheckGameOver_PvE.matrix[x][y]) {
	            col++;
	            up(x - 1, y);
	         }
	      }
	   }

	   public static void down(int x, int y) {
	      if (x + 1 <= 14) {
	         if (CheckGameOver_PvE.matrix[x + 1][y] == CheckGameOver_PvE.matrix[x][y]) {
	            col++;
	            down(x + 1, y);
	         }
	      }
	   }

	   public static void RightUp(int x, int y) {
	      if (x - 1 >= 0 && y + 1 <= 14) {
	         if (CheckGameOver_PvE.matrix[x - 1][y + 1] == CheckGameOver_PvE.matrix[x][y]) {
	            rrc++;
	            RightUp(x - 1, y + 1);
	         }
	      }
	   }

	   public static void LeftDown(int x, int y) {
	      if (x + 1 <= 14 && y - 1 >= 0) {
	         if (CheckGameOver_PvE.matrix[x + 1][y - 1] == CheckGameOver_PvE.matrix[x][y]) {
	            rrc++;
	            LeftDown(x + 1, y - 1);
	         }
	      }
	   }

	   public static void RightDown(int x, int y) {
	      if (x + 1 <= 14 && y + 1 <= 14) {
	         if (CheckGameOver_PvE.matrix[x + 1][y + 1] == CheckGameOver_PvE.matrix[x][y]) {
	            lrc++;
	            RightDown(x + 1, y + 1);
	         }
	      }
	   }

	   public static void LeftUp(int x, int y) {
	      if (x - 1 >= 0 && y - 1 >= 0) {
	         if (CheckGameOver_PvE.matrix[x - 1][y - 1] == CheckGameOver_PvE.matrix[x][y]) {
	            lrc++;
	            LeftUp(x - 1, y - 1);
	         }
	      }
	   }
}
