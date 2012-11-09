package wizard_anim.movement;

import java.util.Random;


/**
 *	A <code>Path</code> implementation which moves between any two given
 *	x/y co-ordinates with a random variation which adds a drunken style
 *	to the movement.  Just a joke.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class DrunkenLinePath extends StraightLinePath
{	private int drunken;
	private Random rnd;

	/**
	 *	Create a <code>DrunkenLinePath</code> between two sets of x/y points
	 *	with a given velocity and a random variation.
	 *
	 *	@since				0.1
	 */
	public DrunkenLinePath(long x1,long y1,long x2,long y2 , Velocity v,int dr)
	{	super(x1,y1,x2,y2,v);
		drunken=dr;

		rnd = new Random(System.currentTimeMillis());
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
	{	arr = super.getCoords(time,arr);
		float f = (float)time;
		if((f%1f)==0f)
		{	return arr;
		}
		else
		{	int dr=drunken/2;
			arr[0] += (dr-rnd.nextInt(drunken));
			arr[1] += (dr-rnd.nextInt(drunken));
		}
		return arr;
	}
}
