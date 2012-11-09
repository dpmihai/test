package credits;

import java.io.IOException;
import javax.sound.sampled.*;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jun 30, 2005 Time: 1:30:17 PM
 */
public class PlayAudio {

    /**
     * Play audio file
     *
     * @param fileName relative path from the compiled classes (sound files must be in the compiled classes directory)
     */
    public static void playAudioFile( String fileName ) {
      try {
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( PlayAudio.class.getResource(fileName) );
         playAudioStream( audioInputStream );
      } catch ( Exception e ) {
         System.out.println( "Problem with file " + fileName + ":" );
         e.printStackTrace();
      }
   }

   /** Plays audio from the given audio input stream. */
   public static void playAudioStream( AudioInputStream audioInputStream ) {
      // Audio format provides information like sample rate, size, channels.
      AudioFormat audioFormat = audioInputStream.getFormat();
      System.out.println( "Play input audio format=" + audioFormat );

      // Open a data line to play our type of sampled audio.
      // Use SourceDataLine for play and TargetDataLine for record.
      DataLine.Info info = new DataLine.Info( SourceDataLine.class, audioFormat );
      if ( !AudioSystem.isLineSupported( info ) ) {
         System.out.println( "Play.playAudioStream does not handle this type of audio on this system." );
         return;
      }

      try {
         // Create a SourceDataLine for play back (throws LineUnavailableException).
         SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine( info );
         // System.out.println( "SourceDataLine class=" + dataLine.getClass() );

         // The line acquires system resources (throws LineAvailableException).
         dataLine.open( audioFormat );

         // Adjust the volume on the output line.
         if( dataLine.isControlSupported( FloatControl.Type.MASTER_GAIN ) ) {
            FloatControl volume = (FloatControl) dataLine.getControl( FloatControl.Type.MASTER_GAIN );
            volume.setValue( 6.0F );
         }

         // Allows the line to move data in and out to a port.
         dataLine.start();

         // Create a buffer for moving data from the audio stream to the line.
         int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
         byte [] buffer = new byte[ bufferSize ];

         // Move the data until done or there is an error.
         try {
            int bytesRead = 0;
            while ( bytesRead >= 0 ) {
               bytesRead = audioInputStream.read( buffer, 0, buffer.length );
               if ( bytesRead >= 0 ) {
                  // System.out.println( "Play.playAudioStream bytes read=" + bytesRead +
                  //    ", frameanimation size=" + audioFormat.getFrameSize() + ", frames read=" + bytesRead / audioFormat.getFrameSize() );
                  // Odd sized sounds throw an exception if we don't write the same amount.
                  int framesWritten = dataLine.write( buffer, 0, bytesRead );
               }
            } // while
         } catch ( IOException e ) {
            e.printStackTrace();
         }

         System.out.println( "Play.playAudioStream draining line." );
         // Continues data line I/O until its buffer is drained.
         dataLine.drain();

         System.out.println( "Play.playAudioStream closing line." );
         // Closes the data line, freeing any resources such as the audio device.
         dataLine.close();
      } catch ( LineUnavailableException e ) {
         e.printStackTrace();
      }
   } // playAudioStream
} // Play

