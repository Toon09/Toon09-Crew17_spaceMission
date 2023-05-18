package com.example.planets.Data;

public class DataGetter {

    /**
     * returns 2D array of positions and velocites of a certain object in a specified line
     *      taken from data from the NASA benchmark on: <a href="https://ssd.jpl.nasa.gov/horizons/app.html#/">NASA data generator</a>
     * the data must have the following parameters while being generated:
     *      1.Ephemeris Type: Vector table
     *      2.Target Body: // free to chose
     *      3.Coordinate center: 0°E, 0°N, 0 km @10 (Sun [Sol])
     *      4.Time Specification: //free to chose, make sure the time step matches what you are trying to do
     *      5.Table settings:
     *          Select "State vector {x,y,z,Vx,Vy,Vz}
     *          output units must be in Km/s
     *          Vector labels: on
     *          CSV Format: off
     *          Object summary: on
     * @param line line from where the data starts in the file (ignoring summary at start)
     * @param fileName name of the file, it must be inside the Data folder
     * @return 2D array of doubles with positions and velocities of the object of which the data has been generated found in
     *      the NASA simulation, this data is in the following format.
     *  First dimension  [ 0:x, 1:y, 2:z ]
     *  Second dimension [ 0:Vx, 1:Vy, 2:Vz ]
     */
    public double[][] getTxtExpData(int line, String fileName){
        return (new GetDataTXT()).getData(line, "src/main/java/com/example/planets/Data/" + fileName);

    }

}
