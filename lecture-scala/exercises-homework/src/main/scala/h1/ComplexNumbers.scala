package h1
import scala.language.implicitConversions
import scala.math.abs

// Add necessary class and object definitions in order to make the statements in the main work.
case class Complex(real: Double, imaginary: Double):
	//operator overloading
	def +(that: Complex): Complex = Complex(real + that.real, imaginary + that.imaginary)
	def +(scalar: Double): Complex = Complex(real + scalar, imaginary)
	def *(that: Complex): Complex = Complex(real * that.real - imaginary * that.imaginary, real * that.imaginary + imaginary * that.real)
	def *(scalar: Double): Complex = Complex(real * scalar, imaginary * scalar)
	def unary_- : Complex = Complex(-real, -imaginary)
	
	//string representation
	override def toString: String =
		val realStr = formatPart(real)
		val imaginaryStr = formatPart(abs(imaginary))
		
		if imaginary == 0 then realStr
		else if real == 0 then s"${if imaginary < 0 then "-" else ""}$imaginaryStr" + "i"
		else s"$realStr${if imaginary < 0 then "-" else "+"}$imaginaryStr" + "i"

	private def formatPart(value: Double): String =
		if value == value.toDouble then value.toLong.toString else value.toString

//implicit conversion to Complex
extension (value: Int)
  def +(c: Complex): Complex = Complex(value, 0) + c
  def *(c: Complex): Complex = Complex(value, 0) * c

val I: Complex = Complex(0, 1)

object ComplexNumbers:
  def main(args: Array[String]): Unit =
    
    println(Complex(1,2)) // 1+2i

    println(1 + 2*I + I*3 + 2) // 3+5i

    val c = (2+3*I + 1 + 4*I) * I
    println(-c) // 7-3i