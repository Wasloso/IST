from pygame.sprite import Sprite
from pygame import Surface


class Objective(Sprite):
    def __init__(self, position: tuple[int, int]):
        super().__init__()
        self.image = Surface((15, 15))
        self.image.fill((0, 0, 255))
        self.rect = self.image.get_rect()
        self.rect.x = position[0]
        self.rect.y = position[1]

    def draw(self, screen):
        screen.blit(self.image, self.rect)

    def check_collision(self, rect):
        return self.rect.colliderect(rect)
