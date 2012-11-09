package wizard_anim.movement;


/**
 *	A <code>Path</code> implementation which remembers where it has been.  A
 *	rotating buffer of previous co-ordinates is maintained, such that index -1
 *	is then previous location, -2 is the next to last, and so on.
 *	<p>
 *	Normally each time <code>getCoords()</code> is called the buffer is
 *	updated, however a gap can be set so the buffer is only updated every
 *	so many calls.
 *	<p>
 *	When a movement has only just begun the buffer will not be full of
 *	previous co-ordinates, so users are advised to call <code>getBufferSize()</code>
 *	which returns how many actual samples have been stored thus far.
 *
 *	@author				S.E. Morris
 *	@since				0.1
 */
public class MemoryPath implements Path
{	private double[][] coordsBuffer;		// Buffer
	private int index;						// Position of NEXT EMPTY SPACE
	private int bufferSize;					// Number of entries in buffer
	private int gapCnt,gap;					// Gap between sampling coords
	private Path path;						// Path to wrap


	/**
	 *	Create a new <code>MemoryPath</code> wrapping an existing path, with a
	 *	given buffer size.
	 *
	 *	@since				0.1
	 *	@param p			a path to wrap
	 *	@param sz			buffer size
	 */
	public MemoryPath(Path p,int sz)
	{	this(p,sz,1);
	}
	/**
	 *	Create a new <code>MemoryPath</code> wrapping an existing path, with a
	 *	given buffer size, with a gap between sample collecting.
	 *
	 *	@since				0.1
	 *	@param p			a path to wrap
	 *	@param sz			buffer size
	 *	@param gp			gap between sample collecting
	 */
	public MemoryPath(Path p,int sz,int gp)
	{	path=p;  gap=gp;  gapCnt=0;  index=0;  bufferSize=0;
		coordsBuffer = new double[sz][2];
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
	{	arr = path.getCoords(time,arr);
		gapCnt++;
		if((gapCnt%gap)==0)
		{	coordsBuffer[index][0]=arr[0];
			coordsBuffer[index][1]=arr[1];
			index=(index+1)%coordsBuffer.length;

			bufferSize++;
			if(bufferSize>coordsBuffer.length)
				bufferSize=coordsBuffer.length;
		}
		return arr;
	}

	/**
	 *	Returns the co-ordinates for a previous moment of time, as recorded
	 *	by <code>getCoords()</code>.  An offset of -1 returns the previous
	 *	location, -2 one before, etc.  Check <code>getBufferSize()</code>
	 *	to learn how many samples are available.
	 *
	 *	@since				0.1
	 *	@param offset		a negative index
	 *	@return				the corresponding x/y position (x=[0], y=[1])
	 */
	public double[] getPreviousCoords(int offset)
	{	// Normalise
		offset=(offset+index-1+coordsBuffer.length)%coordsBuffer.length;
		return coordsBuffer[ offset%coordsBuffer.length ];
	}

	/**
	 *	Returns the buffer capacity.
	 *
	 *	@since				0.1
	 *	@return				the buffer size
	 */
	public int getMemorySize()
	{	return coordsBuffer.length;
	}
	/**
	 *	Returns the number of samples in the buffer (which may not be its
	 *	capacity if fewer samples have been collected.)
	 *
	 *	@since				0.1
	 *	@return				the current number of samples
	 */
	public int getBufferSize()
	{	return bufferSize;
	}
	/**
	 *	Resets the memory, ready for re-use.
	 *
	 *	@since				0.1
	 */
	public void reset()
	{	bufferSize=0;  gapCnt=0;
	}
}
