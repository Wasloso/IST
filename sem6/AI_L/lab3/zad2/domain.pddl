(define (domain robot)

    (:requirements 
        :strips 
        :typing
        :negative-preconditions
    )

    (:types 
        room
        robot
    )

    (:predicates 
        (at ?r - robot ?p - room)
        (clean ?p - room)
    )

    (:action move
        :parameters (?r - robot ?from - room ?to - room)
        :precondition (and 
            (at ?r ?from)
            (not (at ?r ?to))
        )
        :effect (and 
            (not (at ?r ?from))
            (at ?r ?to)
        )
    )

    (:action clean
        :parameters (?r - robot ?p - room)
        :precondition (and 
            (at ?r ?p)
            (not (clean ?p))
        )
        :effect (clean ?p)
         
    )
    

    
)
