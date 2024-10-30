import numpy as np
import matplotlib.pyplot as plt


def objective_function(x):
    return (x + 3) * x * (x - 3)


def g1(x):
    return -x - 2


def g2(x):
    return x - 2


def penalty_function(x):
    penalty = 0
    if g1(x) > 0:
        penalty += (g1(x)) ** 2
    if g2(x) > 0:
        penalty += (g2(x)) ** 2
    return penalty


def penalized_objective_function(x, penalty_coefficient):
    return objective_function(x) + penalty_coefficient * penalty_function(x)


def objective_gradient(x):
    return 3 * x**2 - 9


def g1_gradient(x):
    return -1


def g2_gradient(x):
    return 1


def penalty_gradient(x):
    penalty_grad = 0
    if g1(x) > 0:
        penalty_grad += 2 * g1(x) * g1_gradient(x)
    if g2(x) > 0:
        penalty_grad += 2 * g2(x) * g2_gradient(x)
    return penalty_grad


def penalized_gradient(x, r):
    return objective_gradient(x) + r * penalty_gradient(x)


def check_constraints(x):
    return g1(x) <= 0 and g2(x) <= 0


def gradient_descent(x0, max_iter, step_scale, r0=10, tol=1e-3, r_scale=2, show=False):
    r = r0
    x = x0
    iter_count = 0
    success = False
    steps = []
    while iter_count < max_iter and not success:
        steps.append(x)
        print("Iteration: ", iter_count)
        print("x: ", x)
        print("Objective value: ", objective_function(x))
        gradient = penalized_gradient(x, r)
        print("Gradient: ", gradient)
        r *= r_scale
        print("Penalty increased to: ", r)
        print(f"Step: {step_scale * gradient}")
        if abs(gradient) < tol:
            success = True
            print("Converged")
        print("\n")
        x -= step_scale * gradient
        iter_count += 1

    if show:
        visualize(steps, success)
    return x


def visualize(steps, success):
    x = np.linspace(-15, 15, 10000)
    y = objective_function(x)
    plt.plot(x, y, label="f")
    plt.plot(x, g1(x), label="g1")
    plt.plot(x, g2(x), label="g2")
    plt.scatter(
        steps, [objective_function(step) for step in steps], label="steps", c="r"
    )
    plt.scatter(steps[0], objective_function(steps[0]), c="y", label="start", s=100)
    if success:
        plt.title(
            f"Start: {steps[0]}\nConverged: minumum at x={steps[-1]}\nValue: {objective_function(steps[-1])}"
        )
        plt.scatter(
            steps[-1], objective_function(steps[-1]), c="g", label="minimum", s=100
        )
    else:
        plt.title("Did not converge")
    plt.xlim(-12, 12)
    plt.ylim(-12, 12)
    plt.legend()
    plt.grid(True)
    plt.show()


if __name__ == "__main__":
    gradient_descent(
        x0=10, max_iter=1000, step_scale=0.04, r0=10, show=True, tol=1e-2, r_scale=2
    )
    gradient_descent(
        x0=-10, max_iter=1000, step_scale=0.01, r0=10, show=True, tol=1e-3, r_scale=2
    )
    gradient_descent(
        x0=0, max_iter=1000, step_scale=0.1, r0=10, show=True, tol=1e-6, r_scale=2
    )
