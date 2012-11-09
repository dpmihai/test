package wizard_anim.movement;

/**
 *	An abstraction for defining the pace at which a movement occurs over a
 *	given time across a path.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public interface Velocity
{	/**
	 *	Returns the distance along a linear path for a given point in the
	 *	movement timeline.  The timeline is represented as a scale from
	 *	0.0 to 1.0 exclusive, where 0.5, for example, represents halfway
	 *	through.  The result is the distance along an imaginary linear path,
	 *	again scaled between 0.0 (start) and 1.0 (end), which corresponds to
	 *	the supplied point on the timeline.
	 *	<p>
	 *	Feeding in a time of 0.0 should always return a distance of 0.0
	 *	(no movement along the path) and 1.0 should always return 1.0 (the
	 *	movement is complete, the end point is reached) &mdash; however
	 *	non-integer times can return any distance the implementation
	 *	requires.  (Unit testers: make sure you include checks for 0 and 1
	 *	in your own Velocity implementations!)
	 *	<p>
	 *	For example: let us assume a given concrete implementation of
	 *	Velocity accelerates to a given high over the course of the
	 *	timeline.  Feeding in the distance 0.5 will return a distance a
	 *	little before 0.5, because the velocity at that point is below
	 *	the average overall speed for the whole movement.  While another
	 *	implementation, which maintained a constant speed, would see the
	 *	distance returned match exactly the time given.
	 *
	 *	@since				0.1
	 *	@param time			a point in the timeline, between 0.0 and 1.0 exclusive
	 *	@return				the distance along the path, between 0.0 and 1.0 exclusive
	 */
	public double getDistance(double time);
}
