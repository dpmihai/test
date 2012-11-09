package wizard_anim.movement;

import java.util.ArrayList;


/**
 *	A <code>Path</code> implementation which combines paths into a
 *	sequence, with weighting.  (Note: weighting not implemented yet!)
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class SequencedPath implements Path
{	private ArrayList<Path> paths;


	/**
	 *	Create an empty <code>SequencedPath</code>.
	 *
	 *	@since				0.1
	 */
	public SequencedPath()
	{	paths = new ArrayList<Path>();
	}

	/**
	 *	Create a <code>SequencedPath</code> from a varargs array.
	 *
	 *	@since				0.1
	 *	@param p			an array of <code>Path</code> objects
	 */
	public SequencedPath(Path... p)
	{	this();
		for(int i=0;i<p.length;i++)
			this.add(p[i] , 1.0d);
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
	{	if(time>=1d)  time=0.99999d;  // Never go beyond array size at end

		double t=time*(double)paths.size();
		int p1=(int)t;
		double p2=(t%1.0d);
		return paths.get(p1).getCoords(p2,arr);
	}

	/**
	 *	Add a new <code>Path</code> to the end of the sequence.  The
	 *	weight can be used to reduce or increase the effect of the path;
	 *	for example if two paths have a weight of 0.25 and 0.75
	 *	respectively then the first will only take effect for the opening
	 *	quarter of the movement, while the second will take effect over the
	 *	final three quarters.
	 *
	 *	@since				0.1
	 *	@param p			a <code>Path</code> object to append
	 *	@param weight		the duration ratio
	 */
	public void add(Path p,double weight)
	{	paths.add(p);
	}
}
