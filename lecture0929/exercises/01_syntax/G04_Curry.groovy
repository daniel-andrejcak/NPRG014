Closure computeTax = {rate, amount -> amount + amount * rate / 100}

assert 260 == computeTax(30, 200)

//TASK Define vatTax (21%) and incomeTax (19%) as a partially applied functions of computeTax

def vatTax = computeTax.curry(21)
assert 48400 == vatTax(40000)

def incomeTax = computeTax.curry(19)
assert 71162 == incomeTax(59800)

//TASK Define a curried version of computeTax

def tax = {rate -> {amount -> computeTax(rate, amount)}}
assert 55000 == tax(10)(50000)
println 'ok'