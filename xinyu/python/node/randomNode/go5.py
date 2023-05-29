# Created by Xinyu Zhu on 2/10/2023, 9:59 PM
import pygame

# 游戏板大小
BOARD_SIZE = 15
# 棋子半径
PIECE_RADIUS = 20
# 棋子间隔
PIECE_INTERVAL = 40
# 画布大小
WINDOW_SIZE = (PIECE_INTERVAL * (BOARD_SIZE + 1), PIECE_INTERVAL * (BOARD_SIZE + 1))
# 棋子颜色
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)

class Game:
    def __init__(self):
        self.board = [[0 for _ in range(BOARD_SIZE)] for _ in range(BOARD_SIZE)]
        self.current_player = 1
        self.game_over = False

    def make_move(self, x, y):
        if self.game_over:
            return
        if self.board[x][y] != 0:
            return
        self.board[x][y] = self.current_player
        self.current_player = 3 - self.current_player
        self.check_game_over()

    def check_game_over(self):
        for i in range(BOARD_SIZE):
            for j in range(BOARD_SIZE):
                if self.board[i][j] == 0:
                    continue
                if self.check_line(i, j, 1, 0) or self.check_line(i, j, 0, 1) or self.check_line(i, j, 1, 1) or self.check_line(i, j, 1, -1):
                    self.game_over = True

    def check_line(self, x, y, dx, dy):
        count = 1
        for i in range(1, 5):
            if x + i * dx < 0 or x + i * dx >= BOARD_SIZE or y + i * dy < 0 or y + i * dy >= BOARD_SIZE:
                break
            if self.board[x + i * dx][y + i * dy] != self.board[x][y]:
                break
            count += 1
        for i in range(1, 5):
            if x - i * dx < 0 or x - i * dx >= BOARD_SIZE or y - i * dy < 0 or y - i * dy >= BOARD_SIZE:
                break
            if self.board[x - i * dx][y - i * dy] != self.board[x][y]:
                break
            count += 1
        return count >= 5

class GUI:
    def __init__(self):
        self.game = Game()
        pygame.init()
        self.window = pygame.display.set_mode(WINDOW_SIZE)
        pygame.display.set_caption("Five in a Row")
        self.font = pygame.font.Font(None, 32)
        self.draw_board()

    def draw_board(self):
        self.window.fill(WHITE)
        for i in range(BOARD_SIZE):
            for j in range(BOARD_SIZE):
                x = PIECE_INTERVAL + i * PIECE_INTERVAL
                y = PIECE_INTERVAL + j * PIECE_INTERVAL
                if self.game.board[i][j] == 1:
                    pygame.draw.circle(self.window, BLACK, (x, y), PIECE_RADIUS)
                elif self.game.board[i][j] == 2:
                    pygame.draw.circle(self.window, WHITE, (x, y), PIECE_RADIUS)
        pygame.display.update()

    def show_text(self, text):
        text_surface = self.font.render(text, True, BLACK)
        text_rect = text_surface.get_rect(center=(WINDOW_SIZE[0] // 2, WINDOW_SIZE[1] // 2))
        self.window.blit(text_surface, text_rect)
        pygame.display.update()

    def run(self):
        while True:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    return
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if self.game.game_over:
                        return
                    x, y = event.pos
                    i = x // PIECE_INTERVAL - 1
                    j = y // PIECE_INTERVAL - 1
                    if i < 0 or i >= BOARD_SIZE or j < 0 or j >= BOARD_SIZE:
                        continue
                    self.game.make_move(i, j)
                    self.draw_board()
                    if self.game.game_over:
                        self.show_text("Player %d wins!" % (3 - self.game.current_player))

            if not self.game.game_over:
                player_name = "Black" if self.game.current_player == 1 else "White"
                self.show_text("%s's turn" % player_name)

if __name__ == "__main__":
    gui = GUI()
    gui.run()
