# Created by Xinyu Zhu on 2/10/2023, 9:55 PM
class Gobang:
    def __init__(self, size=15):
        self.size = size
        self.board = [[' ' for _ in range(size)] for _ in range(size)]
        self.current_player = 'X'

    def draw_board(self):
        for i in range(self.size):
            print(' ---' * self.size)
            print('| ' + ' | '.join(self.board[i]) + ' |')
        print(' ---' * self.size)

    def make_move(self, x, y):
        if self.board[x][y] == ' ':
            self.board[x][y] = self.current_player
            self.current_player = 'O' if self.current_player == 'X' else 'X'
        else:
            print("Invalid move")

    def check_win(self):
        for i in range(self.size):
            for j in range(self.size):
                if self.board[i][j] == ' ':
                    continue
                if j < self.size - 4 and self.board[i][j] == self.board[i][j + 1] == self.board[i][j + 2] == self.board[i][j + 3] == self.board[i][j + 4]:
                    return True
                if i < self.size - 4 and self.board[i][j] == self.board[i + 1][j] == self.board[i + 2][j] == self.board[i + 3][j] == self.board[i + 4][j]:
                    return True
                if i < self.size - 4 and j < self.size - 4 and self.board[i][j] == self.board[i + 1][j + 1] == self.board[i + 2][j + 2] == self.board[i + 3][j + 3] == self.board[i + 4][j + 4]:
                    return True
                if i < self.size - 4 and j > 3 and self.board[i][j] == self.board[i + 1][j - 1] == self.board[i + 2][j - 2] == self.board[i + 3][j - 3] == self.board[i + 4][j - 4]:
                    return True
        return False

if __name__ == '__main__':
    game = Gobang()
    game.draw_board()
    while not game.check_win():
        move = input(f"{game.current_player} player's turn. Enter x, y to make a move: ")
        x, y = map(int, move.split(','))
        game.make_move(x, y)
        game.draw_board()
    print(f"{game.current_player} player wins!")