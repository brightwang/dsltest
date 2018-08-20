class PizzaShop {
    def static order(closure) {
        PizzaShop shop = new PizzaShop()
        shop.with closure
    }

    def size(theSize) { println "size is $theSize" }

    def toppings(String[] theToppings) { println "Toppings received $theToppings" }
}

PizzaShop.order {
    size 'Large'
    toppings 'Olives', 'Bell Pepper', 'Onions'
}