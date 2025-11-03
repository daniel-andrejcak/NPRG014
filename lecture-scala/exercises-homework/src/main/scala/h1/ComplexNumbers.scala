package h1
import scala.language.implicitConversions
import scala.math.abs

// Add necessary class and object definitions in order to make the statements in the main work.
case class Complex(real: Int, imaginary: Int):
	//operator overloading
	def +(that: Complex): Complex = Complex(real + that.real, imaginary + that.imaginary)
	def +(scalar: Int): Complex = Complex(real + scalar, imaginary)
	def *(that: Complex): Complex = Complex(real * that.real - imaginary * that.imaginary, real * that.imaginary + imaginary * that.real)
	def *(scalar: Int): Complex = Complex(real * scalar, imaginary * scalar)
	def unary_- : Complex = Complex(-real, -imaginary)
	
	//string representation
	override def toString: String =
		val realStr = real.toLong.toString
		val imaginaryStr = abs(imaginary).toLong.toString //abs to avoid double minus in output
		
		if imaginary == 0 then realStr
		else if real == 0 then s"${if imaginary < 0 then "-" else ""}$imaginaryStr" + "i"
		else s"$realStr${if imaginary < 0 then "-" else "+"}$imaginaryStr" + "i"

//implicit conversion from Int to Complex
given Conversion[Int, Complex] = (value: Int) => Complex(value, 0)

//global value used to create complex numbers in main()
val I: Complex = Complex(0, 1)

object ComplexNumbers:
  def main(args: Array[String]): Unit =
    
    println(Complex(1,2)) // 1+2i

    println(1 + 2*I + I*3 + 2) // 3+5i

    val c = (2+3*I + 1 + 4*I) * I
    println(-c) // 7-3i