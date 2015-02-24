package michael.assignment1;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 2/23/15
 * Time: 1:11 PM
 */
public class Main {
	public static void main( String[] args ){
		int start = 1, end = 3;
		Scanner in = new Scanner( System.in );
		int prob = 0;
		do{
			Pl( "Enter a problem to see " + start + " to " + end + " or " + ( end + 1 ) + " to exit: ");
			prob = in.nextInt();
			switch ( prob ){
				case 1:
					problem1();
					break;
				case 2:
					problem2();
					break;
				default:
					if( prob == ( end + 1 ) ){
						Pl( "Exit." );
						break;
					}
					Pl( "Not a problem try again." );
					break;
			}
		} while( prob != ( end + 1 ) );

	}

	public static<T> void Pl( T out ){
		System.out.println( out );
	}

	public static<T> void Err( T out ){
		System.err.println( out );
	}

	private static void problem1(){
		Pl( "creating rectangle with w:5, l:5" );
		Rectangle r1 = new Rectangle( 5, 5 );
		Pl( "rectangle 1 perimeter: " + r1.getPerimeter() + ", area: " + r1.getArea() );
		Pl( "creating invalid rectangle with defaults" );
		Rectangle r2 = new Rectangle();
		Pl( "rectangle 2 length: " + r2.getLength() + ", width: " + r2.getWidth() + ", perimeter: " + r2.getPerimeter() + ", area: " + r2.getArea() );
		Pl( "creating invalid rectangle with w:0, l:25" );
		Rectangle r3 = new Rectangle( 0, 25 );
	}

	private static void problem2(){
		/*
		Many programs written with inheritance could be written with composition instead, and
		vice versa. Rewrite class BasePlusCommissionEmployee (Fig. 9.11) of the CommissionEmployee –
		BasePlusCommissionEmployee hierarchy to use composition rather than inheritance.
		 */

		// instantiate BasePlusCommissionEmployee object
		BasePlusCommissionEmployee employee =
			new BasePlusCommissionEmployee(
				"Bob", "Lewis", "333-33-3333", 5000, .04, 300 );
// get base-salaried commission employee data
			System.out.println(
				"Employee information obtained by get methods: \n" );
		System.out.printf( "%s %s\n", "First name is", employee.getFirstName() );
		System.out.printf( "%s %s\n", "Last name is", employee.getLastName() );
		System.out.printf( "%s %s\n", "Social security number is", employee.getSocialSecurityNumber() );
		System.out.printf( "%s %.2f\n", "Gross sales is", employee.getGrossSales() );
		System.out.printf( "%s %.2f\n", "Commission rate is", employee.getCommissionRate() );
		System.out.printf( "%s %.2f\n", "Base salary is", employee.getBaseSalary() );
	}
}
