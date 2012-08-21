package net.jobtest.geme;


public class Game {

    int field[][] = new int[4][4];

    private void mixUpField() {
	int x, y;
	for (int i = 0; i < 500; i++) {
	    x = (int) (Math.random() * 4);
	    y = (int) (Math.random() * 4);
	    this.move(x, y);
	}
    }

    private void genNormalField() {
	for (int i = 0; i < 15; i++) {
	    field[i % 4][i / 4] = i + 1;
	}
    }

    public Game() {
	genNormalField();
	mixUpField();
    }

    public boolean move(int x, int y) {
	
	boolean res = false;
	
	int px0 = -1, py0 = -1;

	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		if (this.field[i][j] == 0) {
		    px0 = i;
		    py0 = j;
		}
	    }
	}
	// Log.i("Game.move()", "x/y = " + x +"/"+ y +", px0/py0 = " + px0 + "/"
	// + py0);
	if (px0 == x || py0 == y) {
	    if (!(px0 == x && py0 == y)) {
		if (px0 == x) {
		    if (py0 < y) {
			for (int i = py0 + 1; i <= y; i++) {
			    //Log.d("Game.move()", "x = "+ x + ", i-1= "+ (i-1) +", i = " + i);
			    if(i < 4)
			    field[x][i - 1] = field[x][i];
			}
		    } else {
			for (int i = py0; i > y; i--) {
			    field[x][i] = field[x][i - 1];
			}
		    }
		}
		if (py0 == y) {
		    if (px0 < x) {
			for (int i = px0 + 1; i <= x; i++) {
			    field[i - 1][y] = field[i][y];
			}
		    } else {
			for (int i = px0; i > x; i--) {
			    field[i][y] = field[i - 1][y];
			}
		    }
		}
		field[x][y] = 0;
		res = true;
	    } else {
		res = false;
	    }
	}

	return res;
    }

    public boolean checkGameOver() {
	int a = 1;
	boolean res = true;
	for (int i = 0; i < 4; i++)
	    for (int j = 0; j < 4; j++) {
		if (i == 3 && j == 3) {
		    a = 0;
		}
		if (field[j][i] != a) {
		    res = false;
		    break;
		}
		a++;
	    }
	return res;
    }
}
