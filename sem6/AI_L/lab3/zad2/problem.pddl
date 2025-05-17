(define (problem rooms-cleaning) (:domain robot)
    (:objects 
        p1 p2 p3 - room
        r - robot
    )

    (:init
        (at r p1)
        (not (clean p1))
        (not (clean p2))
        (not (clean p3))
    )

    (:goal (and
        (clean p1)
        (clean p2)
        (clean p3)
    ))


)
