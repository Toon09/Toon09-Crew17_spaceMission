package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

public class AB4 implements  NumSolver{

    static final double g1 = 55/24.0;
    static final double g2 = -59/24.0;
    static final double g3 = 37/24.0;
    static final double g4 = -9/24.0;



    private Model3D prev1; // 1 step behind
    private Model3D prev2; // 2 steps behind
    private Model3D prev3; // 3 steps behind
    private Model3D temp;

    @Override
    public void step(Model3D model, double dt) {

        AB2Vals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){
            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( g1*model.getVel(i)[0] + g2*prev1.getVel(i)[0] + g3*prev2.getVel(i)[0] + g4*prev3.getVel(i)[0] ),
                    model.getPos(i)[1] + dt * ( g1*model.getVel(i)[1] + g2*prev1.getVel(i)[1] + g3*prev2.getVel(i)[1] + g4*prev3.getVel(i)[1] ),
                    model.getPos(i)[2] + dt * ( g1*model.getVel(i)[2] + g2*prev1.getVel(i)[2] + g3*prev2.getVel(i)[2] + g4*prev3.getVel(i)[2] )   } );
        }


        //update vel
        for(int i=0; i<model.size(); i++){
            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( g1*model.getAcc(i)[0] + g2*prev1.getAcc(i)[0] + g3*prev2.getAcc(i)[0] + g4*prev3.getAcc(i)[0] ),
                    model.getVel(i)[1] + dt * ( g1*model.getAcc(i)[1] + g2*prev1.getAcc(i)[1] + g3*prev2.getAcc(i)[1] + g4*prev3.getAcc(i)[1] ),
                    model.getVel(i)[2] + dt * ( g1*model.getAcc(i)[2] + g2*prev1.getAcc(i)[2] + g3*prev2.getAcc(i)[2] + g4*prev3.getAcc(i)[2] )   } );
        }

        //adds time
        model.addDt(dt);

        //update acc
        model.hDeriv();

        // letting the ship do its plans (works for many ships)
        for(int i=0; i< model.getAmountOfShips(); i++){
            model.getShip(i).executePlans(model.getTime(), dt);
        }


    }


    private void AB2Vals(Model3D model, double dt){

        if( prev1 == null || prev2 == null ){ // boots trapping
            prev1 = model.clone( new RK7() ); // worth having as much precision as possible
            prev2 = model.clone( new RK7() );
            prev3 = model.clone( new RK7() );

            prev1.updatePos(-dt, -dt, false); // a step backwards
            prev2.updatePos(-2*dt, -2*dt, false); // 2 steps backwards
            prev3.updatePos(-3*dt, -3*dt, false); // 2 steps backwards
            temp = model.clone(null); //copy current model

        } else { // using last model
            // they are all cloned with null to prevent using the same method, thus entering a recursion
            prev3 = prev2.clone(null);
            prev2 = prev1.clone(null);
            prev1 = temp.clone(null); //copies previous model
            temp = model.clone(null); //copies current model

        }

    }

    @Override
    public String getName() {
        return "AB4";
    }
}
