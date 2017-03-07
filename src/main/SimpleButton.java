package main;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas ;
import fr.lri.swingstates.canvas.CShape ;
import fr.lri.swingstates.canvas.CText ;
import fr.lri.swingstates.canvas.transitions.EnterOnShape;
import fr.lri.swingstates.canvas.transitions.LeaveOnShape;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.canvas.transitions.ReleaseOnShape;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Release;

import javax.swing.JFrame ;
import javax.swing.WindowConstants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font ;
import java.awt.Paint;
import java.awt.event.WindowStateListener;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
public class SimpleButton {

    private CText label;
    private CRectangle rectangle;
    private CExtensionalTag tag;

    SimpleButton(Canvas canvas, String text) {
    	rectangle = canvas.newRectangle(0, 0, 150, 50);
    	rectangle.setFillPaint(Color.WHITE);
    	tag = new CExtensionalTag(canvas) {};
    	rectangle.addTag(tag);
    	
    	CStateMachine stateMachine = initStateMachine();
    	StateMachineVisualization smv = initStateMachineVisualization(stateMachine);
    	
    	label = canvas.newText(0, 0, text, new Font("verdana", Font.PLAIN, 12));
    	label.addChild(rectangle);
    	label.above(rectangle);
    	
    	stateMachine.attachTo(rectangle);
    }

    public void action() {
	   System.out.println("ACTION!");
    }

    public CShape getShape() {
	   return label;
    }
    
    public CStateMachine initStateMachine() {
    	CStateMachine sm = new CStateMachine() {
    		Paint initColor;
    		
    		// état start
    		public State start = new State() {
    			Transition onHover = new EnterOnShape(">> hover") {
    				public void action() {
    					rectangle.setStroke(new BasicStroke(2));
    				}
    			};
    		};
    		
    		// état hover
    		public State hover = new State() {
    			Transition notOnHover = new LeaveOnShape(">> start") {
    				public void action() {
    					rectangle.setStroke(new BasicStroke(1));
    				}
    			};
    			
    			Transition press = new PressOnShape(">> pressed") {
    				public void action() {
    					rectangle.setStroke(new BasicStroke(1));
    					rectangle.setFillPaint(Color.YELLOW);
    				}
    			};
    		};
    		
    		// état hold
    		public State hold = new State() {
    			Transition rentre = new EnterOnShape(">> pressed") {
    				public void action() {
    					rectangle.setStroke(new BasicStroke(1));
    					rectangle.setFillPaint(Color.YELLOW);
    				}
    			};
    			
    			Transition releaseOutside = new Release(">> start") {
    				public void action() {
    					rectangle.setStroke(new BasicStroke(1));
    					rectangle.setFillPaint(Color.WHITE);
    				}
    			};
    		};
    		
    		// état pressed
    		public State pressed = new State() {
    			Transition release = new ReleaseOnShape(">> hover") {
    				public void action() {
    					rectangle.setStroke(new BasicStroke(2));
    					rectangle.setFillPaint(Color.WHITE);
    					System.out.println("release inside");
    				}
    			};
    			
    			Transition sortDuBouton = new LeaveOnShape(">> hold") {
    				public void action() {
    					rectangle.setFillPaint(Color.WHITE);
    					rectangle.setStroke(new BasicStroke(1));
    				}
    			};
    		};
		};
		return sm;
    }
    
    public StateMachineVisualization initStateMachineVisualization(CStateMachine sm) {
    	StateMachineVisualization smv = new StateMachineVisualization(sm);
    	
    	return smv;
    }

    static public void main(String[] args) {
	   JFrame frame = new JFrame();
	   Canvas canvas = new Canvas(400,400);
	   frame.getContentPane().add(canvas);
	   frame.pack();
	   frame.setVisible(true);
	   frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	   SimpleButton simple = new SimpleButton(canvas, "simple");
	   simple.getShape().translateBy(100,100);
    }

}