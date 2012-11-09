package wizard_tf;

import org.jdesktop.animation.transitions.TransitionTarget;
import org.jdesktop.animation.transitions.ScreenTransition;
import org.jdesktop.animation.transitions.EffectsManager;
import org.jdesktop.animation.transitions.Effect;
import org.jdesktop.animation.transitions.effects.CompositeEffect;
import org.jdesktop.animation.transitions.effects.FadeIn;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 24, 2008
 * Time: 12:01:58 PM
 */
public class WMainPanel extends JPanel implements TransitionTarget {

    private static final int RESULTS_X = 0;
    private int currentTab = 0;
    private int tabs;

    private WPanel[] panels;
    private WButtonPanel buttonPanel;
    private CardLayout cardLayout;			// Layout manager

    //
    // Animation variables
    //
    Animator animator = new Animator(500);

    // Animate for half-second
    // Setup transition with:
    //      "this" as the transition container
    //      "this" as the TransitionTarget callback object
    //      animator as the animator that drives the transition
    private ScreenTransition transition = new ScreenTransition(this, this, animator);
    private CompositeEffect moverFader = null;
    private Paint bgGradient = null;

    public WMainPanel(WPanel[] panels) {
        this.panels = panels;
        tabs = panels.length;

        // Setup the animation parameters
        animator.setAcceleration(.2f);  // Accelerate for first 20%
        animator.setDeceleration(.4f);  // Decelerate for last 40%

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        // Add components to card
		for(int i=0;i<tabs;i++) {
            add(panels[i], panels[i].getTitle());
        }
        cardLayout.show(this, panels[currentTab].getTitle());
    }


    //Change the gradient and effect according to the new window size    
    private void setupBackgroundAndEffect(boolean next) {
        if (bgGradient != null) {
            // init the background gradient according to current height
            bgGradient = new GradientPaint(0, 0, Color.LIGHT_GRAY.brighter(),
                    0, getHeight(), Color.DARK_GRAY.brighter());
        }
        
        // Init resultsEffect with current component size info
        MoveIn mover;
        System.out.println("width="+getWidth());
        // horizontal move -> from left and from right
        if (!next) {
            mover = new MoveIn(getWidth(), 0);
        } else {
            mover = new MoveIn(-getWidth(), 0);
        }
        FadeIn fader = new FadeIn();
        moverFader = new CompositeEffect(mover);
        moverFader.addEffect(fader);
        EffectsManager.setEffect(panels[currentTab], moverFader, EffectsManager.TransitionType.APPEARING);

        // to avoid memory leak
        mover.cleanup(animator);
    }

    public void setButtonPanel(WButtonPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
        this.buttonPanel.enableBackward(false);
    }

    // Handle
    public void updateTab(boolean next) {

        if ( (!next &&(currentTab == 0)) || (next && (currentTab == tabs-1)) ) {
            return;
        }

        setupBackgroundAndEffect(next);
        if (next) {
            if (currentTab < tabs - 1) {
                currentTab++;
            }
        } else {
            if (currentTab > 0) {
                currentTab--;
            }
        }
        System.out.println("START");
        transition.start();
    }

    /**
     * TransitionTarget callback; set up
     * state for next screen
     */
    public void setupNextScreen() {
        if(currentTab == tabs-1) {
            buttonPanel.enableForward(false);
        } else {
            buttonPanel.enableForward(true);
        }

        if(currentTab == 0) {
            buttonPanel.enableBackward(false);
        } else {
            buttonPanel.enableBackward(true);
        }

        cardLayout.show(this, panels[currentTab].getTitle());        
    }


    /**
     * Custom effect: moves a component in to its end location
     * from a specified starting point
     */
    class MoveIn extends Effect {

        private Point startLocation = new Point();
        private PropertySetter ps;

        public MoveIn(int x, int y) {
            System.out.println("Constr");
            startLocation.x = x;
            startLocation.y = y;
        }

        /**
         * Handles setup of animation that will vary the location during the
         * transition
         */
        @Override
        public void init(Animator animator, Effect parentEffect) {
            System.out.println("init");
            Effect targetEffect = (parentEffect == null) ? this : parentEffect;

            ps = new PropertySetter(targetEffect, "location",
                    startLocation, new Point(getEnd().getX(), getEnd().getY()));
            animator.addTarget(ps);
            super.init(animator, parentEffect);
        }

        public void cleanup(Animator animator) {
            //super.cleanup(animator);
            animator.removeTarget(ps);
        }
    }
    
}
