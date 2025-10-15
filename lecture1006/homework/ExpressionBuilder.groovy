// 2025/2026
// TASK The MarkupBuilder in Groovy can transform a hierarchy of method calls and nested closures into a valid XML document.
// Create a NumericExpressionBuilder builder that will read a user-specified hierarchy of simple math expressions and build a tree representation of it.
// The basic arithmetics operations as well as the power (aka '^') operation must be supported.
// It will feature a toString() method that will pretty-print the expression tree into a string with the same semantics, as verified by the assert on the last line.
// This means that parentheses must be placed where necessary with respect to the mathematical operator priorities.
// Change or add to the code in the script. Reuse the infrastructure code at the bottom of the script.
class NumericExpressionBuilder extends BuilderSupport {
    private Item root

    @Override
    protected void setParent(Object parent, Object child) {
        parent.children << child
    }

    @Override
    protected Object createNode(Object name) {
        return new Item(name.toString())
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        def node = new Item(name.toString())
        node.value = attributes['value']
        return node
    }

    // these override methods are not needed for this implementaiton, but still have to be declared
    @Override
    protected Object createNode(Object name, Object value) {
        throw new UnsupportedOperationException("Not implemented")
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
            throw new UnsupportedOperationException("Not implemented")
    }

    @Override
    protected void nodeCompleted(Object parent, Object node) {
        if (parent == null) root = node
    }

    Item rootItem() { root }
}

class Item {
    String name
    def value
    List<Item> children = []

    Item(String name) { this.name = name }

    boolean isLeaf() { name in ['number', 'variable'] }

    static Map<String, Integer> PRECEDENCE = [
        'number': 4, 
        'variable': 4,
        'power': 3,
        '^': 3,
        '*': 2,
        '/': 2,
        '+': 1,
        '-': 1
    ]

    @Override
    String toString() {
        if (isLeaf()) return value.toString()

        def op = (name == 'power') ? '^' : name

        def left = children[0]
        def right = children[1]

        def leftStr = left.toString()
        def rightStr = right.toString()

        // add parentheses if left operator has lower precedence than parent operator 
        if (!left.isLeaf() && PRECEDENCE[left.name] < PRECEDENCE[name])
            leftStr = "(${leftStr})"

        // add parentheses if right operator has lower precedence than parent operaotor or parent operator is right-associative
        if (!right.isLeaf() &&
            (PRECEDENCE[right.name] < PRECEDENCE[name] ||
             (PRECEDENCE[right.name] == PRECEDENCE[name] && name in ['power', '^'])))
            rightStr = "(${rightStr})"

        return "${leftStr} ${op} ${rightStr}"
    }
}
//------------------------- Do not modify beyond this point!

def build(builder, String specification) {
    def binding = new Binding()
    binding['builder'] = builder
    new GroovyShell(binding).evaluate(specification)
}

//Custom expression to display. It should be eventually pretty-printed as 10 + x * (2 - 3) / 8 ^ (9 - 5)
String description = '''
builder.'+' {
    number(value: 10)
    '*' {
        variable(value: 'x')
        '/' {
            '-' {
                number(value: 2)
                number(value: 3)
            }
            power {
                number(value: 8)
                '-' {
                    number(value: 9)
                    number(value: 5)
                }
            }
        }
    }
}
'''

//XML builder building an XML document
build(new groovy.xml.MarkupBuilder(), description)

//NumericExpressionBuilder building a hierarchy of Items to represent the expression
def expressionBuilder = new NumericExpressionBuilder()
build(expressionBuilder, description)
def expression = expressionBuilder.rootItem()
println (expression.toString())
assert '10 + x * (2 - 3) / 8 ^ (9 - 5)' == expression.toString()