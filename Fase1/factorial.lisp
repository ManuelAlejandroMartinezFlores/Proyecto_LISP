(defun factorial (n) (cond ((= n 0) 1) (T (* n (factorial (- n 1))))))
