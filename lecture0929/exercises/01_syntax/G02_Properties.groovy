class City {
    String name
    int size
    boolean capital = false
    
    static def create(String n, int v, boolean e = false) {
        return new City(name: n, size: v, capital: e)
    }
    
    def String toString() {
        if (this.capital) {
            return "Capital city of ${this.name}, population: ${this.size}"
        }
        else {
            return "City of ${this.name}, population: ${this.size}"
        }
    }
}

println City.create("Brno", 400000)
def praha = City.create("Praha", 1300000, true)
println praha

City pisek = new City(name: 'P�sek', size: 25000, capital: false)
City tabor = new City(size: 35000, capital: false, name: 'T�bor')

println pisek
pisek.size = 25001
println pisek

println tabor
//TASK Provide a customized toString() method overriding Object::toString() that prints the name and the population
assert 'City of P�sek, population: 25001' == pisek.toString()
assert 'Capital city of Praha, population: 1300000' == praha.toString()