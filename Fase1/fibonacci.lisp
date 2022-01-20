(defun fibonacci (n)
    (if (<= n 0)
        0
        (if (= n 1)
            1
            (+ (fibonacci (- n 1)) (fibonacci (- n 2))))))