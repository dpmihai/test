package wizard_anim.movement;

/**
 *	Simple utility class for measuring time from a given point over a given
 *	duration.
 *	<p>
 *	<i>Note: this class should be an interface, allowing different concrete
 *	implementations.  For example, one might return its 'moment' based upon
 *	the passage of 'real time' from a given start time across a given duration
 *	(which is what this class actually does.)  Another might space its
 *	'moments' out equally over a pre-determined number of frames, which would
 *	be useful for when you wanted to pre-generate an animation for later
 *	playback (and/or saving to a file.)
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class AnimationController
{	private long startTime;
	private long duration;

	/**
	 *	Constructs a controller for a given timescale.
	 *
	 *	@since				0.1
	 *	@param dur			duration of animation, in millis
	 */
	public AnimationController(long dur)
	{	duration=dur;
	}

	/**
	 *	Reset the timer to measure the duration from the current moment.
	 *
	 *	@since				0.1
	 */
	public void start()
	{	startTime=System.currentTimeMillis();
	}

	/**
	 *	Returns the current point on the timeline for the current time.
	 *
	 *	@since				0.1
	 *	@return				the point on the timeline, between 0.0 and 1.0 exclusive
	 */
	public double getMoment()
	{	long t=System.currentTimeMillis();
		t-=startTime;
		return ((double)t)/((double)duration);
	}
}