package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;

public class Hohmann {

    Model3D model;

    /**
     * sets up everything
     * @param model all information of the system (planets, etc)
     */
    public Hohmann(Model3D model){
        this.model = model.clone( new RK4() ); //////////////////////////////// make so you can get the solver from the model
    }

}
