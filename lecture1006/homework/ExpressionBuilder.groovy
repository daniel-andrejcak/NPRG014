// 2024/2025
// TASK The MarkupBuilder in Groovy can transform a hierarchy of method calls and nested closures into a valid XML document.
// Create a NumericExpressionBuilder builder that will read a user-specified hierarchy of simple math expressions and build a tree representation of it.
// The basic arithmetics operations as well as the power (aka '^') operation must be supported.
// It will feature a toString() method that will pretty-print the expression tree into a string with the same semantics, as verified by the assert on the last line.
// This means that parentheses must be placed where necessary with respect to the mathematical operator priorities.
// Change or add to the code in the script. Reuse the infrastructure code at the bottom of the script.
class NumericExpressionBuilder extends BuilderSupport {
    Item root

    @Override
    protected void setParent(Object parent, Object child) {
        parent.addChild(child)
    }

    @Override
    protected Object createNode(Object name) {
        new Item(type: name.toString())
    }

    @Override
    protected Object createNode(Object name, Object value) {
        new Item(type: name.toString(), value: value)
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        new Item(type: name.toString(), value: attributes['value'])
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        new Item(type: name.toString(), value: value)
    }

    @Override
    protected void nodeCompleted(Object parent, Object node) {
        if (parent == null) {
            root = node
        }
    }

    Item rootItem() {
        root
    }
}

class Item {
    String type
    def value
    List<Item> children = []

    void addChild(Item child) {
        children << child
    }

    private static final Map<String, Integer> PRECEDENCE = [
        'number': 4, 'variable': 4,
        'power' : 3, '^'       : 3,
        '*'     : 2, '/'       : 2,
        '+'     : 1, '-'       : 1
    ]

    private boolean isLeaf() {
        type in ['number', 'variable']
    }

    private int precedence() {
        PRECEDENCE[type] ?: 0
    }

    @Override
    String toString() {
        if (isLeaf()) {
            return value.toString()
        }
        // binary operator
        if (children.size() == 2) {
            def left = children[0], right = children[1]
            String lstr = left.toString()
            String rstr = right.toString()

            if (left.precedence() < this.precedence()) {
                lstr = "(${lstr})"
            }
            if (right.precedence() < this.precedence() ||
                (type in ['-', '/'] && right.precedence() == this.precedence()) ||
                (type in ['power','^'] && right.precedence() == this.precedence())) {
                rstr = "(${rstr})"
            }

            def op = (type == 'power' ? '^' : type)
            return "${lstr} ${op} ${rstr}"
        }
        // unary / fallback
        children ? children[0].toString() : value?.toString()
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