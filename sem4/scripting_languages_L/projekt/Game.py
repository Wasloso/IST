import pygame
from player import Player
from maze import Maze
import sys
from objective import Objective

# Initialize Pygame
pygame.init()

# Constants
HEIGHT = 600
WIDTH = 800
FPS = 60
cell_size = 50

# Screen setup
screen = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Maze Game")

# Create player
maze = Maze(cell_size, 20, 20)
maze.grid[0][0].collidable = False
player = Player(maze.player_start, 5)
objective = Objective(maze.objective_position)
print(maze)
points = 0

# Clock for controlling frame rate
clock = pygame.time.Clock()


def handle_events():
    keys = pygame.key.get_pressed()
    current_row = player.rect.y // cell_size
    current_column = player.rect.x // cell_size
    if keys[pygame.K_UP]:
        new_rect = player.try_move(0, -1)
        if not maze.check_collision(new_rect):
            player.rect = new_rect
    if keys[pygame.K_DOWN]:
        new_rect = player.try_move(0, 1)
        if not maze.check_collision(new_rect):
            player.rect = new_rect

    if keys[pygame.K_LEFT]:
        new_rect = player.try_move(-1, 0)

        if not maze.check_collision(new_rect):
            player.rect = new_rect

    if keys[pygame.K_RIGHT]:
        new_rect = player.try_move(1, 0)

        if not maze.check_collision(new_rect):
            player.rect = new_rect

    if objective.check_collision(player.rect):
        print("Objective reached")
        maze.generate_maze()

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()


while True:
    handle_events()

    # Update game state
    # Add any game logic updates here

    # Clear the screen
    screen.fill((0, 0, 0))

    # Draw everything
    maze.draw(screen)
    player.draw(screen)
    objective.draw(screen)

    # Update the display
    pygame.display.flip()

    # Cap the frame rate
    clock.tick(FPS)
