//TASK Define the unless (aka if not) method

def unless(condition, closure){
    // closure.delegate = code
    // closure.resolveStrategy = Closure.DELEGATE_ONLY
    if (!condition){
        closure()
        closure.call()
    }
}

unless(1 > 5) {
    println "Condition not satisfied!"
    def value = 10
    println "Value is $value"
}

println 'done'