import pygame


class Player(pygame.sprite.Sprite):
    def __init__(self, start_position: tuple[int, int], velocity: float):
        super().__init__()
        self.image = pygame.Surface((15, 15))
        self.image.fill((255, 255, 255))
        self.rect = self.image.get_rect()
        self.rect.x = start_position[0]
        self.rect.y = start_position[1]
        self.velocity = velocity

    def move(self, x: int, y: int):
        self.rect.x += x * self.velocity
        self.rect.y += y * self.velocity
        return self.rect

    def try_move(self, x: int, y: int):
        new_rect = self.rect.copy()
        new_rect.x += x * self.velocity
        new_rect.y += y * self.velocity
        return new_rect

    def draw(self, screen):
        screen.blit(self.image, self.rect)
