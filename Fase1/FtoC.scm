(define FtoC
    (lambda (F)
        (exact->inexact (/ (* (- F 32) 5) 9))))