package wizard_anim.movement;


/**
 *	A <code>Velocity</code> implementation which maintains a consistent
 *	speed from start to finish.  The distance returned mirrors exactly the
 *	time entered.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class ConstantVelocity implements Velocity
{	/**
	 *	Returns the distance along a hypothetical linear path for the given time.
	 *
	 *	@since				0.1
	 *	@param time			a moment between 0.0 and 1.0 exclusive
	 *	@return				the corresponding distance, between 0.0 and 1.0 exclusive
	 */
	public double getDistance(double time)
	{	return time;
	}
}
