package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.ArrayList;

/*
    // https://youtu.be/l_iZk4n5QFU [1h lecture on trajectory planning]

    + https://youtu.be/l_iZk4n5QFU?t=569 [ Hohmann transfer theory ]
    + https://youtu.be/l_iZk4n5QFU?t=786 [ arbitrary impulse transfer ]
 */
public class Hohmann {

    Model3D model;

    /**
     * sets up everything h
     * @param model all information of the system (planets, etc)
     */
    public Hohmann(Model3D model){
        this.model = model.clone();
    }


    /**
     * Must generate an ArrayList<double[][]> of the following format:
     *      first dimension  [ 0:start of time interval, 1:end of interval ] //times to start and stop accelerating
     *      second dimension [ 0:vel. in x, 1:vel. in y, 2:vel. in z ]
     * @return Returns an ArrayList<double[][]> of the format mentioned above that contains the information
     *      of how the rocket will change its course.
     */
    private ArrayList<double[][]> getTrajectory(){
        return null;
    }

}
