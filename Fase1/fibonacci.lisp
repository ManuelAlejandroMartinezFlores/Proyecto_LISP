(defun fibonacci (n) (cond ((= n 1) 1) ((= n 2) 1) (T (+ (fibonacci (- n 1)) (fibonacci (- n 2))))))
