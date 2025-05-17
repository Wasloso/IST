(define (problem distance-based-transport-scenario)
  (:domain complex-distance-transport) ; Ensure this matches your domain name

  (:objects
    locA locB locC - location ; Three locations: A (start), B (mid-point), C (end)
    truck1 - truck             ; Our trusty truck
    plane1 - plane             ; Our speedy plane
    package1 - package         ; The package to deliver
  )

  (:init
    ;; Initial positions
    (at-vehicle truck1 locA)
    (at-vehicle plane1 locA) ; Plane also starts at locA (e.g., an airport)
    (at-package package1 locA)

    ;; Define connections:
    (road-connected locA locB) ; Short road connection
    (road-connected locB locC) ; Another road connection
    (air-connected locA locC)  ; Long air connection

    ;; Initialize costs and distances:
    (= (total-cost) 0)

    ;; Define distances between locations
    (= (distance locA locB) 50)   ; Short distance
    (= (distance locB locC) 100)  ; Medium distance
    (= (distance locA locC) 400)  ; Long distance (directly by air)

    ;; Define cost parameters for vehicles
    ;; Truck: lower base cost, higher cost per unit distance
    (= (base-cost truck1) 10)
    (= (cost-per-unit-distance truck1) 1.0) ; 1 unit cost per 1 unit distance

    ;; Plane: higher base cost (e.g., airport fees), lower cost per unit distance
    (= (base-cost plane1) 50)
    (= (cost-per-unit-distance plane1) 0.2) ; 0.2 unit cost per 1 unit distance
  )

  (:goal
    (at-package package1 locC) ; The package needs to reach locC
  )

  ;; The planner will try to minimize this total cost
  (:metric minimize (total-cost))
)