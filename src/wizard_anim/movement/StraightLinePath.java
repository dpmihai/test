package wizard_anim.movement;


/**
 *	A <code>Path</code> implementation which moves between any two given
 *	x/y co-ordinates.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class StraightLinePath implements Path
{	private long startX,startY;
	private double incX,incY;
	private Velocity velocity;


	/**
	 *	Create a <code>StraightLinePath</code> between two sets of x/y points
	 *	with a given velocity.
	 *
	 *	@since				0.1
	 */
	public StraightLinePath(long x1,long y1,long x2,long y2 , Velocity v)
	{	startX=x1;  startY=y1;
		incX=x2-x1;  incY=y2-y1;
		velocity=v;
	}

	/**
	 *	Returns the co-ordinates for the given moment of time.
	 *
	 *	@since				0.1
	 *	@param time			a moment between 0.0 and 1.0 exclusive
	 *	@return				the corresponding x/y position (x=[0], y=[1])
	 */
	public double[] getCoords(double time)
	{	return this.getCoords(time, new double[2]);
	}
	/**
	 *	Returns the co-ordinates for the given moment of time into an
	 *	existing array.
	 *
	 *	@since				0.1
	 *	@param time			a moment between 0.0 and 1.0 exclusive
	 *	@param arr			the array to use
	 *	@return				the corresponding x/y position (x=[0], y=[1])
	 */
	public double[] getCoords(double time,double[] arr)
	{	if(time>1d)  time=1d;
		double d = velocity.getDistance(time);
		arr[0]=startX+(incX*d);  arr[1]=startY+(incY*d);
		return arr;
	}
}
