package wizard_anim.movement;

/**
 *	A <code>Velocity</code> implementation which accelerates from a soft
 *	start.  Uses the sine function over a 90 degree arc.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class SoftAccelerationVelocity implements Velocity
{	private final static double ARC = Math.PI/2;

	/**
	 *	Returns the distance along a hypothetical linear path for the given time.
	 *
	 *	@since				0.1
	 *	@param time			a moment between 0.0 and 1.0 exclusive
	 *	@return				the corresponding distance, between 0.0 and 1.0 exclusive
	 */
	public double getDistance(double time)
	{	return 1d-Math.sin((1d-time)*ARC);
	}
}
