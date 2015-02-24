package michael.assignment1;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 2/23/15
 * Time: 3:18 PM
 */
public class BasePlusCommissionEmployee {
	private double baseSalary; // base salary per week
	private CommissionEmployee comEmp;

	// six-argument constructor
	public BasePlusCommissionEmployee( String first, String last, String ssn, double sales, double rate, double salary ) {
		comEmp = new CommissionEmployee( first, last, ssn, sales, rate ) ;
		setBaseSalary( salary ); // validate and store base salary
	} // end six-argument BasePlusCommissionEmployee constructor

	// set base salary
	public void setBaseSalary( double salary ) {
		if ( salary >= 0.0 )
			baseSalary = salary;
		else
			throw new IllegalArgumentException(
				"Base salary must be >= 0.0" );
	} // end method setBaseSalary

	// return base salary
	public double getBaseSalary() {
		return baseSalary;
	} // end method getBaseSalary

	// calculate earnings
	public double earnings() {
// not allowed: commissionRate and grossSales private in superclass
		return baseSalary + ( comEmp.getCommissionRate() * comEmp.getGrossSales() );
	} // end method earnings

	// return String representation of BasePlusCommissionEmployee
	@Override // indicates that this method overrides a superclass method
	public String toString() {
// not allowed: attempts to access private superclass members
		return String.format(
			"%s: %s %s\n%s: %s\n%s: %.2f\n%s: %.2f\n%s: %.2f",
			"base-salaried commission employee", comEmp.getFirstName(), comEmp.getLastName(),
			"social security number", comEmp.getSocialSecurityNumber(),
			"gross sales", comEmp.getGrossSales(), "commission rate", comEmp.getCommissionRate(),
			"base salary", baseSalary );
	} // end method toString

	public String getFirstName() {
		return comEmp.getFirstName();
	}

	public String getLastName() {
		return comEmp.getLastName();
	}

	public String getSocialSecurityNumber() {
		return comEmp.getSocialSecurityNumber();
	}

	public double getGrossSales() {
		return comEmp.getGrossSales();
	}

	public double getCommissionRate() {
		return comEmp.getCommissionRate();
	}
}
