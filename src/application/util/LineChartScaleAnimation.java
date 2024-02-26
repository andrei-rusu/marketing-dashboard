package application.util;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class LineChartScaleAnimation implements EventHandler<MouseEvent>
{

	static final Duration ANIMATION_DURATION = new Duration(500);
	static final double ANIMATION_SCALE = 2.1d;
	private Data<String, Number> data;

	private boolean entered;

	public LineChartScaleAnimation(LineChart<String, Number> chart, Data<String, Number> item, boolean entered) {
		this.data = item;
		this.entered = entered;
	}

	@Override
	public void handle(MouseEvent e)
	{
		final Animation animation = new Transition() {
		     {
		         setCycleDuration(ANIMATION_DURATION);
		     }

		     protected void interpolate(double frac) {
		    	 if(entered)
		    	 {
		    		 data.getNode().setScaleX(1+((ANIMATION_SCALE-1)*frac));
		    		 data.getNode().setScaleY(1+((ANIMATION_SCALE-1)*frac));
		    	 }else
		    	 {
		    		 data.getNode().setScaleX(ANIMATION_SCALE-((ANIMATION_SCALE-1)*frac));
		    		 data.getNode().setScaleY(ANIMATION_SCALE-((ANIMATION_SCALE-1)*frac));
		    	 }
		     }

		 };
		 animation.play();
	}

}