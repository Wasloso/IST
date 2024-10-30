import numpy as np
from scipy.optimize import minimize
import matplotlib.pyplot as plt


def f1(x):
    x1, x2 = x
    return 3 * x1 + 5 * x2


def f2(x):
    x1, x2 = x
    return x1 + 3 * x2


def g1(x):
    x1, x2 = x
    return 4 * x1 + 3 * x2 - 11


def g2(x):
    x1, x2 = x
    return 5 * x1 + 2 * x2 - 7


def g3(x):
    x1, x2 = x
    return x1 + 3 * x2 - 4


def g4(x):
    x1, x2 = x
    return x1


def g5(x):
    x1, x2 = x
    return x2


def objective(x, a):
    a1, a2 = a
    return a1 * f1(x) + a2 * f2(x)


points = [(4, 0), (7 / 3, 5 / 9), (0, 11 / 3)]
result = None
best = None

for a in [(0.5, 0.5), (0.25, 0.75), (0.75, 0.25)]:
    for p in points:
        if result is None or objective(p, a) < result:
            result = objective(p, a)
            best = (p, a)


x_opt, a_opt = best
os_x1 = np.linspace(-1, 5, num=100)
os_x2 = np.linspace(-1, 5, num=100)
siatka_X1, siatka_X2 = np.meshgrid(os_x1, os_x2)
macierz_Z = objective([siatka_X1, siatka_X2], a_opt)

fig, ax = plt.subplots(figsize=(8, 8))
cplot = ax.contourf(siatka_X1, siatka_X2, macierz_Z, 100, cmap="Spectral_r", alpha=1)
ax.contour(
    siatka_X1,
    siatka_X2,
    g1([siatka_X1, siatka_X2]),
    levels=[0],
    colors="k",
    linestyles="dashed",
    label="g1(x) = 0",
)
ax.contour(
    siatka_X1,
    siatka_X2,
    g2([siatka_X1, siatka_X2]),
    levels=[0],
    colors="b",
    linestyles="dashed",
    label="g2(x) = 0",
)
ax.contour(
    siatka_X1,
    siatka_X2,
    g3([siatka_X1, siatka_X2]),
    levels=[0],
    colors="r",
    linestyles="dashed",
    label="g3(x) = 0",
)
ax.contour(
    siatka_X1,
    siatka_X2,
    g4([siatka_X1, siatka_X2]),
    levels=[0],
    colors="g",
    linestyles="dashed",
    label="g4(x) = 0",
)
ax.contour(
    siatka_X1,
    siatka_X2,
    g5([siatka_X1, siatka_X2]),
    levels=[0],
    colors="m",
    linestyles="dashed",
    label="g5(x) = 0",
)
ax.plot(x_opt[0], x_opt[1], "ro", label="Optimal point", markersize=10)
ax.set_xlabel(r"$x_1$", fontsize=16)
ax.set_ylabel(r"$x_2$", fontsize=16)
ax.set_title(
    f"Best params {x_opt=}, {a_opt=}\n" + r"$0.5f_1(x) + 0.5f_2(x)$", fontsize=16
)
fig.colorbar(cplot)
plt.show()
