# Created by Xinyu Zhu on 2/10/2023, 10:02 PM
import random
import pygame
import sys

# 设置窗口大小
WINDOW_SIZE = (400, 400)
# 设置游戏板块大小
BOARD_SIZE = 4
# 设置每个方块的大小
BLOCK_SIZE = 80
# 设置每个方块之间的间隔
BLOCK_INTERVAL = 20
# 设置数字方块的字体大小
FONT_SIZE = 40
# 设置方块的颜色
COLORS = {
    2: (238, 228, 218),
    4: (237, 224, 200),
    8: (242, 177, 121),
    16: (245, 149, 99),
    32: (246, 124, 95),
    64: (246, 94, 59),
    128: (237, 207, 114),
    256: (237, 204, 97),
    512: (237, 200, 80),
    1024: (237, 197, 63),
    2048: (237, 194, 46),
}
# 设置背景颜色
BACKGROUND_COLOR = (187, 173, 160)

class Game:
    def __init__(self):
        self.board = [[0] * BOARD_SIZE for _ in range(BOARD_SIZE)]
        self.score = 0
        self.game_over = False
        self.add_random_block()
        self.add_random_block()

    def add_random_block(self):
        empty_blocks = [(i, j) for i in range(BOARD_SIZE) for j in range(BOARD_SIZE) if self.board[i][j] == 0]
        if empty_blocks:
            i, j = random.choice(empty_blocks)
            self.board[i][j] = 2 if random.random() < 0.9 else 4

    def move_left(self):
        for i in range(BOARD_SIZE):
            new_row = []
            last_block = None
            for j in range(BOARD_SIZE):
                if self.board[i][j] != 0:
                    if last_block is None:
                        last_block = self.board[i][j]
                    elif last_block == self.board[i][j]:
                        new_row.append(last_block * 2)
                        self.score += last_block * 2
                        last_block = None
                    else:
                        new_row.append(last_block)
                        last_block = self.board[i][j]
            if last_block is not None:
                new_row.append(last_block)
            while len(new_row) < BOARD_SIZE:
                new_row.append(0)
            self.board[i] = new_row

    def move_right(self):
        for i in range(BOARD_SIZE):
            new_row = []
            last_block = None
            for j in range(BOARD_SIZE - 1, -1, -1):
                if self.board[i][j] != 0:
                    if last_block is None:
                        last_block = self.board[i][j]
                    elif last_block == self.board[i][j]:
                        new_row.append(last_block * 2)
                        self.score += last_block * 2
                        last_block = None
                    else:
                        new_row.append(last_block)
                        last_block = self.board[i][j]
            if last_block is not None:
                new_row.append(last_block)
            while len(new_row) < BOARD_SIZE:
                new_row.insert(0, 0)
            self.board[i] = new_row

        def move_up(self):
            for j in range(BOARD_SIZE):
                new_column = []
                last_block = None
                for i in range(BOARD_SIZE):
                    if self.board[i][j] != 0:
                        if last_block is None:
                            last_block = self.board[i][j]
                        elif last_block == self.board[i][j]:
                            new_column.append(last_block * 2)
                            self.score += last_block * 2
                            last_block = None
                        else:
                            new_column.append(last_block)
                            last_block = self.board[i][j]
                if last_block is not None:
                    new_column.append(last_block)
                while len(new_column) < BOARD_SIZE:
                    new_column.append(0)
                for i in range(BOARD_SIZE):
                    self.board[i][j] = new_column[i]

        def move_down(self):
            for j in range(BOARD_SIZE):
                new_column = []
                last_block = None
                for i in range(BOARD_SIZE - 1, -1, -1):
                    if self.board[i][j] != 0:
                        if last_block is None:
                            last_block = self.board[i][j]
                        elif last_block == self.board[i][j]:
                            new_column.append(last_block * 2)
                            self.score += last_block * 2
                            last_block = None
                        else:
                            new_column.append(last_block)
                            last_block = self.board[i][j]
                if last_block is not None:
                    new_column.append(last_block)
                while len(new_column) < BOARD_SIZE:
                    new_column.insert(0, 0)
                for i in range(BOARD_SIZE):
                    self.board[i][j] = new_column[i]

        def is_game_over(self):
            for i in range(BOARD_SIZE):
                for j in range(BOARD_SIZE):
                    if self.board[i][j] == 0:
                        return False
                    if j > 0 and self.board[i][j] == self.board[i][j - 1]:
                        return False
                    if j < BOARD_SIZE - 1 and self.board[i][j] == self.board[i][j + 1]:
                        return False
                    if i > 0 and self.board[i][j] == self.board[i - 1][j]:
                        return False
                    if i < BOARD_SIZE - 1 and self.board[i][j] == self.board[i + 1][j]:
                        return False
            return True

class Game2048:
    def __init__(self):
        pygame.init()
        pygame.display.set_caption('2048')
        self.window = pygame.display.set_mode(WINDOW_SIZE)
        self.font = pygame.font.Font(None, FONT_SIZE)
        self.game = Game()

    def draw_board(self):
        self.window.fill(BACKGROUND_COLOR)
        for i in range(BOARD_SIZE):
            for j in range(BOARD_SIZE):
                if self.game.board[i][j] != 0:
                    color = COLORS[self.game.board[i][j]]
                else:
                    color = (205, 193, 180)
                x = j * (BLOCK_SIZE + BLOCK_INTERVAL) + BLOCK_INTERVAL
                y = i * (BLOCK_SIZE + BLOCK_INTERVAL) + BLOCK_INTERVAL
                pygame.draw.rect(self.window, color, (x, y, BLOCK_SIZE, BLOCK_SIZE))
                if self.game.board[i][j] != 0:
                    text = self.font.render(str(self.game.board[i][j]), True, color)
            text_rect = text.get_rect(center=(x + BLOCK_SIZE / 2, y + BLOCK_SIZE / 2))
            self.window.blit(text, text_rect)

    def draw_score(self):
        text = self.font.render(f"Score: {self.game.score}", True, (238, 228, 218))
        self.window.blit(text, (BLOCK_INTERVAL, BLOCK_INTERVAL / 2))

    def game_loop(self):
        FPS = 30
        while True:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_LEFT:
                        self.game.move_left()
                    elif event.key == pygame.K_RIGHT:
                        self.game.move_right()
                    elif event.key == pygame.K_UP:
                        self.game.move_up()
                    elif event.key == pygame.K_DOWN:
                        self.game.move_down()
                    if self.game.is_game_over():
                        print("Game over!")
                        pygame.quit()
                        sys.exit()

            self.draw_board()
            self.draw_score()
            pygame.display.update()
            pygame.time.Clock().tick(FPS)

if __name__ == '__main__':
    Game2048().game_loop()
