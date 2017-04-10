package com.example.lars_peter.moreendlessrunners;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by SharkGaming on 07/04/2017.
 */

public class GameThread extends Thread
{
    public static final int MAX_FPS = 60;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;


    public GameThread(SurfaceHolder _surfaceHolder, GameView _gameView)
    {
        super();
        this.surfaceHolder = _surfaceHolder;
        this.gameView = _gameView;
    }

    public void setRunning(boolean _running)
    {
        this.running = _running;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running)
        {
            startTime = System.nanoTime();
            canvas = null;

            try
            {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            }
            catch (Exception e) {e.printStackTrace();}
             finally
            {
                if(canvas != null)
                {
                    try
                    {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try
            {
                if(waitTime > 0)
                {
                    this.sleep(waitTime);
                }
            }
            catch (Exception e) {e.printStackTrace();}

            totalTime += System.nanoTime() - startTime;
            frameCount ++;
            if(frameCount == MAX_FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/ 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }

        }
    }
}
