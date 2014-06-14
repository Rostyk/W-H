package com.ros.workandhome.activities.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.Button;


public class WHCircleView extends Button{
		// ===========================================================
        // Fields
        // ===========================================================
        protected final int ARCSTROKEWIDTH = 20;
        // Set startup-values
        protected int mySecondsPassed = 0;
        protected int mySecondsTotal = 0;
      
        // Our Painting-Device (Pen/Pencil/Brush/Whatever...)
        protected final Paint myArcSecondPaint = new Paint();
        protected final Paint myArcMinutePaint = new Paint();
        protected final Paint myCountDownTextPaint = new Paint();
        protected final Paint myPizzaTimeTextPaint = new Paint();

        // ===========================================================
        // Constructors
        // ==========================================================

        public WHCircleView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            initView();
        }

        public WHCircleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public WHCircleView(Context context) {
                super(context);
                initView();
                
        }
        public void initView() {
        	//this.setBackground(getResources().getDrawable(R.drawable.pizza));
            
        	 /*
            // Black text for the countdown
            this.myCountDownTextPaint.setARGB(150, 0, 0, 0);
            this.myCountDownTextPaint.setTextSize(110);
            this.myCountDownTextPaint.setFakeBoldText(true);
            */
            /*
            // Orange text for the IT PIZZA TIME
            this.myPizzaTimeTextPaint.setARGB(255, 255, 60, 10);
            this.myPizzaTimeTextPaint.setTextSize(110);
            this.myPizzaTimeTextPaint.setFakeBoldText(true);
            */
            

            // Our minute-arc-paint fill be a lookthrough-red.
            this.myArcMinutePaint.setARGB(255, 0, 255, 0);
            this.myArcMinutePaint.setAntiAlias(true);
            this.myArcMinutePaint.setStyle(Style.STROKE);
            this.myArcMinutePaint.setStrokeWidth(ARCSTROKEWIDTH/3);

             
            // ur minute-arc-paint fill be a less lookthrough-orange.
            this.myArcSecondPaint.setARGB(200, 255, 130, 20);
            this.myArcSecondPaint.setAntiAlias(true);
            this.myArcSecondPaint.setStyle(Style.STROKE);
            this.myArcSecondPaint.setStrokeWidth(ARCSTROKEWIDTH / 3);
            
        }
        // ===========================================================
        // onXYZ(...) - Methods

        // ===========================================================

        
        @Override
        protected void onDraw(Canvas canvas) {
        	  super.onDraw(canvas);
                /* Calculate the time left,
                 * until our pizza is finished. */
                int secondsLeft = this.mySecondsTotal - this.mySecondsPassed;
                // Check if pizza is already done
                if(secondsLeft <= 0){
                        /* Draw the "! PIZZA !"-String
                         *  to the middle of the screen */
                    /*    
                	String itIsPizzaTime = getResources().getString(
                        R.string.pizza_countdown_end);
                        canvas.drawText(itIsPizzaTime, 
                        10, (this.getHeight() / 2) + 30, 
                       this.myPizzaTimeTextPaint); 
                       */                    
                }
                else{
                        // At least one second left
                        float angleAmountMinutes = ((this.mySecondsPassed * 1.0f)/ this.mySecondsTotal)* 360;
                        float angleAmountSeconds = ((60 -secondsLeft % 60) * 1.0f)/ 60 * 360;

                        /* Calculate an Rectangle, 
                        * with some spacing to the edges */
                        RectF arcRect = new RectF(ARCSTROKEWIDTH / 2, ARCSTROKEWIDTH / 2, this.getWidth() - ARCSTROKEWIDTH / 2, this.getHeight() - ARCSTROKEWIDTH / 2);
                        // Draw the Minutes-Arc into that rectangle             
                        canvas.drawArc(arcRect, -90, angleAmountMinutes, false, this.myArcMinutePaint);

                        

                        // Draw the Seconds-Arc into that rectangle     

                        //canvas.drawArc(arcRect, -90, angleAmountSeconds, false, this.myArcSecondPaint);

                        
                        String timeDisplayString;
                        if(secondsLeft > 60) // Show minutes
                                timeDisplayString = "" + (secondsLeft / 60);
                        else // Show seconds when less than a minute
                                timeDisplayString = "" + secondsLeft;
                        
                        // Draw the remaining time.
                        //canvas.drawText(timeDisplayString, this.getWidth() / 2 - (30 * timeDisplayString.length()), this.getHeight()/ 2 + 30, this.myCountDownTextPaint);
                }

        }
        // ===========================================================
        // Getter & Setter
        // ===========================================================

        
        public void updateSecondsPassed(int someSeconds){
                this.mySecondsPassed = someSeconds;
        }
        
        public void updateSecondsTotal(int totalSeconds){
                this.mySecondsTotal = totalSeconds;
        }

}
