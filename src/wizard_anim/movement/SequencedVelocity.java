package wizard_anim.movement;

import java.util.ArrayList;

/**
 *	A <code>Velocity</code> implementation which combines velocities into a
 *	sequence, with weighting.  (Note: weighting not implemented yet!)
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class SequencedVelocity implements Velocity
{	private ArrayList<Velocity> velocities;

	/**
	 *	Create an empty <code>SequencedVelocity</code>.
	 *
	 *	@since				0.1
	 */
	public SequencedVelocity()
	{	velocities = new ArrayList<Velocity>();
	}

	/**
	 *	Create a <code>SequencedVelocity</code> from a varargs array.
	 *
	 *	@since				0.1
	 *	@param v			an array of <code>Velocity</code> objects
	 */
	public SequencedVelocity(Velocity... v)
	{	velocities = new ArrayList<Velocity>();
		for(int i=0;i<v.length;i++)
			this.add(v[i] , 1.0d);
	}

	/**
	 *	Returns the distance along a hypothetical linear path for the given time.
	 *
	 *	@since				0.1
	 *	@param time			a moment between 0.0 and 1.0 exclusive
	 *	@return				the corresponding distance, between 0.0 and 1.0 exclusive
	 */
	public double getDistance(double time)
	{	// Never go beyond array size at end
		if(time>=1.0d)  time=0.99999d;

		int sz=velocities.size();

		// Work out how much time each velocity has in the total 0 to 1 scale.
		double perVel = 1d/sz;

		// Scale the time up over all the velocities we hold, then divide
		// into integer and fraction.  The former tells us which velocity
		// to use, and the latter, the time into that velocity.
		double scale = time*sz;
		int v1=(int)scale;
		double v2=(scale%1d);
		double vel=velocities.get(v1).getDistance(v2);
		return (v1+vel)/sz;
	}

	/**
	 *	Add a new <code>Velocity</code> to the end of the sequence.  The
	 *	weight can be used to reduce or increase the effect of the velocity;
	 *	for example if two velocities have a weight of 0.25 and 0.75
	 *	respectively then the first will only take effect for the opening
	 *	quarter of the movement, while the second will take effect over the
	 *	final three quarters.
	 *
	 *	@since				0.1
	 *	@param v			a <code>Velocity</code> object to append
	 *	@param weight		the duration ratio
	 */
	public void add(Velocity v,double weight)
	{	velocities.add(v);
	}
}
