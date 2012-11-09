package wizard_anim.movement;


/**
 *	A <code>Velocity</code> implementation which simulates an object falling
 *	and landing under gravity.  Note: the physics are faked, no actual force/
 *	acceleration calculations are done.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class GravityBounceVelocity implements Velocity
{	private final static double ARC = Math.PI/2;

	/**
	 *	Returns the distance along a hypothetical linear path for the given time.
	 *
	 *	@since				0.1
	 *	@param time			a moment between 0.0 and 1.0 exclusive
	 *	@return				the corresponding distance, between 0.0 and 1.0 exclusive
	 */
	public double getDistance(double time)
	{	if(time<0.70d)
		{	// First 70%, just accelerate
			time*=(1d/0.70d);
			return 1d-Math.sin((1d-time)*ARC);
		}
		else if(time<0.90d)
		{	// 70%-89%, big bounce
			time-=0.70d;
			return 1d-(Math.sin((1d-time*5d)*Math.PI)/5d);
		}
		else if(time<1.00d)
		{	// 90%-99%, little bounce
			time-=0.90d;
			return 1d-(Math.sin((1d-time*10d)*Math.PI)/10d);
		}
		else
		{	// Should never happen
			return 0d;
		}
	}
}
