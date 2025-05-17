(define (domain complex-distance-transport) ; Zmieniona nazwa domeny

    (:requirements
        :strips
        :typing
        :negative-preconditions
        :action-costs      ; Wymagane do użycia (increase (total-cost) ...)
        :numeric-fluents
        ; :durative-actions ; Jeśli chcesz, by loty/rejsy miały czas trwania, dodaj to
        ; :multi-agent      ; Jeśli chcesz symulować wielu agentów, dodaj to
    )

    (:types
        plane boat truck - vehicle
        location
        package
    )

    (:predicates
        ; Define your predicates here, for example:
        (at-vehicle ?v - vehicle ?l - location)
        (at-package ?p - package ?l - location)
        (in ?p - package ?v - vehicle)
    )

    ; Define your actions here, for example:
    (:action action_name
        :parameters ()
        :precondition (and )
        :effect (and )
    
    )

)
