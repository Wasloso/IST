import sympy as sp
from sympy import Piecewise
import numpy as np
import matplotlib.pyplot as plt

x = sp.symbols("x")


def get_penalty(g1, g2):
    return Piecewise((0, g1 < 0), (g1**2, True)) + Piecewise((0, g2 < 0), (g2**2, True))


def get_grad(f):
    return sp.diff(f, x)


def new_f(f, g1, g2, r):
    return f + r * get_penalty(g1, g2)


def get_fun_lambd(f, g1, g2, r):
    """Return lambdified functions:
    f: original function
    F: new function with penalty
    grad: gradient of F"""
    F = new_f(f, g1, g2, r)
    return sp.lambdify(x, f), sp.lambdify(x, F), sp.lambdify(x, get_grad(F))


def lambdify_constraints(g1, g2):
    return sp.lambdify(x, g1), sp.lambdify(x, g2)


def external_penalty(f, g1, g2, x0, r, max_iter, r_scale=2, steep_scale=0.01, tol=1e-6):
    x_val = x0
    curr_r = r
    f_lambd, F, gradient = get_fun_lambd(f, g1, g2, r)
    iter_number = 0
    minimum = None
    minumum_x = None
    steps = []
    g1_lambda, g2_lambda = lambdify_constraints(g1, g2)

    for _ in range(max_iter):
        steps.append(x_val)
        steep = steep_scale * -gradient(x_val)
        if abs(steep) < tol:
            break
        x_val += steep
        if (f_lambd(x_val) < minimum if minimum else True) and sp.lambdify(
            x, get_penalty(g1, g2)
        )(x_val) == 0:
            minimum = f_lambd(x_val)
            minumum_x = x_val
        print(f"Iteration {iter_number}: x = {x_val}")
        print(f"Function value: {f_lambd(x_val)}")
        iter_number += 1
        if g1_lambda(x_val) > 0 or g2_lambda(x_val) > 0:
            print("Constraint violated")
            curr_r *= r_scale
        _, _, gradient = get_fun_lambd(f, g1, g2, curr_r)

    print(f"\nNumber of iterations: {iter_number}")
    print(f"Minimum at x = {minumum_x} with value {minimum}")
    visualize(f_lambd, sp.lambdify(x, g1), sp.lambdify(x, g2), steps, x0, minumum_x)


def visualize(f, g1, g2, steps, start, minumum):
    x = np.linspace(-20, 20, 100000)
    y = f(x)
    g1y = g1(x)
    g2y = g2(x)
    stepsy = [f(step) for step in steps]

    plt.plot(x, y, label="f(x)")
    plt.plot(x, g1y, label="g1(x)")
    plt.plot(x, g2y, label="g2(x)")
    plt.plot(steps, stepsy, "o", label="Steps")
    plt.plot(start, f(start), "go", label=f"Start")
    plt.plot(minumum, f(minumum), "gX", label=f"Minimum", markersize=8)
    plt.legend()
    plt.xlabel("x")
    plt.ylabel("y")
    plt.title(
        f"Starting point: {start}, iterations: {len(steps)}\nMinumum at x={minumum} with value {f(minumum)}"
    )

    plt.xlim(-20, 20)
    plt.ylim(-20, 20)
    plt.grid(True)
    plt.show()


if __name__ == "__main__":
    f = (x - 3) * x * (x + 3)
    print(f)
    print(sp.diff(f, x))

    g1 = -x - 2
    g2 = x - 2
    penalty = get_penalty(g1, g2)
    grad = get_grad(penalty)
    print(grad)
    # external_penalty(
    #     f=f,
    #     g1=g1,
    #     g2=g2,
    #     x0=-3,
    #     r=2,
    #     r_scale=10,
    #     max_iter=50,
    #     tol=1e-3,
    #     steep_scale=0.05,
    # )
    # external_penalty(
    #     f=f,
    #     g1=g1,
    #     g2=g2,
    #     x0=0,
    #     r=1,
    #     r_scale=10,
    #     max_iter=10,
    #     tol=1e-6,
    #     steep_scale=0.1,
    # )
    # external_penalty(
    #     f=f,
    #     g1=g1,
    #     g2=g2,
    #     x0=0.75,
    #     r=1,
    #     r_scale=10,
    #     max_iter=10,
    #     tol=1e-6,
    #     steep_scale=0.1,
    # )
    # external_penalty(
    #     f=f,
    #     g1=g1,
    #     g2=g2,
    #     x0=-0.75,
    #     r=1,
    #     r_scale=10,
    #     max_iter=10,
    #     tol=1e-6,
    #     steep_scale=0.1,
    # )
    # external_penalty(
    #     f=f,
    #     g1=g1,
    #     g2=g2,
    #     x0=-15,
    #     r=50,
    #     r_scale=2,
    #     max_iter=500,
    #     tol=1e-3,
    #     steep_scale=0.01,
    # )
