package wizard_anim.movement;


/**
 *	An abstraction for defining the course a movement takes within a physical
 *	two dimensional space.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public interface Path
{	/**
	 *	Returns the co-ordinates for the given moment of time.
	 *
	 *	@since				0.1
	 *	@param distance		a point on a hypothetical line between 0.0 and 1.0 exclusive
	 *	@return				the corresponding x/y position (x=[0], y=[1])
	 */
	public double[] getCoords(double distance);
	/**
	 *	Returns the co-ordinates for the given moment of time into an
	 *	existing array.
	 *
	 *	@since				0.1
	 *	@param distance		a point on a hypothetical line between 0.0 and 1.0 exclusive
	 *	@param arr			the array to use
	 *	@return				the corresponding x/y position (x=[0], y=[1])
	 */
	public double[] getCoords(double distance,double[] arr);
}
