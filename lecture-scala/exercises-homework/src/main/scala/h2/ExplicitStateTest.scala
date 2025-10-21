/*
Implement the classes below such that the main (without modifications) prints out the something like this:

Person John Doe aged 24
Person John Doe aged 25
List(h2.PersonState@3d24753a)
Person John Doe aged 24
Thing Box with color (255,0,0)
Person Joe aged 24

*/


package h2

import scala.collection.mutable.ListBuffer


trait WithExplicitState:
  /* add necessary declarations here */
  type State

  protected def state: State
  protected def state_=(state: State): Unit


class PersonState(val name: String, val age: Int)

class Person extends WithExplicitState:
  /* Implement this class. It should have no knowledge of the trait History. It should use instances of PersonState as the state. */
  type State = PersonState
  
  private var _state: PersonState = new PersonState("", 0)
  
  protected def state: PersonState = _state
  protected def state_=(s: PersonState): Unit = _state = s
  
  def setName(name: String): this.type =
    _state = new PersonState(name, _state.age)
    this
  
  def setAge(age: Int): this.type =
    _state = new PersonState(_state.name, age)
    this
  
  override def toString: String = s"Person ${_state.name} aged ${_state.age}"


type RGBColor = (Int, Int, Int)
class ThingState(val name: String, val color: RGBColor)

class Thing extends WithExplicitState:
  /* Implement this class. It should have no knowledge of the trait History. It should use instances of ThingState as the state. */
  type State = ThingState
  
  private var _state: ThingState = new ThingState("", (0, 0, 0))
  
  protected def state: ThingState = _state
  protected def state_=(s: ThingState): Unit = _state = s
  
  def setName(name: String): this.type =
    _state = new ThingState(name, _state.color)
    this
  
  def setColor(color: RGBColor): this.type =
    _state = new ThingState(_state.name, color)
    this
  
  override def toString: String = s"Thing ${_state.name} with color ${_state.color}"


trait History extends WithExplicitState:
    /* Add necessary declarations here. This trait should have no knowledge of classes Person, Thing, PersonState, ThingState.
       It should depend only on the trait WithExplicitState.
    */

    val hist = ListBuffer.empty[State]

    def checkpoint(): this.type =
      hist.append(state)
      this

    def history = hist.toList

    def restoreTo(s: State): this.type =
      state = s
      this


object ExplicitStateTest:
  def main(args: Array[String]): Unit =
    // The inferred type of variable "john" should be "Person & History".
    val john = (new Person with History).setName("John Doe").setAge(24).checkpoint()

    println(john)
    john.setAge(25)

    println(john)
    println(john.history)

    val johnsPrevState = john.history(0)
    john.restoreTo(johnsPrevState)
    println(john)

    // The inferred type of variable "box" should be "Thing & History".
    val box = new Thing with History
    box.setName("Box")
    box.setColor((255, 0, 0))
    println(box)

    val joe = new Person with History
    joe.restoreTo(johnsPrevState).setName("Joe")
    println(joe)

    // The line below must not compile. It should complain about an incompatible type.
    // box.restoreTo(johnsPrevState)